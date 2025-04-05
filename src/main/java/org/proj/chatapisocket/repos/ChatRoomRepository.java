package org.proj.chatapisocket.repos;

import org.proj.chatapisocket.models.ChatRoom;
import org.proj.chatapisocket.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findAllByMembers(Set<User> members);

    @Query("SELECT COUNT(c) > 0 FROM ChatRoom c " +
            "WHERE c.isGroup = false " +
            "AND ( " +
            "   (SELECT COUNT(m) FROM c.members m WHERE m.id IN (:user1Id, :user2Id)) = 2 " +
            "   AND SIZE(c.members) = 2 " +
            ")")
    boolean existsPrivateChatBetweenUsers(@Param("user1Id") Long user1Id,
                                          @Param("user2Id") Long user2Id);
}

