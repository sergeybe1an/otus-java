package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {

    private final static Logger logger = LoggerFactory.getLogger(ResourcesFileLoader.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final File inputFile;

    public ResourcesFileLoader(String fileName) {
        this.inputFile = getFile(fileName);
    }

    @Override
    public List<Measurement> load() {
        try (var bufferedReader = new BufferedReader(new FileReader(inputFile))) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            logger.info("{} file loaded - {}", inputFile.getPath(), sb);
            return mapper.readValue(sb.toString(), new TypeReference<>() {});
        } catch (IOException ioe) {
            logger.error("Error while reading file {} - {}!", inputFile.getPath(), ioe.getMessage());
            return Collections.emptyList();
        }
    }

    private File getFile(String fileName) {
        try {
            URL url = this.getClass().getClassLoader().getResource(fileName);

            if (url == null) throw new URISyntaxException(fileName, "URL is null!");

            return new File(new URI(url.toString()));
        } catch (URISyntaxException e) {
            logger.error("Wrong file name {}!", fileName);
            throw new RuntimeException(e);
        }
    }
}
