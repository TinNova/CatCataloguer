package com.tinnovakovic.catcataloguer.shared

interface ExceptionHandler {

    fun execute(throwable: Throwable): ErrorToUser

}