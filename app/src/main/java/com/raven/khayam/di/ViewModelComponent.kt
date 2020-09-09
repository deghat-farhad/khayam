package com.raven.khayam.di

import android.app.Application
import com.raven.khayam.poemList.view.MainActivity
import dagger.BindsInstance
import dagger.Component

@Component(modules = [ViewModelModule::class, DataModule::class, DomainModule::class])
interface ViewModelComponent {
    @Component.Builder
    interface Builder {
        fun build(): ViewModelComponent

        @BindsInstance
        fun application(application: Application): Builder
    }

    fun injectActivity(mainActivity: MainActivity)
}