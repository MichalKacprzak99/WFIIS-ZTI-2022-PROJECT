package zti.restaurantmatcher.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.Collection;

import static java.lang.Math.pow;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Node
public class Restaurant{
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String city;
    private String country;
    private String phone;
    private String cuisine;
    private Collection<Integer> ratings;

    private double compareValue;

    public double getCompareValue(double friendsWeight){
        if (ratings == null) return 0.0;
        compareValue = pow(averageRating() * ratings.size(), friendsWeight);
        return compareValue;
    }
    private double averageRating() {
        if (ratings == null) return 0.0;
        return ratings.stream()
                .mapToDouble(d -> d)
                .average()
                .orElse(0.0);
    }
}
