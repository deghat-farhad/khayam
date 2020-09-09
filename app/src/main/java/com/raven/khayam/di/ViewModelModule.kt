package com.raven.khayam.di

import androidx.lifecycle.ViewModel
import com.raven.khayam.poemList.ViewModelPoemList
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ViewModelPoemList::class)
    fun bingHomeViewModel(viewModelHome: ViewModelPoemList): ViewModel
}