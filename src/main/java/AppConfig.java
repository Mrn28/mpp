import Repository.BiletDBRepository;
import Repository.UserDBRepository;
import Repository.ZborDBRepository;
import Service.BiletService;
import Service.UserService;
import Service.ZborService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class AppConfig {

    @Bean
    public UserService userService() {
        return new UserService(userDBRepository());
    }

    @Bean
    public UserDBRepository userDBRepository() {
        Properties serverProps = loadProperties("bd.config");
        return new UserDBRepository(serverProps);
    }

    @Bean
    public ZborService zborService() {
        return new ZborService(zborDBRepository());
    }

    @Bean
    public ZborDBRepository zborDBRepository() {
        Properties serverProps = loadProperties("bd.config");
        return new ZborDBRepository(serverProps);
    }

    @Bean
    public BiletService biletService() {
        return new BiletService(biletDBRepository());
    }

    @Bean
    public BiletDBRepository biletDBRepository() {
        Properties serverProps = loadProperties("bd.config");
        return new BiletDBRepository(serverProps, zborDBRepository());
    }

    private Properties loadProperties(String filename) {
        Properties props = new Properties();
        try (FileReader reader = new FileReader(filename)) {
            props.load(reader);
            return props;
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file: " + e.getMessage(), e);
        }
    }
}
