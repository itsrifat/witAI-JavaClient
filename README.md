witAI-JavaClient
================

Wit-JavaClient is a java wrapper for wit.ai( see https://wit.ai/docs/api) HTTP API.

How to Use
----------

Create a ```WitClient``` and use its methods to make different API calls to wit.

To get a message by a query:

```java
String apiKey ="my api key";
WitClient client = new WitClient(apiKey);  //it uses Apache HttpClient under the hood

String query = "Hey Mr tambourine man play a song for me";
Message message = client.getMessage(query);
```

To get list of all the entities:

```java
List<String> entityNames = client.getEntities();
```

To get an entity

```java
String entityId="myEntityId";
Entity entity = client.getEntity(etityId);
```

List of available methods in ```WitClient```:
--------------------------
```java

public Message getMessage(String query)
public Message getMessage(String query,String version)
public Message getMessage(File audioFlie)
public Message getMessageById(String messageId)
public List<String> getEntities()
public Entity getEntity(String entityId)
public Entity postEntity(Entity entity)
public Entity putEntity(Entity entity)
public Boolean deleteEntity(String entityId)
public Entity postEntityValues(String entityId,Map<String,Object> values)
public Boolean deleteEntityValue(String entityId, String value)
public Entity postEntityValueExpression(String entityId,String value,String expression)
public Boolean deleteEntityValueExpression(String entityId, String value,String expression)
```

All of the methods throws WitException if desired input not found or desired result not found after api call.
