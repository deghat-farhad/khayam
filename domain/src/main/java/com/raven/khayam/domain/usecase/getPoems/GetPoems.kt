package com.raven.khayam.domain.usecase.getPoems

import com.raven.khayam.domain.model.Poem
import com.raven.khayam.domain.repository.PoemRepository
import com.raven.khayam.domain.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow

class GetPoems(
    private val poemRepository: PoemRepository
) : UseCase<List<Poem>>{
    override fun invoke(): Flow<List<Poem>> {
        return poemRepository.getPoems()
    }
}
