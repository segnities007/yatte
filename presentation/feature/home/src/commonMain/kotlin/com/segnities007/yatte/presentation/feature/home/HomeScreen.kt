package com.segnities007.yatte.presentation.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.core.component.LocalSetHeaderConfig
import com.segnities007.yatte.presentation.core.component.YatteScaffold
import com.segnities007.yatte.presentation.feature.home.component.EmptyTasksView
import com.segnities007.yatte.presentation.feature.home.component.TaskList
import com.segnities007.yatte.presentation.feature.home.component.formatDate
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.component.YatteLoadingIndicator
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.core.generated.resources.nav_history
import yatte.presentation.core.generated.resources.nav_settings
import yatte.presentation.core.generated.resources.Res as CoreRes
import yatte.presentation.feature.home.generated.resources.*
import yatte.presentation.feature.home.generated.resources.Res as HomeRes
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

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
    val scrollState = rememberLazyListState()
    val pagerState = rememberPagerState(
        initialPage = INITIAL_PAGE,
        pageCount = { PAGE_COUNT },
    )

    // ページ変更時に日付を更新
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val offset = page - INITIAL_PAGE
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val date = today.plus(offset, DateTimeUnit.DAY)
            viewModel.onIntent(HomeIntent.SelectDate(date))
        }
    }

    val undoLabel = stringResource(HomeRes.string.snackbar_undo)
    
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeEvent.NavigateToAddTask -> actions.onAddTask()
                is HomeEvent.NavigateToHistory -> actions.onHistory()
                is HomeEvent.NavigateToSettings -> actions.onSettings()
                is HomeEvent.NavigateToEditTask -> actions.onEditTask(event.taskId)
                is HomeEvent.ShowError -> onShowSnackbar(event.message)
                is HomeEvent.ShowTaskCompleted -> {
                    val message = getString(HomeRes.string.snackbar_task_completed, event.task.title)
                    coroutineScope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = message,
                            actionLabel = undoLabel,
                            duration = SnackbarDuration.Short,
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onIntent(HomeIntent.UndoCompleteTask(event.task, event.date))
                        }
                    }
                }
            }
        }
    }

    // グローバルHeaderの設定（SideEffectで即座に更新）
    val setHeaderConfig = LocalSetHeaderConfig.current
    val prevDayDesc = stringResource(HomeRes.string.cd_prev_day)
    val nextDayDesc = stringResource(HomeRes.string.cd_next_day)
    val dateText = if (state.selectedDate != null) {
        formatDate(state.selectedDate!!)
    } else {
        ""
    }
    
    val headerConfig = remember(dateText, pagerState.currentPage) {
        HeaderConfig(
            title = { 
                Text(
                    text = dateText,
                    modifier = Modifier.bounceClick(onTap = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(INITIAL_PAGE)
                        }
                    })
                ) 
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    modifier = Modifier.bounceClick()
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = prevDayDesc,
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    modifier = Modifier.bounceClick()
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = nextDayDesc,
                    )
                }
            },
        )
    }
    
    SideEffect {
        setHeaderConfig(headerConfig)
    }

    // YatteScaffold を使用（Headerはグローバルなので省略）
    YatteScaffold(
        isNavigationVisible = isNavigationVisible,
        contentPadding = contentPadding,
    ) { listContentPadding ->
        // コンテンツ (背面)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            // このページの日付を計算
            val offset = page - INITIAL_PAGE
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val pageDate = today.plus(offset, DateTimeUnit.DAY)
            // その日のタスクをフィルタリング
            val tasksForPage = state.tasksForDate(pageDate)

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.isLoading -> {
                        YatteLoadingIndicator(
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                    tasksForPage.isEmpty() -> {
                        EmptyTasksView(
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                    else -> {
                        TaskList(
                            tasks = tasksForPage,
                            contentPadding = listContentPadding,
                            onCompleteTask = { viewModel.onIntent(HomeIntent.CompleteTask(it, pageDate)) },
                            onSkipTask = { task ->
                                viewModel.onIntent(HomeIntent.SkipTask(task, pageDate))
                            },
                            onTaskClick = { viewModel.onIntent(HomeIntent.NavigateToEditTask(it.id.value)) },
                        )
                    }
                }
            }
        }
    }
}