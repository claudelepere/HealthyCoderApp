package com.healthycoderapp;

import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class BMICalculatorTest {
  
  private String environment = "DEV";
  
  @BeforeAll
  static void setUpBeforeClass() {
    System.out.println("before all the unit tests");
  }
  
  @AfterAll
  static void tearDownAfterClass() {
    System.out.println("after all the unit tests");
  }

  @Nested
  class IsDietRecommendedTests {
    @ParameterizedTest(name = "weight={0}, height={1}")
  //  @ValueSource(doubles = {89.0, 95.0, 110.0})
    @CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)
    void should_ReturnTrue_When_DietRecommended(double coderWeight, double coderHeight) {
      // given
      double weight = coderWeight;
      double height = coderHeight;
      
      //when
      System.out.println(25.0 * (height * height));
      double bmi = weight / (height * height);
      System.out.println(bmi);
      boolean recommended = BMICalculator.isDietRecommended(weight, height);
      
      // then
      assertTrue(recommended);
    }

    @Test
    void should_ReturnFalse_When_DietNotRecommended() {
      // given
      double weight = 50.0;
      double height = 1.92;
      
      //when
      boolean recommended = BMICalculator.isDietRecommended(weight, height);
      
      // then
      assertFalse(recommended);
    }

    @Test
    void should_ThrowArithmeticException_When_HeightZero() {
      // given
      double weight = 50.0;
      double height = 0.0;
      
      //when
      Executable executable = () -> BMICalculator.isDietRecommended(weight, height);
      
      // then
      assertThrows(ArithmeticException.class, executable);
    }
  }
  

  @Nested
  @DisplayName("°°°°°°°°°°°°°°°° sample inner class display name")
  class FindCoderWithWorstBMITests {
    
    @BeforeEach
    void setUpBeforeClass() {
      System.out.println("before each in Nested FindCoderWithWorstBMITests");
    }
    
    @Test
    @DisplayName(">>>>>>>>> sample method display name")
    @DisabledOnOs(OS.LINUX)
    void should_ReturnCoderWithWorstBMI_When_CoderListNotEmpty() {
      // given
      List<Coder> coders = new ArrayList<>();
      coders.add(new Coder(1.80, 60.0));
      coders.add(new Coder(1.82, 98.0));
      coders.add(new Coder(1.82, 64.7));
      
      //when
      Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);
      
      // then
      assertAll(
          () -> assertEquals(1.82, coderWorstBMI.getHeight()),
          () -> assertEquals(98.0, coderWorstBMI.getWeight())
      );
    }
    
    @Test
    void should_ReturnCoderWithWorstBMIin1Ms_When_CoderListHas10000Elements() {
      // given
      assumeTrue(BMICalculatorTest.this.environment.equals("PROD"));
      List<Coder> coders = new ArrayList<>();
      for (int i = 0; i < 10000; i++) {
        coders.add(new Coder(1.0 + i, 10.0 + i));
      }
      
      // when
      Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);

      // then
      assertTimeout(Duration.ofMillis(1L), executable);
    }

    @Test
    void should_ReturnNullCoderWithWorstBMI_When_CoderListEmpty() {
      // given
      List<Coder> coders = new ArrayList<>();
      
      //when
      Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);
      
      // then
      assertNull(coderWorstBMI);
    }
  }

  @Nested
  class GetBMIScoresTests {
    @Test
    void should_ReturnCorrectBMIScoreArray_When_CoderListNotEmpty() {
      //given
      List<Coder> coders = new ArrayList<>();
      coders.add(new Coder(1.80, 60.0));
      coders.add(new Coder(1.82, 98.0));
      coders.add(new Coder(1.82, 64.7));
      double[] expected = {18.52, 29.59, 19.53};
      
      //when
      double[] bmiScores = BMICalculator.getBMIScores(coders);

      //then
      assertArrayEquals(expected, bmiScores);
    }
  }
}
