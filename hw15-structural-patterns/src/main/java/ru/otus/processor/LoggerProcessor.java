package ru.otus.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Message;

public record LoggerProcessor(Processor processor) implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(LoggerProcessor.class);

    @Override
    public Message process(Message message) {
        logger.info("log processing message:{}", message);
        return processor.process(message);
    }
}
