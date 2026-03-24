package com.khater.rwaq.designSystem.component.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.theme.theme.Theme

@Composable
fun RwaqScaffold(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Theme.colorScheme.brand.onBrand,
    hasStatusBarColor: Boolean = false,
    statusBarColor: Color = if (hasStatusBarColor) Color(0xFFFBF7F0) else backgroundColor,
    bottomBar: @Composable () -> Unit = {},
    snakeBar: @Composable () -> Unit = {},
    overlays: ScaffoldScope .() -> Unit = {},
    topBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val scope = remember { ScaffoldScopeImpl() }.apply {
        items.clear()
        overlays()
    }

    val hasBlur = scope.items.any { it.isVisible }
    Box(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (hasBlur) Modifier.blur(4.dp) else Modifier
            )
            .background(statusBarColor)
//            .then(
//                if (!hasStatusBarColor) Modifier
//                     else Modifier.background(statusBarColor)
//                    .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
//            )

            .windowInsetsPadding(WindowInsets.statusBars)
            .systemBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
//        Image(
//            painter = painterResource(Res.drawable.bg),
//            contentDescription = "main background",
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.Crop
//        )

        Column(
            modifier = Modifier.background(backgroundColor)
        ) {
            topBar()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                content()
            }
            bottomBar()
        }

        Box(
            modifier = Modifier.align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            snakeBar()
        }
    }

    scope.items.forEach {
        it.content(scope, it.isVisible)
    }


}