package ru.otus.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.processor.ProcessorExceptionByTime;

public class ExceptionProcessorTest {

    @Test
    @DisplayName("Тестируем процессор с четной секундой")
    void evenSecondTest() {
        // given
        var message = new Message.Builder(1L).build();

        var listener = mock(Listener.class);

        var complexProcessor = new ComplexProcessor(List.of(new ProcessorExceptionByTime()), (ex) -> {});

        complexProcessor.addListener(listener);

        // when
        complexProcessor.handle(message);
        complexProcessor.removeListener(listener);
        complexProcessor.handle(message);

        // then
        verify(listener, times(1)).onUpdated(message);
    }
}
