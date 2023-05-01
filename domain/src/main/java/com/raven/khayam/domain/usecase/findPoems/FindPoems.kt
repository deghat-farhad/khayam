package com.raven.khayam.domain.usecase.findPoems

import com.raven.khayam.domain.model.Poem
import com.raven.khayam.domain.repository.PoemRepository
import com.raven.khayam.domain.usecase.base.UseCaseWithParams
import kotlinx.coroutines.flow.Flow

class FindPoems(
    private val poemRepository: PoemRepository
) : UseCaseWithParams<List<Poem>, FindPoemsParams>{
    override fun invoke(params: FindPoemsParams): Flow<List<Poem>> {
        return poemRepository.findPoems(params.searchPhrase)
    }
}