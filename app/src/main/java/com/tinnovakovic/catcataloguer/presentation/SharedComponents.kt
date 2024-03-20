package com.tinnovakovic.catcataloguer.presentation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ToastErrorMessage(error: String) {
    Toast.makeText(
        LocalContext.current,
        error,
        Toast.LENGTH_SHORT
    ).show()
}