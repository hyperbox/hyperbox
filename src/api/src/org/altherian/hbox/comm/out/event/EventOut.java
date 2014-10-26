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

package org.altherian.hbox.comm.out.event;

import org.altherian.hbox.comm.in.ObjectIn;
import org.altherian.hbox.comm.out.ObjectOut;
import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.event.HyperboxEvents;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Raw Event Comm message, normally extended.
 * 
 * @author noteirak
 */
// TODO need to make abstract
public abstract class EventOut {
   
   private String eventId;
   private Date time;
   private Map<String, ObjectOut> mapping = new HashMap<String, ObjectOut>();
   
   /**
    * Empty constructor for serialisation classes.<br/>
    * <ul>
    * <b>!! DONOT USE !!</b>
    * </ul>
    */
   protected EventOut() {
      // Used for (de)serialization
   }
   
   /**
    * Build a new Event Comm object with the given String ID
    * 
    * @param time When the event occurred
    * @param srvOut The Server from which this event originated from
    * @param id The ID of the Event Comm
    */
   public EventOut(Date time, String id, ServerOut srvOut) {
      this.time = time;
      eventId = id;
      set(ServerOut.class, srvOut);
   }
   
   /**
    * Build a new Event Comm object with the given SystemEvent ID
    * 
    * @param time When the event occurred
    * @param srvOut The Server from which this event originated from
    * @param id The ID of the Event Comm
    */
   public EventOut(Date time, Enum<?> id, ServerOut srvOut) {
      this(time, id.toString(), srvOut);
   }
   
   /**
    * Build a new Event Comm object with the given ID and include the given data.
    * 
    * @param time When the event occurred
    * @param srvOut The Server from which this event originated from
    * @param id The Event ID to include
    * @param data The object data to include
    * @see ObjectIn
    * @see HyperboxEvents
    */
   public EventOut(Date time, String id, ServerOut srvOut, ObjectOut data) {
      this(time, id, srvOut);
      set(data);
   }
   
   /**
    * Get this event ID
    * 
    * @return This event ID as String
    */
   public String getId() {
      return eventId;
   }
   
   public Date getTime() {
      return time;
   }
   
   public ServerOut getServer() {
      return get(ServerOut.class);
   }
   
   public String getServerId() {
      return getServer().getId();
   }
   
   /**
    * Set the given data under the given label
    * 
    * @param label The label for this data
    * @param data The data as _ObjectIO object
    * @see ObjectIn
    */
   protected void set(String label, ObjectOut data) {
      mapping.put(label, data);
   }
   
   protected void set(Class<?> label, ObjectOut data) {
      set(label.getName(), data);
   }
   
   /**
    * Include a Comm object to this event using its class name as label.
    * 
    * @param object a _ObjectIO objec to add to this event message.
    * @see ObjectIn
    * @see Class
    */
   protected void set(ObjectOut object) {
      set(object.getClass().getName(), object);
   }
   
   /**
    * Get the object that was stored under this label, or null if none is mapped.
    * 
    * @param s The label to search for
    * @return The object under this label, or null if no oject was found
    * @see HashMap
    */
   protected Object get(String s) {
      return mapping.get(s);
   }
   
   /**
    * Get the object that was stored using its class name as label
    * 
    * @param c The Class to use as label
    * @return The object under this label, or null if no oject was found
    * @see HashMap
    * @see Class
    */
   @SuppressWarnings("unchecked")
   protected <T extends ObjectOut> T get(Class<T> c) {
      return (T) get(c.getName());
   }
   
}
