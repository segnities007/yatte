package com.segnites007.yatte

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
