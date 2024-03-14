package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSerializer implements Serializer {

    private final static Logger logger = LoggerFactory.getLogger(FileSerializer.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final File outputFile;

    public FileSerializer(String fileName) {
        this.outputFile = new File(fileName);
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try (var bufferedWriter = new BufferedWriter(new FileWriter(outputFile))) {
            var json = mapper.writeValueAsString(data);
            bufferedWriter.write(json);
            logger.info("{} - written to file - {}", outputFile.getPath(), json);
        } catch (IOException ioe) {
            logger.error("Error while writing in file {} : {}", outputFile.getPath(), ioe.getMessage());
        }
    }
}
