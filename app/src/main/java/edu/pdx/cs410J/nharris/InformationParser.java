package edu.pdx.cs410J.nharris;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

class InformationParser {

  String dateTimeValidator(PhoneCall call, String start, String end) {

    DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");

    try {
      dateTimeFormat.parse(start);
    } catch (DateTimeParseException ex) {
      return "Start date/time bad format: " + start + ". Expected mm/dd/yyyy";
    }
    try {
      dateTimeFormat.parse(end);
    } catch (DateTimeParseException ex) {
      return "Start date/time bad format: " + end + ". Expected mm/dd/yyyy";
    }

    //convert this.phoneArgs to an array with a new string array size of [0]
    // (thanks intellij for that, not sure why) and take all those String[] args make a new call
    // to invoke dateTimeMaker... This is not an ideal process.... But is required to eliminate duplicate code.
    Date beginDateObject = call.dateTimeMaker(start);
    Date endDateObject = call.dateTimeMaker(end);
    if(endDateObject.before(beginDateObject))
      return "Your END DATE is BEFORE your START DATE for " +
          "the phone call. -> End date: " + start +
          " - Start date: " + end;

    return "";
  }

}
