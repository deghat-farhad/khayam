package com.vuxur.khayyam.pages.poemList.view.poem_list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vuxur.khayyam.R

@Composable
fun ShareButton(
    modifier: Modifier = Modifier,
    onCopyText: () -> Unit,
    onShareText: () -> Unit,
    onShareImage: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    FloatingActionButton(
        modifier = modifier,
        onClick = { expanded = true }
    ) {
        Icon(
            imageVector = Icons.Filled.Share,
            contentDescription = null
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
    ) {
        DropdownMenuItem(
            onClick = {
                onCopyText()
                expanded = false
            },
            text = {
                Text(
                    text = stringResource(id = R.string.copyText)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.CopyAll,
                    contentDescription = null
                )
            }
        )
        DropdownMenuItem(
            onClick = {
                onShareText()
                expanded = false
            },
            text = {
                Text(
                    text = stringResource(id = R.string.shareText)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.TextSnippet,
                    contentDescription = null
                )
            }
        )
        DropdownMenuItem(
            onClick = {
                onShareImage()
                expanded = false
            },
            text = {
                Text(
                    text = stringResource(id = R.string.shareImage)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Image,
                    contentDescription = null
                )
            }
        )
    }
}