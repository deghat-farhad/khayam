package com.vuxur.khayyam.domain.usecase.findPoems

import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.repository.PoemRepository
import com.vuxur.khayyam.domain.usecase.base.UseCaseWithParams
import kotlinx.coroutines.flow.Flow

class FindPoems(
    private val poemRepository: PoemRepository
) : UseCaseWithParams<List<Poem>, FindPoemsParams>{
    override suspend fun invoke(params: FindPoemsParams): Flow<List<Poem>> {
        return poemRepository.findPoems(params.searchPhrase, params.locale)
    }
}