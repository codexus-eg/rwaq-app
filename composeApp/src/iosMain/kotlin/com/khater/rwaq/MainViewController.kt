package com.khater.rwaq

import androidx.compose.ui.window.ComposeUIViewController
import com.khater.rwaq.di.initKoin
import com.khater.rwaq.presentation.App
import io.github.tbib.klocation.IOSKLocationServices

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
        IOSKLocationServices().requestPermission()
    }
) { App() }