package com.example.mapper;

import com.example.domain.State;

/**
 * Maps state names to their abbreviations.
 */
public class StateAbrevMapper implements ValueMapper {

    @Override
    public String apply(String s) {
        State state = State.getByName(s);
        if (state != null) {
            return state.getAbbreviation();
        }
        return "";
    }
}
