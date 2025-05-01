package com.vuxur.khayyam.pages.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vuxur.khayyam.domain.model.TranslationOptions
import com.vuxur.khayyam.domain.usecase.settings.translation.getSelectedTranslationOption.GetSelectedTranslationOption
import com.vuxur.khayyam.domain.usecase.settings.translation.getSelectedTranslationOption.GetSelectedTranslationOptionParams
import com.vuxur.khayyam.domain.usecase.settings.translation.getTranslations.GetAvailableTranslations
import com.vuxur.khayyam.domain.usecase.settings.translation.useMatchingSystemLanguageTranslation.UseMatchSystemLanguageTranslation
import com.vuxur.khayyam.domain.usecase.settings.translation.useSpecificTranslation.UseSpecificTranslation
import com.vuxur.khayyam.domain.usecase.settings.translation.useSpecificTranslation.UseSpecificTranslationParams
import com.vuxur.khayyam.domain.usecase.settings.translation.useUntranslated.UseUntranslated
import com.vuxur.khayyam.mapper.TranslationItemMapper
import com.vuxur.khayyam.mapper.TranslationOptionsItemMapper
import com.vuxur.khayyam.model.TranslationItem
import com.vuxur.khayyam.model.TranslationOptionsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun viewIsReady(deviceLocale: Locale) {
        viewModelScope.launch {
            _uiState.value = UiState.Loaded(
                availableTranslations =
                translationItemMapper.mapToPresentation(getAvailableTranslations())
                    .filterNot { it.isUntranslated() }
            )
            onSelectedTranslationOptionChange(deviceLocale)
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

    fun popBack() {
        consumeEvent(Event.PopBack)
    }

    fun onEventConsumed(
        eventConsumed: Event,
    ) {
        (_uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
            _uiState.value = uiStateSnapshot.copy(
                events = uiStateSnapshot.events.filterNot { event ->
                    event == eventConsumed
                },
            )
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
        (_uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
            _uiState.value = uiStateSnapshot.copy(
                events = (uiStateSnapshot.events + eventToConsume)
            )
        }
    }

    private fun onSelectedTranslationOptionChange(deviceLocale: Locale) {
        (_uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
            viewModelScope.launch {
                val getSelectedTranslationOptionParams =
                    GetSelectedTranslationOptionParams(deviceLocale)
                getSelectedTranslationOption(getSelectedTranslationOptionParams).collect { translationOption ->
                    _uiState.value = uiStateSnapshot.copy(
                        selectedTranslationOption =
                        translationOptionsItemMapper.mapToPresentation(translationOption)
                    )
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
        ) : UiState()
    }

    sealed class Event {
        data object PopBack : Event()
    }
}