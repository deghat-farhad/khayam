package com.raven.khayam.domain.usecase.findPoems

import com.raven.khayam.domain.model.Poem
import com.raven.khayam.domain.repository.PoemRepository
import com.raven.khayam.domain.usecase.base.UseCaseWithParams
import io.reactivex.Observable
import io.reactivex.Scheduler

class FindPoems(
    executorScheduler: Scheduler,
    uiScheduler: Scheduler,
    private val poemRepository: PoemRepository
) : UseCaseWithParams<List<Poem>, FindPoemsParams>(executorScheduler, uiScheduler) {
    override fun buildUseCaseObservable(params: FindPoemsParams): Observable<List<Poem>> {
        return poemRepository.findPoems(params.searchPhrase)
    }
}