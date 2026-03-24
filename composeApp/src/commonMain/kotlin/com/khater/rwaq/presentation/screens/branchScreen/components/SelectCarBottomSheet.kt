package com.khater.rwaq.presentation.screens.branchScreen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.EmptyOrErrorContent
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarUiState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.add_new_car
import rwaq.composeapp.generated.resources.choose_car
import rwaq.composeapp.generated.resources.correct
import rwaq.composeapp.generated.resources.delete
import rwaq.composeapp.generated.resources.empty_car
import rwaq.composeapp.generated.resources.no_cars_to_display
import rwaq.composeapp.generated.resources.save

@Composable
fun SelectCarBottomSheet(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onCarSelected: (CarUiState) -> Unit,
    selectedCar: CarUiState,
    onDeleteCar: (carId: String) -> Unit,
    onAddCar: () -> Unit,
    onSaveChanges: () -> Unit,
    cars: List<CarUiState>,
) {
    Surface(
        color = Theme.colorScheme.brand.brand
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topEnd = Theme.spacing._12, topStart = Theme.spacing._12))
                .background(Theme.colorScheme.brand.onBrand)
                .padding(Theme.spacing._16)
                .statusBarsPadding()
        ) {

            RwaqTopBar(
                leadingContent = { RwaqBackButton(onClick = onCancel,hasDropShadow = true) },
                isCenterAligned = true,
                middleContent = {
                    Text(
                        text = stringResource(Res.string.choose_car),
                        style = Theme.typography.title.small,
                        color = Theme.colorScheme.primary.primary,
                        modifier = Modifier.offset(x = (-22).dp)
                    )
                },
                modifier = Modifier.padding(bottom = Theme.spacing._12)
            )


            if (cars.isEmpty()) {
                Spacer(modifier = Modifier.weight(1f))
                EmptyOrErrorContent(
                    painter = painterResource(Res.drawable.empty_car),
                    message = stringResource(Res.string.no_cars_to_display),
                    )
            } else {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(Theme.spacing._12)
                ) {
                    items(cars) { car ->
                        val isSelected = car.id == selectedCar.id

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) Theme.colorScheme.primary.primary else Color.LightGray.copy(
                                        alpha = 0.5f
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onCarSelected(car) }
                                .padding(Theme.spacing._12),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (isSelected) Theme.colorScheme.primary.primary else Color.Transparent
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) Theme.colorScheme.primary.primary else Color.LightGray,
                                        shape = RoundedCornerShape(6.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        painter = painterResource(Res.drawable.correct),
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }


                            CarItem(
                                car = car,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = Theme.spacing._12)
                            )

                             Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Theme.colorScheme.error)
                                    .clickable { onDeleteCar(car.id) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.delete),
                                    contentDescription = "Delete",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.weight(1f))
             Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Theme.spacing._16),
                verticalArrangement = Arrangement.spacedBy(Theme.spacing._12)
            )
            {
                 OutlinedButton(
                    onClick = onAddCar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Text(
                        text = stringResource(Res.string.add_new_car),
                        style = Theme.typography.body.large,
                        color = Theme.colorScheme.primary.primary
                    )
                }

                 Button(
                    onClick = onSaveChanges,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Theme.colorScheme.brand.brand
                    )
                ) {
                    Text(
                        text = stringResource(Res.string.save),
                        style = Theme.typography.body.large,
                        color = Color.White
                    )
                }
            }
        }
    }
}