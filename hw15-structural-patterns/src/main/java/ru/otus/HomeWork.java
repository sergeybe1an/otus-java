package ru.otus;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.ProcessorChangeFields;
import ru.otus.processor.ProcessorExceptionByTime;

public class HomeWork {

    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        var processors = List.of(
            new ProcessorChangeFields(),
            new ProcessorExceptionByTime(LocalDateTime::now)
        );

        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var listenerHistory = new HistoryListener();
        complexProcessor.addListener(listenerHistory);

        var objectForMessage = new ObjectForMessage();
        objectForMessage.setData(List.of("one", "two"));

        var message = new Message.Builder(1L)
            .field11("field11")
            .field12("field12")
            .field13(objectForMessage)
            .build();

        listenerHistory.onUpdated(message);
        var result = complexProcessor.handle(message);
        logger.info("result:{}", result);
        logger.info("history:{}", listenerHistory.findMessageById(message.getId()));

        complexProcessor.removeListener(listenerHistory);
    }
}
