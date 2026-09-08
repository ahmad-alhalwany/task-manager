package com.taskmanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Smoke test: if the Spring application context fails to start
 * (missing bean, bad config, circular dependency), this test fails.
 */
@SpringBootTest
class TaskManagerApplicationTests {

    @Test
    void contextLoads() {
        // Intentionally empty — success means the context wired correctly
    }
}
