package cart.ai.shopping.infrastructure.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
public class KafkaErrorHandlerConfig {

    @Bean
    public CommonErrorHandler errorHandler(KafkaTemplate<Object, Object> template) {
        log.warn("Handling error for topic {}", template.getDefaultTopic());

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template);

        return new DefaultErrorHandler(recoverer, new FixedBackOff(2000L, 2));
    }
}
