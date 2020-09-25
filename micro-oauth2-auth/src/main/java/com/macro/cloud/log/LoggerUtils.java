package com.macro.cloud.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author xtqb
 * @version v 1.0
 * @date 2020/7/10 0010 9:18
 * @description：
 */
@Component
public class LoggerUtils {
    @Value(value = "${spring.logger.isdebug}")
    private boolean isDebug;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void info(String message) {
        if (isDebug) {
            logger.info("message >>> {}", message);
        }
    }

    public void error(String message) {
        if (isDebug) {
            logger.error("message >>> {}", message);
        }
    }
}
