package com.segnities007.yatte.presentation.feature.home.component

import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.core.component.LocalSetHeaderConfig
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.component.button.YatteIconButton
import com.segnities007.yatte.presentation.designsystem.component.layout.formatDate
import com.segnities007.yatte.presentation.feature.home.HomeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.home.generated.resources.cd_next_day
import yatte.presentation.feature.home.generated.resources.cd_prev_day
import yatte.presentation.feature.home.generated.resources.Res as HomeRes

private const val INITIAL_PAGE = 500

@Composable
fun HomeHeader(
    state: HomeState,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
) {
    val setHeaderConfig = LocalSetHeaderConfig.current
    val prevDayDesc = stringResource(HomeRes.string.cd_prev_day)
    val nextDayDesc = stringResource(HomeRes.string.cd_next_day)
    val dateText = if (state.selectedDate != null) {
        formatDate(state.selectedDate)
    } else {
        ""
    }
    
    val headerConfig = remember(dateText, pagerState.currentPage) {
        HeaderConfig(
            title = { 
                YatteText(
                    text = dateText,
                    modifier = Modifier.bounceClick(onTap = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(INITIAL_PAGE)
                        }
                    })
                ) 
            },
            navigationIcon = {
                YatteIconButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = prevDayDesc,
                )
            },
            actions = {
                YatteIconButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = nextDayDesc,
                )
            },
        )
    }
    
    SideEffect {
        setHeaderConfig(headerConfig)
    }
}
