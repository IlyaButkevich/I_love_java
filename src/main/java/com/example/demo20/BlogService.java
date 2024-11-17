package com.example.demo20;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    @Autowired
    private BlogRepository repo;

    public List<Blog> ListAll(String keyword) {
        if (keyword != null) {
            return repo.searchBlogall(keyword);
        }
        return repo.findAll();
    }
    public void save(Blog blog) {
        repo.save(blog);
    }

    public Blog get(Long id) {
        return repo.findById(id).get();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
