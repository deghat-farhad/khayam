package com.raven.khayam.domain.usecase.base

import io.reactivex.observers.DisposableObserver

abstract class DefaultObserver<T> : DisposableObserver<T>() {
    override fun onError(e: Throwable) {
        e.printStackTrace()
    }

    override fun onNext(t: T) {

    }

    override fun onComplete() {

    }
}