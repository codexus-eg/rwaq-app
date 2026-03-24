package com.khater.rwaq.presentation.screens.privacyPolicyScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.scaffold.HomeScaffold
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.composables.SnackBarContainer
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.util.LocalNavigationProvider
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.privacy_policy
import rwaq.composeapp.generated.resources.privacy_policy_content
import rwaq.composeapp.generated.resources.profile_screen

@Composable
fun PrivacyPolicyScreen(){

    val navController = LocalNavigationProvider.current
    HomeScaffold(
        hasStatusBarColor = true,
        snackBarState = SnackBarState(),
        topBar = {
            RwaqTopBar(
                containerColor = Color(0xFFFBF7F0),
                leadingContent = {
                    RwaqBackButton(
                        onClick = { navController.navigateUp() },
                        hasDropShadow = true,
                        tint = Theme.colorScheme.shadePrimary
                    )
                },
                middleContent = {
                    com.khater.rwaq.designSystem.component.text.Text(
                        text = stringResource(Res.string.privacy_policy),
                        color = Theme.colorScheme.shadePrimary,
                        style = Theme.typography.headline.medium,
                        modifier = Modifier.offset(x = (-22).dp)
                    )
                },
                isCenterAligned = true,
            )
        },
    ) {
        Column  (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding( 16.dp)
        ) {
        com.khater.rwaq.designSystem.component.text.Text(
            text = stringResource(Res.string.privacy_policy_content),
            color = Theme.colorScheme.shadePrimary,
            style = Theme.typography.body.medium,
        )}
    }
}