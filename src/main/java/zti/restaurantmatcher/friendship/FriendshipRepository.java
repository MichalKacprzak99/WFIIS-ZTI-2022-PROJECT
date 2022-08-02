package zti.restaurantmatcher.friendship;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import zti.restaurantmatcher.user.User;

@Repository
public interface FriendshipRepository extends Neo4jRepository<User, Long> {

    @Query("MATCH (u1: User) WHERE id(u1) = $userId MATCH (u2: User) WHERE id(u2) = $friendId CREATE (u1)-[rel: FRIEND_WITH]->(u2)")
    void createFriendship(Long userId, Long friendId);

    @Query("MATCH (u1: User) WHERE id(u1) = $userId MATCH (u2: User) WHERE id(u2) = $friendId RETURN EXISTS( (u1)-[:FRIEND_WITH]-(u2) )")
    Boolean checkFriendship(Long userId, Long friendId);

    @Query("MATCH (u1: User) WHERE id(u1) = $userId MATCH (u2: User) WHERE id(u2) = $friendId MATCH (u1)-[rel:FRIEND_WITH]-(u2) DETACH DELETE rel")
    void deleteFriendship(Long userId, Long friendId);
}