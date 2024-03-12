package ru.otus.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Message;

public class ProcessorExceptionByTime implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(ProcessorExceptionByTime.class);
    private final DateTimeProvider dateTimeProvider;

    public ProcessorExceptionByTime(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (isEvenSecond()) {
            logger.error("current second - is even!");
            throw new RuntimeException("current second - is even!");
        }

        return message;
    }

    private boolean isEvenSecond() {
        return dateTimeProvider.getDateTime().getSecond() % 2 == 0;
    }
}
