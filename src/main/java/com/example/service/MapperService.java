package com.example.service;

import com.example.domain.OutputMapping;
import com.example.mapper.ValueMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.apachecommons.CommonsLog;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Map;

@CommonsLog
public class MapperService {

    private XmlMapper xmlMapper;
    private ObjectMapper objectMapper;

    public MapperService() {
        xmlMapper = new XmlMapper();
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Converts XML input to JSON output based on the mappings in the mapper file. Assumes the XML input is formatted
     * as a list of items. Reads/writes one item at a time to keep memory consumption low (as opposed to reading
     * the entire input file into memory at once).
     * @param inputXmlFile the input file containing xml to format
     * @param outputJsonFile the output file that will contain the transformed data represented as JSON
     * @param mapperJsonFile the JSON file containing the instructions for transforming data
     * @throws Exception
     */
    public void convertToJson(File inputXmlFile, File outputJsonFile, File mapperJsonFile) throws Exception {
        Map<String, OutputMapping> mapperData = objectMapper.readValue(
                mapperJsonFile,
                new TypeReference<Map<String, OutputMapping>>() {});

        XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
        XMLStreamReader sr = null;

        try (FileWriter fileWriter = new FileWriter(outputJsonFile, true); SequenceWriter seqWriter = objectMapper.writer().writeValuesAsArray(fileWriter)) {
            sr = xmlInputFactory.createXMLStreamReader(new FileInputStream(inputXmlFile));
            advanceToNextElement(sr); // move to list (i.e. <patients>)
            advanceToNextElement(sr); // move to first item in list (i.e. <patient>)
            while (sr.hasNext()) {
                Map<String, String> item = xmlMapper.readValue(sr, new TypeReference<Map<String, String>>() {});
                seqWriter.write(processItem(item, mapperData));
                advanceToNextElement(sr); // move to next item in list
            }
        } catch (Exception e) {
            log.error(e);
            throw e;
        } finally {
            if (sr != null) {
                sr.close();
            }
        }
    }

    /**
     * Converts the input data to JSON output by mapping fields using field mapping data passed in.
     * @param inputItem the map representation of the input object where the key is the field name
     *                  and value is field value
     * @param fieldOutputMappings the map representation of the mapper file which tells how to map input
     *                            fields to output fields
     * @return the JSON object created from the input with the mappings applied
     */
    ObjectNode processItem(Map<String, String> inputItem, Map<String, OutputMapping> fieldOutputMappings) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();

        //Only map the the fields that are specified in the mapping file
        fieldOutputMappings.forEach((fieldToMap, outputMapping) -> {
            ValueMapper valueMapper = outputMapping.getMapperType().getValueMapper();
            String value = inputItem.get(fieldToMap); // initialize to value in the input file
            if (valueMapper != null) {
                value = valueMapper.apply(value); // overwrite if there is a mapper for the field
            }
            if (outputMapping.isNumeric()) {
                node.put(outputMapping.getOutputKey(), Long.parseLong(value));
            } else {
                node.put(outputMapping.getOutputKey(), value);
            }
        });
        return node;
    }

    private void advanceToNextElement(XMLStreamReader sr) throws XMLStreamException {
        if (sr.hasNext()) {
            sr.next(); // advance past the current state
        } else {
            return;
        }
        while (!sr.isStartElement()) {  // advance to the next StartElement
            if (sr.hasNext()) {
                sr.next();
            } else {
                return;
            }
        }
    }
}
