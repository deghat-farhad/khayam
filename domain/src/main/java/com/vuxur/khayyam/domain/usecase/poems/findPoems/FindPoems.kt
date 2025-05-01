package com.vuxur.khayyam.domain.usecase.poems.findPoems

import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.repository.PoemRepository
import com.vuxur.khayyam.domain.usecase.base.UseCaseWithParams

class FindPoems(
    private val poemRepository: PoemRepository
) : UseCaseWithParams<List<Poem>, FindPoemsParams> {
    override suspend fun invoke(params: FindPoemsParams): List<Poem> {
        return poemRepository.findPoems(params.searchPhrase, params.translation)
    }
}