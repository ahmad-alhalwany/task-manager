package com.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point — like FastAPI's `app = FastAPI()` + `uvicorn.run(...)` combined.
 *
 * @SpringBootApplication turns on:
 *   - component scanning (finds @RestController, @Service, etc. under this package)
 *   - auto-configuration (wires DataSource, Security, Jackson from the classpath)
 */
@SpringBootApplication
public class TaskManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagerApplication.class, args);
    }
}
