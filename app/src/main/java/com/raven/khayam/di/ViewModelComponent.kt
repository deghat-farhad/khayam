package com.raven.khayam.di

import com.raven.khayam.poemList.MainActivity
import dagger.Component

@Component(modules = [ViewModelModule::class])
interface ViewModelComponent {
    @Component.Builder
    interface Builder {
        fun build(): ViewModelComponent
    }

    fun injectActivity(mainActivity: MainActivity)
}