package mx.gps.graphqlsqpr;

import mx.gps.graphqlsqpr.user.domain.entities.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends R2dbcRepository<User, Integer> {

}