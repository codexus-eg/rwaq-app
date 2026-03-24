package com.khater.rwaq.presentation.screens.branchScreen.components


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import coil3.compose.AsyncImage
import com.khater.rwaq.designSystem.component.textField.BasicTextField
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.screens.branchScreen.uiState.AddCarStep
import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarBrandUiState
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.CarColor
import com.khater.rwaq.presentation.util.Dimensions
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.add_new_car
import rwaq.composeapp.generated.resources.correct
import rwaq.composeapp.generated.resources.enter_plate_number
import rwaq.composeapp.generated.resources.next
import rwaq.composeapp.generated.resources.plate_number
import rwaq.composeapp.generated.resources.save
import rwaq.composeapp.generated.resources.select_brand
import rwaq.composeapp.generated.resources.select_color

//
//@Composable
//fun CarDetailsBottomSheet(
//    modifier: Modifier = Modifier,
//    onCancel: () -> Unit,
//    onSave: () -> Unit,
//    carBrands: List<CarBrandUiState>,
//    colors: List<Color>,
//    selectedBrand: CarBrandUiState?,
//    selectedColor: Color,
//    carNumber: String,
//    onBrandSelect: (CarBrandUiState) -> Unit,
//    onColorSelect: (Color) -> Unit,
//    onCarNumberChange: (String) -> Unit
//) {
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .clip(RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp))
//            .background(Theme.colorScheme.brand.onBrand)
//            .imePadding()
//            .statusBarsPadding(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // --- Top Bar ---
//        RwaqTopBar(
//            leadingContent = { RwaqBackButton(onClick = onCancel) },
//            isCenterAligned = true,
//            middleContent = {
//                Text(
//                    text = "اضافة سيارة جديدة", // "Add New Car"
//                    style = Theme.typography.title.small,
//                    color = Theme.colorScheme.primary.primary
//                )
//            },
//            modifier = Modifier.padding(bottom = Theme.spacing._16)
//        )
//
//        // --- 1. Car Brands List ---
//        Text(
//            text = "نوع السيارة", // "Car Type"
//            style = Theme.typography.body.medium,
//            color = Theme.colorScheme.primary.primary,
//            modifier = Modifier.padding(Theme.spacing._16).fillMaxWidth(),
//            textAlign = TextAlign.Start
//        )
//
//        LazyRow(
//            contentPadding = PaddingValues(horizontal = Theme.spacing._16),
//            horizontalArrangement = Arrangement.spacedBy(Theme.spacing._12),
//            modifier = Modifier.fillMaxWidth().padding(bottom = Theme.spacing._16)
//        ) {
//            items(carBrands) { brand ->
//                val isSelected = brand.id == selectedBrand?.id
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier
//                        .clickable { onBrandSelect(brand) }
//                        .padding(4.dp)
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(60.dp)
//                            .clip(CircleShape)
//                            .background(if (isSelected) Theme.colorScheme.primary.primary.copy(alpha = 0.1f) else Color.Transparent)
//                            .border(
//                                width = if (isSelected) 2.dp else 1.dp,
//                                color = if (isSelected) Theme.colorScheme.primary.primary else Theme.colorScheme.border.primary,
//                                shape = CircleShape
//                            ),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        // Placeholder for Car Logo
//                        // If you have a URL, use AsyncImage here.
//                        // Using Text initials for now as placeholder.
//                        Text(
//                            text = brand.name.take(2).uppercase(),
//                            style = Theme.typography.title.small,
//                            color = if (isSelected) Theme.colorScheme.primary.primary else Color.Gray
//                        )
//                    }
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = brand.name,
//                        style = Theme.typography.body.small,
//                        color = if (isSelected) Theme.colorScheme.primary.primary else Theme.colorScheme.secondary.secondary,
//                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
//                    )
//                }
//            }
//        }
//
//        // --- 2. Color Selection ---
//        Text(
//            text = "لون السيارة", // "Car Color"
//            style = Theme.typography.body.medium,
//            color = Theme.colorScheme.primary.primary,
//            modifier = Modifier.fillMaxWidth().padding(
//                bottom = Theme.spacing._8,
//                start = Theme.spacing._16,
//                top = Theme.spacing._16
//            ),
//            textAlign = TextAlign.Start
//        )
//
//        LazyRow(
//            contentPadding = PaddingValues(horizontal = Theme.spacing._16),
//            horizontalArrangement = Arrangement.spacedBy(Theme.spacing._12),
//            modifier = Modifier.fillMaxWidth().padding(bottom = Theme.spacing._16)
//        ) {
//            items(colors) { color ->
//                val isSelected = color == selectedColor
//                Box(
//                    modifier = Modifier
//                        .size(40.dp)
//                        .clip(CircleShape)
//                        .background(color)
//                        .border(
//                            width = if (isSelected) 2.dp else 1.dp,
//                            color = if (isSelected) Theme.colorScheme.primary.primary else Color.LightGray,
//                            shape = CircleShape
//                        )
//                        .clickable { onColorSelect(color) },
//                    contentAlignment = Alignment.Center
//                ) {
//                    androidx.compose.animation.AnimatedVisibility(
//                        visible = isSelected,
//                        enter = fadeIn(tween(200)),
//                        exit = fadeOut(tween(200))
//                    ) {
//                        Icon(
//                            painter = painterResource(Res.drawable.correct),
//                            contentDescription = "Selected",
//                            tint = if (color == Color.White) Color.Black else Color.White,
//                            modifier = Modifier.size(24.dp)
//                        )
//                    }
//                }
//            }
//        }
//
//        // --- 3. Car Number Input ---
//        BasicTextField(
//            value = carNumber,
//            hint = "أرقام وحروف اللوحة", // "Plate numbers and letters"
//            title = "رقم السيارة", // "Car Number"
//            onValueChanged = onCarNumberChange,
//            modifier = Modifier.fillMaxWidth().padding(
//
//                all = Theme.spacing._16
//            ),
//            shape = RoundedCornerShape(12.dp),
//            textFieldHeight = 56.dp
//        )
//
//        Spacer(modifier = Modifier.weight(1f))
//        Button(
//            onClick = onSave,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(Theme.spacing._16)
//                .navigationBarsPadding()
//                .height(50.dp),
//            shape = RoundedCornerShape(12.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Theme.colorScheme.brand.brand
//            ),
//            enabled = carNumber.isNotBlank() && selectedBrand != null
//        ) {
//            Text(
//                text = "إضافة السيارة", // "Add Car"
//                style = Theme.typography.body.large,
//                color = Color.White
//            )
//        }
//    }
//}


