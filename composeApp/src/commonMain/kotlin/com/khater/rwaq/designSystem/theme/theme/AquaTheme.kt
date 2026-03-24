package com.khater.rwaq.designSystem.theme.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.khater.rwaq.designSystem.theme.color.scheme.ColorScheme
import com.khater.rwaq.designSystem.theme.color.scheme.LightColorScheme
import com.khater.rwaq.designSystem.theme.color.scheme.LocalColorScheme
import com.khater.rwaq.designSystem.theme.radius.AquaRadius
import com.khater.rwaq.designSystem.theme.radius.LocalRadius
import com.khater.rwaq.designSystem.theme.radius.Radius
import com.khater.rwaq.designSystem.theme.spacing.AquaSpacing
import com.khater.rwaq.designSystem.theme.spacing.LocalSpacing
import com.khater.rwaq.designSystem.theme.spacing.Spacing
import com.khater.rwaq.designSystem.theme.typography.LocalTypography
import com.khater.rwaq.designSystem.theme.typography.Typography
import com.khater.rwaq.designSystem.theme.typography.createThemeTypography
import com.khater.rwaq.designSystem.util.AppLanguage
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi

@OptIn(InternalResourceApi::class, ExperimentalResourceApi::class)
@Composable
fun RwaqTheme(
    language: String = AppLanguage.English.iso,
    content: @Composable () -> Unit,
){   val colorScheme = LightColorScheme
    val typography = createThemeTypography(language)
    val layoutDirection = remember(language) {
        if (language == AppLanguage.Arabic.iso) LayoutDirection.Rtl else LayoutDirection.Ltr
    }
    CompositionLocalProvider(
        LocalColorScheme provides colorScheme,
        LocalSpacing provides AquaSpacing,
        LocalRadius provides AquaRadius,
        LocalTypography provides typography,
        LocalLayoutDirection provides layoutDirection,
    ) {
        content()
    }

}

object Theme {
    val colorScheme: ColorScheme
        @Composable @ReadOnlyComposable get() = LocalColorScheme.current

    val typography: Typography
        @Composable @ReadOnlyComposable get() = LocalTypography.current

    val spacing: Spacing
        @Composable @ReadOnlyComposable get() = LocalSpacing.current

    val radius: Radius
        @Composable @ReadOnlyComposable get() = LocalRadius.current
}