package zti.restaurantmatcher.restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import zti.restaurantmatcher.friendship.FriendshipRepository;
import zti.restaurantmatcher.user.User;
import zti.restaurantmatcher.user.UserRepository;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendshipRepository friendshipRepository;

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

    @PostMapping("/{id}/rate")
    public void rateRestaurant(@PathVariable String id, @RequestBody String rating){
        restaurantService.rateRestaurant(Long.parseLong(id), Integer.parseInt(rating));
    }
    @PostMapping("/match")
    public Collection<Restaurant> matchRestaurant(@RequestBody Map<String, String> userData){

        Boolean areFriends = friendshipRepository.checkFriendship(Long.parseLong(userData.get("firstUserId")), Long.parseLong(userData.get("secondUserId")));
        int areFriendsInt = areFriends ? 1 : 0;
        Collection<User> firstUserFriends = userRepository.getUserFriendships(Long.parseLong(userData.get("firstUserId")));
        Collection<User> secondUserFriends = userRepository.getUserFriendships(Long.parseLong(userData.get("secondUserId")));
        Set<User> commonFriends = firstUserFriends.stream().distinct().filter(secondUserFriends::contains).collect(Collectors.toSet());
        final double friendsWeight;
        double friendsWeight1;
        try{
            friendsWeight1 = areFriendsInt + 2 * commonFriends.size() / (double)(firstUserFriends.size() + secondUserFriends.size());
        }
        catch (ArithmeticException e){
            friendsWeight1 = 1;
        }
        friendsWeight = friendsWeight1;
        List<Restaurant> restaurants = restaurantService.getAll();
        restaurants.sort(Comparator.comparingDouble((Restaurant r) -> r.getCompareValue(friendsWeight)).reversed());
        System.out.println(restaurants);
        return restaurants;
    }
}