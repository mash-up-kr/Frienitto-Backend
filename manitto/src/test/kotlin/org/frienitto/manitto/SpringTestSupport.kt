package org.frienitto.manitto

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional

@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
@SpringBootTest(classes = [ManittoApplication::class])
@Transactional
abstract class SpringTestSupport