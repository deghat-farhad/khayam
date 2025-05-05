package com.vuxur.khayyam.domain.usecase.poems.getRandomPoem

import com.vuxur.khayyam.domain.common.resolve
import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.repository.PoemRepository
import com.vuxur.khayyam.domain.usecase.base.UseCaseWithParams

class GetRandomPoem(
    private val poemRepository: PoemRepository,
) : UseCaseWithParams<Poem, GetRandomPoemParams> {
    override suspend fun invoke(params: GetRandomPoemParams): Poem {
        return poemRepository.getRandomPoem(params.translationOptions.resolve())
    }
}