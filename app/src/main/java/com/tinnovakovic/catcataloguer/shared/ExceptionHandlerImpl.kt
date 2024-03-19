package com.tinnovakovic.catcataloguer.shared

import android.util.Log
import com.tinnovakovic.catcataloguer.R
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ExceptionHandlerImpl @Inject constructor(
    private val contextProvider: ContextProvider
) : ExceptionHandler {

    override fun execute(throwable: Throwable): ErrorToUser {

        val context = contextProvider.getContext()

        val errorToLog = when (throwable) {
            is IOException -> "IOException: ${throwable.message}"
            is HttpException -> "HttpException, Code: ${throwable.code()}, Message: ${throwable.message()}"
            else -> "Other Exception, Message: ${throwable.message}"
        }

        val errorToUser: ErrorToUser = when (throwable) {
            is IOException -> ErrorToUser(
                context.getString(R.string.io_error_message)
            )

            is HttpException -> ErrorToUser(
                context.getString(R.string.http_error_message)
            )

            else -> ErrorToUser(context.getString(R.string.generic_error_message))
        }

        Log.e(javaClass.name, errorToLog)
        return errorToUser
    }
}

data class ErrorToUser(
    val message: String
)

