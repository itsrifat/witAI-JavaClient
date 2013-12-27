package org.itsrifat.wit.domain;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.itsrifat.wit.json.MessageDeserializer;

import java.util.List;

/**
 * Author: Md Moinul Hossain
 * email: moinul.hossain@csebuet.org
 * Date: 12/24/13
 */
@JsonDeserialize(using = MessageDeserializer.class)
public class Message {


  private String id;
  private String messageBody;
  private Double confidence;
  private Intent intent;
  private List<EntityResult> entities;
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMessageBody() {
    return messageBody;
  }

  public void setMessageBody(String messageBody) {
    this.messageBody = messageBody;
  }

  public Double getConfidence() {
    return confidence;
  }

  public void setConfidence(Double confidence) {
    this.confidence = confidence;
  }

  public Intent getIntent() {
    return intent;
  }

  public void setIntent(Intent intent) {
    this.intent = intent;
  }

  public List<EntityResult> getEntities() {
    return entities;
  }

  public void setEntities(List<EntityResult> entities) {
    this.entities = entities;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}
