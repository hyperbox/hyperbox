package org.altherian.hboxd.exception.security;


@SuppressWarnings("serial")
public class InvalidCredentialsException extends SecurityException {
   
   public InvalidCredentialsException() {
      super("Invalid Credentials");
   }
   
}
