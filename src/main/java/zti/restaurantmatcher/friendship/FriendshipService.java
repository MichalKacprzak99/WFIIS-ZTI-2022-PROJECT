package zti.restaurantmatcher.friendship;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;

@Service
public class FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    public void saveFriendship(Long userId, Long friendId) {
        friendshipRepository.createFriendship(userId, friendId);
    }

    public Boolean checkFriendship(Long userId, Long friendId) {
        return friendshipRepository.checkFriendship(userId, friendId);
    }

    public void deleteFriendship(Long userId, Long friendId) {
        friendshipRepository.deleteFriendship(userId, friendId);
    }

}
