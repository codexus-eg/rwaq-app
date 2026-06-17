package com.khater.rwaq

import android.app.Application
import com.khater.rwaq.di.initKoin
import com.khater.rwaq.presentation.util.AndroidContext
import org.koin.android.ext.koin.androidContext

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin{
            androidContext(this@Application)
        }
        AndroidContext.init(this)

    }
}