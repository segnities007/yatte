package com.segnities007.yatte.presentation.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.core.component.LocalSetHeaderConfig
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteEmptyState
import com.segnities007.yatte.presentation.designsystem.component.button.YatteIconButton
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteLoadingIndicator
import com.segnities007.yatte.presentation.designsystem.component.layout.YatteScaffold
import com.segnities007.yatte.presentation.feature.home.component.TaskList
import com.segnities007.yatte.presentation.designsystem.component.layout.formatDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import yatte.presentation.feature.home.generated.resources.cd_next_day
import yatte.presentation.feature.home.generated.resources.cd_prev_day
import yatte.presentation.feature.home.generated.resources.empty_no_tasks
import yatte.presentation.feature.home.generated.resources.empty_tasks_emoji
import yatte.presentation.feature.home.generated.resources.snackbar_task_completed
import yatte.presentation.feature.home.generated.resources.snackbar_undo
import kotlin.time.Clock
import yatte.presentation.feature.home.generated.resources.Res as HomeRes

import com.segnities007.yatte.presentation.feature.home.component.HomeContent
import com.segnities007.yatte.presentation.feature.home.component.HomeHeader
import com.segnities007.yatte.presentation.feature.home.component.HomeSideEffects

private const val INITIAL_PAGE = 500
private const val PAGE_COUNT = 1000


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    actions: HomeActions,
    contentPadding: PaddingValues,
    isNavigationVisible: Boolean = true,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = INITIAL_PAGE,
        pageCount = { PAGE_COUNT },
    )

    HomeSetupPagerEffect(pagerState = pagerState, onSelectDate = { viewModel.onIntent(HomeIntent.SelectDate(it)) })
    HomeSideEffects(
        viewModel = viewModel,
        actions = actions,
        snackbarHostState = snackbarHostState,
        coroutineScope = coroutineScope,
        onShowSnackbar = onShowSnackbar
    )
    HomeHeader(state = state, pagerState = pagerState, coroutineScope = coroutineScope)

    YatteScaffold(
        isNavigationVisible = isNavigationVisible,
        contentPadding = contentPadding,
    ) { listContentPadding ->
        HomeContent(
            state = state,
            pagerState = pagerState,
            contentPadding = listContentPadding,
            onCompleteTask = { task, date -> viewModel.onIntent(HomeIntent.CompleteTask(task, date)) },
            onSkipTask = { task, date -> viewModel.onIntent(HomeIntent.SkipTask(task, date)) },
            onSnoozeTask = { task -> viewModel.onIntent(HomeIntent.SnoozeTask(task)) },
            onTaskClick = { viewModel.onIntent(HomeIntent.NavigateToEditTask(it.id.value)) },
        )
    }
}

@Composable
private fun HomeSetupPagerEffect(
    pagerState: PagerState,
    onSelectDate: (LocalDate) -> Unit,
) {
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val offset = page - INITIAL_PAGE
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val date = today.plus(offset, DateTimeUnit.DAY)
            onSelectDate(date)
        }
    }
}