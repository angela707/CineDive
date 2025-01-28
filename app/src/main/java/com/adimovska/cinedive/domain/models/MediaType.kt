package com.adimovska.cinedive.domain.models

import androidx.annotation.StringRes
import com.adimovska.cinedive.R

enum class MediaType(@StringRes val nameId: Int) {
    MOVIE(nameId = R.string.movies),
    TV_SHOW(nameId = R.string.tv_shows)
}