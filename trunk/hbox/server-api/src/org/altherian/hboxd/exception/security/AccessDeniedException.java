package org.altherian.hboxd.exception.security;


@SuppressWarnings("serial")
public class AccessDeniedException extends SecurityException {
   
   public AccessDeniedException() {
      super("Access denied");
   }
   
}
