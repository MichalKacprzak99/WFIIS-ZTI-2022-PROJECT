package zti.restaurantmatcher.restaurant;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RestaurantRepository extends Neo4jRepository<Restaurant, Long> {

    @Query("MATCH (r:Restaurant) RETURN r")
    List<Restaurant> getAllRestaurants();
    @Query("MATCH (r:Restaurant) WHERE id(r)= $restaurantId MATCH (u:User) WHERE id(u)=$ownerId CREATE (u)-[rel: OWNER_OF]->(r)")
    void setRestaurantOwner(Long restaurantId, Long ownerId);
}