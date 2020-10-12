package com.example.bookmarkse_kotlin.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

open class AppExecutors constructor(
    val diskIO: Executor = DiskIOThreadExecutor(),
    val mainThread: Executor = MainThreadExecutor()
) {

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable?) {
            if (command != null) {
                mainThreadHandler.post(command)
            }
        }
    }
}