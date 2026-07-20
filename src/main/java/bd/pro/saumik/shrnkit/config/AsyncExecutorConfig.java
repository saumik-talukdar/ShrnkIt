package bd.pro.saumik.shrnkit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncExecutorConfig {

    @Bean(name = "analyticsExecutor")
    public Executor analyticsExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(500);

        executor.setThreadNamePrefix("analytics-");

        executor.initialize();

        return executor;
    }

    @Bean(name = "mailExecutor")
    public Executor mailExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(100);

        executor.setThreadNamePrefix("mail-");

        executor.initialize();

        return executor;
    }
}