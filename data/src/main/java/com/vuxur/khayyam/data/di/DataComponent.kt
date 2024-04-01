package com.vuxur.khayyam.data.di

import android.app.Application
import com.vuxur.khayyam.domain.repository.PoemRepository
import dagger.BindsInstance
import dagger.Component

@Component(modules = [RepositoryModule::class])
interface DataComponent {
    @Component.Builder
    interface Builder{
        fun build(): DataComponent

        @BindsInstance
        fun application(application: Application): Builder

    }
    fun poemRepository(): PoemRepository
}