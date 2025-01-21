package hr.algebra.pi.observers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingObserver implements MaterialObserver {
    private static final Logger logger = LoggerFactory.getLogger(LoggingObserver.class);

    @Override
    public void onMaterialEvent(String event, Object data) {
        logger.info("Event: {}, Data: {}", event, data);
    }
}
