package com.example.receiptvalidatorservice.usecases.impl;

import com.example.receiptvalidatorservice.config.properties.PathProperties;
import com.example.receiptvalidatorservice.exception.SchemaValidationException;
import lombok.extern.slf4j.Slf4j;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;

@Service
@Slf4j
public class JsonSchemaValidatorService {

    private final Schema schema;


    public JsonSchemaValidatorService(PathProperties pathProperties) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(pathProperties.getJsonSchema())) {
            if (inputStream == null) {
                log.error("Schema file not found: " + pathProperties.getJsonSchema());
                throw new FileNotFoundException("Schema file not found: " + pathProperties.getJsonSchema());
            }

            JSONObject jsonSchema = new JSONObject(new JSONTokener(inputStream));
            this.schema = SchemaLoader.load(jsonSchema);

        } catch (Exception e) {
            throw new SchemaValidationException("Failed to load JSON schema", e);
        }
    }

    public void validate(String rawJson) {
        try {
            JSONObject json = new JSONObject(rawJson);
            schema.validate(json);
        } catch (ValidationException e) {
            throw new SchemaValidationException("JSON validation error: " + e.getMessage());
        }
    }
}