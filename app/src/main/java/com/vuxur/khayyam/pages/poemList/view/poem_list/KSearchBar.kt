package com.vuxur.khayyam.pages.poemList.view.poem_list

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.vuxur.khayyam.R
import com.vuxur.khayyam.model.LocaleItem
import com.vuxur.khayyam.utils.getPoemsFontFamily
import com.vuxur.khayyam.utils.getPoemsFontSize
import com.vuxur.khayyam.utils.toLayoutDirection

@Composable
fun KSearchBar(
    modifier: Modifier = Modifier,
    onChange: ((String) -> Unit)? = null,
    onNextResult: (String) -> Unit,
    onPreviousResult: (String) -> Unit,
    isThereAnyResult: Boolean,
    isThereNextResult: Boolean,
    isTherePreviousResult: Boolean,
    localeItem: LocaleItem.CustomLocale,
) {
    var searchPhrase by remember { mutableStateOf("") }
    val isError = searchPhrase.isNotEmpty() && !isThereAnyResult

    val fontFamily = remember(localeItem) {
        getPoemsFontFamily(localeItem)
    }

    val fontSize = remember(localeItem) {
        getPoemsFontSize(localeItem)
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .height(56.dp)
            .paint(
                painter = painterResource(
                    id =
                    if (isSystemInDarkTheme())
                        R.drawable.paper_dark
                    else
                        R.drawable.paper_light
                ),
                contentScale = ContentScale.Crop
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                isError = isError,
                modifier = Modifier.weight(0.1f),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                ),
                value = searchPhrase,
                onValueChange = {
                    searchPhrase = it
                    onChange?.invoke(it)
                },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = stringResource(id = R.string.search)) },
                placeholder = { Text(text = stringResource(id = R.string.search)) },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                textStyle = LocalTextStyle.current.copy(
                    fontFamily = fontFamily,
                    fontSize = fontSize,
                    color = if (isError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        LocalTextStyle.current.color
                    }
                )
            )
            CompositionLocalProvider(LocalLayoutDirection provides localeItem.locale.toLayoutDirection()) {
                Row {
                    NavigationIconButton(
                        onClick = { onPreviousResult(searchPhrase) },
                        enabled = isTherePreviousResult,
                        contentDescription = stringResource(R.string.previous_result),
                        icon = if (LocalLayoutDirection.current == LayoutDirection.Rtl)
                            Icons.Filled.KeyboardArrowRight
                        else
                            Icons.Filled.KeyboardArrowLeft
                    )

                    NavigationIconButton(
                        onClick = { onNextResult(searchPhrase) },
                        enabled = isThereNextResult,
                        contentDescription = stringResource(R.string.next_result),
                        icon = if (LocalLayoutDirection.current == LayoutDirection.Rtl)
                            Icons.Filled.KeyboardArrowLeft
                        else
                            Icons.Filled.KeyboardArrowRight
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationIconButton(
    onClick: () -> Unit,
    enabled: Boolean,
    contentDescription: String,
    icon: ImageVector
) {
    IconButton(
        onClick = onClick,
        enabled = enabled
    ) {
        Icon(
            modifier = Modifier.alpha(if (enabled) 1f else .3f),
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
