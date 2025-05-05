package com.vuxur.khayyam.domain.usecase.poems.getPoems

import com.vuxur.khayyam.domain.common.resolve
import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.repository.PoemRepository
import com.vuxur.khayyam.domain.usecase.base.UseCaseWithParams

class GetPoems(
    private val poemRepository: PoemRepository,
) : UseCaseWithParams<List<Poem>, GetPoemsParams> {
    override suspend fun invoke(params: GetPoemsParams): List<Poem> {
        return (poemRepository.getPoems(params.translationOptions.resolve()))
    }
}
