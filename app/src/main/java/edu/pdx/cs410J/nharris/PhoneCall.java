package edu.pdx.cs410J.nharris;

import edu.pdx.cs410J.AbstractPhoneCall;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <code>PhoneCall</code> is the info passed in from the
 * command line to be provided to <code>WebPhoneBill</code>.
 */
public class PhoneCall extends AbstractPhoneCall implements Comparable<PhoneCall>{
  private final DateTimeFormatter dateTimeFormat;

  private String callerName = ""; //Name of person making call
  private String endDate = ""; //Date ended
  private String beginTime = ""; //Time the call started
  private String beginAMOrPM = "";
  private String endTime = ""; //Time ended
  private String endAMOrPM = "";
  private String callerNumber = ""; //Caller phone number (person p# doing the calling)
  private String calleeNumber = ""; //Callee phone number (person p# being called)
  private String beginDate = ""; //Date the call started

  /**
   * Creates a new <code>PhoneCall</code> object.
   */
  PhoneCall(String[] args) {
    this.dateTimeFormat = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
    if(args.length == 9) {
      this.callerName = args[0];
      this.callerNumber = args[1];
      this.calleeNumber = args[2];
      this.beginDate = args[3];
      this.beginTime = args[4];
      this.beginAMOrPM = args[5].toUpperCase();
      this.endDate = args[6];
      this.endTime = args[7];
      this.endAMOrPM = args[8].toUpperCase();
    } else if (args.length == 7) {
      this.callerName = args[0];
      this.beginDate = args[1];
      this.beginTime = args[2];
      this.beginAMOrPM = args[3].toUpperCase();
      this.endDate = args[4];
      this.endTime = args[5];
      this.endAMOrPM = args[6].toUpperCase();
    } else if (args.length == 1) {
      this.callerName = args[0];
    }
  }

  /**
   * Grabs caller name from the accessed <code>PhoneCall</code> class
   *
   * @return A String of the callers name
   */
  public String getCustomer() {
    return this.callerName;
  }


  /**
   * Grabs the caller number from the accessed <code>PhoneCall</code> class
   *
   * @return The callers phone number as a string
   */
  @Override
  public String getCaller() {
    return this.callerNumber;
  }


  /**
   * Grabs callee number from the accessed <code>PhoneCall</code> class
   *
   * @return The callee phone number as a string
   */
  @Override
  public String getCallee() {
    return this.calleeNumber;
  }


  /**
   * Grabs the calls begin time of the call from the accessed <code>PhoneCall</code> class
   *
   * @return A date object with the exact start time of the call
   */
  @Override
  public Date getBeginTime() {
    return dateTimeMaker(this.beginDate + " " + this.beginTime + " " + this.beginAMOrPM);
  }


  /**
   * Grabs the calls begin time from the accessed <code>PhoneCall</code> class
   *
   * @return The calls begin time as a string
   */
  @Override
  public String getBeginTimeString() {
    return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(getBeginTime());
  }


  /**
   * Grabs the end time of the call from the accessed <code>PhoneCall</code> class
   *
   * @return A date object with the exact start time of the call in ISO-8601
   */
  @Override
  public Date getEndTime() {
    return dateTimeMaker(this.endDate + " " + this.endTime + " " + this.endAMOrPM);
  }


  /**
   * Grabs the calls end time from the accessed <code>PhoneCall</code> class
   *
   * @return The calls end time as a string
   */
  @Override
  public String getEndTimeString() {
    return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(getEndTime());
  }

  /**
   * Calculates the duration of the call down to the single digit minute
   *
   * @return String of the duration
   */
  public String getCallDuration() {
    Date startDate = getBeginTime();
    Date endDate = getEndTime();
    //Calc the diff using milliseconds, convert milliseconds to minutes w/ diff/1000*60
    return String.valueOf((endDate.getTime() - startDate.getTime()) / (1000 * 60));
  }

  /**
   * Makes the date and time for the class from the values within the class for date, time, and time of day designator
   * of AM/PM
   *
   * @return Date to be used elsewhere
   */
  Date dateTimeMaker(String dateTimeString) {
    LocalDateTime ldt = LocalDateTime.parse(dateTimeString, this.dateTimeFormat);
    ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
    return Date.from(zdt.toInstant());
  }

  /**
   * Converts all this.PhoneCall to CSV string.
   * @return A single string of all call info as a CSV string
   */
  String phoneCallCSV() {
    return this.callerName + "," + this.callerNumber + "," + this.calleeNumber + ","
            + this.beginDate + "," + this.beginTime + "," + this.beginAMOrPM +
            "," + this.endDate + "," + this.endTime + "," + this.beginAMOrPM;
  }

  /**
   * Compares and sorts phone calls
   * @param o the object to be compared.
   * @return -1, 0 or 1 if this object is less than, equal to, or greater than the one passed in
   */
  @Override
  public int compareTo(PhoneCall o) {
    if(this.getBeginTime().equals(o.getBeginTime()))
    {
      return this.callerNumber.compareTo(o.callerNumber);
    }
    return this.getBeginTime().compareTo(o.getBeginTime());
  }


}
