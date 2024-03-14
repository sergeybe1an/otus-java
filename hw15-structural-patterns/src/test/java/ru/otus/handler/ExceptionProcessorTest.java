package ru.otus.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.DateTimeProvider;
import ru.otus.processor.ProcessorExceptionByTime;

public class ExceptionProcessorTest {

    @Test
    @DisplayName("Тестируем процессор с четной секундой")
    void evenSecondTest() {
        var dateTimeProvider = mock(DateTimeProvider.class);
        var message = new Message.Builder(1L).build();
        var processor = new ProcessorExceptionByTime(dateTimeProvider);

        when(dateTimeProvider.getDateTime())
            .thenReturn(LocalDateTime.ofEpochSecond(2, 0, ZoneOffset.UTC));

        assertThatExceptionOfType(RuntimeException.class)
            .isThrownBy(() -> processor.process(message));
    }

    @Test
    @DisplayName("Тестируем процессор с нечетной секундой")
    void oddSecondTest() {
        var dateTimeProvider = mock(DateTimeProvider.class);
        var message = new Message.Builder(1L).build();
        var processor = new ProcessorExceptionByTime(dateTimeProvider);

        when(dateTimeProvider.getDateTime())
            .thenReturn(LocalDateTime.ofEpochSecond(3, 0, ZoneOffset.UTC));

        var result = processor.process(message);

        assertThat(result).isEqualTo(message);
    }
}
