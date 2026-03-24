package com.khater.rwaq.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.theme.theme.Theme

@Composable
fun RwaqPagerIndicator(
    modifier: Modifier = Modifier,
    indicatorSize: Int,
    pagerState: PagerState,
    isAuthIndicator: Boolean = true,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(indicatorSize) { index ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (isAuthIndicator) {
                            if (pagerState.currentPage >= index) Theme.colorScheme.brand.brand else  Color(0xFFCFCFCF)
                        } else {
                            if (pagerState.currentPage == index) Theme.colorScheme.brand.brand else  Color(0xFFCFCFCF)
                        }
                    )
            )
        }
    }
}