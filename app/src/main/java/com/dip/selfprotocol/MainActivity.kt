package com.dip.selfprotocol

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.compose.rememberNavController
import com.dip.selfprotocol.domain.repository.SettingsRepository
import com.dip.selfprotocol.presentation.navigation.AppNavGraph
import com.dip.selfprotocol.presentation.theme.SelfProtocolTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Hide content in recent apps
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        setContent {
            val isDarkTheme by settingsRepository.isDarkTheme.collectAsState(initial = true)
            
            SelfProtocolTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val hasAppLockEnabled by settingsRepository.hasAppLockEnabled.collectAsState(initial = false)
                    val autoLock by settingsRepository.autoLock.collectAsState(initial = true)
                    val pin by settingsRepository.pin.collectAsState(initial = null)
                    
                    var isLocked by remember { mutableStateOf(false) }
                    var hasCheckedInitialLock by remember { mutableStateOf(false) }

                    val lifecycleOwner = LocalLifecycleOwner.current
                    DisposableEffect(lifecycleOwner, hasAppLockEnabled, autoLock) {
                        val observer = LifecycleEventObserver { _, event ->
                            if (hasAppLockEnabled && autoLock) {
                                if (event == Lifecycle.Event.ON_STOP) {
                                    isLocked = true
                                }
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)
                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }
                    
                    LaunchedEffect(hasAppLockEnabled) {
                        if (!hasCheckedInitialLock && hasAppLockEnabled) {
                            isLocked = true
                            hasCheckedInitialLock = true
                        }
                    }

                    if (isLocked && hasAppLockEnabled) {
                        LockScreen(
                            correctPin = pin ?: "",
                            onUnlock = { isLocked = false },
                            isBiometricEnabled = runBlocking { settingsRepository.isBiometricEnabled.first() }
                        )
                    } else {
                        val navController = rememberNavController()
                        AppNavGraph(navController = navController)
                    }
                }
            }
        }
    }

    @Composable
    fun LockScreen(correctPin: String, onUnlock: () -> Unit, isBiometricEnabled: Boolean) {
        var pinInput by remember { mutableStateOf("") }
        var error by remember { mutableStateOf(false) }
        
        LaunchedEffect(Unit) {
            if (isBiometricEnabled) {
                val biometricManager = BiometricManager.from(this@MainActivity)
                if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
                    val promptInfo = BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Unlock Self Protocol")
                        .setSubtitle("Use your fingerprint to unlock")
                        .setNegativeButtonText("Use PIN")
                        .build()
                    val biometricPrompt = BiometricPrompt(this@MainActivity, ContextCompat.getMainExecutor(this@MainActivity),
                        object : BiometricPrompt.AuthenticationCallback() {
                            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                super.onAuthenticationSucceeded(result)
                                onUnlock()
                            }
                        })
                    biometricPrompt.authenticate(promptInfo)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Enter PIN", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = pinInput,
                    onValueChange = { 
                        if (it.length <= 6) {
                            pinInput = it
                            error = false
                        }
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = error,
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    if (pinInput == correctPin) {
                        onUnlock()
                    } else {
                        error = true
                        pinInput = ""
                    }
                }) {
                    Text("Unlock")
                }
                if (error) {
                    Text("Incorrect PIN", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
