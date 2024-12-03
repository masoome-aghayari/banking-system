package ir.azkivaam.banking_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author masoome.aghayari
 * @since 12/1/24
 */

@Configuration
public class ThreadPoolConfig {

    @Bean(name = "transactionExecutor")
    public ExecutorService executorService() {
        return new ThreadPoolExecutor(
                5, 50,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100)
        );
    }

    @Bean(name = "notificationExecutor")
    public ExecutorService notificationExecutorService() {
        return Executors.newFixedThreadPool(5);
    }

}
