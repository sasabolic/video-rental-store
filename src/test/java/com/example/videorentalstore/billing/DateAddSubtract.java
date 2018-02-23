package com.example.videorentalstore.billing;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class DateAddSubtract {
 
 public static void main(String arg[]) throws ParseException
  {
 
     LocalDate currentDate = LocalDate.now();    // The current date
 
     String specifiedDateTemp="2015-01-01";
 
     LocalDate specifiedDate = LocalDate.parse(specifiedDateTemp, DateTimeFormatter.ofPattern("yyyy-MM-dd"));     // The current date and time
 
     Period period;  //to specify any period (i.e. 1 year, 2 month, 5 days)  Period.of (1,2,5)
 
     System.out.println("Current Date, Specified Date = " + currentDate +", "+ specifiedDate);
 
     System.out.println("Plus 1 Day = " + currentDate.plusDays(1)  + ", " + specifiedDate.plusDays(1));
     period = Period.of(0, 0, 1);
     System.out.println("Plus 1 Day using Period = " + currentDate.plus(period)  + ", " + specifiedDate.plus(period));
 
     System.out.println("Minus 1 Day = " + currentDate.minusDays(1)  + ", " + specifiedDate.plusDays(-1));
     System.out.println("Minus 1 Day using Period = " + currentDate.minus(period)  + ", " + specifiedDate.minus(period));
 
     System.out.println("Plus 1 Week = " + currentDate.plusWeeks(1)  + ", " + specifiedDate.plusWeeks(1));
     period = Period.ofWeeks(1);
     System.out.println("Plus 1 Week using Period  = " + currentDate.plus(period)  + ", " + specifiedDate.plus(period));
 
     System.out.println("Minus 1 Week = " + currentDate.minusWeeks(1)  + ", " + specifiedDate.plusWeeks(-1));
     System.out.println("Minus 1 Week using Period = " + currentDate.minus(period)  + ", " + specifiedDate.minus(period));
 
     System.out.println("Plus 1 Month = " + currentDate.plusMonths(1)  + ", " + specifiedDate.plusMonths(1));
     period = Period.of(0, 1, 0); // OR Period.ofMonths(1);
     System.out.println("Plus 1 Month using Period = " + currentDate.plus(period)  + ", " + specifiedDate.plus(period));
 
     System.out.println("Minus 1 Month = " + currentDate.minusMonths(1)  + ", " + specifiedDate.plusMonths(-1));
     System.out.println("Minus 1 Month using Period = " + currentDate.minus(period)  + ", " + specifiedDate.minus(period));
 
     System.out.println("Plus 1 Year = " + currentDate.plusYears(1)  + ", " + specifiedDate.plusYears(1));
     period = Period.of(1, 0, 0); // OR Period.ofYears(1);
     System.out.println("Plus 1 Year using Period = " + currentDate.plus(period)  + ", " + specifiedDate.plus(period));
 
     System.out.println("Minus 1 Year = " + currentDate.minusYears(1)  + ", " + specifiedDate.plusYears(-1));
     System.out.println("Minus 1 Year using Period = " + currentDate.minus(period)  + ", " + specifiedDate.minus(period));
 
     System.out.println("Plus 1 Year, 1 Month, 1 Week,  1 Day = " + currentDate.plusYears(1).plusMonths(1).plusWeeks(1).plusDays(1)  + ", " + specifiedDate.plusYears(1).plusMonths(1).plusWeeks(1).plusDays(1));
     period = Period.of(1,1,8);
     System.out.println("Plus 1 Year, 1 Month, 1 Week,  1 Day using Period = " + currentDate.plus(period)  + ", " + specifiedDate.plus(period));
 
     System.out.println("Minus 1 Year, 1 Month, 1 Week,  1 Day = " + currentDate.minusYears(1).minusMonths(1).minusWeeks(1).minusDays(1)  + ", " + specifiedDate.minusYears(1).minusMonths(1).minusWeeks(1).minusDays(1));
     System.out.println("Minus 1 Year, 1 Month, 1 Week,  1 Day using Period = " + currentDate.minus(period)  + ", " + specifiedDate.minus(period));
 
     System.out.println("Using negative values in Period");
 
     period = Period.of(-1,-1,-8);
     System.out.println("Minus 1 Year, 1 Month, 1 Week,  1 Day using Period = " + currentDate.plus(period)  + ", " + specifiedDate.plus(period));
 
  }
 
}
