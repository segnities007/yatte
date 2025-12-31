package com.segnities007.yatte

import kotlin.test.Test
import kotlin.test.assertEquals

class ComposeAppCommonTest {
    @Test
    fun Given_1and2_When_Added_Then_ResultIs3() {
        // Arrange
        val a = 1
        val b = 2

        // Act
        val result = a + b

        // Assert
        assertEquals(3, result)
    }
}
