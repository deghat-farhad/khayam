package com.vuxur.khayyam.domain.usecase.getTranslations

import com.vuxur.khayyam.domain.model.Translation
import com.vuxur.khayyam.domain.repository.TranslationRepository
import com.vuxur.khayyam.domain.usecase.base.UseCase

class GetAvailableTranslations(
    private val translationRepository: TranslationRepository
) : UseCase<List<Translation>> {
    override suspend fun invoke(): List<Translation> {
        return translationRepository.getAvailableTranslations()
    }
}