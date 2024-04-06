package com.vuxur.khayyam.domain.usecase.getPoems

import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.repository.PoemRepository
import com.vuxur.khayyam.domain.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow

class GetPoems(
    private val poemRepository: PoemRepository
) : UseCase<List<Poem>>{
    override suspend fun invoke(): Flow<List<Poem>> {
        return poemRepository.getPoems()
    }
}
