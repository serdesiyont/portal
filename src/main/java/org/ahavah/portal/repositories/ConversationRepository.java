package org.ahavah.portal.repositories;

import java.util.List;

import org.ahavah.portal.entities.Conversation;
import org.ahavah.portal.entities.User;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> findByUserOrderByCreatedAtDesc(User user, Limit limit);
}
