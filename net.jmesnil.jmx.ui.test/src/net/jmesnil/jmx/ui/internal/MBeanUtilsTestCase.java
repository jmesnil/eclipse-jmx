package net.jmesnil.jmx.ui.internal;

import junit.framework.TestCase;

public class MBeanUtilsTestCase extends TestCase {

    public void testNullValue() throws Exception {
        assertNull(MBeanUtils.getValue(null, "whatever"));
        assertNull(MBeanUtils.getValue("whatever", null));
        assertNull(MBeanUtils.getValue(null, null));
    }

    public void testNonPrimitiveType() throws Exception {
        String value = "any value";
        assertEquals(value, MBeanUtils.getValue(value, "java.util.Vector"));
    }

    public void testBooleanValue() throws Exception {
        assertEquals(Boolean.TRUE, MBeanUtils.getValue("true", "boolean"));
        assertEquals(Boolean.FALSE, MBeanUtils.getValue("false", "boolean"));
        assertEquals(Boolean.FALSE, MBeanUtils.getValue("whatever", "boolean"));
    }

    public void testByteValue() throws Exception {
        assertEquals((byte) 0, MBeanUtils.getValue("0", "byte"));
        assertEquals((byte) 1, MBeanUtils.getValue("1", "byte"));
        try {
            MBeanUtils.getValue("whatever", "byte");
            fail();
        } catch (NumberFormatException e) {

        }
    }

    public void testCharValue() throws Exception {
        assertEquals('a', MBeanUtils.getValue("a", "char"));
        assertEquals('o', MBeanUtils.getValue("only take the first char",
                "char"));
    }

    public void testShortValue() throws Exception {
        assertEquals((short) 1, MBeanUtils.getValue("1", "short"));
        try {
            MBeanUtils.getValue("not a short", "short");
            fail();
        } catch (NumberFormatException e) {

        }
    }

    public void testIntValue() throws Exception {
        assertEquals(1, MBeanUtils.getValue("1", "int"));
        try {
            MBeanUtils.getValue("not a int", "int");
            fail();
        } catch (NumberFormatException e) {

        }
    }

    public void testLongValue() throws Exception {
        assertEquals((long) 1, MBeanUtils.getValue("1", "long"));
        try {
            MBeanUtils.getValue("not a long", "long");
            fail();
        } catch (NumberFormatException e) {

        }
    }

    public void testFloatValue() throws Exception {
        assertEquals(1.0f, MBeanUtils.getValue("1", "float"));
        try {
            MBeanUtils.getValue("not a float", "float");
            fail();
        } catch (NumberFormatException e) {

        }
    }

    public void testDoubleValue() throws Exception {
        assertEquals(1.0, MBeanUtils.getValue("1", "double"));
        try {
            MBeanUtils.getValue("not a double", "double");
            fail();
        } catch (NumberFormatException e) {

        }
    }

}
