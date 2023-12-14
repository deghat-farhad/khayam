package com.raven.khayam.poemList.view.poem_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction

@Composable
fun KSearchBar(
    modifier: Modifier = Modifier,
    onChange: ((String) -> Unit)? = null,
    onSearch: ((String) -> Unit)? = null,
    onNextResult: (String) -> Unit,
    onPreviousResult: (String) -> Unit,
    isThereAnyResult: Boolean,
    isThereNextResult: Boolean,
    isTherePreviousResult: Boolean,
) {
    var searchPhrase by remember { mutableStateOf("") }
    val isError = searchPhrase.isNotEmpty() && !isThereAnyResult
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(
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
                ),
                value = searchPhrase,
                onValueChange = {
                    searchPhrase = it
                    onChange?.invoke(it)
                },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "") },
                placeholder = { Text(text = "Search") },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onSearch?.invoke(searchPhrase)
                }),
                textStyle = if (isError) {
                    TextStyle(color = MaterialTheme.colorScheme.error)
                } else {
                    LocalTextStyle.current
                }
            )
            IconButton(
                onClick = { onPreviousResult(searchPhrase) },
                enabled = isTherePreviousResult
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                )
            }
            IconButton(
                onClick = { onNextResult(searchPhrase) },
                enabled = isThereNextResult
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = null,
                )
            }
        }
    }
}