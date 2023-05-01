package com.raven.khayam.di

import com.raven.khayam.domain.repository.PoemRepository
import com.raven.khayam.domain.usecase.findPoems.FindPoems
import com.raven.khayam.domain.usecase.getPoems.GetPoems
import dagger.Module
import dagger.Provides

@Module
class DomainModule {
    @Provides
    fun getPoems(
        poemRepository: PoemRepository,
   ) = GetPoems(poemRepository)

    @Provides
    fun findPoems(
        poemRepository: PoemRepository,
    ) = FindPoems(poemRepository)
}