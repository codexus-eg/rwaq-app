package com.khater.rwaq.presentation.screens.rewardScreen.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.button.PrimaryButton
import com.khater.rwaq.presentation.screens.homeScreen.uiStates.HomeScreenInteractionListener
import com.khater.rwaq.presentation.screens.productScreen.components.QuantitySelector
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductDetailsUiState
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductScreenInteractionListener
import com.khater.rwaq.presentation.screens.rewardScreen.uiState.RewardInteractionListener
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.add
import rwaq.composeapp.generated.resources.currency_sar

@Composable
fun RewardProductFooter(
    details: ProductDetailsUiState,
    points: Double,
    listener: RewardInteractionListener,
) {
    val currentPrice = details.calculatedTotalPrice

// Logic: Button is enabled ONLY if User Points >= Price
    val isRedeemEnabled =  points >= currentPrice
    val maxRewardQuantity = if (details.calculatedSingleUnitTestPrice > 0.0) {
        (points / details.calculatedSingleUnitTestPrice).toInt()
    } else {
        Int.MAX_VALUE
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        QuantitySelector(
            count = details.productQuantity,
            maxCount = maxRewardQuantity,
            onDecrease = { listener.onQuantityChange(-1) },
            onIncrease = { listener.onQuantityChange(1) },
            isBig = true
        )

        PrimaryButton(
            text = "${stringResource(Res.string.add)}  ${details.calculatedTotalPrice} ${stringResource(Res.string.currency_sar)} ",
            onClick = listener::onAddToCart,
            isEnabled = isRedeemEnabled,
            isLoading = details.isAddingToCart,
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .animateContentSize()
        )
    }
}
