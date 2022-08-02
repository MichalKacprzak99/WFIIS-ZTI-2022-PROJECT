package zti.restaurantmatcher.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Node
public class Restaurant {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String city;
    private String country;
    private String phone;
    private String cuisine;
}