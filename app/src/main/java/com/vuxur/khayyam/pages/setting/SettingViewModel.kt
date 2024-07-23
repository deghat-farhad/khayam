package com.vuxur.khayyam.pages.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vuxur.khayyam.domain.usecase.getSelectedTranslationOption.GetSelectedTranslationOption
import com.vuxur.khayyam.domain.usecase.getTranslations.GetAvailableTranslations
import com.vuxur.khayyam.domain.usecase.useMatchingSystemLanguageTranslation.UseMatchSystemLanguageTranslation
import com.vuxur.khayyam.domain.usecase.useSpecificTranslation.UseSpecificTranslation
import com.vuxur.khayyam.domain.usecase.useSpecificTranslation.UseSpecificTranslationParams
import com.vuxur.khayyam.domain.usecase.useUntranslated.UseUntranslated
import com.vuxur.khayyam.mapper.TranslationItemMapper
import com.vuxur.khayyam.mapper.TranslationOptionsItemMapper
import com.vuxur.khayyam.model.TranslationItem
import com.vuxur.khayyam.model.TranslationOptionsItem
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun viewIsReady() {
        viewModelScope.launch {
            _uiState.value = UiState.Loaded(
                availableTranslations =
                translationItemMapper.mapToPresentation(getAvailableTranslations())
                    .filterNot { it.isUntranslated() }
            )
            onSelectedTranslationOptionChange()
        }
    }

    fun setSpecificTranslation(translationOptionsItem: TranslationOptionsItem.Specific) {
        viewModelScope.launch {
            useSpecificTranslation(
                UseSpecificTranslationParams(
                    translationOptionsItemMapper.mapToDomain(translationOptionsItem)
                )
            )
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

    private fun onSelectedTranslationOptionChange() {
        (_uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
            viewModelScope.launch {
                getSelectedTranslationOption().collect { translationOption ->
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