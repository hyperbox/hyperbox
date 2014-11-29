/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2014 Maxime Dor
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
