package com.example.mapper;

import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class DobToAgeMapperTest {

    private static final LocalDate TODAY = LocalDate.of(2019, 5, 28);
    private DobToAgeMapper mapper;
    private Clock fixedClock;
    private Clock clock;

    @Before
    public void setup() {
        // mock the clock so calls to LocalDate.now(clock) will always return the same date: 5/28/2019
        clock = mock(Clock.class);
        fixedClock = Clock.fixed(TODAY.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
        mapper = new DobToAgeMapper(clock);
    }

    /**
     * Verify the calculated age is correct. Uses 05/28/2019 as the current day.
     */
    @Test
    public void apply_ageIsCorrect() {
        assertEquals("35", mapper.apply("05/29/1983"));
        assertEquals("36", mapper.apply("05/28/1983"));
    }

    /**
     * Verify the DOB in an unexpected format throws an exception.
     */
    @Test(expected = DateTimeParseException.class)
    public void apply_unexpectedDateFormat() {
        mapper.apply("05-28-1983");
    }

    /**
     * Verify a DOB in the future returns "0".
     */
    @Test
    public void apply_dobInFuture() {
        assertEquals("0", mapper.apply("05/28/2983"));
    }

}