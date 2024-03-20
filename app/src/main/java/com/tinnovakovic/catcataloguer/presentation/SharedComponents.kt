package com.tinnovakovic.catcataloguer.presentation

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import com.tinnovakovic.catcataloguer.ui.theme.spacing

@Composable
fun ToastErrorMessage(error: String) {
    Toast.makeText(
        LocalContext.current,
        error,
        Toast.LENGTH_SHORT
    ).show()
}

@Composable
fun ExpandingCard(
    title: String,
    description: String
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .padding(top = MaterialTheme.spacing.medium)
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                expanded = !expanded
            },
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.small)
            )
            Text(
                text = description,
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),

                maxLines = if (!expanded) 2 else 30
            )
        }
    }
}