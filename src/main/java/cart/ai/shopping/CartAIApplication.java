package cart.ai.shopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CartAIApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartAIApplication.class, args);
    }

}
