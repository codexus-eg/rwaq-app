package com.khater.rwaq.designSystem.component.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.khater.rwaq.designSystem.theme.color.scheme.ColorScheme
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.DialogContainer
import com.khater.rwaq.presentation.composables.SnackBarContainer
import com.khater.rwaq.presentation.model.DialogState
import com.khater.rwaq.presentation.model.SnackBarState


@Composable
internal fun HomeScaffold(
    snackBarState: SnackBarState,
    dialogState: DialogState = DialogState(),
    modifier: Modifier = Modifier,
    hasStatusBarColor: Boolean = false,
    containerBackground:Color = Theme.colorScheme.brand.onBrand,
    topBar: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    RwaqScaffold(
        hasStatusBarColor = hasStatusBarColor,
        topBar = topBar,
        overlays = {
            dialog(dialogState.isVisible) {
                DialogContainer(dialogState)
            }
        }) {
        Box {
            Column(
                modifier = modifier.fillMaxSize()
                    .background(containerBackground),
                horizontalAlignment = Alignment.CenterHorizontally
            ) { content() }

            Box(
                modifier = Modifier.align(Alignment.TopCenter).padding(all = 12.dp).fillMaxWidth()
            ) { SnackBarContainer(snackBarState) }

        }
    }
}
