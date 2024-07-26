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
import com.vuxur.khayyam.model.TranslationItem
import com.vuxur.khayyam.model.TranslationOptionsItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    selectedTranslationOptionItem: TranslationOptionsItem?,
    availableTranslations: List<TranslationItem>,
    onSpesificTranslationSelection: (TranslationOptionsItem.Specific) -> Unit,
    onPopBack: () -> Unit,
    onUseUntranslated: () -> Unit,
    onUseMatchSystemLanguageTranslation: () -> Unit,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

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
                    text = stringResource(R.string.translation_settings_title),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(R.string.translation_settings_description),
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
                            .clickable { onUseUntranslated() }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected =
                            selectedTranslationOptionItem is TranslationOptionsItem.Untranslated,
                            onClick = onUseUntranslated
                        )
                        Column(
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                stringResource(R.string.original_persian_translation_title),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                stringResource(R.string.original_persian_translation_description),
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
                            .clickable { onUseMatchSystemLanguageTranslation() }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedTranslationOptionItem is TranslationOptionsItem.MatchDeviceLanguage,
                            onClick = onUseMatchSystemLanguageTranslation
                        )
                        Column(
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                stringResource(R.string.system_default_translation_title),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.system_default_translation_description),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            // Display current selection
                            Spacer(modifier = Modifier.height(8.dp))
                            if (selectedTranslationOptionItem is TranslationOptionsItem.MatchDeviceLanguage) {
                                Text(
                                    text = stringResource(
                                        R.string.selected_title,
                                        selectedTranslationOptionItem.translation.displayLanguage,
                                        selectedTranslationOptionItem.translation.translator
                                    ),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
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
                                if (selectedTranslationOptionItem !is TranslationOptionsItem.Specific)
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
                                selected = selectedTranslationOptionItem is TranslationOptionsItem.Specific,
                                onClick = {
                                    if (selectedTranslationOptionItem !is TranslationOptionsItem.Specific)
                                        dropdownExpanded = true
                                }
                            )
                            Column(
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.authentic_translations_title),
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.authentic_translations_description),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                // Display current selection
                                Spacer(modifier = Modifier.height(8.dp))
                                if (selectedTranslationOptionItem is TranslationOptionsItem.Specific) {
                                    Text(
                                        text = stringResource(
                                            R.string.selected_title,
                                            selectedTranslationOptionItem.translation.displayLanguage,
                                            selectedTranslationOptionItem.translation.translator
                                        ),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                } else {
                                    Text(
                                        text = stringResource(R.string.no_translation_selected),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
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
                                Text(stringResource(R.string.select_translation))
                            }
                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false }
                            ) {
                                availableTranslations.forEach { translation ->
                                    DropdownMenuItem(
                                        onClick = {
                                            onSpesificTranslationSelection(
                                                TranslationOptionsItem.Specific(
                                                    translation
                                                )
                                            )
                                            dropdownExpanded = false
                                        },
                                        text = {
                                            Text("${translation.displayLanguage} - ${translation.translator}")
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