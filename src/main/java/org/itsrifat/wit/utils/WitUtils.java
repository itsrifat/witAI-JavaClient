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
}
