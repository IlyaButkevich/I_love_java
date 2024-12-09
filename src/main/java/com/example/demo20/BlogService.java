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

    public List<Blog> searchByName(String keyword) {
        return repo.searchBlogname(keyword);
    }

    public List<Blog> searchByDate(String keyword) {
        return repo.searchBlogdate(keyword);
    }

    public List<Blog> searchByDateAndName(String keyword) {
        return repo.searchBlogdatename(keyword);
    }

    public List<Blog> searchByText(String keyword) {
        return repo.searchBlogtext(keyword);
    }

    public List<Blog> searchByDateAndText(String keyword) {
        return repo.searchBlogdatetext(keyword);
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

    public Blog getBlogById(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new RuntimeException("Post not found with id: " + id)
        );
    }
}