package org.altherian.hboxd.controller.action;

import org.altherian.hbox.comm.Request;
import org.altherian.hbox.exception.HyperboxException;

public interface _ActionManager {
   
   public void start() throws HyperboxException;
   
   public void stop();
   
   public _HyperboxAction get(Request req);
   
   public _HyperboxAction get(String id);
   
}
