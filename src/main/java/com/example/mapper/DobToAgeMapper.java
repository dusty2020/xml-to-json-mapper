package com.example.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Maps a date of birth string in the format MM/dd/yyyy into an age string.
 */
public class DobToAgeMapper implements ValueMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private Clock clock;

    public DobToAgeMapper() {
        clock = Clock.systemDefaultZone();
    }

    public DobToAgeMapper(Clock clock) {
        this.clock = clock;
    }

    @Override
    public String apply(String s) {
        LocalDate dob = LocalDate.parse(s, formatter);
        LocalDate now = LocalDate.now(clock);
        if (dob.isAfter(now)) {
            return "0";
        }
        return String.valueOf(ChronoUnit.YEARS.between(dob, now));
    }
}
