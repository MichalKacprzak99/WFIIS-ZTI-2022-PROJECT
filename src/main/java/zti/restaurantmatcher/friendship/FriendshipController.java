package zti.restaurantmatcher.friendship;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import zti.restaurantmatcher.user.User;
import zti.restaurantmatcher.user.UserRepository;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/friendships")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Add new friendship to database")
    @PostMapping("/")
    public void addFriendship(@RequestBody Map<String, String> friendshipData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Optional<User> userRes = userRepository.getUserByEmail(currentPrincipalName);

        if(userRes.isEmpty())
            throw new UsernameNotFoundException("Could not findUser with email = " + currentPrincipalName);
        User user = userRes.get();
        friendshipService.saveFriendship(user.getId(), Long.parseLong(friendshipData.get("userId")));
    }

    @Operation(summary = "Check if two users are friends")
    @PostMapping(value = "/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean checkFriendship(@RequestParam String userId, @RequestParam String friendId) {
        return friendshipService.checkFriendship(Long.parseLong(userId), Long.parseLong(friendId));
    }

    @Operation(summary = "Delete friendships")
    @DeleteMapping("/")
    public String deleteFriendshipById(@RequestParam String userId, @RequestParam String friendId) {
        friendshipService.deleteFriendship(Long.parseLong(userId), Long.parseLong(friendId));
        return "Friendship deleted successfully";
    }
}