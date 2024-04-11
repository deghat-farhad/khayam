package com.vuxur.khayyam.pages.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vuxur.khayyam.domain.usecase.getSelectedPoemLocale.GetSelectedPoemLocale
import com.vuxur.khayyam.domain.usecase.getSupportedLocales.GetSupportedLocale
import com.vuxur.khayyam.domain.usecase.setSelectedPoemLocale.SetSelectedPoemLocale
import com.vuxur.khayyam.domain.usecase.setSelectedPoemLocale.SetSelectedPoemLocaleParams
import com.vuxur.khayyam.mapper.LocaleItemMapper
import com.vuxur.khayyam.model.LocaleItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getSelectedPoemLocale: GetSelectedPoemLocale,
    private val getSupportedLocale: GetSupportedLocale,
    private val localeItemMapper: LocaleItemMapper,
    private val setSelectedPoemLocale: SetSelectedPoemLocale,
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun viewIsReady() {
        getLocaleSettingInfo()
    }

    fun setSelectedPoemLocale(localeItem: LocaleItem) {
        viewModelScope.launch {
            val params = SetSelectedPoemLocaleParams(
                locale = localeItemMapper.mapToDomain(localeItem)
            )
            setSelectedPoemLocale(params)
            updateUiState(eventToConsume = Event.popBack)
        }
    }

    private fun getLocaleSettingInfo() {
        viewModelScope.launch {
            getSupportedLocale().combine(getSelectedPoemLocale()) { supportedLocales, selectedLocale ->
                updateUiState(
                    supportedLocales = localeItemMapper.mapToPresentation(supportedLocales),
                    selectedPoemLocale = localeItemMapper.mapToPresentation(selectedLocale)
                )
            }.collect()
        }
    }

    fun onEventConsumed(event: Event) {
        updateUiState(eventConsumed = event)
    }

    private fun updateUiState(
        supportedLocales: List<LocaleItem>? = null,
        selectedPoemLocale: LocaleItem? = null,
        eventToConsume: Event? = null,
        eventConsumed: Event? = null,
    ) {
        when (val uiStateSnapshot = _uiState.value) {
            is UiState.Loaded -> {
                _uiState.value = UiState.Loaded(
                    supportedLocales = supportedLocales ?: uiStateSnapshot.supportedLocales,
                    selectedPoemLocale = selectedPoemLocale ?: uiStateSnapshot.selectedPoemLocale,
                    events = (uiStateSnapshot.events.filterNot { it == eventConsumed } + eventToConsume).filterNotNull(),
                )
            }

            UiState.Loading -> {
                if (supportedLocales == null) throw IllegalArgumentException("supportedLocales can not be null in loading state.")
                if (selectedPoemLocale == null) throw IllegalArgumentException("selectedPoemLocale can not be null in loading state.")
                _uiState.value = UiState.Loaded(
                    supportedLocales = supportedLocales,
                    selectedPoemLocale = selectedPoemLocale,
                )
            }
        }
    }

    sealed class UiState {
        data object Loading : UiState()
        data class Loaded(
            val supportedLocales: List<LocaleItem>,
            val selectedPoemLocale: LocaleItem,
            val events: List<Event> = emptyList(),
        ) : UiState()
    }

    sealed class Event {
        data object popBack : Event()
    }
}