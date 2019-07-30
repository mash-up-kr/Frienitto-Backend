package org.frienitto.manitto.dto

open class Response<T>(val code: Int = 0, val msg: String = "", val data: T? = null) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response<*>) return false

        if (code != other.code) return false
        if (msg != other.msg) return false
        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code
        result = 31 * result + msg.hashCode()
        result = 31 * result + (data?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Response(code=$code, msg='$msg', data=$data)"
    }
}