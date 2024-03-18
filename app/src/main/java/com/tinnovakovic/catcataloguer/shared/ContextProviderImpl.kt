package com.tinnovakovic.catcataloguer.shared

import android.app.Application
import android.content.Context
import javax.inject.Inject

class ContextProviderImpl @Inject constructor(
    private val context: Context
) : ContextProvider {

    override fun getContext() = context

}