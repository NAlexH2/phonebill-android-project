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

public class TextParser implements PhoneBillParser<PhoneBill> {
  private final Reader reader;

  public TextParser(Reader reader) {
    this.reader = reader;
  }

  @Override
  public PhoneBill parse() throws ParserException {
    String utility;
    String[] callArgs;
    ArrayList<String> lineData = new ArrayList<>();
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
        bill.addPhoneCall(callParsedFromFile);
      }
      return bill;
    } catch (IOException | IndexOutOfBoundsException ex) {
      throw new ParserException("");//File exists but there was actually nothing to parse, back out and load collection
    }
  }

  public PhoneBill parseByDateRange(PhoneBill toDump, String beginDateTime, String endDateTime)
      throws IOException {
    String utility;
    ArrayList<String> lineData = new ArrayList<>();
    String[] callArgs;
    Date beginDateObject = toDump.getPhoneCalls().first().dateTimeMaker(beginDateTime);
    Date endDateObject = toDump.getPhoneCalls().first().dateTimeMaker(endDateTime);
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
