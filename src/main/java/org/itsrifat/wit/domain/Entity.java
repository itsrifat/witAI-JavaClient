package org.itsrifat.wit.domain;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Author: Md Moinul Hossain
 * email: moinul.hossain@csebuet.org
 * Date: 12/24/13
 */
public class Entity {

  private String id;
  private String doc;
  private Boolean builtin;

  private List<Map<String,Object>> values;

  private Integer version;

  @JsonProperty("created_at")
  private Date createdAt;
  @JsonProperty("updated_at")
  private Date updatedAt;

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDoc() {
    return doc;
  }

  public void setDoc(String doc) {
    this.doc = doc;
  }

  public Boolean getBuiltin() {
    return builtin;
  }

  public void setBuiltin(Boolean builtin) {
    this.builtin = builtin;
  }

  public List<Map<String, Object>> getValues() {
    return values;
  }

  public void setValues(List<Map<String, Object>> values) {
    this.values = values;
  }

  @Override
  public String toString(){
    return ReflectionToStringBuilder.toString(this);
  }
}
