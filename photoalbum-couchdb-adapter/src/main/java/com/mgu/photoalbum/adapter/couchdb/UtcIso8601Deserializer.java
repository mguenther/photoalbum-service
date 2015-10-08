package com.mgu.photoalbum.adapter.couchdb;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.io.IOException;

public class UtcIso8601Deserializer extends JsonDeserializer<DateTime> {

    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendYear(4, 4)
            .appendLiteral("-")
            .appendMonthOfYear(2)
            .appendLiteral("-")
            .appendDayOfMonth(2)
            .appendLiteral("T")
            .appendHourOfDay(2)
            .appendLiteral(":")
            .appendMinuteOfHour(2)
            .appendLiteral(":")
            .appendSecondOfMinute(2)
            .appendLiteral(".")
            .appendFractionOfSecond(3, 3)
            .appendLiteral("Z")
            .appendTimeZoneOffset("", false, 2, 2)
            .toFormatter();

    @Override
    public DateTime deserialize(
            final JsonParser jsonParser,
            final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return FORMATTER.withZone(DateTimeZone.UTC).parseDateTime(jsonParser.getValueAsString());
    }
}