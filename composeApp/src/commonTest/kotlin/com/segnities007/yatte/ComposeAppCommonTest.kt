package com.segnities007.yatte

import kotlin.test.Test
import kotlin.test.assertEquals

class ComposeAppCommonTest {
    @Test
    fun `GIVEN 1 and 2 WHEN added THEN result is 3`() {
        // Arrange
        val a = 1
        val b = 2

        // Act
        val result = a + b

        // Assert
        assertEquals(3, result)
    }
}
