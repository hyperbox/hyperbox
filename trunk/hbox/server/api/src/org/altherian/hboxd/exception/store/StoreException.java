package org.altherian.hboxd.exception.store;

import org.altherian.hbox.exception.HyperboxRuntimeException;

@SuppressWarnings("serial")
public class StoreException extends HyperboxRuntimeException {
   
   public StoreException(String s) {
      super(s);
   }
   
   public StoreException(Throwable t) {
      super(t);
   }
   
   public StoreException(String s, Throwable t) {
      super(s, t);
   }
   
}
