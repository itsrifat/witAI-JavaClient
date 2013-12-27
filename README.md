witAI-JavaClient
================

Wit-JavaClient is a java wrapper for wit.ai( see https://wit.ai/docs/api) HTTP API.

How to Use
----------

Its pretty simple. Just create a wit client and use its methods to make different API calls to wit.

To get a message by a query:

```java
String apiKey ="my api key";
WitClient client = new WitClient(apiKey);

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



All of the methods throws WitException if desired input not found or desired result not found after api call.
