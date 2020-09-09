package com.raven.khayam.domain.usecase.getPoems

import com.raven.khayam.domain.model.Poem
import com.raven.khayam.domain.repository.PoemRepository
import com.raven.khayam.domain.usecase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetPoems(
    executorScheduler: Scheduler,
    uiScheduler: Scheduler,
    private val poemRepository: PoemRepository
) :
    UseCase<List<Poem>>(executorScheduler, uiScheduler) {
    override fun buildUseCaseObservable(): Observable<List<Poem>> {
        return poemRepository.getPoems()
    }
}