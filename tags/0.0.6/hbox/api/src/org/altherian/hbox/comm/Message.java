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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Represent a stateless message comm object.<br/>
 * This is a general purpose object to send/receive message between The Hyperbox Server & Client or for inter-object communication.<br/>
 * The message contains a name, which can be arbitrarily set, and pair of key/values to hold objects in the message.<br/>
 * A Map is used to implement this mapping. If you want to send message with transaction and state tracking, use Request & Answer.
 * 
 * @see Answer
 * @see Request
 * @author noteirak
 */
public class Message implements _Container {
   
   private String name = "";
   private Date time;
   private Map<String, Object> data = new HashMap<String, Object>();
   
   /**
    * Create an empty message without a task. This should be not be used except by serializers.
    */
   protected Message() {
      // serializors only.
   }
   
   /**
    * Create a message with the given name.
    * 
    * @param name The name to create this message with.
    */
   public Message(String name) {
      time = new Date();
      this.name = name;
   }
   
   /**
    * Create a message with the given name and the given object
    * 
    * @param name The name to create this message with.
    * @param o The object to include to this message.
    */
   public Message(String name, Object o) {
      this(name);
      set(o);
   }
   
   public String getName() {
      return name;
   }
   
   public Date getTime() {
      return time;
   }
   
   @Override
   public void set(String label, Object object) {
      data.put(label, object);
   }
   
   @Override
   public void set(Enum<?> e, Object object) {
      data.put(e.toString(), object);
   }
   
   @Override
   public void set(Class<?> c, Object object) {
      data.put(c.getName(), object);
   }
   
   @Override
   public void set(Object object) {
      set(object.getClass().getName(), object);
   }
   
   @SuppressWarnings("unchecked")
   @Override
   public <T> T get(Class<T> c) {
      return (T) data.get(c.getName());
   }
   
   @SuppressWarnings("unchecked")
   @Override
   public <T> T get(Class<?> label, Class<T> type) {
      return (T) data.get(label.getName());
   }
   
   /**
    * Return the object mapped under the label given, or null if no object was included in this message.
    * 
    * @see Map
    * @param s The label to use for looking up the object.
    * @return The object mapped to this label, or null if no such map exist.
    */
   @Override
   public Object get(String s) {
      return data.get(s);
   }
   
   /**
    * Return the object mapped under this enum string representation, or null if no object was included in this message.
    * 
    * @see Map
    * @param e The Enum to use for looking up the object. The lookup will use <code>toString()</code>
    * @return The object mapped to this label, or null if no such map exist.
    */
   @Override
   public Object get(Enum<?> e) {
      return data.get(e.toString());
   }
   
   /**
    * Checks if an object mapped to the given label is included in this message.
    * 
    * @param s The label to check
    * @return true if an object exists for this label, false if not.
    */
   @Override
   public boolean has(String s) {
      return data.containsKey(s);
   }
   
   /**
    * Checks if an object mapped under the given class name is included in this message.
    * 
    * @param c The label to check
    * @return true if an object exists for this class name as label, false if not.
    */
   @Override
   public boolean has(Class<?> c) {
      return has(c.getName());
   }
   
   @Override
   public boolean has(Enum<?> e) {
      return has(e.toString());
   }
   
   @Override
   public boolean hasData() {
      return !data.isEmpty();
   }
   
}
