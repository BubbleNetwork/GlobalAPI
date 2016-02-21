package com.thebubblenetwork.api.global.java;

import java.lang.reflect.Array;

/**
 * The Bubble Network 2016
 * GlobalAPI
 * 21/02/2016 {08:22}
 * Created February 2016
 */
public class ArrayBuilder<T> implements Cloneable{
    private final Class<T> clazz;
    public final int length;
    private T[] array;

    public ArrayBuilder(Class<T> clazz,int size){
        this.clazz = clazz;
        length = size;
        buildnew();
    }

    private ArrayBuilder(Class<T> clazz,int size,T[] array){
        this.clazz = clazz;
        length = size;
        if(array.length != length)throw new IllegalArgumentException("Invalid array length");
        this.array = array;
    }

    public T[] build(){
        return array.clone();
    }

    public ArrayBuilder<T> withNull(int slot){
        array[slot] = null;
        return this;
    }

    public ArrayBuilder<T> withT(int slot,T t){
        array[slot] = t;
        return this;
    }

    private void buildnew(){
        array = (T[])Array.newInstance(clazz,length);
    }

    public ArrayBuilder<T> clear(){
        buildnew();
        return this;
    }

    public ArrayBuilder<T> clone(){
        return new ArrayBuilder<T>(clazz,length,array);
    }
}
