package com.segnities007.yatte.presentation.feature.history.component

import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.core.component.LocalSetHeaderConfig
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.history.generated.resources.title_history
import yatte.presentation.feature.history.generated.resources.Res as HistoryRes

@Composable
fun HistoryHeader() {
    val setHeaderConfig = LocalSetHeaderConfig.current
    val historyTitle = stringResource(HistoryRes.string.title_history)
    
    val headerConfig = remember {
        HeaderConfig(title = { YatteText(historyTitle) })
    }
    
    SideEffect {
        setHeaderConfig(headerConfig)
    }
}
