package zti.restaurantmatcher.restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;


@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/add")
    public Restaurant addRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantService.saveRestaurant(restaurant);
    }

    @GetMapping("/{id}")
    public Restaurant getRestaurantById(@PathVariable String id) {
        Optional<Restaurant> restaurantOpt = restaurantService.getRestaurantById(Long.parseLong(id));
        if (restaurantOpt.isPresent()) {
            return restaurantOpt.get();
        }
        throw new NoSuchElementException("No restaurant found with given id.");
    }

    @GetMapping
    public Collection<Restaurant> getAllRestaurants() {
        return restaurantService.getAll();
    }

    @GetMapping("/count")
    public Long getCountOfRestaurants() {
        return restaurantService.getCountOfRestaurants();
    }

    @DeleteMapping("/{id}")
    public String deleteRestaurantById(@PathVariable String id) {
        restaurantService.deleteRestaurant(Long.parseLong(id));
        return "Restaurant deleted successfully";
    }

    @DeleteMapping
    public String deleteAllRestaurants() {
        restaurantService.deleteAllRestaurants();
        return "All Restaurants deleted successfully";
    }

}