package com.khater.rwaq.presentation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.khater.rwaq.designSystem.component.snackbar.SnackBar
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.model.SnackBarState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.error
import rwaq.composeapp.generated.resources.ic_check_circle
import rwaq.composeapp.generated.resources.ic_close_circle
import rwaq.composeapp.generated.resources.success

private const val ANIMATION_DURATION = 500

@Composable
fun SnackBarContainer(
    snackBarState: SnackBarState,
    modifier: Modifier = Modifier,
) {
    val targetTint =
        if (snackBarState.isSuccess) Theme.colorScheme.success else Theme.colorScheme.error

    val animatedTint by animateColorAsState(
        targetValue = targetTint,
        animationSpec = tween(durationMillis = ANIMATION_DURATION),
    )

    AnimatedVisibility(
        visible = snackBarState.isVisible,
        enter = ENTER_ANIMATION,
        exit = EXIT_ANIMATION
    ) {
        Crossfade(
            targetState = snackBarState.isSuccess,
            animationSpec = tween(ANIMATION_DURATION)
        ) { isSuccess ->
            val leadingIcon =
                if (isSuccess) Res.drawable.ic_check_circle else Res.drawable.ic_close_circle

            val title =
                if (isSuccess) Res.string.success else Res.string.error
            val containerColor =
                if (isSuccess) Theme.colorScheme.success.copy(alpha = 0.1f)
                else  Color(0xFFFFEFEB)
            SnackBar(
                title = snackBarState.title ?: stringResource(title),
                message = snackBarState.message ?: "",
                leadingIcon = painterResource(leadingIcon),
                modifier = modifier,
                tint = animatedTint,
                containerColor = containerColor
            )
        }
    }
}

private val ENTER_ANIMATION = fadeIn(tween(ANIMATION_DURATION)) +
        slideInHorizontally(
            animationSpec = tween(ANIMATION_DURATION),
            initialOffsetX = { it / 2 },
        )

private val EXIT_ANIMATION = fadeOut(tween(ANIMATION_DURATION)) +
        slideOutHorizontally(
            animationSpec = tween(ANIMATION_DURATION),
            targetOffsetX = { it / 2 },
        )