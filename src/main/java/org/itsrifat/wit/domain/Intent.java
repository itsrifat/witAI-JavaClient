package org.itsrifat.wit.domain;

/**
 * Author: Md Moinul Hossain
 * email: moinul.hossain@csebuet.org
 * Date: 12/24/13
 */


public class Intent {
  private String value;

  public Intent(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
