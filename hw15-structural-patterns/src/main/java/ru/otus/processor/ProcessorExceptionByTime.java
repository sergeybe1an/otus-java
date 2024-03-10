package ru.otus.processor;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Message;

public class ProcessorExceptionByTime implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(ProcessorExceptionByTime.class);

    @Override
    public Message process(Message message) {
        int currentSecond = LocalDateTime.now().getSecond();

        if (currentSecond % 2 == 0) {
            logger.error("{} second - is even!", currentSecond);
            throw new RuntimeException("Now is even second!");
        }

        return message;
    }
}
