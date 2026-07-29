package com.dip.selfprotocol

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SelfProtocolApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialization if needed
    }
}
