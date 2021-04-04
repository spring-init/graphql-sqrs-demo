package mx.gps.graphqlsqpr.user.domain.entities

import io.leangen.graphql.annotations.GraphQLQuery

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

//@Getter
//@Setter
@Entity
class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.annotation.Id
    @GraphQLQuery(name = "id", description = "A User's id")
    Integer id
    @GraphQLQuery(name = "name", description = "A User's name")
    String name
}
