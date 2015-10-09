package com.mgu.photoalbum.converter;

public interface Converter<I, O> {

    O convert(I input);
}