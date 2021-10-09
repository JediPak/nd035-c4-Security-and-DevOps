package com.udacity.examples.Testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ParamTest {

   private List <String> input;
   private String output;

   public ParamTest(List <String> input, String output) {
      super();
      this.input = input;
      this.output = output;
   }

   @Parameterized.Parameters
   public static Collection initData(){
//      Object[][] inputs = new Object[][]
//
//              {
//                      {
//                              Arrays.asList("One", "", "Two", "", "", "", "Three"),
//                              "One, Two, Three"
//                      },
//
//                      {
//                              Arrays.asList("One", "", "Two", "Three", "", "", "Four"),
//                              "One, Two, Three, Four"
//                      },
//
//                      {
//                              Arrays.asList("One", "Two", "", "", "", "", ""),
//                              "One"
//                      }
//              };
      List<Object[]> result = new ArrayList <>();
      result.add( new Object[] {(Arrays.asList("One", "", "Two", "", "", "", "Three")), "One, Two, Three"} );
      result.add( new Object[] {(Arrays.asList("One", "", "Two", "Three", "", "", "Four")), "One, Two, Three, Four"} );
      result.add( new Object[] {(Arrays.asList("One", "Two", "", "", "", "", "")), "One"} );


      return result;//Arrays.asList(inputs);
   }

   @Test
   public void VerifyTest(){
//      String actual = Helper.getMergedList(input);
      assertEquals("Comparing output for getMergedList()",
              Helper.getMergedList(input), output);

   }
}

