package net.jmesnil.jmx.ui.internal.views;

public class AttributeDisplayUtil {

    static String toString(Object obj, boolean detailed) {
        Class clazz = obj.getClass();
        if (clazz.isArray()) {
            Class componentType = clazz.getComponentType();
            if (componentType.isPrimitive()) {
                if (componentType == Boolean.TYPE) {
                    return getBooleanArrayAsString((boolean[]) obj, detailed);
                }
                if (componentType == Byte.TYPE) {
                    return getByteArrayAsString((byte[]) obj, detailed);
                }
                if (componentType == Character.TYPE) {
                    return getCharArrayAsString((char[]) obj, detailed);
                }
                if (componentType == Short.TYPE) {
                    return getShortArrayAsString((short[]) obj, detailed);
                }
                if (componentType == Integer.TYPE) {
                    return getIntArrayAsString((int[]) obj, detailed);
                }
                if (componentType == Long.TYPE) {
                    return getLongArrayAsString((long[]) obj, detailed);
                }
                if (componentType == Float.TYPE) {
                    return getFloatArrayAsString((float[]) obj, detailed);
                }
                if (componentType == Double.TYPE) {
                    return getDoubleArrayAsString((double[]) obj, detailed);
                }
            } else {
                return getObjectArrayAsString((Object[]) obj, detailed);
            }
        }
        return obj.toString();
    }

    private static String getBooleanArrayAsString(boolean[] booleans,
            boolean detailed) {
        if (detailed) {
            StringBuffer buff = new StringBuffer();
            for (int i = 0; i < booleans.length; i++) {
                buff.append(booleans[i]).append("\n"); //$NON-NLS-1$
            }
            return buff.toString();
        } else {
            return "boolean[" + booleans.length + "]"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private static String getByteArrayAsString(byte[] bytes, boolean detailed) {
        if (detailed) {
            StringBuffer buff = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                buff.append(bytes[i]).append("\n"); //$NON-NLS-1$
            }
            return buff.toString();
        } else {
            return "byte[" + bytes.length + "]"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private static String getCharArrayAsString(char[] chars, boolean detailed) {
        if (detailed) {
            StringBuffer buff = new StringBuffer();
            for (int i = 0; i < chars.length; i++) {
                buff.append(chars[i]).append("\n"); //$NON-NLS-1$
            }
            return buff.toString();
        } else {
            return "char[" + chars.length + "]"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private static String getShortArrayAsString(short[] shorts, boolean detailed) {
        if (detailed) {
            StringBuffer buff = new StringBuffer();
            for (int i = 0; i < shorts.length; i++) {
                buff.append(shorts[i]).append("\n"); //$NON-NLS-1$
            }
            return buff.toString();
        } else {
            return "short[" + shorts.length + "]"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private static String getIntArrayAsString(int[] ints, boolean detailed) {
        if (detailed) {
            StringBuffer buff = new StringBuffer();
            for (int i = 0; i < ints.length; i++) {
                buff.append(ints[i]).append("\n"); //$NON-NLS-1$
            }
            return buff.toString();
        } else {
            return "int[" + ints.length + "]"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private static String getLongArrayAsString(long[] longs, boolean detailed) {
        if (detailed) {
            StringBuffer buff = new StringBuffer();
            for (int i = 0; i < longs.length; i++) {
                buff.append(longs[i]).append("\n"); //$NON-NLS-1$
            }
            return buff.toString();
        } else {
            return "long[" + longs.length + "]"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private static String getFloatArrayAsString(float[] floats, boolean detailed) {
        if (detailed) {
            StringBuffer buff = new StringBuffer();
            for (int i = 0; i < floats.length; i++) {
                buff.append(floats[i]).append("\n"); //$NON-NLS-1$
            }
            return buff.toString();
        } else {
            return "float[" + floats.length + "]"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private static String getDoubleArrayAsString(double[] doubles,
            boolean detailed) {
        if (detailed) {
            StringBuffer buff = new StringBuffer();
            for (int i = 0; i < doubles.length; i++) {
                buff.append(doubles[i]).append("\n"); //$NON-NLS-1$
            }
            return buff.toString();
        } else {
            return "double[" + doubles.length + "]"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private static String getObjectArrayAsString(Object[] objs, boolean detailed) {
        if (detailed) {
            StringBuffer buff = new StringBuffer();
            for (int i = 0; i < objs.length; i++) {
                buff.append(objs[i]).append("\n"); //$NON-NLS-1$
            }
            return buff.toString();
        } else {
            return objs.getClass().getComponentType().getName() + "[" //$NON-NLS-1$
                    + objs.length + "]"; //$NON-NLS-1$
        }
    }

}
