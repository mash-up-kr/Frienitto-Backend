package org.frienitto.manitto.util

import java.net.InetAddress
import java.security.MessageDigest

fun generateToken(key: String, length: Int = 64): String {
    return MessageDigest.getInstance("SHA-256")
            .digest((System.nanoTime().toString() + InetAddress.getLocalHost().hostAddress + key).toByteArray())
            .joinToString("") { "%02X".format(it) }
            .take(length)
}

fun generateAuthCode(key: String): String {
    return generateToken(key, 8)
}