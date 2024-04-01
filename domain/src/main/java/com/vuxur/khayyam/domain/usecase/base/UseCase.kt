package com.vuxur.khayyam.domain.usecase.base

import kotlinx.coroutines.flow.Flow

interface UseCase<T> {
    operator fun invoke():Flow<T>
}