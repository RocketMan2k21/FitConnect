package org.connect.fitconnect

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform