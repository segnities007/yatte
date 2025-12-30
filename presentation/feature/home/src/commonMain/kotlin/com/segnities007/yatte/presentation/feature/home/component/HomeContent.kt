package com.segnities007.yatte.presentation.feature.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteEmptyState
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteLoadingIndicator
import com.segnities007.yatte.presentation.feature.home.HomeState
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.home.generated.resources.empty_no_tasks
import yatte.presentation.feature.home.generated.resources.empty_tasks_emoji
import kotlin.time.Clock
import yatte.presentation.feature.home.generated.resources.Res as HomeRes

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
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
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
