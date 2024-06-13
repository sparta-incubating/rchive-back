package kr.sparta.rchive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class RchiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(RchiveApplication.class, args);
    }

}
