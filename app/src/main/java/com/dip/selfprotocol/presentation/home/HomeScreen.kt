package com.dip.selfprotocol.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dip.selfprotocol.util.bounceClick
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    onNavigateToRules: () -> Unit,
    onNavigateToLessons: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100) // Small delay for smoother entry
        isVisible = true
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            val isCompact = maxWidth < 600.dp
            
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                        initialOffsetY = { -50 },
                        animationSpec = tween(600)
                    )
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Self Protocol",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Rules Over Emotions",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                if (isCompact) {
                    // Phone layout - Vertical
                    Column(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        AnimatedHomeCard(
                            title = "Rules",
                            icon = Icons.Default.Rule,
                            iconColor = Color(0xFFEF4444), // RedAccent
                            modifier = Modifier.weight(1f),
                            isVisible = isVisible,
                            delayMs = 200,
                            onClick = onNavigateToRules
                        )
                        AnimatedHomeCard(
                            title = "Past Lessons",
                            icon = Icons.Default.MenuBook,
                            iconColor = Color(0xFF3B82F6), // BlueAccent
                            modifier = Modifier.weight(1f),
                            isVisible = isVisible,
                            delayMs = 300,
                            onClick = onNavigateToLessons
                        )
                        AnimatedHomeCard(
                            title = "Settings",
                            icon = Icons.Default.Settings,
                            iconColor = Color(0xFFA1A1AA), // Zinc 400
                            modifier = Modifier.weight(1f),
                            isVisible = isVisible,
                            delayMs = 400,
                            onClick = onNavigateToSettings
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                } else {
                    // Tablet / Landscape layout - Horizontal
                    Row(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedHomeCard(
                            title = "Rules",
                            icon = Icons.Default.Rule,
                            iconColor = Color(0xFFEF4444),
                            modifier = Modifier.weight(1f).height(240.dp),
                            isVisible = isVisible,
                            delayMs = 200,
                            onClick = onNavigateToRules
                        )
                        AnimatedHomeCard(
                            title = "Past Lessons",
                            icon = Icons.Default.MenuBook,
                            iconColor = Color(0xFF3B82F6),
                            modifier = Modifier.weight(1f).height(240.dp),
                            isVisible = isVisible,
                            delayMs = 300,
                            onClick = onNavigateToLessons
                        )
                        AnimatedHomeCard(
                            title = "Settings",
                            icon = Icons.Default.Settings,
                            iconColor = Color(0xFFA1A1AA),
                            modifier = Modifier.weight(1f).height(240.dp),
                            isVisible = isVisible,
                            delayMs = 400,
                            onClick = onNavigateToSettings
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedHomeCard(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    isVisible: Boolean,
    delayMs: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(500, delayMillis = delayMs)) +
                slideInVertically(
                    initialOffsetY = { 50 },
                    animationSpec = tween(500, delayMillis = delayMs)
                ),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .bounceClick(onClick = onClick),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(iconColor.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = iconColor,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
