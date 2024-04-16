package com.vuxur.khayyam.domain.usecase.base

interface UseCase<T> {
    suspend operator fun invoke(): T
}