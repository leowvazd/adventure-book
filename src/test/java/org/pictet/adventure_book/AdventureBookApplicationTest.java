package org.pictet.adventure_book;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

class AdventureBookApplicationTest {

    @Test
    void mainStartsTheSpringApplication() {
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            String[] args = {"--spring.profiles.active=test"};

            AdventureBookApplication.main(args);

            springApplication.verify(() -> SpringApplication.run(eq(AdventureBookApplication.class), eq(args)));
        }
    }
}
