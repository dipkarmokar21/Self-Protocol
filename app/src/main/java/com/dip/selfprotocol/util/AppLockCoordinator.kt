package com.dip.selfprotocol.util

import java.util.concurrent.atomic.AtomicInteger

object AppLockCoordinator {
    private val autoLockPauseCount = AtomicInteger(0)

    fun pauseAutoLock() {
        autoLockPauseCount.incrementAndGet()
    }

    fun resumeAutoLock() {
        while (true) {
            val current = autoLockPauseCount.get()
            if (current == 0 || autoLockPauseCount.compareAndSet(current, current - 1)) {
                return
            }
        }
    }

    val isAutoLockPaused: Boolean
        get() = autoLockPauseCount.get() > 0
}
