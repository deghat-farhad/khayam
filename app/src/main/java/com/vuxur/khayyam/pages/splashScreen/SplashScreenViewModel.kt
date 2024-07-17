package com.vuxur.khayyam.pages.splashScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vuxur.khayyam.domain.usecase.getSelectedPoemLocale.GetSelectedPoemLocale
import com.vuxur.khayyam.mapper.LocaleItemMapper
import com.vuxur.khayyam.model.LocaleItem
import com.vuxur.khayyam.pages.poemList.view.viewModel.PoemListViewModel.Event
import com.vuxur.khayyam.pages.poemList.view.viewModel.PoemListViewModel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val getSelectedPoemLocale: GetSelectedPoemLocale,
    private val localeItemMapper: LocaleItemMapper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Initialized())
    val uiState: StateFlow<UiState> = _uiState

    fun viewIsReady() {
        navigateToNextScreen()
    }

    private fun navigateToNextScreen() {
        viewModelScope.launch {
            consumeEvent(Event.Navigate.ToPoemList)
        }
    }

    private suspend fun whereToNavigate(): Event.Navigate {
        val selectedPoemLocaleItem = localeItemMapper.mapToPresentation(
            getSelectedPoemLocale().first()
        )
        return if (selectedPoemLocaleItem is LocaleItem.NoLocale) {
            Event.Navigate.ToLanguageSetting
        } else {
            Event.Navigate.ToPoemList
        }
    }

    fun onEventConsumed(
        eventConsumed: Event,
    ) {
        (_uiState.value as? UiState.Initialized)?.let { uiStateSnapshot ->
            _uiState.value = uiStateSnapshot.copy(
                events = uiStateSnapshot.events.filterNot { event ->
                    event == eventConsumed
                }
            )
        }
    }

    private fun consumeEvent(
        eventToConsume: Event,
    ) {
        (_uiState.value as? UiState.Initialized)?.let { uiStateSnapshot ->
            _uiState.value = uiStateSnapshot.copy(
                events = (uiStateSnapshot.events + eventToConsume)
            )
        }
    }

    sealed class UiState {
        data class Initialized(
            val events: List<Event> = emptyList(),
        ) : UiState()
    }

    sealed class Event {
        sealed class Navigate : Event() {
            data object ToLanguageSetting : Navigate()
            data object ToPoemList : Navigate()
        }
    }
}
