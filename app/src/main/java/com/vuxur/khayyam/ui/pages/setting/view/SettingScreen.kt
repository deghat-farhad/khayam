package com.vuxur.khayyam.ui.pages.setting.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vuxur.khayyam.R
import com.vuxur.khayyam.model.TimeOfDayItem
import com.vuxur.khayyam.model.TranslationItem
import com.vuxur.khayyam.model.TranslationOptionsItem
import com.vuxur.khayyam.ui.ui_components.DialWithDialogExample
import com.vuxur.khayyam.utils.formatForLocale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    selectedTranslationOptionItem: TranslationOptionsItem?,
    availableTranslations: List<TranslationItem>,
    onSpesificTranslationSelection: (TranslationOptionsItem.Specific) -> Unit,
    onPopBack: () -> Unit,
    onUseUntranslated: () -> Unit,
    onUseMatchSystemLanguageTranslation: () -> Unit,
    onSetRandomPoemNotificationTime: (TimeOfDayItem) -> Unit,
    isRandomPoemNotificationEnabled: Boolean,
    onToggleRandomPoemNotification: (enabled: Boolean) -> Unit,
    randomPoemNotificationTime: TimeOfDayItem?,
    isTimePickerVisible: Boolean,
    setTimePickerVisibility: (isVisible: Boolean) -> Unit,
    showNotificationPermissionDenialMessage: Boolean,
    isNotificationPermissionRationaleDialogVisible: Boolean,
    onDismissNotificationPermissionRationaleDialog: () -> Unit,
    onConfirmNotificationPermissionRationaleDialog: () -> Unit,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(showNotificationPermissionDenialMessage) {
        if (showNotificationPermissionDenialMessage)
            snackbarHostState.showSnackbar(
                message = context.getString(R.string.random_poem_permission_denial_message)
            )
    }

    if (isNotificationPermissionRationaleDialogVisible) {
        AlertDialog(
            onDismissRequest = onDismissNotificationPermissionRationaleDialog,
            title = { Text(stringResource(R.string.random_poem_notification_permission_rationale_title)) },
            text = {
                Text(stringResource(R.string.random_poem_notification_permission_rationale_caption))
            },
            confirmButton = {
                TextButton(onClick = onConfirmNotificationPermissionRationaleDialog) {
                    Text(stringResource(R.string.allow))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissNotificationPermissionRationaleDialog) {
                    Text(stringResource(R.string.no_thanks))
                }
            }
        )
    }

    if (isTimePickerVisible) {
        DialWithDialogExample(
            onConfirm = { timePickerState ->
                val timeOfDayItem = TimeOfDayItem(
                    hour = timePickerState.hour,
                    minute = timePickerState.minute,
                )
                onSetRandomPoemNotificationTime(timeOfDayItem)
            },
            onDismiss = {
                setTimePickerVisibility(false)
            },
            initialTime = randomPoemNotificationTime
        )
    }
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
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Color.Transparent
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(state = scrollState)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            Card(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.translation_settings_title),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(R.string.translation_settings_description),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    // Card 1: Original Language
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
                    // Card 2: System Language
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
                                val displayLanguage = when (selectedTranslationOptionItem) {
                                    is TranslationOptionsItem.MatchDeviceLanguage.Available -> selectedTranslationOptionItem.translation.displayLanguage
                                    is TranslationOptionsItem.MatchDeviceLanguage.Unavailable -> selectedTranslationOptionItem.fallBackTranslation.displayLanguage
                                }
                                val translator = when (selectedTranslationOptionItem) {
                                    is TranslationOptionsItem.MatchDeviceLanguage.Available -> selectedTranslationOptionItem.translation.translator
                                    is TranslationOptionsItem.MatchDeviceLanguage.Unavailable -> selectedTranslationOptionItem.fallBackTranslation.translator
                                }
                                Text(
                                    text = stringResource(
                                        R.string.selected_title,
                                        displayLanguage,
                                        translator,
                                    ),
                                    style = MaterialTheme.typography.bodySmall
                                )
                                // Display friendly fallback message if using fallback translation
                                if (selectedTranslationOptionItem is TranslationOptionsItem.MatchDeviceLanguage.Unavailable) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = stringResource(R.string.fallback_translation_message),
                                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary)
                                    )
                                }
                            }
                        }
                    }
                    // Card 3: Authentic Translations
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
            // Card 4: Random Poem Notification
            Card(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.random_poem_notification_settings_title),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(R.string.random_poem_notification_description),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isRandomPoemNotificationEnabled)
                                stringResource(R.string.disable_daily_poem_notification)
                            else
                                stringResource(R.string.enable_daily_poem_notification),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = isRandomPoemNotificationEnabled,
                            onCheckedChange = { onToggleRandomPoemNotification(it) }
                        )
                    }
                    if (isRandomPoemNotificationEnabled) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { setTimePickerVisibility(true) }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Schedule, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(
                                    R.string.random_poem_time_display,
                                    randomPoemNotificationTime?.formatForLocale(context)
                                        ?: "--:--"
                                ),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}