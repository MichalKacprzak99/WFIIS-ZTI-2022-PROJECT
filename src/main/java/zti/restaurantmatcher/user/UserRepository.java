package zti.restaurantmatcher.user;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {

    @Query("MATCH (u:User) RETURN u")
    List<User> getAllUsers();

    @Query("MATCH (u:User) WHERE u.email=$email RETURN u")
    Optional<User> getUserByEmail(String email);

    @Query("MATCH (u1: User) WHERE id(u1)=$id MATCH (u1)--(friend:User) RETURN friend")
    Collection<User> getUserFriendships(Long id);
}