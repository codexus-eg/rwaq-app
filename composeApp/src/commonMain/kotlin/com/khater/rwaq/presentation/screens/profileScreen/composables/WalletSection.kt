package com.khater.rwaq.presentation.screens.profileScreen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adamglin.composeshadow.dropShadow
import com.khater.rwaq.designSystem.theme.theme.Theme
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.currency_sar
import rwaq.composeapp.generated.resources.my_points
import rwaq.composeapp.generated.resources.point
import rwaq.composeapp.generated.resources.wallet


@Composable
fun WalletSection(
    modifier: Modifier = Modifier,
    balance: Double,
    points: Int,
    onWalletClick: () -> Unit,
    onPointsClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth() .dropShadow(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF001E14).copy(0.04f),
            blur = 20.dp,
            offsetY = 2.dp,
            offsetX = 0.dp
        )
            .clip(RoundedCornerShape(12.dp))
            .background(Theme.colorScheme.brand.onBrand)
             ,
        ) {
        SettingItemCard(
            settingName = stringResource(Res.string.wallet),
            settingSubName = "$points ${stringResource(Res.string.currency_sar)}",
            onClick = onWalletClick,
            hasNavigationIcon = false
        )
//        HorizontalDivider(
//            modifier = Modifier.padding(horizontal = 16.dp), // Indent divider
//            thickness = 0.5.dp,
//            color = Color.LightGray.copy(alpha = 0.5f)
//        )
//        SettingItemCard(
//            settingName = stringResource(Res.string.my_points),
//            settingSubName = "$points ${stringResource(Res.string.point)}",
//            onClick = onPointsClick
//        )

    }
}