package com.segnities007.yatte.presentation.feature.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteEmptyState
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteLoadingIndicator
import com.segnities007.yatte.presentation.feature.home.HomeState
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlinx.datetime.todayIn
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.pager.rememberPagerState
import yatte.presentation.feature.home.generated.resources.empty_no_tasks
import yatte.presentation.feature.home.generated.resources.empty_tasks_emoji

import yatte.presentation.feature.home.generated.resources.Res as HomeRes
import androidx.compose.material3.MaterialTheme

private const val INITIAL_PAGE = 500

@Composable
fun HomeContent(
    state: HomeState,
    pagerState: PagerState,
    contentPadding: PaddingValues,
    onCompleteTask: (Task, LocalDate) -> Unit,
    onSkipTask: (Task, LocalDate) -> Unit,
    onSnoozeTask: (Task) -> Unit,
    onTaskClick: (Task) -> Unit,
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
    ) { page ->
        val offset = page - INITIAL_PAGE
        val nowMillis = Clock.System.now().toEpochMilliseconds()
        val today = Instant.fromEpochMilliseconds(nowMillis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        val pageDate = today.plus(offset, DateTimeUnit.DAY)
        val tasksForPage = state.tasksForDate(pageDate).sortedBy { it.time }

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading -> {
                    YatteLoadingIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                tasksForPage.isEmpty() -> {
                    YatteEmptyState(
                        emoji = stringResource(HomeRes.string.empty_tasks_emoji),
                        message = stringResource(HomeRes.string.empty_no_tasks),
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                else -> {
                    TaskList(
                        tasks = tasksForPage,
                        contentPadding = contentPadding,
                        onCompleteTask = { onCompleteTask(it, pageDate) },
                        onSkipTask = { onSkipTask(it, pageDate) },
                        onSnoozeTask = onSnoozeTask,
                        onTaskClick = onTaskClick,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun HomeContentPreview() {
    MaterialTheme {
        val pagerState = rememberPagerState(initialPage = 0, pageCount = { 1 })
        HomeContent(
            state = HomeState(
                allTasks = listOf(
                     Task(
                        id = TaskId("1"),
                        title = "Design Mockup",
                        time = LocalTime(10, 0),
                        createdAt = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds()).toLocalDateTime(TimeZone.currentSystemDefault()),
                    )
                )
            ),
            pagerState = pagerState,
            contentPadding = PaddingValues(0.dp),
            onCompleteTask = { _, _ -> },
            onSkipTask = { _, _ -> },
            onSnoozeTask = {},
            onTaskClick = {},
        )
    }
}
