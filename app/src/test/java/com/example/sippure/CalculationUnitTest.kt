package com.example.sippure

import junit.framework.TestCase.assertEquals
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.test.Test

class CalculationUnitTest {
    @Test
    fun add_check(){
        val calculation = Calculations()
        val result = calculation.add(5,5)

        assertEquals(10,result)

    }
    @Test
    fun add_usingMockito(){
        val calculator = mock(Calculations::class.java)
        `when`(calculator.add(5,5)).thenReturn(10)

        assertEquals(10,calculator.add(5,5))
    }

}