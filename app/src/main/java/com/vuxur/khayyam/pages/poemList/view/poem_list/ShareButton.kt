package com.vuxur.khayyam.pages.poemList.view.poem_list

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TextSnippet
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
    Box {
        FloatingActionButton(
            modifier = modifier,
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = stringResource(R.string.share)
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
                        contentDescription = stringResource(id = R.string.copyText)
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
                        imageVector = Icons.AutoMirrored.Filled.TextSnippet,
                        contentDescription = stringResource(id = R.string.shareText)
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
                        contentDescription = stringResource(id = R.string.shareImage)
                    )
                }
            )
        }
    }
}