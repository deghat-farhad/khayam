package com.raven.khayam.di

import com.raven.khayam.domain.repository.PoemRepository
import com.raven.khayam.domain.usecase.getPoems.GetPoems
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

@Module
class DomainModule {
    @Provides
    @Named("ioScheduler")
    fun ioScheduler(): Scheduler = Schedulers.io()

    @Provides
    @Named("mainThreadScheduler")
    fun mainThreadScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    fun getHome(
        poemRepository: PoemRepository,
        @Named("ioScheduler") ioScheduler: Scheduler,
        @Named("mainThreadScheduler") mainThreadScheduler: Scheduler
    ) = GetPoems(ioScheduler, mainThreadScheduler, poemRepository)
}