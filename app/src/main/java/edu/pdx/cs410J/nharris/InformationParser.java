package edu.pdx.cs410J.nharris;


import android.provider.ContactsContract;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

class InformationParser {

    void validNumber(String callerNum, String calleeNum) throws CommandLineParserException {


        if (callerNum.length() > 12) {
            throw new CommandLineParserException("Caller phone number is too long. Max number of digits in phone number is 10!");
        }
        if (!callerNum.matches("^\\d{3}-\\d{3}-\\d{4}$")) {
            throw new CommandLineParserException("Caller phone number is invalid. Phone number must contain " +
                    "only numbers and dashes - EX: 123-456-7890");
        }
        if (calleeNum.length() > 12) {
            throw new CommandLineParserException("Callee phone number is too long. Max number of digits in phone number is 10!");
        }
        if (!calleeNum.matches("^\\d{3}-\\d{3}-\\d{4}|\\d{3}$")) {
            throw new CommandLineParserException("Callee phone number is invalid. Phone number must contain " +
                    "only numbers and dashes - EX: 123-456-7890 -OR- 911.");
        }
    }

    String argIndexExists(String[] allInfo) throws CommandLineParserException {
        for(String i : allInfo) {
            if(i.isEmpty()) {
                throw new CommandLineParserException("Required information was missing.");
            }
        }
        return "";
    }

    void dateTimeValidator(PhoneCall call, String start, String end)
        throws CommandLineParserException {

        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");

        try {
            dateTimeFormat.parse(start);
        } catch (DateTimeParseException ex) {
            throw new CommandLineParserException("Start date/time is incorrect: " + start +
                    " - run the program with the option -README to learn how to use this program.");
        }
        try {
            dateTimeFormat.parse(end);
        } catch (DateTimeParseException ex) {
            throw new CommandLineParserException("End date/time is incorrect: " + end +
                    " - run the program with the option -README to learn how to use this program.");
        }

        //convert this.phoneArgs to an array with a new string array size of [0]
        // (thanks intellij for that, not sure why) and take all those String[] args make a new call
        // to invoke dateTimeMaker... This is not an ideal process.... But is required to eliminate duplicate code.
        Date beginDateObject = call.dateTimeMaker(start);
        Date endDateObject = call.dateTimeMaker(end);
        if(endDateObject.before(beginDateObject))
            throw new CommandLineParserException("Your END DATE is BEFORE your START DATE for " +
                "the phone call. -> End date: " + start +
                " - Start date: " + end);
    }

    /**
     * Exception used when an option argument is found in the positions outside
     * the first two indices of the command line arguments
     */
    static class CommandLineParserException extends Exception {
        CommandLineParserException(String message) {
            super(message);
        }
    }
}
