package com.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void calculatorInit(){
        calculator = new Calculator();
    }

    @Test
    void shouldSubstractTwoPositivesTest() {
        //act
        int result = calculator.subtract(5, 3);
        //assert
        assertEquals(2, result);
    }

    @Test
    void shouldSubstractNegativeResultTest() {
        //act
        int result = calculator.subtract(3, 5);
        //assert
        assertEquals(-2, result);
    }

    @Test
    void shouldSubstractZeroTest() {
        //act
        int result = calculator.subtract(5, 0);
        //assert
        assertEquals(5, result);
    }

    @Test
    void shouldSubstractFromZeroTest() {
        //act
        int result = calculator.subtract(0, 5);
        //assert
        assertEquals(-5, result);
    }

    @Test
    void shouldSubstractTwoNegativesTest() {
        //act
        int result = calculator.subtract(-3, -2);
        //assert
        assertEquals(-1, result);

    }

}