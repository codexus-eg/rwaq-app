package com.khater.rwaq.designSystem.theme.color.scheme

import com.khater.rwaq.designSystem.theme.color.White38
import com.khater.rwaq.designSystem.theme.color.White
import com.khater.rwaq.designSystem.theme.color.White60
import com.khater.rwaq.designSystem.theme.color.colorPalette


internal val LightColorScheme = ColorScheme(
    brand = ColorScheme.Brand(
        brand = colorPalette.coffee,
        brandVariant = colorPalette.lightCoffee,
        onBrandVariant = colorPalette.thinBlue,
        onBrand = White
    ),
    primary = ColorScheme.Primary(
        primary = colorPalette.darkGray,
        onPrimary = White,
        onPrimaryBody = White60,
        onPrimaryHint = White38
    ),
    secondary = ColorScheme.Secondary(
        secondary = colorPalette.gray,
        secondaryText = colorPalette.gray,
        secondaryVariant = colorPalette.gray
    ),
    border = ColorScheme.Border(
        disabled = colorPalette.silver,
        brand = colorPalette.coffee,
        error = colorPalette.red,
        success = colorPalette.green,
        primary = colorPalette.sweetGray
    ),
    background = ColorScheme.Background(
        surfaceLow = colorPalette.gray,
        surface = colorPalette.antiFlashWhite,
        surfaceHigh = colorPalette.gray,
        bgError = colorPalette.red,
        bgWarning = colorPalette.yellow,
        bgSuccess = colorPalette.lightGreen
    ),
    shadePrimary = colorPalette.gray,
    shadeSecondary = colorPalette.gray,
    shadeTertiary = colorPalette.gray,
    stroke = colorPalette.gray,
    textDisabled = colorPalette.silverFoil,
    disabled = colorPalette.silverFoil,
    error = colorPalette.red,
    warning = colorPalette.yellow,
    success = colorPalette.green
)