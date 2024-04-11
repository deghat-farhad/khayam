package com.vuxur.khayyam.domain.usecase.base

import kotlinx.coroutines.flow.Flow
interface UseCaseWithParams<T,P> {
    suspend operator fun invoke(params: P): Flow<T>
}