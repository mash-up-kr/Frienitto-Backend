package org.frienitto.manitto.dto

data class Response<T>(val code: Int, val msg: String, val data: T? = null)