package edu.pdx.cs410J.nharris;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.SortedSet;

public class TextDumper {
  private final Writer writer;

  public TextDumper(Writer writer) {
    this.writer = writer;
  }

  /**
   * Writes all of PhoneBills current collection of calls to file
   * @param bill The PhoneBill currently being access using <code>PhoneBill.this</code>
   */
  public void dump(PhoneBill bill) {
    try (PrintWriter pw = new PrintWriter(this.writer)) {
      SortedSet<PhoneCall> calls = bill.getPhoneCalls();
      for (PhoneCall call : calls) {
        //Go through the bill calls collection, and write to the file using a built string
        // using PhoneCalls methods
        pw.println(lineBuilder(call));
      }
      pw.flush();
    }
  }

  public String lineBuilder(PhoneCall call) {
    return call.getCallerName() + "," + call.getCaller() + "," + call.getCallee() + ","
        + call.getBeginTimeStringFile() + "," + call.getEndTimeStringFile() + ","
        + call.getCallDuration();
  }
}
