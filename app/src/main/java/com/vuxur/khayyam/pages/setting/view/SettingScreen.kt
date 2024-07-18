package com.vuxur.khayyam.pages.setting.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vuxur.khayyam.R
import com.vuxur.khayyam.model.LocaleItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    currentLocale: LocaleItem?,
    supportedLocales: List<LocaleItem.CustomLocale>,
    onLanguageSelected: (LocaleItem) -> Unit,
    onPopBack: () -> Unit,
    onOriginalLanguageSelected: () -> Unit,
    onSystemLanguageSelected: () -> Unit,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    val isAuthenticTranslation = supportedLocales.contains(currentLocale)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(
                        onClick = onPopBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.translation_settings),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(R.string.translation_setting_caption),
                    style = MaterialTheme.typography.bodyMedium
                )
                // Card 1: Original Language
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOriginalLanguageSelected() }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected =
                            (currentLocale as? LocaleItem.CustomLocale)?.isOriginal ?: false,
                            onClick = onOriginalLanguageSelected
                        )
                        Column(
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                stringResource(R.string.original_persian_language),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                stringResource(R.string.original_language_caption),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                // Card 2: System Language
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSystemLanguageSelected() }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentLocale is LocaleItem.SystemLocale,
                            onClick = onSystemLanguageSelected
                        )
                        Column(
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                stringResource(R.string.system_default),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.system_default_language_caption),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                // Card 3: Authentic Translations
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (!isAuthenticTranslation)
                                    dropdownExpanded = true
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isAuthenticTranslation,
                                onClick = {
                                    if (!isAuthenticTranslation)
                                        dropdownExpanded = true
                                }
                            )
                            Column(
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.authentic_translations),
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.authentic_translation_caption),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                // Display current selection
                                Spacer(modifier = Modifier.height(8.dp))
                                supportedLocales.firstOrNull {
                                    it == (currentLocale as? LocaleItem.CustomLocale)
                                }?.let {
                                    Text(
                                        text = stringResource(
                                            R.string.selected_title,
                                            it.locale.displayLanguage
                                        ),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                } ?: Text(
                                    text = stringResource(R.string.no_translation_selected),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                        // Dropdown menu for selecting translations
                        Box {
                            OutlinedButton(
                                modifier = Modifier.padding(bottom = 8.dp, start = 8.dp),
                                onClick = {
                                    dropdownExpanded = true
                                },
                            ) {
                                Text(stringResource(R.string.select_language))
                            }
                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false }
                            ) {
                                supportedLocales.forEach { translation ->
                                    DropdownMenuItem(
                                        onClick = {
                                            onLanguageSelected(translation)
                                            dropdownExpanded = false
                                        },
                                        text = {
                                            Text(translation.locale.displayLanguage)
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}