package zti.restaurantmatcher.user;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Get user by id")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        Optional<User> userOpt = userService.getUserById(Long.parseLong(id));
        if (userOpt.isPresent()) {
            return userOpt.get();
        }
        throw new NoSuchElementException("No user found with given id.");
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/email")
    public Optional<User> getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }

    @Operation(summary = "Get all users")
    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAll();
    }


    @Operation(summary = "Delete user with given id")
    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable String id) {
        userService.deleteUser(Long.parseLong(id));
        return "User deleted successfully";
    }

    @Operation(summary = "Get friends of user with given id")
    @GetMapping("/{id}/friends/")
    public Collection<User> getUserFriendships(@PathVariable String id) {
        return userService.getUserFriendships(Long.parseLong(id));
    }

}