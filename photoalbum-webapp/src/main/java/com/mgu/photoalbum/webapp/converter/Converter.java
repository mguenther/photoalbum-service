package com.mgu.photoalbum.webapp.converter;

public interface Converter<I, O> {

    O convert(I input);
}