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

public interface ArrayTypeMBean {

    // attributes
    boolean[] getBooleans();

    byte[] getBytes();

    char[] getChars();

    short[] getShorts();

    int[] getInts();

    long[] getLongs();

    float[] getFloats();

    double[] getDoubles();

    String[] getStrings();

    Object[] getObjects();

    // operations
    boolean[] booleansOp(boolean[] b);

    byte[] bytesOp(byte[] b);

    char[] charsOp(char[] c);

    short[] shortsOp(short[] s);

    int[] intsOp(int[] i);

    long[] longsOp(long[] l);

    float[] floatsOp(float[] f);

    double[] doublesOp(double[] d);

    Object[] objectsOp(Object[] o);
}
