package com.mgu.photoalbum.adapter.couchdb;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.IOException;

/**
 * Implementation of {@link JsonDeserializer} which deserializes a
 * <code>long</code>-based timestamp into an instance of {@link DateTime}.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class UtcTimestampDeserializer extends JsonDeserializer<DateTime> {

    @Override
    public DateTime deserialize(final JsonParser jp, final DeserializationContext ctx)
            throws IOException {
        final long millis = jp.getLongValue();
        return DateTime.now(DateTimeZone.UTC).withMillis(millis);
    }
}