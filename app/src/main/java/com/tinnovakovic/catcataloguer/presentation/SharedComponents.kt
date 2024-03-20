package com.tinnovakovic.catcataloguer.presentation

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
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
    var expanded by rememberSaveable {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatMediumTopAppBar(
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior,

) {
    MediumTopAppBar(
        windowInsets = WindowInsets(0.dp),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        navigationIcon = navigationIcon,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )

    )
}

@Composable
fun AnimatedLinearProgressIndicator(
    indicatorProgress: Float,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    var progress by rememberSaveable { mutableStateOf(0F) }
    val progressAnimDuration = 1_500
    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = progressAnimDuration, easing = FastOutSlowInEasing),
    )
    LinearProgressIndicator(
        progress = { progressAnimation },
        modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.spacing.small),
        strokeCap = StrokeCap.Round

    )
    LaunchedEffect(lifecycleOwner) {
        progress = indicatorProgress
    }
}

@Composable
fun SubTitle(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = MaterialTheme.spacing.large)
    )
}