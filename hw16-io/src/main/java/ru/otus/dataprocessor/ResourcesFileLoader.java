package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {

    private final static Logger logger = LoggerFactory.getLogger(ResourcesFileLoader.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        try (var bufferedReader = new BufferedReader(new InputStreamReader(getFileAsIS(fileName)))) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            logger.info("{} file loaded - {}", fileName, sb);
            return mapper.readValue(sb.toString(), new TypeReference<>() {});
        } catch (IOException ioe) {
            logger.error("Error while reading file {} - {}!", fileName, ioe.getMessage());
            return Collections.emptyList();
        }
    }

    private InputStream getFileAsIS(String fileName) {
        return this.getClass().getClassLoader().getResourceAsStream(fileName);
    }
}
