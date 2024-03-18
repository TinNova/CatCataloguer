package com.tinnovakovic.catcataloguer.shared

import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes stringRes: Int): String
}
