package com.khater.rwaq.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButtonDefaults.elevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adamglin.composeshadow.dropShadow
import com.khater.rwaq.designSystem.component.icon.Icon
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.navigation.NavigationBarItems
import com.khater.rwaq.presentation.util.Dimensions
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun RwaqBottomNavigation(
    modifier: Modifier = Modifier,
    selectedScreen: Int,
    screens: List<NavigationBarItems>,
    height: Dp = Dimensions.BOTTOM_NAV_HEIGHT.dp,
    elevation: Dp = 2.dp,
    shape: Shape = RoundedCornerShape(topStart = Theme.spacing._16, topEnd = Theme.spacing._16),
    backgroundColor: Color = Color.White,
    selectedTextStyle: TextStyle = Theme.typography.body.extraSmall,
    unselectedTextStyle: TextStyle = Theme.typography.body.extraSmall,
    showDash: Boolean = false,
    showDot: Boolean = false,
    iconSize: Dp = 24.dp,
    dotSize: Dp = 6.dp,
    dotBottomPadding: Dp = 2.dp,
    lineWidth: Dp = 32.dp,
    lineThickness: Dp = 3.dp,
    selectedColor: Color = Theme.colorScheme.brand.brand,
    unSelectedColor: Color = Theme.colorScheme.secondary.secondary,
    selectedIconColor: Color = Theme.colorScheme.brand.brandVariant,
    unSelectedIconColor: Color = Theme.colorScheme.secondary.secondary,
    badgeColor: Color = Color(0xffff7675),
    badgeCount: Int = 0,
    badgeTextColor: Color = Color.White,
    verticalSpacing: Dp = 4.dp,
    onItemSelected: (Int) -> Unit,
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(
                shape = shape,
                color = Color(0xFF1F1F1F).copy(alpha = 0.06f),
                offsetY = (-4).dp,
                blur = 25.dp
            )
            //.shadow(elevation = elevation, shape = shape)
            .background(color = backgroundColor)
            .navigationBarsPadding()
            .height(height)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for ((index, screen) in screens.withIndex()) { // Added index
                val isSelected = index == selectedScreen // Changed comparison

                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    val interactionSource = remember { MutableInteractionSource() }

                    if (isSelected && showDash) {
                        Box(
                            modifier = Modifier
                                .width(lineWidth)
                                .height(lineThickness)
                                .background(
                                    color = selectedColor,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .align(Alignment.TopCenter)
                        )
                    }

                    BottomNavItem(
                        modifier = Modifier
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                onItemSelected(index)
                            },
                        screen = screen,
                        selectedTextStyle = selectedTextStyle,
                        unselectedTextStyle = unselectedTextStyle,
                        selectedColor = selectedColor,
                        unSelectedColor = unSelectedColor,
                        isSelected = isSelected,
                        iconSize = iconSize,
                        badgeCount = if (index==2) badgeCount else 0,
                        badgeColor = badgeColor,
                        badgeTextColor = badgeTextColor,
                        verticalSpacing = verticalSpacing,
                        selectedIconColor = selectedIconColor,
                        unSelectedIconColor = unSelectedIconColor
                    )

                    if (isSelected && showDot) {
                        Box(
                            modifier = Modifier
                                .padding(bottom = dotBottomPadding)
                                .size(dotSize)
                                .background(color = selectedColor, shape = CircleShape)
                                .align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    modifier: Modifier,
    screen: NavigationBarItems,
    isSelected: Boolean,
    badgeCount: Int,
    iconSize: Dp,
    selectedColor: Color,
    unSelectedColor: Color,
    badgeColor: Color,
    badgeTextColor: Color,
    selectedTextStyle: TextStyle,
    unselectedTextStyle: TextStyle,
    verticalSpacing: Dp,
    selectedIconColor: Color,
    unSelectedIconColor: Color
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        BadgedBox(
            badge = {
                if (badgeCount > 0) {
                    Badge(
                        containerColor = badgeColor
                    ) {
                        Text(
                            text = "$badgeCount",
                            color = badgeTextColor
                        )
                    }
                }
            },
            content = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(verticalSpacing),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(
                            resource = if (isSelected) {
                                screen.fillIcon
                            } else {
                                screen.icon
                            }
                        ),
                        modifier = Modifier
                            .size(iconSize),
                        tint = if (isSelected){
                            selectedIconColor
                        }else{
                            unSelectedIconColor
                        },
                        contentDescription = stringResource(screen.title)
                    )

                    Text(
                        text = stringResource(screen.title),
                        color = if (isSelected) {
                            selectedColor
                        } else {
                            unSelectedColor
                        },
                        maxLines = 1,
                        style = if (isSelected) {
                            selectedTextStyle
                        } else {
                            unselectedTextStyle
                        }
                    )
                }
            }
        )
    }
}