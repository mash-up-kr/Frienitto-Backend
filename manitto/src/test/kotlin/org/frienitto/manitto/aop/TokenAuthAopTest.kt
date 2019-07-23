package org.frienitto.manitto.aop

import org.frienitto.manitto.SpringWebMvcTestSupport
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class TokenAuthAopTest : SpringWebMvcTestSupport() {

    @Test
    fun `유효 하지 않은 토큰으로 api를 요청 했을 때`() {
        mockMvc.perform(get("/api/v1/room/1").header("X-Authorization", "invalid"))
                .andDo { print() }
                .andExpect { status().is4xxClientError }
    }
}