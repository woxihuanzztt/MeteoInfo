/*
 * Copyright 1998-2014 University Corporation for Atmospheric Research/Unidata
 *
 *   Portions of this software were developed by the Unidata Program at the
 *   University Corporation for Atmospheric Research.
 *
 *   Access and use of this software shall impose the following obligations
 *   and understandings on the user. The user is granted the right, without
 *   any fee or cost, to use, copy, modify, alter, enhance and distribute
 *   this software, and any derivative works thereof, and its supporting
 *   documentation for any purpose whatsoever, provided that this entire
 *   notice appears in all copies of the software, derivative works and
 *   supporting documentation.  Further, UCAR requests that the user credit
 *   UCAR/Unidata in any publications that result from the use of this
 *   software or in any product that includes this software. The names UCAR
 *   and/or Unidata, however, may not be used in any advertising or publicity
 *   to endorse or promote any products or commercial entity unless specific
 *   written permission is obtained from UCAR/Unidata. The user also
 *   understands that UCAR/Unidata is not obligated to provide the user with
 *   any support, consulting, training or assistance of any kind with regard
 *   to the use, operation and performance of this software nor to provide
 *   the user with any updates, revisions, new versions or "bug fixes."
 *
 *   THIS SOFTWARE IS PROVIDED BY UCAR/UNIDATA "AS IS" AND ANY EXPRESS OR
 *   IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *   DISCLAIMED. IN NO EVENT SHALL UCAR/UNIDATA BE LIABLE FOR ANY SPECIAL,
 *   INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING
 *   FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,
 *   NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION
 *   WITH THE ACCESS, USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package org.meteoinfo.ndarray;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.time.LocalDateTime;

/**
 * Concrete implementation of Array specialized for shorts. Data storage is with
 * 1D java array of shorts.
 * <p/>
 * issues: what should we do if a conversion loses accuracy? nothing ? Exception
 * ?
 *
 * @author caron
 * @see Array
 */
public class ArrayShort extends Array {

    // package private. use Array.factory()
    static ArrayShort factory(Index index, boolean isUnsigned) {
        return ArrayShort.factory(index, isUnsigned, null);
    }

