package com.khater.rwaq.presentation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.error
import rwaq.composeapp.generated.resources.success
import rwaq.composeapp.generated.resources.sucess_mark

import com.khater.rwaq.designSystem.component.dialog.Dialog
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.model.DialogState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val ANIMATION_DURATION = 500

@Composable
fun DialogContainer(
    dialogState: DialogState,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = dialogState.isVisible,
        enter = ENTER_ANIMATION,
        exit = EXIT_ANIMATION,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Crossfade(
            targetState = true,
            animationSpec = tween(ANIMATION_DURATION),
            modifier = Modifier.fillMaxSize()
                .background(Color(0xFFAFA9A9).copy(0.4f)),

            ) { isSuccess ->
            val title = if (isSuccess) Res.string.success else Res.string.error
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Dialog(
                    title = dialogState.title ?: stringResource(title),
                    message = dialogState.message ?: "",
                    icon = painterResource(Res.drawable.sucess_mark),
                    modifier = modifier.padding(horizontal = Theme.spacing._16),
                )
            }

        }
    }
}

private val ENTER_ANIMATION = fadeIn(tween(ANIMATION_DURATION))


private val EXIT_ANIMATION = fadeOut(tween(ANIMATION_DURATION))