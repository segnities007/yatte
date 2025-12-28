package com.segnities007.yatte.presentation.feature.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.core.component.LocalLibraries
import com.mikepenz.aboutlibraries.Libs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.entity.Library

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseScreen(
    onBackClick: () -> Unit,
) {
    var selectedLibrary by remember { mutableStateOf<Library?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Open Source Licenses") },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.bounceClick()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LibrariesContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            libraries = LocalLibraries.current ?: Libs.Builder().withJson("[]").build(),
            showLicenseBadges = false,
            onLibraryClick = { library: Library ->
                selectedLibrary = library
            }
        )

        selectedLibrary?.let { library ->
            AlertDialog(
                onDismissRequest = { selectedLibrary = null },
                title = { Text(library.name) },
                text = {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "Version: ${library.artifactVersion ?: "Unknown"}",
                            style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (library.licenses.isNotEmpty()) {
                            library.licenses.forEach { license ->
                                Text(
                                    text = license.name,
                                    style = androidx.compose.material3.MaterialTheme.typography.titleSmall
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                license.url?.let { url ->
                                     Text(
                                        text = url,
                                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                                        color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            }
                        } else {
                            Text("No license information available.")
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { selectedLibrary = null }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}
