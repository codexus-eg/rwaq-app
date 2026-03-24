package com.khater.rwaq.presentation.screens.branchScreen.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.button.PrimaryButton
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarUiState
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.change
import rwaq.composeapp.generated.resources.choose
import rwaq.composeapp.generated.resources.choose_car
import rwaq.composeapp.generated.resources.please_choose_car

@Composable
fun ChooseCarSection(
    modifier: Modifier = Modifier,
    selectedCar: CarUiState,
    onClickChooseCar: () -> Unit
) {
    val isCarSelected = selectedCar.id.isNotEmpty()

    Row(
        modifier = modifier.fillMaxWidth()
            .clip(RoundedCornerShape(Theme.spacing._12)).border(
                0.5.dp,
                Theme.colorScheme.shadePrimary.copy(0.5f),
                RoundedCornerShape(Theme.spacing._12)
            ).padding(Theme.spacing._12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        if (isCarSelected) {
             CarItem(
                car = selectedCar,
                modifier = Modifier.weight(1f).padding(end = Theme.spacing._8)
            )
        } else {
             Text(
                text = stringResource(Res.string.please_choose_car),
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.primary.primary
            )
        }

        PrimaryButton(
            text = if (isCarSelected) stringResource(Res.string.change) else stringResource(Res.string.choose),
            style = Theme.typography.body.medium,
            onClick = onClickChooseCar,
            modifier = Modifier.height(40.dp)
        )

    }
}