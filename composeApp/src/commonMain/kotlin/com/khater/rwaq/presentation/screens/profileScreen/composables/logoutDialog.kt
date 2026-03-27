package com.khater.rwaq.presentation.screens.profileScreen.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.button.OutlinedButton
import com.khater.rwaq.designSystem.component.button.PrimaryButton
import com.khater.rwaq.designSystem.component.dialog.BasicDialog
import com.khater.rwaq.designSystem.component.scaffold.ScaffoldScope
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.are_you_sure_you_want_to_logout
import rwaq.composeapp.generated.resources.back
import rwaq.composeapp.generated.resources.logout

fun ScaffoldScope.logoutDialog(
    showLogoutDialog: Boolean,
    isLoading: Boolean,
    onLogoutClick: () -> Unit = { },
    onDismissDialog: () -> Unit = { },
    ) {

    dialog(showLogoutDialog) {
        BasicDialog(
            isVisible = showLogoutDialog,
            onDismiss = onDismissDialog,
            onCancelClick = onDismissDialog,
            actionButtons = {

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = Theme.spacing._24, bottom = Theme.spacing._8),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Theme.spacing._12)
                ) {
                    OutlinedButton(
                        text = stringResource(Res.string.back),
                        onClick = onDismissDialog,
                        modifier = Modifier.weight(1f)
                            .height(45.dp),
                        style = Theme.typography.body.medium
                    )
                    PrimaryButton(
                        text = stringResource(Res.string.logout),
                        onClick = onLogoutClick,
                        modifier = Modifier.weight(1f).height(45.dp),
                        containerColor = Theme.colorScheme.error,
                        isLoading = isLoading,
                        isEnabled = !isLoading,
                        style = Theme.typography.body.medium
                    )
                }
            }
        ) {
            Text(
                text = stringResource(Res.string.are_you_sure_you_want_to_logout),
                style = Theme.typography.title.small,
                color = Theme.colorScheme.primary.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = Theme.spacing._12)
            )
        }
    }
}


fun ScaffoldScope.confirmationDialog(
    isVisible: Boolean,
    isLoading: Boolean,
    title: StringResource,
    confirmButtonText:  StringResource,
    onConfirmClick: () -> Unit,
    onDismissDialog: () -> Unit,
) {
    dialog(isVisible) {
        BasicDialog(
            isVisible = isVisible,
            onDismiss = onDismissDialog,
            onCancelClick = onDismissDialog,
            actionButtons = {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = Theme.spacing._24, bottom = Theme.spacing._8),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Theme.spacing._12)
                ) {
                    OutlinedButton(
                        text = stringResource(Res.string.back),
                        onClick = onDismissDialog,
                        modifier = Modifier.weight(1f).height(45.dp),
                        style = Theme.typography.body.medium
                    )
                    PrimaryButton(
                        text = stringResource(confirmButtonText),
                        onClick = onConfirmClick,
                        modifier = Modifier.weight(1f).height(45.dp),
                        containerColor = Theme.colorScheme.error,
                        isLoading = isLoading,
                        isEnabled = !isLoading,
                        style = Theme.typography.body.medium
                    )
                }
            }
        ) {
            Text(
                text = stringResource(title),
                style = Theme.typography.title.small,
                color = Theme.colorScheme.primary.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = Theme.spacing._12)
            )
        }
    }
}