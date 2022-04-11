package com.example.mapper;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This enum contains the supported mapper types. Add any new mappers to this enum so the mapper can be used (by name)
 * in mapper files.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum MapperType {
    NONE(null),
    DOB_TO_AGE(new DobToAgeMapper()),
    GENDER(new FullGenderMapper()),
    STATE_ABREV(new StateAbrevMapper());

    private ValueMapper valueMapper;
}
