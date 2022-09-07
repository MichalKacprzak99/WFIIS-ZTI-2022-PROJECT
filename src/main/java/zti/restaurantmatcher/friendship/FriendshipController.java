package zti.restaurantmatcher.friendship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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

    @PostMapping("/")
    public void addFriendship(@RequestBody Map<String, String> friendshipData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().toString().contains("RESTAURATOR")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        String currentPrincipalName = authentication.getName();

        Optional<User> userRes = userRepository.getUserByEmail(currentPrincipalName);

        if(userRes.isEmpty())
            throw new UsernameNotFoundException("Could not findUser with email = " + currentPrincipalName);
        User user = userRes.get();
        friendshipService.saveFriendship(user.getId(), Long.parseLong(friendshipData.get("userId")));
    }

    @PostMapping(value = "/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean checkFriendship(@RequestParam String userId, @RequestParam String friendId) {
        return friendshipService.checkFriendship(Long.parseLong(userId), Long.parseLong(friendId));
    }

    @DeleteMapping("/")
    public String deleteFriendshipById(@RequestParam String userId, @RequestParam String friendId) {
        friendshipService.deleteFriendship(Long.parseLong(userId), Long.parseLong(friendId));
        return "Friendship deleted successfully";
    }
}