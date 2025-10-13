package org.jacobo.pruebacapitole;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PruebaCapitoleApplication {

    public static void main(String[] args) {
        SpringApplication.run(PruebaCapitoleApplication.class, args);
    }

}
