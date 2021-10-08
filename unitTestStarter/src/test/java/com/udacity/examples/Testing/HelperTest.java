package com.udacity.examples.Testing;

import junit.framework.TestCase;
import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import com.udacity.examples.Testing.Helper;

import static org.junit.Assert.assertEquals;


public class HelperTest {

   private List <String> empName = Arrays.asList("One", "", "Two", "", "", "", "Three");
   private List<Integer> expYears = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 0);

   // This method must be public and static
   @BeforeClass
   public static void initClass() {
      System.out.println("init Class executed");
   }
   @Before
   public void init() {
      System.out.println("init executed"); 
   }
   @After
   public void teardown() {
      System.out.println("teardown executed"); 
   }

   @AfterClass
   public static void teardownclass() {
      System.out.println("teardown Class executed");
   }

	@Test
   public void testMergedList(){
      String actual = Helper.getMergedList(empName);
      String expected = "One, Two, Three";
      assertEquals("Comparing output for getMergedList()",
              actual, expected);
   }

   @Ignore
   @Test
   public void testStats(){
      IntSummaryStatistics stat = Helper.getStats(expYears);
      Integer actual = stat.getMax();
      Integer expected = 7;
      assertEquals("Comparing output for getStats()",
              actual, expected);
   }
}
