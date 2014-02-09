package org.altherian.hboxc.controller;

import org.altherian.hboxc.event._EventProcessor;

public class CoreToFrontEventProcessor implements _EventProcessor {
   
   private _EventProcessor postProcessor;
   
   public CoreToFrontEventProcessor(_EventProcessor postProcessor) {
      this.postProcessor = postProcessor;
   }
   
   @Override
   public void post(Object o) {
      /*
      if (!(o instanceof EventOutput)) {
         postProcessor.post(o);
      } else {
         Logger.debug("Ignored Event: " + o.getClass().getSimpleName());
      }
       */

      postProcessor.post(o);
   }
   
}
