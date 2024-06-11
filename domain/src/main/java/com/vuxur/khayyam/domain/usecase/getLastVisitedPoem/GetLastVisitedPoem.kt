package com.vuxur.khayyam.domain.usecase.getLastVisitedPoem

import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow

class GetLastVisitedPoem(
    private val settingRepository: SettingRepository
) : UseCase<Flow<Poem?>> {
    override suspend fun invoke(): Flow<Poem?> {
        return settingRepository.lastVisitedPoem
    }
}