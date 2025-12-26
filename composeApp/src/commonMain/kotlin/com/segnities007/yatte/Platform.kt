package com.segnities007.yatte

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
