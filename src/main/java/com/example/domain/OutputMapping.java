package com.example.domain;

import com.example.mapper.MapperType;
import lombok.Data;

@Data
public class OutputMapping {

    private String outputKey;
    private MapperType mapperType;
    private boolean numeric;
}
