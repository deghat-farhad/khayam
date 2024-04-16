package com.vuxur.khayyam.domain.usecase.base

interface UseCaseWithParams<T,P> {
    suspend operator fun invoke(params: P): T
}