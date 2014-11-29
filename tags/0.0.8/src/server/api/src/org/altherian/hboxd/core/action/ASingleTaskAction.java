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

package org.altherian.hboxd.core.action;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Request;
import org.altherian.hboxd.controller.action._Action;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.session.SessionContext;

/**
 * Skeleton structure for a single-task action.<br/>
 * This class will handle the start, finish & failed return type.</br> The following are used :
 * <ul>
 * <li>Start : AnswerType.STARTED</li>
 * <li>Finish : AnswerType.SUCCESS</li>
 * <li>Failed : AnswerType.FAILED</li>
 * </ul>
 * 
 * @see AnswerType
 * @see _Action
 * @see AbstractHyperboxAction
 * @author noteirak
 */
public abstract class ASingleTaskAction extends AbstractHyperboxAction {
   
   private Request request;
   
   @Override
   public AnswerType getStartReturn() {
      return AnswerType.STARTED;
   }
   
   @Override
   public AnswerType getFinishReturn() {
      return AnswerType.SUCCESS;
   }
   
   @Override
   public AnswerType getFailReturn() {
      return AnswerType.FAILED;
   }
   
   @Override
   public void run(Request request, _Hyperbox hbox) {
      this.request = request;
      process(request, hbox);
   }
   
   protected void process(Request request, _Hyperbox hbox) {
      // TODO turn into abstract
      // stub
   }
   
   /**
    * {@link #send(Class, Object)}
    * 
    * @param k the data
    */
   protected void send(Object k) {
      send(k.getClass(), k);
   }
   
   /**
    * SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, key, value))
    * 
    * @param key Enum
    * @param value Object
    */
   protected void send(Enum<?> key, Object value) {
      SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, key, value));
   }
   
   /**
    * SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, key, value))
    * 
    * @param key Class key
    * @param value Object
    */
   protected void send(Class<?> key, Object value) {
      SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, key, value));
   }
   
   /**
    * SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, key, value))
    * 
    * @param key String key
    * @param value Object
    */
   protected void send(String key, Object value) {
      SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, key, value));
   }
   
}
