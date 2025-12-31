package com.segnities007.yatte.presentation.feature.settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import com.segnities007.yatte.presentation.core.component.LocalLibraries
import com.segnities007.yatte.presentation.designsystem.component.card.YatteCard
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

@Composable
fun LicenseContent(
    paddingValues: PaddingValues,
    onLibraryClick: (Library) -> Unit,
) {
    val libraries = LocalLibraries.current?.libraries ?: emptyList()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(YatteSpacing.sm)
    ) {
        items(libraries) { library ->
            YatteCard(
                onClick = { onLibraryClick(library) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(YatteSpacing.md)
                ) {
                    YatteText(
                        text = library.name,
                        style = YatteTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                         YatteText(
                            text = library.artifactVersion ?: "",
                            style = YatteTheme.typography.bodySmall,
                            color = YatteTheme.colors.onSurfaceVariant
                        )
                        if (library.licenses.isNotEmpty()) {
                             YatteText(
                                text = library.licenses.firstOrNull()?.name ?: "",
                                style = YatteTheme.typography.bodySmall,
                                color = YatteTheme.colors.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
