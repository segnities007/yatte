package com.segnities007.yatte.presentation.feature.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.entity.Library
import com.segnities007.yatte.presentation.designsystem.component.layout.YatteScaffold
import com.segnities007.yatte.presentation.feature.settings.component.LicenseContent
import com.segnities007.yatte.presentation.feature.settings.component.LicenseDialog
import com.segnities007.yatte.presentation.feature.settings.component.LicenseHeader

@Composable
fun LicenseScreen(
    onBackClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isNavigationVisible: Boolean = false,
) {
    var selectedLibrary by remember { mutableStateOf<Library?>(null) }

    LicenseHeader(onBackClick = onBackClick)

    YatteScaffold(
        isNavigationVisible = isNavigationVisible,
        contentPadding = contentPadding,
    ) { listContentPadding ->
        LicenseContent(
            paddingValues = listContentPadding,
            onLibraryClick = { selectedLibrary = it }
        )
    }

    selectedLibrary?.let { library ->
        LicenseDialog(
            library = library,
            onDismiss = { selectedLibrary = null }
        )
    }
}

