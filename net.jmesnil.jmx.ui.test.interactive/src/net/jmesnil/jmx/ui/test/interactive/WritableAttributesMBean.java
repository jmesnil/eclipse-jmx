package net.jmesnil.jmx.ui.test.interactive;

public interface WritableAttributesMBean {
    boolean isBoolean();

    void setBoolean(boolean value);

    byte getByte();

    void setByte(byte value);

    char getChar();

    void setChar(char value);

    short getShort();

    void setShort(short value);

    int getInt();

    void setInt(int value);

    long getLong();

    void setLong(long value);

    float getFloat();

    void setFloat(float value);

    double getDouble();

    void setDouble(double value);

    String getStringWithNewlines();

    void setStringWithNewlines(String value);
}
