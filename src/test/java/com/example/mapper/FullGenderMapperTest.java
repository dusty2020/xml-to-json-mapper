package com.example.mapper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FullGenderMapperTest {

    private FullGenderMapper mapper = new FullGenderMapper();

    /**
     * Verify the proper values are returned.
     */
    @Test
    public void apply_fullGenderIsReturned() {
        assertEquals("male", mapper.apply("m"));
        assertEquals("female", mapper.apply("f"));
    }

    /**
     * Verify "unknown" is returned for unrecognized gender codes.
     */
    @Test
    public void apply_unknownValue() {
        assertEquals("unknown", mapper.apply("z"));
    }

    /**
     * Verify the case of the gender code doesn't matter to convert to the full description
     */
    @Test
    public void apply_caseSensitiveInput() {
        assertEquals("female", mapper.apply("F"));
        assertEquals("female", mapper.apply("f"));
    }
}