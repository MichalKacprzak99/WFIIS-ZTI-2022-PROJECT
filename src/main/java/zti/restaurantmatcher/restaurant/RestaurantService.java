package zti.restaurantmatcher.restaurant;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<Restaurant> getAll() {
        return restaurantRepository.getAllRestaurants();
    }

    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Optional<Restaurant> getRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }

    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }

    public void deleteAllRestaurants() {
        restaurantRepository.deleteAll();
    }

    public Long getCountOfRestaurants() {
        return restaurantRepository.count();
    }

}