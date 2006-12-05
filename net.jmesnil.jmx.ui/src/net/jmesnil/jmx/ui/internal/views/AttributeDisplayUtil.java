package net.jmesnil.jmx.ui.internal.views;

import java.lang.reflect.Array;

import org.eclipse.core.runtime.Assert;

public class AttributeDisplayUtil {

    private static final String NULL = "null"; //$NON-NLS-1$

    static String toString(Object obj, boolean detailed) {
        if (obj == null) {
            return NULL;
        }
        Class clazz = obj.getClass();
        if (clazz.isArray()) {
            if (detailed) {
                return toDetailedString(obj);
            } else {
                return toSimpleString(obj);
            }
        }
        return obj.toString();
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
        try {
            Class clazz = AttributeDisplayUtil.class.getClassLoader()
                    .loadClass(type);
            if (clazz.isArray()) {
                return clazz.getComponentType().getName() + "[]"; //$NON-NLS-1$
            }
        } catch (ClassNotFoundException e) {
        }
        return type;
    }
}
