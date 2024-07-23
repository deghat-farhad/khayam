package com.vuxur.khayyam.domain.usecase.useUntranslated

import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.usecase.base.UseCase

class UseUntranslated(
    private val settingRepository: SettingRepository
) : UseCase<Unit> {
    override suspend fun invoke() {
        settingRepository.useUntranslated()
    }
}