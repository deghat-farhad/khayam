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
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
        viewModelScope.launch {
            _uiState.value = UiState.Loaded(
                supportedLocales = localeItemMapper.mapToPresentation(getSupportedLocale())
            )
            collectSelectedPoemLocale()
        }
    }

    fun setSelectedPoemLocale(localeItem: LocaleItem) {
        viewModelScope.launch {
            val params = SetSelectedPoemLocaleParams(
                locale = localeItemMapper.mapToDomain(localeItem)
            )
            setSelectedPoemLocale(params)
            consumeEvent(eventToConsume = Event.PopBack)
        }
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

    private fun consumeEvent(
        eventToConsume: Event,
    ) {
        (_uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
            _uiState.value = uiStateSnapshot.copy(
                events = (uiStateSnapshot.events + eventToConsume)
            )
        }
    }

    private fun collectSelectedPoemLocale() {
        (_uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
            viewModelScope.launch {
                getSelectedPoemLocale().collect { selectedLocale ->
                    _uiState.value = uiStateSnapshot.copy(
                        selectedPoemLocale = localeItemMapper.mapToPresentation(selectedLocale)
                    )
                }
            }
        }
    }

    sealed class UiState {
        data object Loading : UiState()
        data class Loaded(
            val supportedLocales: List<LocaleItem>,
            val selectedPoemLocale: LocaleItem? = null,
            val events: List<Event> = emptyList(),
        ) : UiState()
    }

    sealed class Event {
        data object PopBack : Event()
    }
}