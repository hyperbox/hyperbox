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

package org.altherian.tool;

import java.util.EnumMap;
import java.util.Map;

public class BiEnumMap<K extends Enum<K>, V extends Enum<V>> {
   
   private Map<K, V> keyMap;
   private Map<V, K> valueMap;
   
   public BiEnumMap(Class<K> keyClass, Class<V> valueClass) {
      keyMap = new EnumMap<K, V>(keyClass);
      valueMap = new EnumMap<V, K>(valueClass);
   }
   
   public void clear() {
      keyMap.clear();
      valueMap.clear();
   }
   
   public boolean contains(K arg) {
      return keyMap.containsKey(arg);
   }
   
   public boolean containsReverse(V arg) {
      return valueMap.containsKey(arg);
   }
   
   public V get(K arg0) {
      return keyMap.get(arg0);
   }
   
   public K getReverse(V arg0) {
      return valueMap.get(arg0);
   }
   
   public boolean isEmpty() {
      return keyMap.isEmpty();
   }
   
   public void put(K arg0, V arg1) {
      keyMap.put(arg0, arg1);
      valueMap.put(arg1, arg0);
   }
   
   public int size() {
      return keyMap.size();
   }
   
}
