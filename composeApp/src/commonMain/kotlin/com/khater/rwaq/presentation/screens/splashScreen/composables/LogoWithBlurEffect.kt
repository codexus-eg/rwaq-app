package com.khater.rwaq.presentation.screens.splashScreen.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.logo
import rwaq.composeapp.generated.resources.rwaq_logo

@Composable
fun LogoWithBlurEffect(){
    Box(modifier = Modifier, contentAlignment = Alignment.Center) {

         Image(
            painter = painterResource(Res.drawable.rwaq_logo),
            contentDescription = "Logo",
            modifier = Modifier.height(180.dp),
            contentScale = ContentScale.Inside
         )
    }
}