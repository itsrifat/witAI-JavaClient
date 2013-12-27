package org.itsrifat.wit.utils;


import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.audio.AudioParser;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;


/**
 * Author: Moinul Hossain Rifat
 * email: moinul.hossain@csebuet.org
 * Date: 12/25/13
 */
public class WitUtils {
  private static final Logger LOGGER = Logger.getLogger(WitUtils.class);
  public static boolean isValidJSON(final String json) {
    boolean valid = false;
    try {
      final JsonParser parser = new ObjectMapper().getJsonFactory()
          .createJsonParser(json);
      while (parser.nextToken() != null) {
      }
      valid = true;
    } catch (JsonParseException jpe) {

    } catch (IOException ioe) {
    }

    return valid;
  }

  public static Boolean validateAudioFleType(File file,String targetContentType){
    InputStream input=null;
    try{
      input = new FileInputStream(file);
      ContentHandler handler = new DefaultHandler();
      Metadata metadata = new Metadata();
      Parser parser = new AudioParser();
      ParseContext parseCtx = new ParseContext();
      parser.parse(input, handler, metadata, parseCtx);
      input.close();
      for(String meta:metadata.names()){
        LOGGER.debug(meta+":"+metadata.get(meta));
      }
      if(metadata.get("Content-Type").contains(targetContentType)){
        return true;
      }
      else {
        return false;
      }


    }
    catch (FileNotFoundException ex){

    }
    catch (IOException ex){

    }
    catch (SAXException e) {

    } catch (TikaException e) {

    }
    return false;

  }
}
