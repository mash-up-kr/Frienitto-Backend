package org.frienitto.manitto

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest(
        webEnvironment= SpringBootTest.WebEnvironment.MOCK,
        classes = [ManittoApplication::class]
)
@AutoConfigureMockMvc
class SpringWebMvcTestSupport {

    @Autowired
    protected lateinit var mockMvc: MockMvc
}