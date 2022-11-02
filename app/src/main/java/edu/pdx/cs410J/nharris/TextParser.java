package edu.pdx.cs410J.nharris;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.PhoneBillParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * This class parses all information from a text file and sends it back to the user either
 * for regular viewing purposes, or because a search was performed.
 */
public class TextParser implements PhoneBillParser<PhoneBill> {
  private final Reader reader;

  /**
   * This parser requires a Reader by way of <code>new TextParser(new FileReader(toPull))</code>
   * where <code>toPull was the file found</code>
   * @param reader - The object used to read a stream of characters
   */
  public TextParser(Reader reader) {
    this.reader = reader;
  }

  /**
   * Parses all calls in a phone bill whithin the specified reader
   * @return A <code>PhoneBill</code> which has a collection of <code>PhoneCall</code> by way of
   * <code>SortedSet</code> of type <code>PhoneCall</code>
   * @throws ParserException - If a file exists, but there was nothing to parse.
   */
  @Override
  public PhoneBill parse() throws ParserException {
    // String to use to shrink down a call for customer name
    String utility;
    // All the details after customer name
    String[] callArgs;
    // Each line in the file is to be stored in the ArrayList so that when the program
    // adds phone calls to the bill, a simple for loop is utilized
    ArrayList<String> lineData = new ArrayList<>();

    // "Prime the pump" with a try
    try (BufferedReader br = new BufferedReader(this.reader)) {
      // while the current line isn't null
      while((utility = br.readLine()) != null) {
        // and the current line isn't empty
        if (!utility.equals(""))
          // add the whole line to the previously mentioned ArrayList
          lineData.add(utility);
      }
      // Customer name is always the first item on the line, grab that real quick
      utility = lineData.get(0).split(",")[0];
      // PhoneBill requires customer name as a bare minimum, it's useful elsewhere as quick access
      // and guarantees that the current PhoneBill object we are working with has *at least* the
      // name of the customer
      PhoneBill bill = new PhoneBill(utility);

      // Loading up the bill!
      for(String i : lineData)
      {
        // callArgs is an array of strings, so we can split the entire line and store it in this
        // variable.
        callArgs = i.split(",");
        // Because the duration of a call is calculated elsewhere, the callArgs only has 8 indices
        // vs the 9 required - the 9th being the duration.
        callArgs = Arrays.copyOf(callArgs, 9);

        // Create a PhoneCall object with newly created callArgs
        PhoneCall callParsedFromFile = new PhoneCall(callArgs);
        // Load that object into the SortedSet inside of PhoneBill!
        bill.addPhoneCall(callParsedFromFile);
      }
      // Return our whole entire beautiful bill <3
      return bill;

      // Or not...
    } catch (IOException | IndexOutOfBoundsException ex) {
      // File exists but there was actually nothing to parse, back out and load collection
      throw new ParserException("");
    }
  }

  /**
   * Gathers all calls that happened within a specified date range on a already established collection
   * of calls.
   * @param toDump - Current customers PhoneBill to be scanned and dumped to an object to be returned.
   * @param beginDateTime - The date the user specified to start on
   * @param endDateTime - The date the user specified to end on (exclusive)
   * @return - PhoneBill with the identified date ranges
   * @throws IOException If the file wasn't found or corrupt
   */
  public PhoneBill parseByDateRange(PhoneBill toDump, String beginDateTime, String endDateTime)
      throws IOException {
    String utility; // For customer name just as the parse function
    ArrayList<String> lineData = new ArrayList<>(); // Same as parse function
    String[] callArgs; // Same as parse function
    // Using the power of the date libraries, making a date and time is much easier than working
    // from scratch.
    Date beginDateObject = toDump.getPhoneCalls().first().dateTimeMaker(beginDateTime);
    Date endDateObject = toDump.getPhoneCalls().first().dateTimeMaker(endDateTime);

    // Similar to parse, but very modified due to having to scan explicitly for the date/time
    // range for each line in the file. Being that the calls are all sorted in the file by date/time
    // this executes with relative ease.
    try (BufferedReader br = new BufferedReader(this.reader)) {
      while((utility = br.readLine()) != null) {
        if (!utility.equals(""))
          lineData.add(utility);
      }
      utility = lineData.get(0).split(",")[0];
      PhoneBill bill = new PhoneBill(utility);
      for(String i : lineData)
      {
        callArgs = i.split(",");
        callArgs = Arrays.copyOf(callArgs, 9);
        PhoneCall callParsedFromFile = new PhoneCall(callArgs);
        if(callParsedFromFile.getBeginTime().after(beginDateObject)
            && callParsedFromFile.getEndTime().before(endDateObject)) {
          bill.addPhoneCall(callParsedFromFile);
        }
      }
      return bill;
    }
  }
}
