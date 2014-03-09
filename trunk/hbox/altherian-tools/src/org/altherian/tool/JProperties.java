package org.altherian.tool;

import java.util.Properties;

@SuppressWarnings("serial")
public class JProperties extends Properties {
   
   public String getProperty(Enum<?> key) {
      return getProperty(key.toString());
   }
   
}