@Composable
fun AddCarWizardBottomSheet(
    modifier: Modifier = Modifier,
    step: AddCarStep,
    onCancel: () -> Unit,
    onNext: () -> Unit,
    // Data
    carBrands: List<CarBrandUiState>,
    carColors: List<CarColor>,
    // State
    selectedBrand: CarBrandUiState?,
    selectedColor: Color?,
    carNumber: String,
    // Events
    onBrandSelect: (CarBrandUiState) -> Unit,
    onColorSelect: (CarColor) -> Unit,
    onCarNumberChange: (String) -> Unit
) {
    val isNextEnabled = when (step) {
        AddCarStep.SELECT_BRAND -> selectedBrand != null
        AddCarStep.SELECT_COLOR -> selectedColor != null
        AddCarStep.ENTER_NUMBER -> carNumber.isNotEmpty()
    }

    val title = when (step) {
        AddCarStep.SELECT_BRAND -> stringResource(Res.string.select_brand)
        AddCarStep.SELECT_COLOR -> stringResource(Res.string.select_color)
        AddCarStep.ENTER_NUMBER -> stringResource(Res.string.enter_plate_number)
    }

    val buttonText = if (step == AddCarStep.ENTER_NUMBER) stringResource(Res.string.save) else stringResource(
        Res.string.next)

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = Theme.colorScheme.brand.onBrand
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp))
                .background(Theme.colorScheme.brand.onBrand)
                .padding(top = Theme.spacing._16, start = Theme.spacing._16, end = Theme.spacing._16)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .imePadding()
        )
        {
             RwaqTopBar(
                leadingContent = { RwaqBackButton(onClick = onCancel) },
                isCenterAligned = true,
                middleContent = {
                    Text(
                        text = stringResource(Res.string.add_new_car), // "Add New Car"
                        style = Theme.typography.title.small,
                        color = Theme.colorScheme.primary.primary
                    )
                },
                modifier = Modifier.padding(bottom = Theme.spacing._24)
            )

            // --- Dynamic Content based on Step ---
            Text(
                text = title,
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.primary.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .padding(bottom = Theme.spacing._16)
                    .fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Box() {
                AnimatedContent(
                    targetState = step,
                    transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
                    label = "StepAnimation"
                ) { currentStep ->
                    when (currentStep) {
                        AddCarStep.SELECT_BRAND -> {
                            BrandGrid2(
                                brands = carBrands,
                                selectedBrand = selectedBrand,
                                onSelect = onBrandSelect
                            )
                        }

                        AddCarStep.SELECT_COLOR -> {
                            ColorGrid2(
                                colors = carColors ,
                                selectedColor = selectedColor,
                                onSelect = onColorSelect
                            )
                        }

                        AddCarStep.ENTER_NUMBER -> {
                            Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                                BasicTextField(
                                    value = carNumber,
                                    hint = stringResource(Res.string.plate_number),
                                    onValueChanged = onCarNumberChange,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    textFieldHeight = 56.dp
                                )
                            }
                        }
                    }
                }
            }
           Spacer(modifier = Modifier.weight(1f))


            // --- Action Button ---
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Theme.colorScheme.brand.brand,
                    disabledContainerColor = Color.Gray
                ),
                enabled = isNextEnabled
            ) {
                Text(
                    text = buttonText,
                    style = Theme.typography.body.large,
                    color = Color.White
                )
            }
        }
    }
}
@Composable
fun ColorGrid2(
    colors: List<CarColor>,
    selectedColor: Color?,
    onSelect: (CarColor) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        colors.chunked(4).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEach { color ->
                    val isSelected = color.color == selectedColor

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .size(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(color.color)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected)
                                    Theme.colorScheme.primary.primary
                                else
                                    Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelect(color) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                painter = painterResource(Res.drawable.correct),
                                contentDescription = null,
                                tint = if (color.color == Color.White || color.color == Color.Yellow)
                                    Color.Black
                                else
                                    Color.White
                            )
                        }
                    }
                }

                repeat(4 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}


