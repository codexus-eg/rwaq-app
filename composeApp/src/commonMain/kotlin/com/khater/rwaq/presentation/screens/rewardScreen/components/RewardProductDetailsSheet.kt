package com.khater.rwaq.presentation.screens.rewardScreen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.screens.homeScreen.uiStates.HomeScreenInteractionListener
import com.khater.rwaq.presentation.screens.productScreen.components.ExtensionsSection
import com.khater.rwaq.presentation.screens.productScreen.components.ProductFooter
import com.khater.rwaq.presentation.screens.productScreen.components.ProductHeader
import com.khater.rwaq.presentation.screens.productScreen.components.SizesSection
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductDetailsUiState
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductScreenInteractionListener
import com.khater.rwaq.presentation.screens.rewardScreen.uiState.RewardInteractionListener
import com.khater.rwaq.presentation.util.Dimensions
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.product_details

@Composable
fun RewardProductDetailsSheet(
    isVisible: Boolean,
    details: ProductDetailsUiState?,
    points: Double,
    onDismiss: () -> Unit,
    listener: RewardInteractionListener
) {

    AnimatedVisibility(
        visible = isVisible && details != null,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = Modifier.fillMaxSize()
    ) {
        details?.let {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = Color(0xffF7F7F7)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp))
                        .background(Color(0xffF7F7F7))
                        .statusBarsPadding()
                        .systemBarsPadding()
                        .imePadding()
                )
                {
                    RwaqTopBar(
                        leadingContent = { RwaqBackButton(onClick = onDismiss) },
                        isCenterAligned = true,
                        middleContent = {
                            androidx.compose.material3.Text(
                                text = stringResource(Res.string.product_details), // "Add New Car"
                                style = Theme.typography.title.small,
                                color = Theme.colorScheme.primary.primary
                            )
                        },
                        modifier = Modifier.padding(bottom = Theme.spacing._24)
                    )

                    ProductDetailsContent(
                        details,
                        points = points,
                        listener
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductDetailsContent(
    details: ProductDetailsUiState,
    points: Double,

    listener: RewardInteractionListener,
 ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()

    ) {
        AsyncImage(
            model = details.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .height(220.dp)
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            contentScale = ContentScale.Inside
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
                .background(Color.White)
                .padding(16.dp)
                .padding(bottom = Dimensions.BOTTOM_NAV_HEIGHT.dp)
                .verticalScroll(rememberScrollState())
        ) {
            ProductHeader(details)
            Text(
                text = details.description,
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.primary.primary,
                modifier = Modifier.padding(top = 12.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
            RewardProductFooter(details, points,listener)
        }

    }
}