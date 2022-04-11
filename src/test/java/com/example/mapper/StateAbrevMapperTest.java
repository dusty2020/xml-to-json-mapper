package com.example.mapper;

import org.junit.Test;

import static org.junit.Assert.*;

public class StateAbrevMapperTest {

    private StateAbrevMapper mapper = new StateAbrevMapper();

    /**
     * Verify correct abbreviations are returned for state names.
     */
    @Test
    public void apply_returnsCorrectVals() {
        assertEquals("MI", mapper.apply("Michigan"));
        assertEquals("AK", mapper.apply("Alaska"));
    }

    /**
     * Verify unknown values return empty string.
     */
    @Test
    public void apply_unknownValueReturnsEmpty() {
        assertEquals("", mapper.apply("Mississississippi"));
    }
}