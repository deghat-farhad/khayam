package com.raven.khayam.domain.usecase.base

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.observers.DisposableObserver

abstract class UseCase<T>(
    private val executorScheduler: Scheduler,
    private val uiScheduler: Scheduler
) {
    abstract fun buildUseCaseObservable(): Observable<T>

    open fun execute(observer: DisposableObserver<T>): DisposableObserver<T>? {
        val observable = buildUseCaseObservable()
            .subscribeOn(executorScheduler)
            .observeOn(uiScheduler)
        return (observable.subscribeWith(observer))
    }
}