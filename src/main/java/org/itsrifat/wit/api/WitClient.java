package org.itsrifat.wit.api;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.itsrifat.wit.WitException;
import org.itsrifat.wit.domain.Entity;
import org.itsrifat.wit.domain.Message;
import org.itsrifat.wit.utils.WitUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: Md Moinul Hossain
 * email: moinul.hossain@csebuet.org
 * Date: 12/25/13
 */
public class WitClient {
  private static final Logger LOGGER = Logger.getLogger(WitClient.class);

  private static final Integer MAX_QUERY_LENGTH=256;
  private static final String DEFAULT_URL_STRING="https://api.wit.ai/";
  private static final String MESSAGE_PATH="message";
  private static final String MESSAGES_PATH="messages";
  private static final String ENTITY_PATH = "entities";
  private static final String ENTITY_VALUE_PATH="values";
  private static final String ENTITY_VALUE_EXPRESSION_PATH="expressions";
  private static final String QUERY_PARAM="q";
  private static final String VERSION_PARAM = "v";

  private String apiKey;
  private CloseableHttpClient httpClient;
  private String apiUrlString;



  public WitClient(String apiKey, CloseableHttpClient httpClient,String apiUrlString) {
    this.apiKey = apiKey;
    this.apiUrlString = apiUrlString;

    this.httpClient = httpClient;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public CloseableHttpClient getHttpClient() {
    return httpClient;
  }

  public void setHttpClient(CloseableHttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public String getApiUrlString() {
    return apiUrlString;
  }

  public void setApiUrlString(String apiUrlString) {
    this.apiUrlString = apiUrlString;
  }


  public WitClient(String apiKey) {
    this.apiKey = apiKey;
    this.httpClient = HttpClientBuilder.create().build();
    this.apiUrlString = DEFAULT_URL_STRING;
  }


  public WitClient(String apiKey, String apiUrlString) {
    this.apiKey = apiKey;
    this.apiUrlString = apiUrlString;
  }



  private HttpGet buildRequest(String query){
    HttpGet get = new HttpGet();
    try{
      query=URLEncoder.encode(query,"UTF-8");
      URI url = new URIBuilder(getApiUrlString()+MESSAGE_PATH).setParameter(QUERY_PARAM,query).build();
      get.setURI(url);
      get.setHeader("Authorization","Bearer "+getApiKey());
    }
    catch (URISyntaxException ex){
      LOGGER.error(ex);
    }
    catch (UnsupportedEncodingException ex) {
      LOGGER.error(ex);
    }
    return get;
  }
  private HttpGet buildRequest(String query,String version){
    HttpGet get = new HttpGet();
    try{
      query=URLEncoder.encode(query,"UTF-8");
      String path = getApiUrlString()+MESSAGE_PATH;
      URI url = new URIBuilder(path)
          .setParameter(QUERY_PARAM,query).setParameter(VERSION_PARAM,version)
          .build();
      get.setURI(url);
      get.setHeader("Authorization","Bearer "+getApiKey());
    }
    catch (URISyntaxException ex){
      LOGGER.error(ex);
    }
    catch (UnsupportedEncodingException ex) {
      LOGGER.error(ex);
    }

    return get;
  }

  private Message getMessage(HttpGet getRequest) throws WitException{
    CloseableHttpResponse response=null;
    Message message=null;
    LOGGER.debug("Sending get request to:"+getRequest.getURI().toString());
    try{
      response = getHttpClient().execute(getRequest);
      HttpEntity entity = response.getEntity();
      String data = EntityUtils.toString(entity);
      LOGGER.debug("Server Returned:"+data);
      if(WitUtils.isValidJSON(data)){
        ObjectMapper mapper = new ObjectMapper();
        message = mapper.readValue(data, Message.class);
      }
      else{
        throw new WitException(data);
      }
    }
    catch (IOException ex){
      LOGGER.error(ex);
    }
    finally {
      try{
        response.close();
      }
      catch (Exception ex){
        LOGGER.error(ex);
      }
    }
    return message;

  }


  public Message getMessage(String query) throws WitException{
    if(query.length() > MAX_QUERY_LENGTH){
      throw new WitException("Length of Query must be must be > 0 and < "+MAX_QUERY_LENGTH);
    }
    HttpGet httpGet = buildRequest(query);
    Message message = getMessage(httpGet);
    return message;
  }
  public Message getMessage(String query, String version) throws WitException{
    if(query.length() > MAX_QUERY_LENGTH){
      throw new WitException("Length of Query must be must be > 0 and < "+MAX_QUERY_LENGTH);
    }
    HttpGet httpGet = buildRequest(query,version);
    Message message = getMessage(httpGet);
    return message;
  }

  public Message getMessageById(String messageId) throws WitException{
    Message message=null;
    HttpGet get = new HttpGet();
    CloseableHttpResponse response;

    try{
      String path = getApiUrlString()+MESSAGES_PATH+"/"+messageId;
      URI url = new URIBuilder(path).build();
      LOGGER.debug("Sending get request to:"+url);
      get.setURI(url);
      get.setHeader("Authorization","Bearer "+getApiKey());

      response = getHttpClient().execute(get);
      String data = EntityUtils.toString(response.getEntity());
      LOGGER.debug("Server returned:"+data);
      if(WitUtils.isValidJSON(data)){
        ObjectMapper mapper = new ObjectMapper();
        message = mapper.readValue(data, Message.class);
      }
      else {
        throw new WitException("Server Returned: "+data);
      }
    }
    catch (URISyntaxException ex){
      LOGGER.error(ex);
    }
    catch (IOException ex){
      LOGGER.error(ex);
    }

    return message;
  }


  public Message getMessage(File audioFile) throws WitException{
    Message message = null;
    CloseableHttpResponse response=null;
    try{
      URI url = new URIBuilder(getApiUrlString()+MESSAGE_PATH).build();
      LOGGER.debug("Sending post request to:"+url);

      HttpPost post = new HttpPost(url);
      FileBody bin = new FileBody(audioFile);
      System.out.println(bin.getContentType());
      HttpEntity reqEntity = MultipartEntityBuilder.create()
          .addPart("file", bin)
          .build();
      post.setEntity(reqEntity);
      post.setHeader("Authorization","Bearer "+getApiKey());
      response = getHttpClient().execute(post);
      String data = EntityUtils.toString(response.getEntity());
      LOGGER.debug("Server returned:"+data);
      if(WitUtils.isValidJSON(data)){
        ObjectMapper mapper = new ObjectMapper();
        message = mapper.readValue(data, Message.class);
      }
      else {
        throw new WitException("Server returned: "+data);
      }
    }
    catch (IOException ex){

    }
    catch (URISyntaxException ex){

    }
    finally {
      try{
        response.close();
      }
      catch (Exception ex){
        LOGGER.error(ex);
      }
    }
    return message;
  }

  public List<String> getEntities() throws WitException{
    HttpGet get = new HttpGet();
    CloseableHttpResponse response;
    try{
      String path = getApiUrlString()+ENTITY_PATH;
      URI url = new URIBuilder(path).build();
      LOGGER.debug("Sending get request to:"+url);
      get.setURI(url);
      get.setHeader("Authorization","Bearer "+getApiKey());
      LOGGER.debug("Attempting to get:"+get.getURI().toString());
      response = getHttpClient().execute(get);
      String data = EntityUtils.toString(response.getEntity());
      LOGGER.debug("Server returned:"+data);
      if(WitUtils.isValidJSON(data)){
        ObjectMapper mapper = new ObjectMapper();
        List<String> entities = mapper.readValue(data, List.class);
        return entities;
      }
      else {
        throw new WitException("Server returned:"+data);
      }
    }
    catch (URISyntaxException ex){
      LOGGER.error(ex);
    }
    catch (IOException ex){
      LOGGER.error(ex);
    }
    return null;
  }

  public Entity getEntity(String entityId) throws WitException{
    Entity entity;
    HttpGet get = new HttpGet();
    CloseableHttpResponse response;

    try{
      String path = getApiUrlString()+ENTITY_PATH+"/"+entityId;
      URI url = new URIBuilder(path).build();
      LOGGER.debug("Sending get request to:"+url);
      get.setURI(url);
      get.setHeader("Authorization","Bearer "+getApiKey());
      response = getHttpClient().execute(get);
      String data = EntityUtils.toString(response.getEntity());
      LOGGER.debug("Server returned:"+url);
      if(WitUtils.isValidJSON(data)){
        ObjectMapper mapper = new ObjectMapper();
        entity = mapper.readValue(data, Entity.class);
        return entity;
      }
      else {
        throw new WitException("Server returned"+data);
      }
    }
    catch (URISyntaxException ex){
      LOGGER.error(ex);
    }
    catch (IOException ex){
      LOGGER.error(ex);
    }
    return null;
  }

  public Entity postEntity(Entity entity) throws WitException{

    CloseableHttpResponse response=null;
    try{
      String path =  getApiUrlString()+ENTITY_PATH;
      URI url = new URIBuilder(path).build();
      LOGGER.debug("Sending post request to:"+url);
      HttpPost post = new HttpPost(url);
      ObjectMapper mapper = new ObjectMapper();
      String entityJson = mapper.writeValueAsString(entity);
      LOGGER.debug(entityJson);
      StringEntity input = new StringEntity(entityJson);
      input.setContentType("application/json");
      post.setEntity(input);
      post.setHeader("Authorization","Bearer "+getApiKey());
      response = getHttpClient().execute(post);
      String data = EntityUtils.toString(response.getEntity());
      LOGGER.debug("Server returned: "+data);
      if(WitUtils.isValidJSON(data)){
        entity = mapper.readValue(data, Entity.class);
        return entity;
      }
      else {
        throw new WitException("Server returned: "+data);
      }
    }
    catch (IOException ex){
        LOGGER.error(ex);
    }
    catch (URISyntaxException ex){

    }
    finally {
      try{
        response.close();
      }
      catch (Exception ex){
        LOGGER.error(ex);
      }
    }
    return null;
  }

  public Entity putEntity(String entityId,Entity entity) throws WitException{

    CloseableHttpResponse response=null;
    try{
      URI url = new URIBuilder(getApiUrlString()+ENTITY_PATH+"/"+entityId).build();
      LOGGER.debug("Sending put request to:"+url);
      HttpPut put = new HttpPut(url);
      ObjectMapper mapper = new ObjectMapper();
      String entityJson = mapper.writeValueAsString(entity);
      StringEntity input = new StringEntity(entityJson);
      input.setContentType("application/json");
      put.setEntity(input);
      put.setHeader("Authorization","Bearer "+getApiKey());
      response = getHttpClient().execute(put);
      String data = EntityUtils.toString(response.getEntity());
      LOGGER.debug("Server returned: "+data);
      if(WitUtils.isValidJSON(data)){
        entity = mapper.readValue(data, Entity.class);
        return entity;
      }
      else {
        throw new WitException("Server returned: "+data);
      }

    }
    catch (IOException ex){
      LOGGER.error(ex);
    }
    catch (URISyntaxException ex){

    }
    finally {
      try{
        response.close();
      }
      catch (Exception ex){
        LOGGER.error(ex);
      }
    }
    return null;
  }

  public String deleteEntity(String entityId) throws WitException{

    CloseableHttpResponse response=null;
    try{
      URI url = new URIBuilder(getApiUrlString()+ENTITY_PATH+"/"+entityId).build();
      LOGGER.debug("Sending delete request to: "+url);
      HttpDelete delete = new HttpDelete(url);
      delete.setHeader("Authorization","Bearer "+getApiKey());
      response = getHttpClient().execute(delete);
      String data = EntityUtils.toString(response.getEntity());
      if(response.getStatusLine().getStatusCode() != 404){
        return data;
      }
      else {
        throw new WitException("Entity with id "+entityId+" Not Found. Server returned:"+data);
      }
    }
    catch (IOException ex){
      LOGGER.error(ex);
    }
    catch (URISyntaxException ex){

    }
    finally {
      try{
        response.close();
      }
      catch (Exception ex){
        LOGGER.error(ex);
      }
    }
    return null;
  }

  public Entity postEntityValues(String entityId,Map<String,Object> values) throws WitException{
    Entity entity;
    CloseableHttpResponse response=null;
    try{
      String path=getApiUrlString()+ENTITY_PATH+"/"+entityId+"/values";
      URI url = new URIBuilder(path).build();
      LOGGER.debug("Sending post request to: "+url);
      HttpPost post = new HttpPost(url);
      ObjectMapper mapper = new ObjectMapper();
      String entityJson = mapper.writeValueAsString(values);
      LOGGER.debug(entityJson);
      StringEntity input = new StringEntity(entityJson);
      input.setContentType("application/json");
      post.setEntity(input);
      post.setHeader("Authorization","Bearer "+getApiKey());

      response = getHttpClient().execute(post);
      String data = EntityUtils.toString(response.getEntity());
      LOGGER.debug("Sever Returned: "+data);
      if(WitUtils.isValidJSON(data)){
        entity = mapper.readValue(data, Entity.class);
        return entity;
      }
      else {
        throw new WitException("Could not post Entity Values .Server returned: "+data);
      }

    }
    catch (IOException ex){
      LOGGER.error(ex);
    }
    catch (URISyntaxException ex){

    }
    finally {
      try{
        response.close();
      }
      catch (Exception ex){
        LOGGER.error(ex);
      }
    }
    return null;
  }
  public Boolean deleteEntityValue(String entityId, String value) throws WitException{

    CloseableHttpResponse response=null;
    try{
      value=URLEncoder.encode(value,"UTF-8");
      String path =  getApiUrlString()+ENTITY_PATH+"/"+entityId+"/values/"+value;
      URI url = new URIBuilder(path).build();
      LOGGER.debug("Sending delete request to: "+url);
      HttpDelete delete = new HttpDelete(url);
      delete.setHeader("Authorization","Bearer "+getApiKey());
      response = getHttpClient().execute(delete);
      String data = EntityUtils.toString(response.getEntity());
      LOGGER.debug("Server returned:"+data);
      if(data.contains("deleted")){
        return true;
      }
      else {
        throw new WitException("Could not delete entity value \""+value+"\" Server returned:"+data);
      }
    }
    catch (IOException ex){
      LOGGER.error(ex);
    }
    catch (URISyntaxException ex){

    }
    finally {
      try{
        response.close();
      }
      catch (Exception ex){
        LOGGER.error(ex);
      }
    }
    return false;
  }

  public Entity postEntityValueExpression(String entityId,String value,String expression) throws WitException{
    Entity entity;
    CloseableHttpResponse response=null;
    try{
      value =   URLEncoder.encode(value,"UTF-8").replace("+","%20");
      String path = getApiUrlString()+ENTITY_PATH+"/"+entityId+"/values/"+value+"/expressions";
      URI url = new URIBuilder(path).build();
      HttpPost post = new HttpPost(url);
      LOGGER.debug("Sending get request to: "+url);
      StringEntity input = new StringEntity(expression);
      post.setEntity(input);
      post.setHeader("Authorization","Bearer "+getApiKey());

      response = getHttpClient().execute(post);
      String data = EntityUtils.toString(response.getEntity());
      LOGGER.debug("Server returned: "+data);
      if(WitUtils.isValidJSON(data)){
        ObjectMapper mapper = new ObjectMapper();
        entity = mapper.readValue(data, Entity.class);
        return entity;
      }
      else {
        throw new WitException("The server returned: "+data);
      }

    }
    catch (IOException ex){
      LOGGER.error(ex);
    }
    catch (URISyntaxException ex){

    }
    finally {
      try{
        response.close();
      }
      catch (Exception ex){
        LOGGER.error(ex);
      }
    }
    return null;
  }
  public Boolean deleteEntityValueExpression(String entityId, String value,String expression) throws WitException{

    CloseableHttpResponse response=null;
    try{
      expression = URLEncoder.encode(expression,"UTF-8").replace("+","%20");
      String path = getApiUrlString()+ENTITY_PATH + "/"+entityId + "/values/" + value + "/expressions/"+expression;
      URI url = new URIBuilder(path).build();
      LOGGER.debug("Sending delete request to: "+url);
      HttpDelete delete = new HttpDelete(url);
      delete.setHeader("Authorization","Bearer "+getApiKey());
      response = getHttpClient().execute(delete);
      String data = EntityUtils.toString(response.getEntity());
      LOGGER.debug("Server returned:"+data);
      if(data.contains("deleted")){
        return true;
      }
      else {
        throw new WitException("Could not delete expression. Server returned:"+data);
      }
    }
    catch (IOException ex){
      LOGGER.error(ex);
    }
    catch (URISyntaxException ex){
      LOGGER.error(ex);
    }
    finally {
      try{
        response.close();
      }
      catch (Exception ex){
        LOGGER.error(ex);
      }
    }
    return false;
  }

}
