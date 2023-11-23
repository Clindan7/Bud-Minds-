package com.innovaturelabs.buddymanagement.repository;

import com.innovaturelabs.buddymanagement.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface CommentRepository extends Repository<Comment,Integer> {
    Comment save(Comment comment);

    List<Comment> findBySessionSessionId(Integer sessionId, Pageable pageable);

    Integer countBySessionSessionId(Integer sessionId);
}
