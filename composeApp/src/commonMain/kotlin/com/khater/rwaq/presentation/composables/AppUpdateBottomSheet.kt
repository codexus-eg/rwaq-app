package com.khater.rwaq.presentation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.button.OutlinedButton
import com.khater.rwaq.designSystem.component.button.PrimaryButton
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.AppUpdateUiState
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.app_update_later
import rwaq.composeapp.generated.resources.app_update_latest_version
import rwaq.composeapp.generated.resources.app_update_release_notes
import rwaq.composeapp.generated.resources.app_update_update_now

@Composable
fun AppUpdateBottomSheet(
    isVisible: Boolean,
    update: AppUpdateUiState?,
    onUpdateClick: () -> Unit,
    onLaterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible && update != null,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
        modifier = modifier.fillMaxSize()
    ) {
        update?.let { updateState ->
            val scrimInteractionSource = remember { MutableInteractionSource() }
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.45f))
                        .clickable(
                            interactionSource = scrimInteractionSource,
                            indication = null,
                            onClick = {
                                if (!updateState.isForceUpdate) {
                                    onLaterClick()
                                }
                            }
                        )
                )

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    color = Theme.colorScheme.brand.onBrand,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    shadowElevation = 12.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = Theme.spacing._24, vertical = 20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(width = 44.dp, height = 4.dp)
                                .background(
                                    color = Theme.colorScheme.stroke,
                                    shape = CircleShape
                                )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 360.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(Theme.spacing._12)
                        ) {
                            Text(
                                text = updateState.title,
                                style = Theme.typography.title.large,
                                color = Theme.colorScheme.shadePrimary,
                                overflow = TextOverflow.Clip
                            )

                            if (updateState.latestVersion.isNotBlank()) {
                                Text(
                                    text = stringResource(
                                        Res.string.app_update_latest_version,
                                        updateState.latestVersion
                                    ),
                                    style = Theme.typography.body.medium,
                                    color = Theme.colorScheme.brand.brand,
                                    overflow = TextOverflow.Clip
                                )
                            }

                            Text(
                                text = updateState.message,
                                style = Theme.typography.body.medium,
                                color = Theme.colorScheme.secondary.secondary,
                                overflow = TextOverflow.Clip
                            )

                            if (updateState.releaseNotes.isNotBlank()) {
                                Text(
                                    text = stringResource(Res.string.app_update_release_notes),
                                    style = Theme.typography.title.small,
                                    color = Theme.colorScheme.shadePrimary,
                                    overflow = TextOverflow.Clip
                                )
                                Text(
                                    text = updateState.releaseNotes,
                                    style = Theme.typography.body.medium,
                                    color = Theme.colorScheme.secondary.secondary,
                                    overflow = TextOverflow.Clip
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        if (updateState.isForceUpdate) {
                            PrimaryButton(
                                text = stringResource(Res.string.app_update_update_now),
                                onClick = onUpdateClick,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(Theme.spacing._12)
                            ) {
                                OutlinedButton(
                                    text = stringResource(Res.string.app_update_later),
                                    onClick = onLaterClick,
                                    modifier = Modifier.weight(1f)
                                )
                                PrimaryButton(
                                    text = stringResource(Res.string.app_update_update_now),
                                    onClick = onUpdateClick,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
