/* Copyright (c) 2008, Nathan Sweet
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of Esoteric Software nor the names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.esotericsoftware.kryo.serializers;

import static com.esotericsoftware.kryo.util.UnsafeUtil.unsafe;
import static com.esotericsoftware.minlog.Log.TRACE;
import static com.esotericsoftware.minlog.Log.trace;

import java.lang.reflect.Field;
import java.util.List;

import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedField;
import com.esotericsoftware.kryo.serializers.UnsafeCacheFields.UnsafeRegionField;
import com.esotericsoftware.kryo.util.IntArray;
import com.esotericsoftware.reflectasm.FieldAccess;

/* Helper class for implementing FieldSerializer using Unsafe-based approach. 
 * @author Roman Levenstein <romixlev@gmail.com> */
final class FieldSerializerUnsafeUtilImpl implements FieldSerializerUnsafeUtil {
	private FieldSerializer serializer;

	public FieldSerializerUnsafeUtilImpl (FieldSerializer serializer) {
		this.serializer = serializer;
	}

	public void createUnsafeCacheFieldsAndRegions (List<Field> validFields, List<CachedField> cachedFields, int baseIndex,
		IntArray useAsm) {
		// Find adjacent fields of primitive types
		long startPrimitives = 0;
		long endPrimitives = 0;
		boolean lastWasPrimitive = false;
		int primitiveLength = 0;
		int lastAccessIndex = -1;
		Field lastField = null;
		long fieldOffset = -1;
		long fieldEndOffset = -1;
		long lastFieldEndOffset = -1;

		for (int i = 0, n = validFields.size(); i < n; i++) {
			Field field = validFields.get(i);

			int accessIndex = -1;
			if (serializer.access != null && useAsm.get(baseIndex + i) == 1)
				accessIndex = ((FieldAccess)serializer.access).getIndex(field.getName());

			fieldOffset = unsafe().objectFieldOffset(field);
			fieldEndOffset = fieldOffset + fieldSizeOf(field.getType());

			if (!field.getType().isPrimitive() && lastWasPrimitive) {
				// This is not a primitive field. Therefore, it marks
				// the end of a region of primitive fields
				endPrimitives = lastFieldEndOffset;
				lastWasPrimitive = false;
				if (primitiveLength > 1) {
					if (TRACE)
						trace("kryo", "Class " + serializer.getType().getName()
							+ ". Found a set of consecutive primitive fields. Number of fields = " + primitiveLength
							+ ". Byte length = " + (endPrimitives - startPrimitives) + " Start offset = " + startPrimitives
							+ " endOffset=" + endPrimitives);
					// TODO: register a region instead of a field
					CachedField cf = new UnsafeRegionField(startPrimitives, (endPrimitives - startPrimitives));
					cf.field = lastField;
					cachedFields.add(cf);
				} else {
					if (lastField != null)
						cachedFields.add(serializer.newCachedField(lastField, cachedFields.size(), lastAccessIndex));
				}
				cachedFields.add(serializer.newCachedField(field, cachedFields.size(), accessIndex));
			} else if (!field.getType().isPrimitive()) {
				cachedFields.add(serializer.newCachedField(field, cachedFields.size(), accessIndex));
			} else if (!lastWasPrimitive) {
				// If previous field was non primitive, it marks a start
				// of a region of primitive fields
				startPrimitives = fieldOffset;
				lastWasPrimitive = true;
				primitiveLength = 1;
			} else {
				primitiveLength++;
			}

			lastAccessIndex = accessIndex;
			lastField = field;
			lastFieldEndOffset = fieldEndOffset;
		}

		if (!serializer.getUseAsmEnabled() && serializer.getUseMemRegions() && lastWasPrimitive) {
			endPrimitives = lastFieldEndOffset;
			if (primitiveLength > 1) {
				if (TRACE) {
					trace("kryo", "Class " + serializer.getType().getName()
						+ ". Found a set of consecutive primitive fields. Number of fields = " + primitiveLength + ". Byte length = "
						+ (endPrimitives - startPrimitives) + " Start offset = " + startPrimitives + " endOffset=" + endPrimitives);
				}
				// register a region instead of a field
				CachedField cf = new UnsafeRegionField(startPrimitives, (endPrimitives - startPrimitives));
				cf.field = lastField;
				cachedFields.add(cf);
			} else {
				if (lastField != null) cachedFields.add(serializer.newCachedField(lastField, cachedFields.size(), lastAccessIndex));
			}
		}
	}

	/** Returns the in-memory size of a field which has a given class */
	private int fieldSizeOf (Class<?> clazz) {
		if (clazz == int.class || clazz == float.class) return 4;

		if (clazz == long.class || clazz == double.class) return 8;

		if (clazz == byte.class || clazz == boolean.class) return 1;

		if (clazz == short.class || clazz == char.class) return 2;

		// Everything else is a reference to an object, i.e. an address
		return unsafe().addressSize();
	}

	public long getObjectFieldOffset (Field field) {
		return unsafe().objectFieldOffset(field);
	}
}
