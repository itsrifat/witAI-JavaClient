package org.itsrifat.wit.domain;

import java.util.Map;

/**
 * Author: Moinul Hossain Rifat
 * email: moinul.hossain@csebuet.org
 * Date: 12/25/13
 */
public class EntityResult {
  private String id;
  private Map<String,? extends Object> data;


  public EntityResult(String id, Map<String,? extends Object> data) {
    this.id = id;
    this.data = data;
  }

  public Map<String,? extends Object> getData() {
    return data;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setData(Map<String, String> data) {
    this.data = data;
  }
}
