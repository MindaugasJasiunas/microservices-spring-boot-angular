package com.example.demo.util.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.IOException;

public class PageRequestDeserializer extends JsonDeserializer<PageRequest> {

    @Override
    public PageRequest deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        int page = node.get("pageNumber").numberValue().intValue();
        int size = node.get("pageSize").numberValue().intValue();

        // cant get field name to sort by & sort direction from JSON - using default = unsorted
        String sortData = node.get("sort").asText(); // {"empty":false,"unsorted":false,"sorted":true}

        return PageRequest.of(page, size, Sort.unsorted());
    }

}