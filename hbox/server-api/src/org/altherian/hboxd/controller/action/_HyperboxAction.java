package org.altherian.hboxd.controller.action;

import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Request;
import org.altherian.hboxd.core._Hyperbox;

import java.util.List;

public interface _HyperboxAction {
   
   /**
    * To which task should this action answer to
    * 
    * @return a list of task as String
    */
   public List<String> getRegistrations();
   
   public AnswerType getStartReturn();
   
   /**
    * null if none is required
    * 
    * @return AnswerType for the return
    */
   public AnswerType getFinishReturn();
   
   public AnswerType getFailReturn();
   
   /**
    * Not implemented
    * 
    * @return Not implemented
    */
   public Class<?>[] getRequiredClasses();
   
   /**
    * Not implemented
    * 
    * @return Not implemented
    */
   public Enum<?>[] getRequiredEnums();
   
   /**
    * Not implemented
    * 
    * @return Not implemented
    */
   public String[] getRequiredData();
   
   public boolean isQueueable();
   
   public boolean isCancelable();
   
   public void run(Request request, _Hyperbox hbox);
   
   public void pause();
   
   public void cancel();
   
}
