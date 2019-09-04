package fr.lacombe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SpringDemoApplication {


    public static void main(String[] args) {
        SpringApplication.run(SpringDemoApplication.class, args);
    }
}
