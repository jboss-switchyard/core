/*
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.switchyard.common.camel;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class that identifies primitive/wrapper combinations
 * and can cast to primitive.
 */
public final class PrimitiveUnboxingUtil {
    /*
     * Map of primitive / wrappers
     */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_CLASSES = 
        new HashMap<Class<?>, Class<?>>();

    static {
        PRIMITIVE_CLASSES.put(boolean.class, Boolean.class);
        PRIMITIVE_CLASSES.put(byte.class, Byte.class);
        PRIMITIVE_CLASSES.put(char.class, Character.class);
        PRIMITIVE_CLASSES.put(double.class, Double.class);
        PRIMITIVE_CLASSES.put(float.class, Float.class);
        PRIMITIVE_CLASSES.put(int.class, Integer.class);
        PRIMITIVE_CLASSES.put(long.class, Long.class);
        PRIMITIVE_CLASSES.put(short.class, Short.class);
    }

    private PrimitiveUnboxingUtil() {
    }
    
    /**
     * Checks to see if we have a primitive/wrapper combination that would allow us to
     * unbox the class into the method's requested primitive.
     * @param argType argType
     * @param object object
     * @return true/false
     */
    public static boolean isUnboxingException(Class<?> argType, Object object) {
        if (PRIMITIVE_CLASSES.containsKey(argType)) {
            return PRIMITIVE_CLASSES.get(argType).isInstance(object);
        } else {
            return false;
        }
    }

    /**
     * Returns the wrapper class cast.
     * @param argType argType
     * @param object object
     * @param <T> <T>
     * @return cast object
     */
    public static <T> T returnPrimitive(Class<T> argType, Object object) {
        if (argType.isPrimitive()) {
            T primitive = (T) object;
            return primitive;
        }
        return null;
    }
}
