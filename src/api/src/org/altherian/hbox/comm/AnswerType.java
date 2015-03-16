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

// TODO change isFinishing variable by a state variable, using int (0 => started , 1 => in progress, 2 => finished)
/**
 * Types of Answer used for communication between Hyperbox Server & Client
 * 
 * @author noteirak
 */
public enum AnswerType {

   /**
    * Acknowledge the reception of a message by the receiving side. Typically used for transferring data in chunk.
    */
   ACK(1, false),

   /**
    * The request has accepted been queued for later processing. The end message will contain a TaskOutput item with only its ID guaranteed to be set.<br/>
    * This does not guarantee the success of the task itself, only that the request name is valid and accepted for the user.<br/>
    * This ends an exchange.
    */
   QUEUED(2, false),

   /**
    * Sent to inform that the required task has been started.
    */
   STARTED(0, false),

   /**
    * Sent to inform that the required task has been paused.
    */
   PAUSED(1, false),

   /**
    * Sent to inform that the task has been stopped.
    */
   STOPPED(1, false),

   /**
    * Sent to update about a currently running task, containing no data except for an item with task progression info.<br/>
    * This must be sent after a STARTED and before any COMPLETED, SUCCESS or FAILED message.
    */
   IN_PROGRESS(1, false),

   /**
    * Sent to update about a currently running task, containing data with the possibility of a task progression item.<br/>
    * This must be sent after a STARTED and before any COMPLETED, SUCCESS or FAILED message.
    */
   DATA(1, false),

   /**
    * Sent to inform that a task was fully completed. This ends an exchange.
    */
   COMPLETED(2, false),

   /**
    * Sent to inform that some action returned successfully.<br/>
    * This should be used for single action task that involve atomic and instantaneous changes - typically configuration changes on the server itself.<br/>
    * This ends an exchange.
    */
   SUCCESS(2, false),

   /**
    * Sent to inform that an action or task was cancelled, either by a user or by the system.<br/>
    * More precise information will be left to the implementation and provided within the message.<br/>
    * For multi-steps tasks, this implies that any change was rolled back.<br/>
    * This ends an exchange.
    */
   CANCELED(2, false),

   /**
    * Sent to inform that the task failed and will be discarded.<br/>
    * This ends an exchange.
    */
   FAILED(2, true),

   /**
    * Sent during a multi-steps task to inform that an error occurred and that a step couldn't be performed, but the transaction will be continue
    * anyway.
    */
   ERROR(1, true),

   /**
    * Sent during a multi-steps task to inform either that:
    * <ul>
    * <li>A command returned an error that could be handled internally and the desired action was undertook.</li>
    * <li>A command is not appropriate for the call, but the intention was understood.</li>
    * <li>A command is deprecated, should not be used in the future.</li>
    * </ul>
    * In any case, the server will still perform the desired action.
    */
   WARNING(1, false),

   /**
    * The supplied arguments given with the task/command are not valid or required arguments are missing.
    */
   INVALID(2, true),

   /**
    * The task/command is not known to the server.
    */
   UNKNOWN(2, true),

   /**
    * While processing a task/command, a server error occurred and therefore the current state of the system is unknown or unstable. This most likely
    * refers to a bug either in the server itself or one
    * of its plugings.<br/>
    * If more information is known, it will be sent alongside this status message.
    */
   SERVER_ERROR(2, true),

   /**
    * The server is currently shutting down and the request will be ignored and discarded.
    */
   SERVER_SHUTDOWN(2, true),

   /**
    * The client is not authenticated and must do so before performing any other action.
    */
   UNAUTHENTICATED(2, true),

   /**
    * The request was refused due to the lack of privileges.
    */
   UNAUTHORIZED(2, true);

   private final static int STARTING = 0;
   private final static int PROGRESS = 1;
   private final static int FINISHING = 2;

   private int type;
   private boolean failing;

   private AnswerType(int type, boolean failing) {
      this.type = type;
      this.failing = failing;
   }

   /**
    * Check if this answer message concluded an exchange.
    * 
    * @return True if this is the last message of this exchange, false if not.
    */
   public boolean isFinishing() {
      return type == FINISHING;
   }

   public boolean isInProgress() {
      return type == PROGRESS;
   }

   public boolean isStarting() {
      return type == STARTING;
   }

   public boolean isFailing() {
      return failing;
   }

}
