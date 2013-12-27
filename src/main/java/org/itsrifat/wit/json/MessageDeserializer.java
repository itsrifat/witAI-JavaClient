package org.itsrifat.wit.json;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.itsrifat.wit.domain.EntityResult;
import org.itsrifat.wit.domain.Intent;
import org.itsrifat.wit.domain.Message;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;



/**
 * Author: Md Moinul Hossain
 * email: moinul.hossain@csebuet.org
 * Date: 12/25/13
 */
public class MessageDeserializer extends JsonDeserializer<Message> {
  private static final Logger LOGGER = Logger.getLogger(MessageDeserializer.class);
  @Override
  public Message deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    LOGGER.info("Trying to deserialize json of Message");
    Message message = new Message();
    ObjectCodec oc = jp.getCodec();
    JsonNode messageNode = oc.readTree(jp);
    message.setId(messageNode.get("msg_id").getTextValue());
    message.setMessageBody(messageNode.get("msg_body").getTextValue());
    JsonNode outcomeNode = messageNode.get("outcome");
    JsonNode intentNode = outcomeNode.get("intent");
    Intent intent = new Intent(intentNode.getTextValue());
    message.setIntent(intent);
    List<EntityResult> entityList = new ArrayList<EntityResult>();
    JsonNode entitiesNode = outcomeNode.get("entities");
    Iterator<Map.Entry<String, JsonNode>> fields = entitiesNode.getFields();
    while ((fields.hasNext())){
      Map.Entry<String, JsonNode> entityNode = fields.next();
      Map<String,Object> entityData = new HashMap<String, Object>();
      JsonNode entityNodeData = entityNode.getValue();
      Iterator<Map.Entry<String, JsonNode>> datas = entityNodeData.getFields();
      while ((datas.hasNext())){
        Map.Entry<String, JsonNode> next = datas.next();
        entityData.put(next.getKey(),next.getValue().getTextValue());
      }
      entityList.add(new EntityResult(entityNode.getKey(),entityData));
    }
    message.setEntities(entityList);
    message.setConfidence(outcomeNode.get("confidence").getDoubleValue());
    return message;
  }
}
