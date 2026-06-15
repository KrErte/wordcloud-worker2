package ee.bitweb.wordcloud_worker2.listener;

import ee.bitweb.wordcloud_worker2.messaging.TextChunkMessage;
import ee.bitweb.wordcloud_worker2.service.WordProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TextChunkListener {

    private static final Logger log = LoggerFactory.getLogger(TextChunkListener.class);

    private final WordProcessingService wordProcessingService;

    public TextChunkListener(WordProcessingService wordProcessingService) {
        this.wordProcessingService = wordProcessingService;
    }

    @RabbitListener(queues = "${wordcloud.rabbitmq.queue}")
    public void onMessage(TextChunkMessage message) {
        log.info("Received job {}", message.jobId());
        wordProcessingService.process(message.jobId(), message.content());
    }
}
