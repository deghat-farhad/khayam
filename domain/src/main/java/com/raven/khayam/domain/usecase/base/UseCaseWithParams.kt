package com.raven.khayam.domain.usecase.base

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.observers.DisposableObserver

abstract class UseCaseWithParams<T, Params>(
    private val executorScheduler: Scheduler,
    private val uiScheduler: Scheduler
) {
    abstract fun buildUseCaseObservable(params: Params): Observable<T>

    open fun execute(observer: DisposableObserver<T>, params: Params): DisposableObserver<T>? {
        val observable = buildUseCaseObservable(params)
            .subscribeOn(executorScheduler)
            .observeOn(uiScheduler)
        return (observable.subscribeWith(observer))
    }
}