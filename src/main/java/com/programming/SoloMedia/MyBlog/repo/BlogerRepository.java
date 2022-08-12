package com.programming.SoloMedia.MyBlog.repo;

import com.programming.SoloMedia.MyBlog.model.Bloger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BlogerRepository extends JpaRepository<Bloger,Long> {

    // we'll define return type of this method as optional of Bloger
    Optional<Bloger> findByUserName(String username);
}

