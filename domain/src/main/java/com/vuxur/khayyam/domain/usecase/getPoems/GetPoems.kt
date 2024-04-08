package com.vuxur.khayyam.domain.usecase.getPoems

import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.repository.PoemRepository
import com.vuxur.khayyam.domain.usecase.base.UseCaseWithParams
import kotlinx.coroutines.flow.Flow

class GetPoems(
    private val poemRepository: PoemRepository
) : UseCaseWithParams<List<Poem>, GetPoemsParams> {
    override fun invoke(params: GetPoemsParams): Flow<List<Poem>> {
        return poemRepository.getPoems(params.locale)
    }
}
