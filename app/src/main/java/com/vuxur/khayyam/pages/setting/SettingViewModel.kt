package com.vuxur.khayyam.pages.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vuxur.khayyam.domain.model.TranslationOptions
import com.vuxur.khayyam.domain.usecase.settings.randomPoemNotification.isEnabled.isRandomPoemNotificationEnabled.IsRandomPoemNotificationEnabled
import com.vuxur.khayyam.domain.usecase.settings.randomPoemNotification.isEnabled.setRandomPoemNotificationsEnabled.SetRandomPoemNotificationEnabled
import com.vuxur.khayyam.domain.usecase.settings.randomPoemNotification.isEnabled.setRandomPoemNotificationsEnabled.SetRandomPoemNotificationEnabledParams
import com.vuxur.khayyam.domain.usecase.settings.randomPoemNotification.time.getRandomPoemNotificationTime.GetRandomPoemNotificationTime
import com.vuxur.khayyam.domain.usecase.settings.randomPoemNotification.time.setRandomPoemNotificationTime.SetRandomPoemNotificationTime
import com.vuxur.khayyam.domain.usecase.settings.randomPoemNotification.time.setRandomPoemNotificationTime.SetRandomPoemNotificationTimeParams
import com.vuxur.khayyam.domain.usecase.settings.translation.getSelectedTranslationOption.GetSelectedTranslationOption
import com.vuxur.khayyam.domain.usecase.settings.translation.getSelectedTranslationOption.GetSelectedTranslationOptionParams
import com.vuxur.khayyam.domain.usecase.settings.translation.getTranslations.GetAvailableTranslations
import com.vuxur.khayyam.domain.usecase.settings.translation.useMatchingSystemLanguageTranslation.UseMatchSystemLanguageTranslation
import com.vuxur.khayyam.domain.usecase.settings.translation.useSpecificTranslation.UseSpecificTranslation
import com.vuxur.khayyam.domain.usecase.settings.translation.useSpecificTranslation.UseSpecificTranslationParams
import com.vuxur.khayyam.domain.usecase.settings.translation.useUntranslated.UseUntranslated
import com.vuxur.khayyam.mapper.TimeOfDayItemMapper
import com.vuxur.khayyam.mapper.TranslationItemMapper
import com.vuxur.khayyam.mapper.TranslationOptionsItemMapper
import com.vuxur.khayyam.model.TimeOfDayItem
import com.vuxur.khayyam.model.TranslationItem
import com.vuxur.khayyam.model.TranslationOptionsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getSelectedTranslationOption: GetSelectedTranslationOption,
    private val getAvailableTranslations: GetAvailableTranslations,
    private val translationOptionsItemMapper: TranslationOptionsItemMapper,
    private val useSpecificTranslation: UseSpecificTranslation,
    private val translationItemMapper: TranslationItemMapper,
    private val useMatchSystemLanguageTranslation: UseMatchSystemLanguageTranslation,
    private val useUntranslated: UseUntranslated,
    private val setRandomPoemNotificationTime: SetRandomPoemNotificationTime,
    private val getRandomPoemNotificationTime: GetRandomPoemNotificationTime,
    private val timeOfDayItemMapper: TimeOfDayItemMapper,
    private val setRandomPoemNotificationEnabled: SetRandomPoemNotificationEnabled,
    private val isRandomPoemNotificationEnabled: IsRandomPoemNotificationEnabled,
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun viewIsReady(deviceLocale: Locale) {
        viewModelScope.launch {
            _uiState.value = UiState.Loaded(
                availableTranslations =
                translationItemMapper.mapToPresentation(getAvailableTranslations())
                    .filterNot { it.isUntranslated() },
                isRandomPoemNotificationEnabled = isRandomPoemNotificationEnabled.invoke().first(),
                randomPoemNotificationTime = getRandomPoemNotificationTime.invoke()
                    .map { timeOfDay ->
                        timeOfDay?.let {
                            timeOfDayItemMapper.mapToPresentation(timeOfDay)
                        }
                    }.first()
            )
            onSelectedTranslationOptionChange(deviceLocale)
            onRandomPoemNotificationEnabledChange()
            onRandomPoemNotificationTimeChange()
        }
        viewModelScope.launch {
            getRandomPoemNotificationTime.invoke().collect {
                println(it)
            }
        }
    }

    fun setSpecificTranslation(translationOptionsItem: TranslationOptionsItem.Specific) {
        val translationOption =
            translationOptionsItemMapper.mapToDomain(translationOptionsItem) as? TranslationOptions.Specific
        translationOption?.let {
            viewModelScope.launch {
                useSpecificTranslation(
                    UseSpecificTranslationParams(
                        translationOption
                    )
                )
            }
        }
    }

    fun setRandomPoemNotificationTime(timeOfDayItem: TimeOfDayItem) {
        val timeOfDay = timeOfDayItemMapper.mapToDomain(timeOfDayItem)
        val setRandomPoemNotificationTimeParams = SetRandomPoemNotificationTimeParams(
            timeOfDay = timeOfDay
        )
        viewModelScope.launch {
            setRandomPoemNotificationTime(setRandomPoemNotificationTimeParams)
        }
        setTimePickerVisibility(isVisible = false)
    }

    fun setRandomPoemNotificationEnabled(isEnabled: Boolean) {
        (_uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
            viewModelScope.launch {
                val params = SetRandomPoemNotificationEnabledParams(isEnabled = isEnabled)

                if (!isEnabled) {
                    setRandomPoemNotificationEnabled(params)
                    return@launch
                }

                if (uiStateSnapshot.randomPoemNotificationTime == null) {
                    setTimePickerVisibility(true)
                    getRandomPoemNotificationTime.invoke()
                        .filterNotNull()
                        .first()
                }

                setRandomPoemNotificationEnabled(params)
            }
        }
    }

    fun setTimePickerVisibility(isVisible: Boolean) {
        _uiState.update { current ->
            if (current is UiState.Loaded)
                current.copy(isTimePickerVisible = isVisible)
            else
                current
        }
    }

    fun popBack() {
        consumeEvent(Event.PopBack)
    }

    fun onEventConsumed(
        eventConsumed: Event,
    ) {
        _uiState.update { current ->
            if (current is UiState.Loaded)
                current.copy(
                    events = current.events.filterNot { event ->
                        event == eventConsumed
                    },
                )
            else
                current
        }
    }

    fun setToUseUntranslated() {
        viewModelScope.launch {
            useUntranslated()
        }
    }

    fun setToUseMatchSystemLanguageTranslation() {
        viewModelScope.launch {
            useMatchSystemLanguageTranslation()
        }
    }

    private fun consumeEvent(
        eventToConsume: Event,
    ) {
        _uiState.update { current ->
            if (current is UiState.Loaded)
                current.copy(
                    events = (current.events + eventToConsume)
                )
            else
                current
        }
    }

    private fun onSelectedTranslationOptionChange(deviceLocale: Locale) {
        viewModelScope.launch {
            val getSelectedTranslationOptionParams =
                GetSelectedTranslationOptionParams(deviceLocale)
            getSelectedTranslationOption(getSelectedTranslationOptionParams).collect { translationOption ->
                _uiState.update { current ->
                    if (current is UiState.Loaded)
                        current.copy(
                            selectedTranslationOption =
                            translationOptionsItemMapper.mapToPresentation(translationOption)
                        )
                    else
                        current
                }
            }
        }
    }

    private fun onRandomPoemNotificationEnabledChange() {
        viewModelScope.launch {
            isRandomPoemNotificationEnabled.invoke()
                .collect { isRandomPoemNotificationEnabled ->
                    _uiState.update { current ->
                        if (current is UiState.Loaded) {
                            current.copy(isRandomPoemNotificationEnabled = isRandomPoemNotificationEnabled)
                        } else current
                    }
                }
        }
    }

    private fun onRandomPoemNotificationTimeChange() {
        viewModelScope.launch {
            getRandomPoemNotificationTime.invoke().collect { randomPoemNotificationTime ->
                _uiState.update { current ->
                    if (current is UiState.Loaded && randomPoemNotificationTime != null)
                        current.copy(
                            randomPoemNotificationTime =
                            timeOfDayItemMapper.mapToPresentation(
                                randomPoemNotificationTime
                            ),
                        )
                    else
                        current
                }
            }
        }
    }

    sealed class UiState {
        data object Loading : UiState()
        data class Loaded(
            val availableTranslations: List<TranslationItem>,
            val selectedTranslationOption: TranslationOptionsItem? = null,
            val events: List<Event> = emptyList(),
            val isRandomPoemNotificationEnabled: Boolean,
            val randomPoemNotificationTime: TimeOfDayItem? = null,
            val isTimePickerVisible: Boolean = false,
        ) : UiState()
    }

    sealed class Event {
        data object PopBack : Event()
    }
}