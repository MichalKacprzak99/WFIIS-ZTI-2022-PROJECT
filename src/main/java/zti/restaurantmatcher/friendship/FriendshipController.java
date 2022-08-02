package zti.restaurantmatcher.friendship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/friendships")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @PostMapping("/")
    public void addFriendship(@RequestParam String userId, @RequestParam String friendId) {
        friendshipService.saveFriendship(Long.parseLong(userId), Long.parseLong(friendId));
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