package com.segnities007.yatte.presentation.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.segnities007.yatte.presentation.feature.home.component.EmptyTasksView
import com.segnities007.yatte.presentation.feature.home.component.TaskList
import com.segnities007.yatte.presentation.feature.home.component.formatDate
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.core.generated.resources.Res as CoreRes
import yatte.presentation.feature.home.generated.resources.Res as HomeRes
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

private const val INITIAL_PAGE = 500
private const val PAGE_COUNT = 1000


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    actions: HomeActions,
    onShowSnackbar: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }
    val pagerState = rememberPagerState(initialPage = INITIAL_PAGE) { PAGE_COUNT }
    val coroutineScope = rememberCoroutineScope()

    // ページ変更時に日付を更新
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val offset = page - INITIAL_PAGE
            val date = today.plus(offset, DateTimeUnit.DAY)
            viewModel.onIntent(HomeIntent.SelectDate(date))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeEvent.NavigateToAddTask -> actions.onAddTask()
                is HomeEvent.NavigateToHistory -> actions.onHistory()
                is HomeEvent.NavigateToSettings -> actions.onSettings()
                is HomeEvent.NavigateToEditTask -> actions.onEditTask(event.taskId)
                is HomeEvent.ShowError -> onShowSnackbar(event.message)
                is HomeEvent.ShowTaskCompleted -> onShowSnackbar(
                    stringResource(HomeRes.string.snackbar_task_completed, event.taskTitle),
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(HomeRes.string.cd_prev_day),
                        )
                    }
                },
                title = {
                    Text(text = state.selectedDate?.let { formatDate(it) } ?: "")
                },
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(HomeRes.string.cd_next_day),
                        )
                    }
                    IconButton(onClick = { viewModel.onIntent(HomeIntent.NavigateToHistory) }) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = stringResource(CoreRes.string.nav_history),
                        )
                    }
                    IconButton(onClick = { viewModel.onIntent(HomeIntent.NavigateToSettings) }) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = stringResource(CoreRes.string.nav_settings),
                        )
                    }
                },
            )
        },
    ) { padding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) { _ ->
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                    state.tasks.isEmpty() -> {
                        EmptyTasksView(
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                    else -> {
                        TaskList(
                            tasks = state.tasks,
                            onCompleteTask = { viewModel.onIntent(HomeIntent.CompleteTask(it)) },
                            onTaskClick = { viewModel.onIntent(HomeIntent.NavigateToEditTask(it.id.value)) },
                        )
                    }
                }
            }
        }
    }
}


