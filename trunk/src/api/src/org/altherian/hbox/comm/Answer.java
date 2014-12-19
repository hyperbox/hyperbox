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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hbox.comm;

/**
 * Answers are sent from the Hyperbox Server/Manager to the Hyperbox Client as a response from a request.<br/>
 * Several answers can be sent from a single request, as long as a final answer with a type that finish the exchanged is sent.<br/>
 * 
 * @author noteirak
 * @see AnswerType
 * @see Message
 * @see Request
 */
public final class Answer extends Message {
   
   private String exchangeId;
   private Command command;
   private AnswerType type;
   
   /**
    * <b>DO NOT USE</b> - <b><i>only for serializers</i></b>
    */
   @SuppressWarnings("unused")
   private Answer() {
      // DO NOT USE!
   }
   
   /**
    * Build an Answer object from the given request and with the given type.
    * 
    * @param request The original request that produced this answer
    * @param type The type of answer this message is
    * @see Message
    * @see AnswerType
    * @see Request
    */
   public Answer(Request request, AnswerType type) {
      super(request.getName());
      exchangeId = request.getExchangeId();
      command = request.getCommand();
      this.type = type;
   }
   
   /**
    * Build an Answer object from the given request and with the given type and attach the given object
    * 
    * @param request The original request that produced this answer
    * @param type The type of answer this message is
    * @param o The objec to attach to this message
    * @see Message
    * @see AnswerType
    * @see Request
    */
   public Answer(Request request, AnswerType type, Object o) {
      this(request, type);
      set(o);
   }
   
   public Answer(Request request, AnswerType type, Throwable e) {
      this(request, type, Exception.class, e.getMessage());
   }
   
   /**
    * Build an Answer object from the given request and with the given type and attach the given object under the given label.
    * 
    * @param request The original request that produced this answer
    * @param type The type of answer this message is
    * @param label The label to set for the given object
    * @param o The objecy to attach to this message
    * @see Message
    * @see AnswerType
    * @see Request
    */
   public Answer(Request request, AnswerType type, String label, Object o) {
      this(request, type);
      set(label, o);
   }
   
   public Answer(Request request, AnswerType type, Enum<?> label, Object o) {
      this(request, type);
      set(label, o);
   }
   
   public Answer(Request request, AnswerType type, Class<?> label, Object o) {
      this(request, type);
      set(label, o);
   }
   
   /**
    * Get the unique identifier for this message and the following one(s).
    * 
    * @return a String containing the unique ID
    */
   public String getExchangeId() {
      return exchangeId;
   }
   
   /**
    * Get the type of answer this message is
    * 
    * @return an AnswerType object
    * @see AnswerType
    */
   public AnswerType getType() {
      return type;
   }
   
   /**
    * Get the command associated with this answer.
    * 
    * @return a Command object set in this answer message.
    * @see Command
    */
   public Command getCommand() {
      return command;
   }
   
   /**
    * Check if this message conclude an exchange between the client and the server based on the type of answer.<br/>
    * For more information on which type concludes an an exchange, refer to the ActionType doc.
    * 
    * @return true if this answer finishes the exchange, false if not.
    * @see AnswerType
    * @see Request
    * @see AnswerType
    */
   public boolean isExchangedFinished() {
      return type.isFinishing();
   }
   
   public boolean isExchangeStarted() {
      return type.isStarting();
   }
   
   public boolean isExchangeInProgress() {
      return type.isInProgress();
   }
   
   public boolean hasFailed() {
      return type.isFailing();
   }
   
   // TODO toString()
   
}
