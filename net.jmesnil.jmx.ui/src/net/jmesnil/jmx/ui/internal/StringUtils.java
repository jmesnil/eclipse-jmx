package net.jmesnil.jmx.ui.internal;

import java.lang.reflect.Array;

import org.eclipse.core.runtime.Assert;

public class StringUtils {

    private static final String NULL = "null"; //$NON-NLS-1$

    public static String toString(Object obj, boolean detailed) {
        if (obj == null) {
            return NULL;
        }
        if (!isArray(obj.toString())) {
            return obj.toString();
        }
        if (detailed) {
            return toDetailedString(obj);
        } else {
            return toSimpleString(obj);
        }
    }

    private static final String toSimpleString(Object arrayObj) {
        Assert.isNotNull(arrayObj);
        Assert.isLegal(arrayObj.getClass().isArray());

        String type = arrayObj.getClass().getComponentType().getName();
        int length = Array.getLength(arrayObj);
        return type + '[' + length + ']';
    }

    private static final String toDetailedString(Object arrayObj) {
        Assert.isNotNull(arrayObj);
        Assert.isLegal(arrayObj.getClass().isArray());

        Object element;
        StringBuffer buff = new StringBuffer();
        int length = Array.getLength(arrayObj);
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                buff.append('\n');
            }

            element = Array.get(arrayObj, i);
            if (element == null) {
                buff.append(NULL);
            } else {
                buff.append(element.toString());
            }
        }
        return buff.toString();
    }

    public static String toString(String type) {
        return toString(type, true);
    }

    public static String toString(String type, boolean detailed) {
        Assert.isNotNull(type);
        Assert.isLegal(type.length() > 0);
        if (!isArray(type)) {
            return type;
        }

        try {
            Class clazz = StringUtils.class.getClassLoader().loadClass(type);
            if (clazz.isArray()) {
                if (detailed) {
                    return clazz.getComponentType().getName() + "[]"; //$NON-NLS-1$
                } else {
                    return clazz.getComponentType().getSimpleName() + "[]"; //$NON-NLS-1$
                }
            }
        } catch (ClassNotFoundException e) {
        }
        return type;
    }

    private static boolean isArray(String type) {
        return type.startsWith("["); //$NON-NLS-1$
    }

}
