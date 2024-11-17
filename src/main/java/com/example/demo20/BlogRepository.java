package com.example.demo20;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    @Query("SELECT p FROM Blog p WHERE CONCAT(p.name, '', p.id, '', p.user, '', p.text, '', p.date) LIKE %?1%")
    List<Blog> searchBlogall(String keyword);

    @Query("SELECT p FROM Blog p WHERE CONCAT(p.name, '') LIKE %?1%")
    List<Blog> searchBlogname(String keyword);

    @Query("SELECT p FROM Blog p WHERE CONCAT(p.date, '') LIKE %?1%")
    List<Blog> searchBlogdate(String keyword);

    @Query("SELECT p FROM Blog p WHERE CONCAT(p.name, '', p.date) LIKE %?1%")
    List<Blog> searchBlogdatename(String keyword);

    @Query("SELECT p FROM Blog p WHERE CONCAT(p.text, '') LIKE %?1%")
    List<Blog> searchBlogtext(String keyword);

    @Query("SELECT p FROM Blog p WHERE CONCAT(p.text, '', p.date) LIKE %?1%")
    List<Blog> searchBlogdatetext(String keyword);

}
