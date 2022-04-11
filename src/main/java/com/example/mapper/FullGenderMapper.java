package com.example.mapper;

/**
 * Maps gender codes to the full string values for the gender.
 */
public class FullGenderMapper implements ValueMapper {

    @Override
    public String apply(String s) {
        switch (s.toLowerCase()) {
            case "f":
                return "female";
            case "m":
                return "male";
            default:
                return "unknown";
        }
    }
}
