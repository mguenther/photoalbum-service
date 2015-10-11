package com.mgu.photoalbum.webapp.converter;

/**
 * Provides the means to convert between to types {@code From} and {@code To}.
 *
 * @param <From>
 *     parameterized type of {@code Converter} with no type boundary
 * @param <To>
 *     parameterized type of {@code Converter} with no type boundary
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
@FunctionalInterface
public interface Converter<From, To> {

    /**
     * Converts the given object of type {@code From} to an object of type
     * {@code To}.
     *
     * @param input
     *      input object of type {@code From}
     * @return
     *      object of target type {@code To}
     */
    To convert(From input);
}