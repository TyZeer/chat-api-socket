package org.proj.chatapisocket.repos;

import org.proj.chatapisocket.models.ChatRoom;
import org.proj.chatapisocket.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findAllByMembers(Set<User> members);
}
