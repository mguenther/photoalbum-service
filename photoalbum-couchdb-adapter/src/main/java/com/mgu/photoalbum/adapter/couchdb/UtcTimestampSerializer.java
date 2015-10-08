package com.mgu.photoalbum.adapter.couchdb;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;

import java.io.IOException;

/**
 * Implementation of {@link JsonSerializer} which serializes a
 * {@link DateTime} instance into a <code>long</code>-based timestamp.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class UtcTimestampSerializer extends JsonSerializer<DateTime> {
    @Override
    public void serialize(final DateTime value, final JsonGenerator jgen, final SerializerProvider provider)
            throws IOException {
        long millis = value.getMillis();
        jgen.writeNumber(millis);
    }
}