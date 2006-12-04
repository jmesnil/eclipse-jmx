package net.jmesnil.jmx.ui.internal.views;

public class AttributeDisplayUtil {

    static String toString(Object obj, boolean detailed) {
        if (obj.getClass().isArray()) {
            if (obj.getClass().getComponentType().isPrimitive()) {
                if (obj.getClass().getComponentType() == Long.TYPE) {
                    return getLongArrayAsString((long[]) obj, detailed);
                }
            } else {
                Object[] objs = (Object[]) obj;
                return getObjectArrayDisplay(objs, detailed);
            }
        }
        return obj.toString();
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

    private static String getObjectArrayDisplay(Object[] objs, boolean detailed) {
        if (detailed) {
            StringBuffer buff = new StringBuffer();
            for (int i = 0; i < objs.length; i++) {
                Object item = objs[i];
                buff.append(item).append("\n"); //$NON-NLS-1$
            }
            return buff.toString();
        } else {
            return objs.getClass().getComponentType().getName() + "[" //$NON-NLS-1$
                    + objs.length + "]"; //$NON-NLS-1$
        }
    }

}
