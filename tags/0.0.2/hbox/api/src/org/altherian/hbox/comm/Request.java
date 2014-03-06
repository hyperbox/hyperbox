/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * 
 * http://hyperbox.altherian.org
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hbox.comm;

// TODO create subclasses for each CoreTasks request
/**
 * Request message represent a statefull message that is sent through the network from the client to the server.<br/>
 * A request message is the start of an exchange session and each exchange has a unique identifier.<br/>
 * A request message contains:
 * <ul>
 * <li>An Exchange ID to uniquely identify this request and its answers messages</li>
 * <li>a command to categorise this message</li>
 * </ul>
 * 
 * @see Answer
 * @see Message
 * @author noteirak
 */
public final class Request extends Message {
   
   private Command command;
   private String exchangeId = String.valueOf(System.currentTimeMillis() + hashCode());
   
   /**
    * Empty constructor<br/>
    * <b>DO NOT USE - TO BE USED BY SERELIAZERS ONLY</b>
    */
   @SuppressWarnings("unused")
   private Request() {
      // do not use!
   }
   
   /**
    * Build a new Request message object with the given CoreTask.
    * 
    * @param task The task to be set in this request message.
    */
   public Request(Enum<?> task) {
      this(Command.VBOX, task);
   }
   
   /**
    * Build a new Request message with the given task and include the given object to this message.
    * 
    * @param task The CoreTask to set as task
    * @param o The object to include to the message - will be mapped using its Class
    * @see Message
    * @see Answer
    * @see HypervisorTasks
    */
   public Request(Enum<?> task, Object o) {
      this(Command.VBOX, task, o);
   }
   
   public Request(Enum<?> task, Object... objs) {
      this(Command.VBOX, task);
      for (Object obj : objs) {
         set(obj);
      }
   }

   public Request(Enum<?> task, Enum<?> label, Object o) {
      this(Command.VBOX, task);
      set(label, o);
   }
   
   /**
    * Build a new Request message with the given command.
    * 
    * @param command The command to set this Request message with.
    * @see Message
    * @see Answer
    * @see Command
    */
   public Request(Command command) {
      this.command = command;
   }
   
   /**
    * Build a new Request message with the given command and task.
    * 
    * @param command The command to set in this Request message.
    * @param task The task to set in this Request message.
    * @see Message
    * @see Answer
    * @see Command
    */
   public Request(Command command, String task) {
      super(task);
      this.command = command;
   }
   
   /**
    * Build a new Request message with the given command and CoreTask ID.
    * 
    * @param command The command to set in this Request message.
    * @param task The CoreTask ID to set in this Request message.
    * @see Message
    * @see Answer
    * @see Command
    * @see HypervisorTasks
    */
   public Request(Command command, Enum<?> task) {
      this(command, task.toString());
   }
   
   /**
    * Build a new Request message with the given command, CoreTask ID and object.
    * 
    * @param command The command to set in this Request message.
    * @param task The CoreTask ID to set in this Request message.
    * @param obj The object to attach to this Request message.
    * @see Message
    * @see Answer
    * @see Command
    * @see HypervisorTasks
    */
   public Request(Command command, Enum<?> task, Object obj) {
      this(command, task.toString());
      set(obj);
   }
   
   public Request(Command command, Enum<?> task, Object... objs) {
      this(command, task.toString());
      for (Object obj : objs) {
         set(obj);
      }
   }
   
   /**
    * Get the unique identifier for this Request and the following Answer(s).
    * 
    * @return a String containing the unique ID
    */
   public String getExchangeId() {
      return exchangeId;
   }
   
   /**
    * Get the command associated with this request.
    * 
    * @return a Command object set in this Request message.
    */
   public Command getCommand() {
      return command;
   }
   
}
