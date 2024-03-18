package com.tinnovakovic.catcataloguer.shared

import androidx.annotation.StringRes
import javax.inject.Inject

class ResourceProviderImpl @Inject constructor(private val context: ContextProvider) :
    ResourceProvider {

    override fun getString(@StringRes stringRes: Int): String =
        context.getContext().getString(stringRes)
}
