package org.altherian.hbox.comm.output;

public class HelloOutput extends ObjectOutput {
   
   private long protocolVersion = 0;
   
   @SuppressWarnings("unused")
   private HelloOutput() {
      // used for (de)serialisation
   }
   
   public HelloOutput(long protocolVersion) {
      this.protocolVersion = protocolVersion;
   }
   
   public long getProtocolVersion() {
      return protocolVersion;
   }
   
}
