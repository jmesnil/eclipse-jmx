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
                if (componentType == Long.TYPE) {
                    return getLongArrayAsString((long[]) obj, detailed);
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
                boolean b = booleans[i];
                buff.append(b).append("\n"); //$NON-NLS-1$
            }
            return buff.toString();
        } else {
            return "boolean[" + booleans.length + "]"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private static String getLongArrayAsString(long[] longElements,
            boolean detailed) {
        if (detailed) {
            StringBuffer buff = new StringBuffer();
            for (int i = 0; i < longElements.length; i++) {
                long longElement = longElements[i];
                buff.append(longElement).append("\n"); //$NON-NLS-1$
            }
            return buff.toString();
        } else {
            return "long[" + longElements.length + "]"; //$NON-NLS-1$ //$NON-NLS-2$
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
