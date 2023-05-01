package com.raven.khayam.domain.usecase.base

import kotlinx.coroutines.flow.Flow
interface UseCaseWithParams<T,P> {
    operator fun invoke(params: P): Flow<T>
}