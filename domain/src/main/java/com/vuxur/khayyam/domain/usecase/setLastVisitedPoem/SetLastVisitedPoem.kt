package com.vuxur.khayyam.domain.usecase.setLastVisitedPoem

import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.usecase.base.UseCaseWithParams

class SetLastVisitedPoem(
    private val settingRepository: SettingRepository
) : UseCaseWithParams<Unit, SetLastVisitedPoemParams> {
    override suspend fun invoke(params: SetLastVisitedPoemParams) {
        settingRepository.setLastVisitedPoem(params.lastVisitedPoem)
    }
}