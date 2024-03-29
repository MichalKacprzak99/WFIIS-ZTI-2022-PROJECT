package zti.restaurantmatcher.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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


    @Operation(summary = "Update restaurant by id")
    @PutMapping("/")
    public Restaurant updateRestaurant(@RequestBody Restaurant restaurant) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.getAuthorities().toString().contains("RESTAURATOR")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return restaurantService.updateRestaurant(restaurant);
    }


    @Operation(summary = "Add new restaurant to database")
    @PostMapping("/")
    public Restaurant addRestaurant(@RequestBody Restaurant restaurant) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.getAuthorities().toString().contains("RESTAURATOR")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        String currentPrincipalName = authentication.getName();

        Optional<User> userRes = userRepository.getUserByEmail(currentPrincipalName);

        if(userRes.isEmpty())
            throw new UsernameNotFoundException("Could not findUser with email = " + currentPrincipalName);
        User user = userRes.get();
        return restaurantService.saveRestaurant(restaurant, user.getId());
    }

    @Operation(summary = "Get restaurant by id")
    @GetMapping("/{id}")
    public Restaurant getRestaurantById(@PathVariable String id) {
        Optional<Restaurant> restaurantOpt = restaurantService.getRestaurantById(Long.parseLong(id));
        if (restaurantOpt.isPresent()) {
            return restaurantOpt.get();
        }
        throw new NoSuchElementException("No restaurant found with given id.");
    }

    @Operation(summary = "Get all restaurants")
    @GetMapping
    public Collection<Restaurant> getAllRestaurants() {return restaurantService.getAll();}


    @Operation(summary = "Delete restaurant with given id")
    @DeleteMapping("/{id}")
    public String deleteRestaurantById(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().toString().contains("RESTAURATOR")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        String currentPrincipalName = authentication.getName();

        Optional<User> userRes = userRepository.getUserByEmail(currentPrincipalName);

        if(userRes.isEmpty())
            throw new UsernameNotFoundException("Could not findUser with email = " + currentPrincipalName);
        User user = userRes.get();
        User restauratorOwner = restaurantService.getRestaurantOwner(Long.valueOf(id));
        if(!Objects.equals(user.getId(), restauratorOwner.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        restaurantService.deleteRestaurant(Long.parseLong(id));
        return "Restaurant deleted successfully";
    }

    @Operation(summary = "Rate restaurant given by id")
    @PostMapping("/{id}/rate")
    public void rateRestaurant(@PathVariable String id, @RequestBody Map<String, String> rateData){
        restaurantService.rateRestaurant(Long.parseLong(id), Integer.parseInt(rateData.get("rating")));
    }
    @Operation(summary = "Get a list of recommended restaurants in order from most to least recommended")
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
        return restaurants;
    }
}