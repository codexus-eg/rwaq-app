package com.khater.rwaq.designSystem.theme.typography

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.khater.rwaq.designSystem.util.AppLanguage
import org.jetbrains.compose.resources.Font
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.alexandria_bold
import rwaq.composeapp.generated.resources.alexandria_medium
import rwaq.composeapp.generated.resources.alexandria_regular
import rwaq.composeapp.generated.resources.alexandria_semi_bold
import rwaq.composeapp.generated.resources.alexandria_thin
import rwaq.composeapp.generated.resources.cairo_bold
import rwaq.composeapp.generated.resources.cairo_medium
import rwaq.composeapp.generated.resources.cairo_regular
import rwaq.composeapp.generated.resources.sf_meduim
import rwaq.composeapp.generated.resources.sf_regural
import rwaq.composeapp.generated.resources.sf_semibold

@Composable
fun createThemeTypography(appLanguage: String): Typography {
    val poppinsFontFamily = FontFamily(
        Font(resource = Res.font.cairo_regular, FontWeight.Normal),
        Font(resource = Res.font.cairo_medium, FontWeight.Medium),
        Font(resource = Res.font.cairo_bold, FontWeight.SemiBold),
    )
    val notoFontFamily = FontFamily(
        Font(resource = Res.font.cairo_regular, FontWeight.Normal),
        Font(resource = Res.font.cairo_medium, FontWeight.Medium),
        Font(resource = Res.font.cairo_bold, FontWeight.SemiBold),
    )
    val alexandriaFontFamily = FontFamily(
        Font(resource = Res.font.sf_regural, FontWeight.Normal),
        Font(resource = Res.font.sf_meduim, FontWeight.Medium),
        Font(resource = Res.font.sf_semibold, FontWeight.Bold),
        Font(resource = Res.font.sf_semibold, FontWeight.SemiBold),
        Font(resource = Res.font.sf_regural, FontWeight.Thin),
    )
    val fontFamily = when (appLanguage) {
        AppLanguage.English.iso -> poppinsFontFamily
        AppLanguage.Arabic.iso -> alexandriaFontFamily
        else -> alexandriaFontFamily
    }

    return Typography(
        headline = Typography.Headline(
            large = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                lineHeight = 40.sp
            ),
            medium = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                lineHeight = 28.sp
            ),
            small = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        ),
        title = Typography.Title(
            large = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 24.sp
            ),
            medium = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp
            ),
            small = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        ),
        body = Typography.Body(
            large = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            medium = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            small = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 22.sp
            ),
            extraSmall = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 20.sp
            )
        ),
        label = Typography.Label(
            large = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp
            ),
            medium = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 22.sp
            ),
            small = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                lineHeight = 16.sp
            ),
            extraSmall = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                lineHeight = 16.sp
            )
        )
    )
}