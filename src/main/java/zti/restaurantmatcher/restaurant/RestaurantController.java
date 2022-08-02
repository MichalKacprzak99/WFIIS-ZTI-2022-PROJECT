package zti.restaurantmatcher.restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import zti.restaurantmatcher.user.User;
import zti.restaurantmatcher.user.UserRepository;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;


@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public Restaurant addRestaurant(@RequestBody Restaurant restaurant) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println(currentPrincipalName);
        Optional<User> userRes = userRepository.getUserByEmail(currentPrincipalName);

        if(userRes.isEmpty())
            throw new UsernameNotFoundException("Could not findUser with email = " + currentPrincipalName);
        User user = userRes.get();
        return restaurantService.saveRestaurant(restaurant, user.getId());
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