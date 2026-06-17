package com.khater.rwaq.presentation.util

import android.content.Context

object AndroidContext {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun get(): Context {
        check(::appContext.isInitialized) {
            "AndroidContext.init() not called"
        }

        return appContext
    }
}