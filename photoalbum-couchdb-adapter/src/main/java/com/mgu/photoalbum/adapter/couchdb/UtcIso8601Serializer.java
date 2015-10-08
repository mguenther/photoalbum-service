package com.mgu.photoalbum.adapter.couchdb;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.io.IOException;

public class UtcIso8601Serializer extends JsonSerializer<DateTime> {

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
    public void serialize(
            final DateTime dateTime,
            final JsonGenerator jsonGenerator,
            final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(FORMATTER.print(dateTime.toDateTime(DateTimeZone.UTC)));
    }
}
