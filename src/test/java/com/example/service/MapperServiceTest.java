package com.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ContainerNode;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class MapperServiceTest {

    private MapperService mapperService = new MapperService();
    private ObjectMapper objectMapper = new ObjectMapper();

    File inputFile = new File("src/test/resources/input.xml");
    File inputFileLarge = new File("src/test/resources/inputLarge.xml");
    File outputFile = new File("src/test/resources/output.json");
    File mapperFile = new File("src/test/resources/exampleMapper.json");
    File expectedOutputFile = new File("src/test/resources/expectedOutput.json");
    File expectedOutputFileLarge = new File("src/test/resources/expectedOutputLarge.json");

    @Before
    public void setup() {
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    @Test
    public void convertToJson_success() throws Exception {
        mapperService.convertToJson(inputFile, outputFile, mapperFile);

        assertEquals(
                objectMapper.readValue(expectedOutputFile, ContainerNode.class).toString(),
                objectMapper.readValue(outputFile, ContainerNode.class).toString());
    }

    @Test
    public void convertToJson_1000Items() throws Exception {
        mapperService.convertToJson(inputFileLarge, outputFile, mapperFile);

        assertEquals(
                objectMapper.readValue(expectedOutputFileLarge, ContainerNode.class).toString(),
                objectMapper.readValue(outputFile, ContainerNode.class).toString());
    }
}
