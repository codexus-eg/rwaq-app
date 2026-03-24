package com.khater.rwaq.presentation.screens.homeScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khater.rwaq.designSystem.theme.theme.Theme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.choose_branch
import rwaq.composeapp.generated.resources.logo
import rwaq.composeapp.generated.resources.rwaq_logo
import rwaq.composeapp.generated.resources.welcome

@Composable
fun ProfileSection(
    modifier: Modifier = Modifier,
    username: String?
) {
    Row( modifier = modifier.fillMaxWidth()
          ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween){
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(Res.string.welcome),
                color = Color(0xFF282828),
                style = Theme.typography.body.medium
                    .copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp
                    ),
            )
            username?.let {
                Text(
                    text = it,
                    color = Color(0xFF000D09),
                    style = Theme.typography.body.medium
                        .copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp
                        ),
                )
            }
        }

        Image(
            painter = painterResource(Res.drawable.rwaq_logo),
            contentDescription = "Logo",
            modifier = Modifier.height(100.dp),
            contentScale = ContentScale.Inside
        )
    }
}