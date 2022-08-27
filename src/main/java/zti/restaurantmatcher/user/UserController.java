package zti.restaurantmatcher.user;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        Optional<User> userOpt = userService.getUserById(Long.parseLong(id));
        if (userOpt.isPresent()) {
            return userOpt.get();
        }
        throw new NoSuchElementException("No user found with given id.");
    }
    @GetMapping("/email")
    public Optional<User> getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/count")
    public Long getCountOfUsers() {
        return userService.getCountOfUsers();
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable String id) {
        userService.deleteUser(Long.parseLong(id));
        return "User deleted successfully";
    }

    @GetMapping("/{id}/friends/")
    public Collection<User> getUserFriendships(@PathVariable String id) {
        return userService.getUserFriendships(Long.parseLong(id));
    }

}