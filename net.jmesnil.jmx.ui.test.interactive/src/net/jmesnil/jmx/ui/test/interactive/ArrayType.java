package net.jmesnil.jmx.ui.test.interactive;

import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;


public class ArrayType extends StandardMBean implements
        ArrayTypeMBean {

    public ArrayType() throws NotCompliantMBeanException {
        super(ArrayTypeMBean.class);
    }

    public boolean[] getBooleans() {
        return new boolean[] { true, false };
    }

    public byte[] getBytes() {
        return new byte[] { 0, 1, 2, 3 };
    }

    public char[] getChars() {
        return new char[] { '0', '1', '2', '3' };
    }

    public short[] getShorts() {
        return new short[] { 0, 1, 2, 3 };
    }

    public int[] getInts() {
        return new int[] { 0, 1, 2, 3 };
    }

    public long[] getLongs() {
        return new long[] { 0, 1, 2, 3 };
    }

    public float[] getFloats() {
        return new float[] { 0.0f, 1.0f, 2.0f, 3.0f };
    }

    public double[] getDoubles() {
        return new double[] { 0.0, 1.0, 2.0, 3.0 };
    }

    public String[] getStrings() {
        return new String[] { "zero", "one", "two", "three" };
    }

}
