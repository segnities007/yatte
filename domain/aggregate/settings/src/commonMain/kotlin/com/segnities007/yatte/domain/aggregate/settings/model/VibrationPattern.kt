package com.segnities007.yatte.domain.aggregate.settings.model

/**
 * バイブレーションパターン
 */
enum class VibrationPattern {
    NORMAL, // 普通 (0, 500, 200, 500)
    SHORT,  // 短い (0, 200, 200, 200)
    LONG,   // 長い (0, 1000, 500, 1000)
    SOS,    // SOS (0, 200, 200, 200, 200, 200, 500, 500, 200, 500, 200, 500, 500, 200, 200, 200, 200, 200, 200)
    HEARTBEAT, // 鼓動 (0, 100, 100, 100)
    ;


}
