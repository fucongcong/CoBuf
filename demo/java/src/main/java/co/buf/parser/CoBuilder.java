package co.buf.parser;

import co.buf.type.CoStruct;

public abstract class CoBuilder {
    public abstract byte[] toByteArray();

    public abstract void parse(byte[] data, CoStruct struct);
}
