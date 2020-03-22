package pretty.april.achieveitserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
public class AchieveitServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AchieveitServerApplication.class, args);
    }

}
