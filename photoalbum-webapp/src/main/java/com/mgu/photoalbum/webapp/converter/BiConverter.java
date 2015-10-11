package com.mgu.photoalbum.webapp.converter;

/**
 * Provides the means to convert between two input types {@code FromA} and
 * {@code FromB} and {@code To}.
 *
 * @param <FromA>
 *     parameterized type of {@code BiConverter} with no type boundary
 * @param <FromB>
 *     parameterized type of {@code BiConverter} with no type boundary
 * @param <To>
 *     parameterized type of {@code BiConverter} with no type boundary;
 *     represents the target type
 */
@FunctionalInterface
public interface BiConverter<FromA, FromB, To> {

    /**
     * Converts the given objects of types {@code FromA} and {@code FromB}
     * to an object of type {@code To}.
     *
     * @param input1
     *      input object of type {@code FromA}
     * @param input2
     *      input object of type {@code FromB}
     * @return
     *      object of target type {@code To}
     */
    To convert(FromA input1, FromB input2);
}