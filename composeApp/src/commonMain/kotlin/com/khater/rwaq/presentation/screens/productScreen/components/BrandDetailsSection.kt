package com.khater.rwaq.presentation.screens.productScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.icon.Icon
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.car
import rwaq.composeapp.generated.resources.car_pickup
import rwaq.composeapp.generated.resources.pick_up_from_branch

@Composable
fun BrandDetailsSection(
  modifier: Modifier = Modifier,
  isPickupFromBranch: Boolean,
  branchName: String,
  carName: String,
  carNumber: String
){

    val text = if (isPickupFromBranch) stringResource(Res.string.pick_up_from_branch) else stringResource(Res.string.car_pickup)
    val body = if (isPickupFromBranch) branchName else "$carName  $carNumber"
    Row(
        modifier = modifier.clip(RoundedCornerShape(Theme.spacing._8)).background(Theme.colorScheme.brand.onBrand.copy(0.3f)).padding(Theme.spacing._8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing._8, alignment = Alignment.End)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Theme.spacing._4),
            horizontalAlignment = Alignment.End
        ) {
            Text(text = text , style = Theme.typography.body.medium, color = Theme.colorScheme.brand.onBrand)
            Text(text = body , style = Theme.typography.body.small, color = Theme.colorScheme.brand.onBrand)
        }

        Icon(
            painter = painterResource(if (isPickupFromBranch) Res.drawable.pick_up_from_branch else Res.drawable.car),
            contentDescription = null,
            tint = Theme.colorScheme.brand.onBrand,
            modifier = Modifier.size(35.dp)
        )
    }
}