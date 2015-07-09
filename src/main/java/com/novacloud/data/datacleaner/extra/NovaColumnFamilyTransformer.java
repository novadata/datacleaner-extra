package com.novacloud.data.datacleaner.extra;

import org.eobjects.analyzer.beans.api.*;
import org.eobjects.analyzer.beans.categories.DataStructuresCategory;
import org.eobjects.analyzer.data.InputColumn;
import org.eobjects.analyzer.data.InputRow;
import org.eobjects.analyzer.util.ReflectionUtils;

import javax.inject.Inject;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

    /**
     * Transformer for selecting values from maps.
     */
    @TransformerBean("Select values from key/value map")
    @Description("Given a specified list of keys, this transformer will select the values from a key/value map and place them as columns within the record")
    @Categorized(DataStructuresCategory.class)

/**
 * Created by root on 12/12/14.
 */
    public class NovaColumnFamilyTransformer  implements Transformer<Object> {

        @Inject
        @Configured
        InputColumn<Map<String, ?>> mapColumn;

        @Inject
        @Configured
        String[] keys;

        @Inject
        @Configured
        Class<?>[] types;

        @Inject
        @Configured
        @Description("Verify that expected type and actual type are the same")
        boolean verifyTypes = false;

        public void setKeys(String[] keys) {
            this.keys = keys;
        }

        public void setTypes(Class<?>[] types) {
            this.types = types;
        }

        public void setMapColumn(InputColumn<Map<String, ?>> mapColumn) {
            this.mapColumn = mapColumn;
        }

        public void setVerifyTypes(boolean verifyTypes) {
            this.verifyTypes = verifyTypes;
        }

        @Override
        public OutputColumns getOutputColumns() {
            String[] keys = this.keys;
            Class<?>[] types = this.types;
            if (keys.length != types.length) {
                // odd case sometimes encountered with invalid configurations or
                // while building a job
                final int length = Math.min(keys.length, types.length);
                keys = Arrays.copyOf(keys, length);
                types = Arrays.copyOf(types, length);
            }
            return new OutputColumns(keys, types);
        }

        @Override
        public Object[] transform(InputRow row) {
            final Map<String, ?> map = row.getValue(mapColumn);
            final Object[] result = new Object[keys.length];

            if (map == null) {
                return result;
            }

            for (int i = 0; i < keys.length; i++) {
                Object value = find(map, keys[i]);
                value = transformValue(value,types[i]);
                if (verifyTypes) {
                    value = types[i].cast(value);
                }
                result[i] = value;
            }

            return result;
        }

        private Object transformValue(Object value, Class<?> type) {
            if(value instanceof  byte[]){
                String stringValue = new String((byte[])value);
                if(ReflectionUtils.isNumber(type)){
                        value = Convert.toNumber(stringValue);
                }else if (ReflectionUtils.isDate(type)){
                        value = Convert.toDate(stringValue);
                }else {
                    value = stringValue;
                }
            }
            return  value;
        }

        /**
         * Searches a map for a given key. The key can be a regular map key, or a
         * simple expression of the form:
         *
         * <ul>
         * <li>foo.bar (will lookup 'foo', and then 'bar' in a potential nested map)
         * </li>
         * <li>foo.bar[0].baz (will lookup 'foo', then 'bar' in a potential nested
         * map, then pick the first element in case it is a list/array and then pick
         * 'baz' from the potential map at that position).
         * </ul>
         *
         * @param map
         *            the map to search in
         * @param key
         *            the key to resolve
         * @return the object in the map with the given key/expression. Or null if
         *         it does not exist.
         */
        public static Object find(Map<String, ?> map, String key) {
            if (map == null || key == null) {
                return null;
            }
            Object result = map.get(key);
            if (result == null) {
                return find(map, key, 0);
            }

            return result;
        }

        private static Object find(Map<String, ?> map, String key, int fromIndex) {
            final int indexOfDot = key.indexOf('.', fromIndex);
            final int indexOfBracket = key.indexOf('[', fromIndex);
            int indexOfEndBracket = -1;
            int arrayIndex = -1;

            boolean hasDot = indexOfDot != -1;
            boolean hasBracket = indexOfBracket != -1;

            if (hasBracket) {
                // also check that there is an end-bracket
                indexOfEndBracket = key.indexOf("].", indexOfBracket);
                hasBracket = indexOfEndBracket != -1;
                if (hasBracket) {
                    final String indexString = key.substring(indexOfBracket + 1, indexOfEndBracket);
                    try {
                        arrayIndex = Integer.parseInt(indexString);
                    } catch (NumberFormatException e) {
                        // not a valid array/list index
                        hasBracket = false;
                    }
                }
            }

            if (hasDot && hasBracket) {
                if (indexOfDot > indexOfBracket) {
                    hasDot = false;
                } else {
                    hasBracket = false;
                }
            }

            if (hasDot) {
                final String prefix = key.substring(0, indexOfDot);
                final Object nestedObject = map.get(prefix);
                if (nestedObject == null) {
                    return find(map, key, indexOfDot + 1);
                }
                if (nestedObject instanceof Map) {
                    final String remainingPart = key.substring(indexOfDot + 1);
                    @SuppressWarnings("unchecked")
                    final Map<String, ?> nestedMap = (Map<String, ?>) nestedObject;
                    return find(nestedMap, remainingPart);
                }
            }

            if (hasBracket) {
                final String prefix = key.substring(0, indexOfBracket);
                final Object nestedObject = map.get(prefix);
                if (nestedObject == null) {
                    return find(map, key, indexOfBracket + 1);
                }
                final String remainingPart = key.substring(indexOfEndBracket + 2);
                try {
                    if (nestedObject instanceof List) {
                        @SuppressWarnings("unchecked")
                        final Map<String, ?> nestedMap = ((List<Map<String, ?>>) nestedObject).get(arrayIndex);
                        return find(nestedMap, remainingPart);
                    } else if (nestedObject.getClass().isArray()) {
                        @SuppressWarnings("unchecked")
                        final Map<String, ?> nestedMap = (Map<String, ?>) Array.get(nestedObject, arrayIndex);
                        return find(nestedMap, remainingPart);
                    }
                } catch (IndexOutOfBoundsException e) {
                    return null;
                }
            }

            return null;
        }

    }

