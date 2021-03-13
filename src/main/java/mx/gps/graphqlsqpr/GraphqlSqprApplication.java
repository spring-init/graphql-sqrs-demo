package mx.gps.graphqlsqpr;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GraphqlSqprApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlSqprApplication.class, args);
    }


    @Bean(initMethod = "migrate")
    Flyway flyway() {
//        String url = "jdbc:" + env.getRequiredProperty("db.url");
//        String user = env.getRequiredProperty("db.user");
//        String password = env.getRequiredProperty("db.password")
        FluentConfiguration config = Flyway
                .configure()
                .locations("classpath:/db/migration/v_alfa")
                .dataSource("jdbc:h2:file:~/testdb2;USER=sa", "sa", "password");
        return new Flyway(config);
    }

}
