package com.raven.khayam.di

import com.raven.khayam.domain.repository.PoemRepository
import com.raven.khayam.domain.usecase.findPoems.FindPoems
import com.raven.khayam.domain.usecase.getPoems.GetPoems
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
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