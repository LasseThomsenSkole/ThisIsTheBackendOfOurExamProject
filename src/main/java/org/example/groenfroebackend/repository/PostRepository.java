package org.example.groenfroebackend.repository;

import org.example.groenfroebackend.model.Enums.JobTitle;
import org.example.groenfroebackend.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p FROM Post p WHERE " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) ")
    Page<Post> searchNews(@Param("keyword") String keyword,
                          Pageable pageable);

    List<Post> findNewsByJobTitle(JobTitle jobTitle);
}

