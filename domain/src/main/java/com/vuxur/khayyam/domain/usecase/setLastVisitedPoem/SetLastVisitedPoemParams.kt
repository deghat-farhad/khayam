package com.vuxur.khayyam.domain.usecase.setLastVisitedPoem

import com.vuxur.khayyam.domain.model.Poem

data class SetLastVisitedPoemParams(
    val lastVisitedPoem: Poem
)