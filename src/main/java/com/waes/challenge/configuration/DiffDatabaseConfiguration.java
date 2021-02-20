package com.waes.challenge.configuration;

import com.waes.challenge.repositories.DiffRepository;
import com.waes.challenge.repositories.InMemoryDiffRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.waes.challenge.configuration.DiffDatabaseConfiguration.DatabaseType.IN_MEMORY;

@Configuration
public class DiffDatabaseConfiguration {

    enum DatabaseType {
        IN_MEMORY
    }

    /**
     * In case, another database implementation needs to be put in place of the default
     * in memory, a new implementation of {@link DiffRepository} must be provided here
     *
     * @param databaseType
     * @return an implementation of {@link DiffRepository}
     */
    @Bean
    DiffRepository diffDatabase (@Value("${database.type:IN_MEMORY}") DatabaseType databaseType) {

        if (databaseType.equals(IN_MEMORY)) {
            return new InMemoryDiffRepository();
        }
        throw new RuntimeException("Database type not configured");

    }
}
