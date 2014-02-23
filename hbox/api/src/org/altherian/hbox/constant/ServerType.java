package org.altherian.hbox.constant;

public enum ServerType {
   
   /**
    * Represent a regular server used to run virtual machines.
    */
   Host,
   
   /**
    * Represent a server dedicated for management of the infrastructure.<br>
    * Currently not implemented.
    */
   Manager,
   
   /**
    * Represent a server that runs virtual machines but is also used as manager for the infrastructure.<br>
    * Currently not implemented.
    */
   ManagerHost;
   
   public String getId() {
      return toString();
   }
   
}
