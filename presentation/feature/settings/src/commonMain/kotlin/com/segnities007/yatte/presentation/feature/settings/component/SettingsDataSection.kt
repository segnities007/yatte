package com.segnities007.yatte.presentation.feature.settings.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.segnities007.yatte.presentation.core.file.FileHelper
import com.segnities007.yatte.presentation.designsystem.component.button.YatteButton
import com.segnities007.yatte.presentation.designsystem.component.card.YatteSectionCard
import com.segnities007.yatte.presentation.designsystem.component.list.YatteActionRow
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.feature.settings.SettingsIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.settings.generated.resources.action_execute
import yatte.presentation.feature.settings.generated.resources.action_export_desc
import yatte.presentation.feature.settings.generated.resources.action_export_title
import yatte.presentation.feature.settings.generated.resources.action_import_desc
import yatte.presentation.feature.settings.generated.resources.action_import_title
import yatte.presentation.feature.settings.generated.resources.action_show
import yatte.presentation.feature.settings.generated.resources.reset_button
import yatte.presentation.feature.settings.generated.resources.reset_desc
import yatte.presentation.feature.settings.generated.resources.reset_title
import yatte.presentation.feature.settings.generated.resources.section_data
import yatte.presentation.feature.settings.generated.resources.section_license_desc
import yatte.presentation.feature.settings.generated.resources.section_license_title
import yatte.presentation.feature.settings.generated.resources.snackbar_import_failed
import yatte.presentation.feature.settings.generated.resources.Res as SettingsRes

@Composable
fun SettingsDataSection(
    onIntent: (SettingsIntent) -> Unit,
    onLicenseClick: () -> Unit,
    fileHelper: FileHelper,
    scope: CoroutineScope,
    onShowSnackbar: (String) -> Unit,
) {
    YatteSectionCard(
        icon = Icons.Default.Delete,
        title = stringResource(SettingsRes.string.section_data),
    ) {
        YatteActionRow(
            title = stringResource(SettingsRes.string.section_license_title),
            subtitle = stringResource(SettingsRes.string.section_license_desc),
            action = {
                YatteButton(
                    text = stringResource(SettingsRes.string.action_show),
                    onClick = onLicenseClick,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
        )

        Spacer(modifier = Modifier.height(YatteSpacing.md))

        YatteActionRow(
            title = stringResource(SettingsRes.string.action_export_title),
            subtitle = stringResource(SettingsRes.string.action_export_desc),
            action = {
                YatteButton(
                    text = stringResource(SettingsRes.string.action_execute),
                    onClick = { onIntent(SettingsIntent.RequestExportHistory) },
                )
            }
        )

        YatteActionRow(
            title = stringResource(SettingsRes.string.action_import_title),
            subtitle = stringResource(SettingsRes.string.action_import_desc),
            action = {
                YatteButton(
                    text = stringResource(SettingsRes.string.action_execute),
                    onClick = {
                        fileHelper.loadFile(
                            onLoaded = { json ->
                                onIntent(SettingsIntent.ImportHistory(json))
                            },
                            onError = { e ->
                                scope.launch {
                                    onShowSnackbar(getString(SettingsRes.string.snackbar_import_failed, e.message ?: "Unknown error"))
                                }
                            }
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                )
            }
        )
        
        Spacer(modifier = Modifier.height(YatteSpacing.md))
        
        YatteActionRow(
            title = stringResource(SettingsRes.string.reset_title),
            subtitle = stringResource(SettingsRes.string.reset_desc),
            action = {
                YatteButton(
                    text = stringResource(SettingsRes.string.reset_button),
                    onClick = { onIntent(SettingsIntent.RequestResetData) },
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        )
    }
}