    /* create new ArrayShort with given indexImpl and backing store.
   * Should be private.
   * @param index use this Index
   * @param storage use this storage. if null, allocate.
   * @return. new ArrayShort.D<rank> or ArrayShort object.
     */
    static ArrayShort factory(Index index, boolean isUnsigned, short[] storage) {
        switch (index.getRank()) {
            case 0:
                return new D0(index, isUnsigned, storage);
            case 1:
                return new D1(index, isUnsigned, storage);
            case 2:
                return new D2(index, isUnsigned, storage);
            case 3:
                return new D3(index, isUnsigned, storage);
            case 4:
                return new D4(index, isUnsigned, storage);
            case 5:
                return new D5(index, isUnsigned, storage);
            case 6:
                return new D6(index, isUnsigned, storage);
            case 7:
                return new D7(index, isUnsigned, storage);
            default:
                return new ArrayShort(index, isUnsigned, storage);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    protected short[] storage;

    /**
     * Create a new Array of type short and the given shape. dimensions.length
     * determines the rank of the new Array.
     *
     * @param dimensions the shape of the Array.
     */
    public ArrayShort(int[] dimensions, boolean isUnsigned) {
        super(isUnsigned ? DataType.USHORT : DataType.SHORT, dimensions);
        storage = new short[(int) indexCalc.getSize()];
    }

    /**
     * Create a new Array using the given IndexArray and backing store. used for
     * sections. Trusted package private.
     *
     * @param ima use this IndexArray as the index
     * @param data use this as the backing store
     */
    ArrayShort(Index ima, boolean isUnsigned, short[] data) {
        super(isUnsigned ? DataType.USHORT : DataType.SHORT, ima);
        /* replace by something better
    if (ima.getSize() != data.length)
      throw new IllegalArgumentException("bad data length"); */
        if (data != null) {
            storage = data;
        } else {
            storage = new short[(int) ima.getSize()];
        }
    }

    /**
     * create new Array with given indexImpl and same backing store
     */
    protected Array createView(Index index) {
        return ArrayShort.factory(index, isUnsigned(), storage);
    }

    /* Get underlying primitive array storage. CAUTION! You may invalidate your warrentee! */
    public Object getStorage() {
        return storage;
    }

    // copy from javaArray to storage using the iterator: used by factory( Object);
    protected void copyFrom1DJavaArray(IndexIterator iter, Object javaArray) {
        short[] ja = (short[]) javaArray;
        for (short aJa : ja) {
            iter.setShortNext(aJa);
        }
    }

    // copy to javaArray from storage using the iterator: used by copyToNDJavaArray;
    protected void copyTo1DJavaArray(IndexIterator iter, Object javaArray) {
        short[] ja = (short[]) javaArray;
        for (int i = 0; i < ja.length; i++) {
            ja[i] = iter.getShortNext();
        }
    }

    public ByteBuffer getDataAsByteBuffer() {
        return getDataAsByteBuffer(null);
    }

    public ByteBuffer getDataAsByteBuffer(ByteOrder order) {
        ByteBuffer bb = super.getDataAsByteBuffer((int) (2 * getSize()), order);
        ShortBuffer ib = bb.asShortBuffer();
        ib.put((short[]) get1DJavaArray(short.class)); // make sure its in canonical order
        return bb;
    }

    /**
     * Return the element class type
     */
    public Class getElementType() {
        return short.class;
    }

    /**
     * Get the value at the specified index.
     *
     * @param i the index
     * @return the value at the specified index.
     */
    public short get(Index i) {
        return storage[i.currentElement()];
    }

    /**
     * Set the value at the specified index.
     *
     * @param i the index
     * @param value set to this value
     */
    public void set(Index i, short value) {
        storage[i.currentElement()] = value;
    }

    public double getDouble(Index i) {
        short val = storage[i.currentElement()];
        return (double) (isUnsigned() ? DataType.unsignedShortToInt(val) : val);
    }

    public void setDouble(Index i, double value) {
        storage[i.currentElement()] = (short) value;
    }

    public float getFloat(Index i) {
        short val = storage[i.currentElement()];
        return (float) (isUnsigned() ? DataType.unsignedShortToInt(val) : val);
    }

    public void setFloat(Index i, float value) {
        storage[i.currentElement()] = (short) value;
    }

    public long getLong(Index i) {
        short val = storage[i.currentElement()];
        return (long) (isUnsigned() ? DataType.unsignedShortToInt(val) : val);
    }

    public void setLong(Index i, long value) {
        storage[i.currentElement()] = (short) value;
    }

    public int getInt(Index i) {
        short val = storage[i.currentElement()];
        return (isUnsigned() ? DataType.unsignedShortToInt(val) : val);
    }

    public void setInt(Index i, int value) {
        storage[i.currentElement()] = (short) value;
    }

    public short getShort(Index i) {
        return storage[i.currentElement()];
    }

    public void setShort(Index i, short value) {
        storage[i.currentElement()] = value;
    }

    public byte getByte(Index i) {
        return (byte) storage[i.currentElement()];
    }

    public void setByte(Index i, byte value) {
        storage[i.currentElement()] = (short) value;
    }

    public char getChar(Index i) {
        short val = storage[i.currentElement()];
        return (char) (isUnsigned() ? DataType.unsignedShortToInt(val) : val);
    }

    public void setChar(Index i, char value) {
        storage[i.currentElement()] = (short) value;
    }

    /**
     * not legal, throw ForbiddenConversionException
     */
    public boolean getBoolean(Index i) {
        throw new ForbiddenConversionException();
    }

    /**
     * not legal, throw ForbiddenConversionException
     */
    public void setBoolean(Index i, boolean value) {
        throw new ForbiddenConversionException();
    }

    /**
     * not legal, throw ForbiddenConversionException
     */
    public String getString(Index i) {
        short v = this.storage[i.currentElement()];
        return isUnsigned() ? String.valueOf(DataType.unsignedShortToInt(v)) :
                String.valueOf(v);
    }

    /**
     * not legal, throw ForbiddenConversionException
     */
    public void setString(Index i, String value) {
        this.storage[i.currentElement()] = Short.parseShort(value);
    }

    /**
     * not legal, throw ForbiddenConversionException
     */
    public Complex getComplex(Index i) {
        throw new ForbiddenConversionException();
    }

    /**
     * not legal, throw ForbiddenConversionException
     */
    public void setComplex(Index i, Complex value) {
        throw new ForbiddenConversionException();
    }

    public LocalDateTime getDate(Index i) { throw new ForbiddenConversionException(); }

    public void setDate(Index i, LocalDateTime value) { throw new ForbiddenConversionException(); }

    public Object getObject(Index i) {
        return isUnsigned() ? getInt(i) : getShort(i);
    }

    public void setObject(Index i, Object value) {
        storage[i.currentElement()] = ((Number) value).shortValue();
    }

    // package private : mostly for iterators
    public double getDouble(int index) {
        short val = storage[index];
        return (double) (isUnsigned() ? DataType.unsignedShortToInt(val) : val);
    }

    public void setDouble(int index, double value) {
        storage[index] = (short) value;
    }

    public float getFloat(int index) {
        short val = storage[index];
        return (float) (isUnsigned() ? DataType.unsignedShortToInt(val) : val);
    }

    public void setFloat(int index, float value) {
        storage[index] = (short) value;
    }

    public long getLong(int index) {
        short val = storage[index];
        return (long) (isUnsigned() ? DataType.unsignedShortToInt(val) : val);
    }

    public void setLong(int index, long value) {
        storage[index] = (short) value;
    }

    public int getInt(int index) {
        short val = storage[index];
        return (isUnsigned() ? DataType.unsignedShortToInt(val) : val);
    }

    public void setInt(int index, int value) {
        storage[index] = (short) value;
    }

    public short getShort(int index) {
        return storage[index];
    }

    public void setShort(int index, short value) {
        storage[index] = value;
    }

    public byte getByte(int index) {
        return (byte) storage[index];
    }

    public void setByte(int index, byte value) {
        storage[index] = (short) value;
    }

    public char getChar(int index) {
        short val = storage[index];
        return (char) (isUnsigned() ? DataType.unsignedShortToInt(val) : val);
    }

    public void setChar(int index, char value) {
        storage[index] = (short) value;
    }

    public boolean getBoolean(int index) {
        throw new ForbiddenConversionException();
    }

    public void setBoolean(int index, boolean value) {
        throw new ForbiddenConversionException();
    }
    
    public String getString(int index) {
        short v = this.storage[index];
        return isUnsigned() ? String.valueOf(DataType.unsignedShortToInt(v)) :
                String.valueOf(v);
    }

    public void setString(int index, String value) {
        this.storage[index] = Short.parseShort(value);
    }
    
    public Complex getComplex(int index) {
        throw new ForbiddenConversionException();
    }

    public void setComplex(int index, Complex value) {
        throw new ForbiddenConversionException();
    }

    public LocalDateTime getDate(int index) { throw new ForbiddenConversionException(); }

    public void setDate(int index, LocalDateTime value) { throw new ForbiddenConversionException(); }

    public Object getObject(int index) {
        return isUnsigned() ? getInt(index) : getShort(index);
    }

    public void setObject(int index, Object value) {
        storage[index] = ((Number) value).shortValue();
    }

    /**
     * Concrete implementation of Array specialized for shorts, rank 0.
     */
    public static class D0 extends ArrayShort {

        private Index0D ix;

        public D0(boolean isUnsigned) {
            super(new int[]{}, isUnsigned);
            ix = (Index0D) indexCalc;
        }

        private D0(Index i, boolean isUnsigned, short[] store) {
            super(i, isUnsigned, store);
            ix = (Index0D) indexCalc;
        }

        public short get() {
            return storage[ix.currentElement()];
        }

        public void set(short value) {
            storage[ix.currentElement()] = value;
        }
    }

    /**
     * Concrete implementation of Array specialized for shorts, rank 1.
     */
    public static class D1 extends ArrayShort {

        private Index1D ix;

        public D1(int len0, boolean isUnsigned) {
            super(new int[]{len0}, isUnsigned);
            ix = (Index1D) indexCalc;
        }

        private D1(Index i, boolean isUnsigned, short[] store) {
            super(i, isUnsigned, store);
            ix = (Index1D) indexCalc;
        }

        public short get(int i) {
            return storage[ix.setDirect(i)];
        }

        public void set(int i, short value) {
            storage[ix.setDirect(i)] = value;
        }
    }

    /**
     * Concrete implementation of Array specialized for shorts, rank 2.
     */
    public static class D2 extends ArrayShort {

        private Index2D ix;

        public D2(int len0, int len1, boolean isUnsigned) {
            super(new int[]{len0, len1}, isUnsigned);
            ix = (Index2D) indexCalc;
        }

        private D2(Index i, boolean isUnsigned, short[] store) {
            super(i, isUnsigned, store);
            ix = (Index2D) indexCalc;
        }

        public short get(int i, int j) {
            return storage[ix.setDirect(i, j)];
        }

        public void set(int i, int j, short value) {
            storage[ix.setDirect(i, j)] = value;
        }
    }

    /**
     * Concrete implementation of Array specialized for shorts, rank 3.
     */
    public static class D3 extends ArrayShort {

        private Index3D ix;

        public D3(int len0, int len1, int len2, boolean isUnsigned) {
            super(new int[]{len0, len1, len2}, isUnsigned);
            ix = (Index3D) indexCalc;
        }

        private D3(Index i, boolean isUnsigned, short[] store) {
            super(i, isUnsigned, store);
            ix = (Index3D) indexCalc;
        }

        public short get(int i, int j, int k) {
            return storage[ix.setDirect(i, j, k)];
        }

        public void set(int i, int j, int k, short value) {
            storage[ix.setDirect(i, j, k)] = value;
        }
    }

    /**
     * Concrete implementation of Array specialized for shorts, rank 4.
     */
    public static class D4 extends ArrayShort {

        private Index4D ix;

        public D4(int len0, int len1, int len2, int len3, boolean isUnsigned) {
            super(new int[]{len0, len1, len2, len3}, isUnsigned);
            ix = (Index4D) indexCalc;
        }

        private D4(Index i, boolean isUnsigned, short[] store) {
            super(i, isUnsigned, store);
            ix = (Index4D) indexCalc;
        }

        public short get(int i, int j, int k, int l) {
            return storage[ix.setDirect(i, j, k, l)];
        }

        public void set(int i, int j, int k, int l, short value) {
            storage[ix.setDirect(i, j, k, l)] = value;
        }
    }

    /**
     * Concrete implementation of Array specialized for shorts, rank 5.
     */
    public static class D5 extends ArrayShort {

        private Index5D ix;

        public D5(int len0, int len1, int len2, int len3, int len4, boolean isUnsigned) {
            super(new int[]{len0, len1, len2, len3, len4}, isUnsigned);
            ix = (Index5D) indexCalc;
        }

        private D5(Index i, boolean isUnsigned, short[] store) {
            super(i, isUnsigned, store);
            ix = (Index5D) indexCalc;
        }

        public short get(int i, int j, int k, int l, int m) {
            return storage[ix.setDirect(i, j, k, l, m)];
        }

        public void set(int i, int j, int k, int l, int m, short value) {
            storage[ix.setDirect(i, j, k, l, m)] = value;
        }
    }

    /**
     * Concrete implementation of Array specialized for shorts, rank 6.
     */
    public static class D6 extends ArrayShort {

        private Index6D ix;

        public D6(int len0, int len1, int len2, int len3, int len4, int len5, boolean isUnsigned) {
            super(new int[]{len0, len1, len2, len3, len4, len5}, isUnsigned);
            ix = (Index6D) indexCalc;
        }

        private D6(Index i, boolean isUnsigned, short[] store) {
            super(i, isUnsigned, store);
            ix = (Index6D) indexCalc;
        }

        public short get(int i, int j, int k, int l, int m, int n) {
            return storage[ix.setDirect(i, j, k, l, m, n)];
        }

        public void set(int i, int j, int k, int l, int m, int n, short value) {
            storage[ix.setDirect(i, j, k, l, m, n)] = value;
        }
    }

    /**
     * Concrete implementation of Array specialized for shorts, rank 7.
     */
    public static class D7 extends ArrayShort {

        private Index7D ix;

        public D7(int len0, int len1, int len2, int len3, int len4, int len5, int len6, boolean isUnsigned) {
            super(new int[]{len0, len1, len2, len3, len4, len5, len6}, isUnsigned);
            ix = (Index7D) indexCalc;
        }

        private D7(Index i, boolean isUnsigned, short[] store) {
            super(i, isUnsigned, store);
            ix = (Index7D) indexCalc;
        }

        public short get(int i, int j, int k, int l, int m, int n, int o) {
            return storage[ix.setDirect(i, j, k, l, m, n, o)];
        }

        public void set(int i, int j, int k, int l, int m, int n, int o, short value) {
            storage[ix.setDirect(i, j, k, l, m, n, o)] = value;
        }
    }

}
