package com.khater.rwaq.presentation.screens.profileScreen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.button.PrimaryButton
import com.khater.rwaq.designSystem.component.icon.Icon
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.domain.util.AppLanguage

import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.confirm
import rwaq.composeapp.generated.resources.ic_arabic
import rwaq.composeapp.generated.resources.ic_cancle
import rwaq.composeapp.generated.resources.ic_english
import rwaq.composeapp.generated.resources.language

@Composable
fun LanguageBottomSheet(
    appLanguages: List<AppLanguage>,
    currentAppLanguage: AppLanguage,
    selectedAppLanguage: AppLanguage,
    onDismissRequest: () -> Unit,
    onConfirmLanguageSelection: () -> Unit,
    onLanguageChanged: (AppLanguage) -> Unit,
) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .dropShadow(
                shape = RoundedCornerShape(
                    topEnd = Theme.spacing._12,
                    topStart = Theme.spacing._12
                ), shadow = Shadow(
                    radius = Theme.spacing._12,
                    spread = 0.dp,
                    color = Color.Black.copy(alpha = .06f),
                    offset = DpOffset(0.dp, (-2).dp)
                )
            )
            .background(
                color = Theme.colorScheme.brand.onBrand,
                shape = RoundedCornerShape(topEnd = Theme.spacing._12, topStart = Theme.spacing._12)
            )
            .padding(
                start = Theme.spacing._16,
                end = Theme.spacing._16,
                top = Theme.spacing._16,
                bottom = Theme.spacing._49
            ),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing._24)
    ) {

        Icon(
            painter = painterResource(Res.drawable.ic_cancle),
            contentDescription = "Cancel",
            modifier = Modifier.size(28.dp)
                .clickable(
                    onClick = onDismissRequest
                )
        )
        Text(
            text = stringResource(Res.string.language),
            style = Theme.typography.title.large,
            color = Theme.colorScheme.primary.primary,
            modifier = Modifier.fillMaxWidth().padding(top = Theme.spacing._8),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing._16)
        ) {
            appLanguages.forEach {
                LanguageOptionItem(
                    isSelected = it == selectedAppLanguage,
                    selectedAppLanguage = it,
                    modifier = Modifier.weight(1f),
                    icon = if (it == AppLanguage.ARABIC) painterResource(Res.drawable.ic_arabic) else painterResource(
                        Res.drawable.ic_english
                    ),
                    onClick = {
                        onLanguageChanged(it)
                    })
            }
        }
        PrimaryButton(
            text = stringResource(Res.string.confirm),
            onClick = { onConfirmLanguageSelection() },
            modifier = Modifier.fillMaxWidth()
        )


    }
//    Box(
//        contentAlignment = Alignment.TopCenter,
//    ) {
//        LazyColumn(
//            modifier = Modifier.padding(vertical = Theme.spacing._12),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(Theme.spacing._4)
//        ) {
//            item {
//                Text(
//                    modifier = Modifier.padding(bottom = 20.dp),
//                    text = stringResource(Res.string.language),
//                    color = Theme.colorScheme.shadePrimary,
//                    style = Theme.typography.title.small,
//                )
//            }
//            items(appLanguages, key = { it.iso }) {
//                LanguageOptionItem(
//                    isSelected = it == selectedAppLanguage,
//                    selectedAppLanguage = it,
//                    icon = if (it == AppLanguage.ARABIC) painterResource(Res.drawable.ic_arabic) else painterResource(
//                        Res.drawable.ic_english
//                    ),
//                    onClick = {
//                        onLanguageChanged(it)
//                    })
//            }
//            item {
//                PrimaryButton(
//                    text = stringResource(Res.string.save),
//                    isEnabled = selectedAppLanguage != currentAppLanguage,
//                    onClick = { onConfirmLanguageSelection() },
//                    modifier = Modifier.padding(top = 20.dp).fillMaxWidth()
//                )
//            }
//        }
//    }

}
