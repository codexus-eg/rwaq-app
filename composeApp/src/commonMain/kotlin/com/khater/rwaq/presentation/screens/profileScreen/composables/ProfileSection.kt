package com.khater.rwaq.presentation.screens.profileScreen.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.adamglin.composeshadow.dropShadow
import com.khater.rwaq.designSystem.component.icon.Icon
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import org.jetbrains.compose.resources.painterResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.hand
import rwaq.composeapp.generated.resources.vip

@Composable
fun ProfileSection(
    painter: Painter,
    username: String,
    phoneNumber: String,
    isVipUser: Boolean,
    modifier: Modifier = Modifier
){

    Row(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF001E14).copy(0.04f),
                blur = 20.dp,
                offsetY = 2.dp,
                offsetX = 0.dp
            )
            .clip(RoundedCornerShape(Theme.spacing._24))
            .background(Theme.colorScheme.brand.onBrand)
            .padding(Theme.spacing._16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing._12)
    ) {
        Image(
            painter = painter,
            contentDescription = "Profile Image",
            modifier = Modifier.size(52.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Theme.spacing._12),
            horizontalAlignment = Alignment.Start
        ) {
            if (username.isNotBlank())
             Text(
                text = username,
                style = Theme.typography.title.small,
                color = Theme.colorScheme.primary.primary,
                 modifier = Modifier.animateContentSize()
            )
             Text(
                text = phoneNumber,
                style = Theme.typography.body.small,
                color = Theme.colorScheme.secondary.secondary,
                 modifier = Modifier.animateContentSize()

             )
        }


            val matrix = ColorMatrix().apply { setToSaturation(0f) }
            Image(
                painter = painterResource(Res.drawable.vip),
                contentDescription = "Vip Icon",
                colorFilter = if (isVipUser) null else ColorFilter.colorMatrix(matrix),
                modifier = Modifier.size(52.dp)
            )

    }

}