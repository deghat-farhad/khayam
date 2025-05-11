package com.vuxur.khayyam.ui.pages.setting.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import com.vuxur.khayyam.entry.user.Activity
import com.vuxur.khayyam.ui.pages.setting.SettingViewModel
import com.vuxur.khayyam.utils.getCurrentLocale

@Composable
fun SettingRoute(
    viewModel: SettingViewModel,
    popBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.onNotificationPermissionResult(isGranted)
        }
    )

    LaunchedEffect(key1 = true) {
        viewModel.viewIsReady(getCurrentLocale(context.resources))
    }

    var showNotificationPermissionDenialMessage = remember { mutableStateOf(false) }

    DisposableEffect(uiState) {
        (uiState as? SettingViewModel.UiState.Loaded)?.let { uiStateSnapshot ->
            uiStateSnapshot.events.forEach { event ->
                when (event) {
                    SettingViewModel.Event.PopBack -> popBack()
                    SettingViewModel.Event.RequestPostNotificationPermission -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (canShowNotificationPermissionRequestDialog(context)
                            )
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            else
                                navigateToAppSettings(context)
                        }
                    }

                    SettingViewModel.Event.ShowPermissionDenialMessage -> {
                        showNotificationPermissionDenialMessage.value = true
                    }
                }
                viewModel.onEventConsumed(event)
            }
        }
        onDispose { }
    }
    (uiState as? SettingViewModel.UiState.Loaded)?.let { uiStateSnapshot ->
        SettingScreen(
            availableTranslations = uiStateSnapshot.availableTranslations,
            selectedTranslationOptionItem = uiStateSnapshot.selectedTranslationOption,
            onPopBack = viewModel::popBack,
            onSpesificTranslationSelection = { specificTranslationItem ->
                viewModel.setSpecificTranslation(specificTranslationItem)
            },
            onUseUntranslated = viewModel::setToUseUntranslated,
            onUseMatchSystemLanguageTranslation = viewModel::setToUseMatchSystemLanguageTranslation,
            onSetRandomPoemNotificationTime = viewModel::setRandomPoemNotificationTime,
            onToggleRandomPoemNotification = viewModel::setRandomPoemNotificationEnabled,
            isRandomPoemNotificationEnabled = uiStateSnapshot.isRandomPoemNotificationEnabled,
            randomPoemNotificationTime = uiStateSnapshot.randomPoemNotificationTime,
            isTimePickerVisible = uiStateSnapshot.isTimePickerVisible,
            setTimePickerVisibility = viewModel::setTimePickerVisibility,
            showNotificationPermissionDenialMessage = showNotificationPermissionDenialMessage.value,
            isNotificationPermissionRationaleDialogVisible = uiStateSnapshot.isNotificationPermissionRationaleDialogVisible,
            onDismissNotificationPermissionRationaleDialog = viewModel::onDismissNotificationPermissionRationaleDialog,
            onConfirmNotificationPermissionRationaleDialog = viewModel::onConfirmNotificationPermissionRationaleDialog,
        )
    }
}

private fun navigateToAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = "package:${context.packageName}".toUri()
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun canShowNotificationPermissionRequestDialog(context: Context): Boolean {
    return !ActivityCompat.shouldShowRequestPermissionRationale(
        context as Activity, Manifest.permission.POST_NOTIFICATIONS
    )
}