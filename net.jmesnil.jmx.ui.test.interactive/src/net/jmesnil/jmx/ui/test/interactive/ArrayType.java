/**
 * Eclipse JMX Console
 * Copyright (C) 2006 Jeff Mesnil
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package net.jmesnil.jmx.ui.test.interactive;

import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

public class ArrayType extends StandardMBean implements ArrayTypeMBean {

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
        return new short[] { 0, 1, 2 };
    }

    public int[] getInts() {
        return new int[] { -1, 0, 1, 2, 3 };
    }

    public long[] getLongs() {
        return new long[] { 0, 1, 2, 3 };
    }

    public float[] getFloats() {
        return new float[] { -1.0f, 0.0f, 1.0f, 2.0f, 3.0f };
    }

    public double[] getDoubles() {
        return new double[] { 0.0, 1.0, 2.0, 3.0 };
    }

    public String[] getStrings() {
        return new String[] { "zero", "one", "two", "three" };
    }

    public Object[] getObjects() {
        return getStrings();
    }

    public Object[][] getMultiDimensionalObjects() {
        Object[] nestedObjs = getObjects();
        Object[][] objs = new Object[3][nestedObjs.length];
        for (int i = 0; i < objs.length; i++) {
            objs[i] = nestedObjs;
        }
        return objs;
    }

    public boolean[] booleansOp(boolean[] b) {
        return getBooleans();
    }

    public byte[] bytesOp(byte[] b) {
        return getBytes();
    }

    public char[] charsOp(char[] c) {
        return getChars();
    }

    public double[] doublesOp(double[] d) {
        return getDoubles();
    }

    public float[] floatsOp(float[] f) {
        return getFloats();
    }

    public int[] intsOp() {
        return getInts();
    }

    public int[] intsOp(int[] i) {
        return getInts();
    }

    public long[] longsOp(long[] l) {
        return getLongs();
    }

    public Object[] objectsOp(Object[] o) {
        return getObjects();
    }

    public Object[][] multiDimensionalObjectsOp(Object[][] o) {
        return getMultiDimensionalObjects();
    }

    public short[] shortsOp(short[] s) {
        return getShorts();
    }
}
