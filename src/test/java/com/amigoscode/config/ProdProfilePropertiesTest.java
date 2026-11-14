package com.amigoscode.config;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Guards the prod profile against hard-coded credentials. Each value must be
 * an env var placeholder with no default, so a missing value stops startup.
 */
class ProdProfilePropertiesTest {

    private static Properties prodProperties() throws IOException {
        return PropertiesLoaderUtils.loadProperties(
                new ClassPathResource("application-prod.properties"));
    }

    @Test
    void datasourceSettingsComeFromEnvironmentWithoutDefaults() throws IOException {
        Properties props = prodProperties();

        assertThat(props.getProperty("spring.datasource.url"))
                .isEqualTo("${SPRING_DATASOURCE_URL}");
        assertThat(props.getProperty("spring.datasource.username"))
                .isEqualTo("${SPRING_DATASOURCE_USERNAME}");
        assertThat(props.getProperty("spring.datasource.password"))
                .isEqualTo("${SPRING_DATASOURCE_PASSWORD}");
    }

    @Test
    void awsCredentialsAreNotSetInTheFile() throws IOException {
        Properties props = prodProperties();

        assertThat(props).doesNotContainKey("aws.access-key-id");
        assertThat(props).doesNotContainKey("aws.secret-access-key");
    }
}
