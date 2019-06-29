package org.frienitto.manitto.dto

data class IssueCodeRequest(val receiverInfo: String, val type: String)

data class VerifyCodeRequest(val receiverInfo: String, val type: String, val code: String)