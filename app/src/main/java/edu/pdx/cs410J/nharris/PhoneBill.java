package edu.pdx.cs410J.nharris;

import edu.pdx.cs410J.AbstractPhoneBill;

import java.util.SortedSet;
import java.util.TreeSet;

public class PhoneBill extends AbstractPhoneBill<PhoneCall> {
  private final String customer;
  private final SortedSet<PhoneCall> calls = new TreeSet<>();

  /**
   * PhoneBill constructor
   * @param customer assignment for match enforcement elsewhere
   */
  PhoneBill(String customer) {
    this.customer = customer;
  }

  /**
   * @return string of current bill customer name
   */
  @Override
  public String getCustomer() {
    return this.customer;
  }

  /**
   * Adds call to current bill
   * @param call  call to add
   */
  @Override
  public void addPhoneCall(PhoneCall call) {
    this.calls.add(call);
  }

  /**
   * Returns the SortedSet calls from the PhoneBill object
   * @return  all calls from the SortedSet
   */
  @Override
  public SortedSet<PhoneCall> getPhoneCalls() {
    return this.calls;
  }


}