@Composable
fun BrandGrid2(
    brands: List<CarBrandUiState>,
    selectedBrand: CarBrandUiState?,
    onSelect: (CarBrandUiState) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        brands.chunked(4).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEach { brand ->
                    val isSelected = brand.id == selectedBrand?.id

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSelect(brand) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected)
                                        Theme.colorScheme.primary.primary
                                    else
                                        Color.LightGray.copy(0.5f),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = brand.name.take(1),
                                style = Theme.typography.title.large,
                                color = Color.Black
                            )

                            AsyncImage(
                                model = brand.logoUrl,
                                contentDescription = brand.name,
                                modifier = Modifier
                                    .matchParentSize()
                                    .padding(Theme.spacing._8)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Theme.colorScheme.brand.onBrand)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = brand.name,
                            style = Theme.typography.body.extraSmall,
                            textAlign = TextAlign.Center,
                            color = if (isSelected)
                                Theme.colorScheme.primary.primary
                            else
                                Theme.colorScheme.secondary.secondary
                        )
                    }
                }

                // Fill empty cells in last row
                repeat(4 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}


// --- Sub-Components ---

@Composable
fun BrandGrid(
    brands: List<CarBrandUiState>,
    selectedBrand: CarBrandUiState?,
    onSelect: (CarBrandUiState) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(brands) { brand ->
            val isSelected = brand.id == selectedBrand?.id
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onSelect(brand) }
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) Theme.colorScheme.primary.primary else Color.LightGray.copy(
                                0.5f
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Use AsyncImage here for real URLs. Using Text for placeholder.

                    Text(
                        text = brand.name.take(1),
                        style = Theme.typography.title.large,
                        color = Color.Black
                    )
                    AsyncImage(
                        model = brand.logoUrl,
                        contentDescription = brand.name,
                        modifier = Modifier.matchParentSize()
                            .padding(Theme.spacing._8)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Theme.colorScheme.brand.onBrand)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = brand.name,
                    style = Theme.typography.body.extraSmall,
                    textAlign = TextAlign.Center,
                    color = if (isSelected) Theme.colorScheme.primary.primary else Theme.colorScheme.secondary.secondary
                )
            }
        }
    }
}

@Composable
fun ColorGrid(
    colors: List<CarColor>,
    selectedColor: Color?,
    onSelect: (CarColor) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(colors) { color ->
            val isSelected = color.color == selectedColor
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.color)
                    .border(
                        width = if (isSelected) 3.dp else 1.dp,
                        color = if (isSelected) Theme.colorScheme.primary.primary else Color.LightGray,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onSelect(color) },
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        painter = painterResource(Res.drawable.correct),
                        contentDescription = null,
                        tint = if (color.color == Color.White || color.color == Color.Yellow) Color.Black else Color.White
                    )
                }
            }
        }
    }
}