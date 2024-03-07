package com.noemi.newschecker.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.noemi.newschecker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsAppBar(title: String, contentDescription: String) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = contentDescription,
                modifier = Modifier.size(32.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}