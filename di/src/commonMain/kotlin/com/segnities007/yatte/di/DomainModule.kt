package com.segnities007.yatte.di

import com.segnities007.yatte.domain.aggregate.alarm.usecase.CancelAlarmUseCase
import com.segnities007.yatte.domain.aggregate.alarm.usecase.GetScheduledAlarmsUseCase
import com.segnities007.yatte.domain.aggregate.alarm.usecase.ScheduleAlarmUseCase
import com.segnities007.yatte.domain.aggregate.history.usecase.AddHistoryUseCase
import com.segnities007.yatte.domain.aggregate.history.usecase.ClearAllHistoryUseCase
import com.segnities007.yatte.domain.aggregate.history.usecase.DeleteHistoryUseCase
import com.segnities007.yatte.domain.aggregate.history.usecase.ExportHistoryUseCase
import com.segnities007.yatte.domain.aggregate.history.usecase.GetHistoryTimelineUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.CompleteTaskUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.CreateTaskUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.DeleteTaskUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.GetAllTasksUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.GetTaskByIdUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.GetTodayTasksUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.SkipTaskUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.UpdateTaskUseCase
import com.segnities007.yatte.domain.aggregate.settings.usecase.GetSettingsUseCase
import com.segnities007.yatte.domain.aggregate.settings.usecase.ResetAllDataUseCase
import com.segnities007.yatte.domain.aggregate.settings.usecase.UpdateSettingsUseCase
import org.koin.dsl.module

/**
 * ユースケースモジュール
 */
val useCaseModule = module {
    // Task UseCases
    factory { CreateTaskUseCase(get()) }
    factory { GetTodayTasksUseCase(get()) }
    factory { GetAllTasksUseCase(get()) }
    factory { GetTaskByIdUseCase(get()) }
    factory { UpdateTaskUseCase(get()) }
    factory { DeleteTaskUseCase(get()) }
    factory { CompleteTaskUseCase(get()) }
    factory { SkipTaskUseCase(get()) }

    // History UseCases
    factory { AddHistoryUseCase(get()) }
    factory { GetHistoryTimelineUseCase(get()) }
    factory { DeleteHistoryUseCase(get()) }
    factory { ClearAllHistoryUseCase(get()) }
    factory { ExportHistoryUseCase(get()) }

    // Alarm UseCases
    factory { ScheduleAlarmUseCase(get(), get()) }
    factory { CancelAlarmUseCase(get(), get()) }
    factory { GetScheduledAlarmsUseCase(get()) }

    // Settings UseCases
    factory { GetSettingsUseCase(get()) }
    factory { UpdateSettingsUseCase(get()) }
    factory { ResetAllDataUseCase(get(), get()) }
}
