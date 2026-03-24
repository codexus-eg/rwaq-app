package com.khater.rwaq.presentation.screens.homeScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.textField.TextField
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.search
import rwaq.composeapp.generated.resources.search_here

@Composable
fun SearchBarSection(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shouldClearFocus: Boolean = false, // Pass this as true when bottom sheet dismisses
    onFocusCleared: () -> Unit = {}
) {

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Watch for the trigger to clear focus (e.g., when bottom sheet closes)
    LaunchedEffect(shouldClearFocus) {
        if (shouldClearFocus) {
            focusManager.clearFocus()
            onFocusCleared()
        }
    }


    TextField(
        value = "",
        hint = stringResource(Res.string.search_here),
        onValueChanged = {},
        readOnly = true,
        modifier = modifier.fillMaxWidth().padding(vertical = 10.dp )
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    onClick()
                }
            }.background(Color.White),
        shape = CircleShape,
        leadingIcon = painterResource(Res.drawable.search),
        onFocusChanged = { isFocused ->
            if (isFocused) onClick()
        }
    )
}