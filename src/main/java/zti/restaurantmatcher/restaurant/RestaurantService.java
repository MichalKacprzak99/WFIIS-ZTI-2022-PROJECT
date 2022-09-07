package zti.restaurantmatcher.restaurant;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zti.restaurantmatcher.user.User;

import java.util.List;
import java.util.Optional;


@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<Restaurant> getAll() {
        return restaurantRepository.getAllRestaurants();
    }

    public Restaurant saveRestaurant(Restaurant restaurant, Long ownerId) {
        Restaurant createdRestaurant = restaurantRepository.save(restaurant);

        restaurantRepository.setRestaurantOwner(createdRestaurant.getId(), ownerId);
        return createdRestaurant;
    }
    public User getRestaurantOwner(Long restaurantId){
        return restaurantRepository.getRestaurantOwner(restaurantId);
    }


    public Restaurant updateRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Optional<Restaurant> getRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }

    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }

    public Long getCountOfRestaurants() {
        return restaurantRepository.count();
    }

    public void rateRestaurant(Long restaurantId, Integer rating) {
        restaurantRepository.rateRestaurant(restaurantId, rating);
    }

}
