package edu.pdx.cs410J.nharris;

import edu.pdx.cs410J.AbstractPhoneCall;

public class PhoneCall extends AbstractPhoneCall {
  @Override
  public String getCaller() {
    return "123-456-9845";
  }

  @Override
  public String getCallee() {
    return "142-512-3123";
  }

  @Override
  public String getBeginTimeString() {
    return "1/2/2022 1:00 PM";
  }

  @Override
  public String getEndTimeString() {
    return "1/2/2022 2:00 PM";
  }
}
