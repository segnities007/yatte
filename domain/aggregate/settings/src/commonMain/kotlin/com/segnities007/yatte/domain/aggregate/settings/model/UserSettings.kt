package com.segnities007.yatte.domain.aggregate.settings.model

/**
 * ユーザー設定のドメインモデル
 *
 * @property defaultMinutesBefore デフォルトのアラーム前通知時間（0〜60分）
 * @property notificationSound 通知音を鳴らすかどうか
 * @property notificationVibration バイブレーションするかどうか
 * @property themeMode テーマモード
 */
data class UserSettings(
    val defaultMinutesBefore: Int = 10,
    val notificationSound: Boolean = true,
    val notificationVibration: Boolean = true,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
) {
    init {
        require(defaultMinutesBefore in 0..MAX_MINUTES_BEFORE) {
            "デフォルト通知時間は0〜${MAX_MINUTES_BEFORE}分の範囲で指定してください"
        }
    }

    companion object {
        private const val MAX_MINUTES_BEFORE = 60

        /**
         * デフォルト設定
         */
        val DEFAULT = UserSettings()
    }
}

