package com.example.demo20;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyAppUserRepository extends JpaRepository<Myappuser, Long>{

    Optional<Myappuser> findByUsername(String username);

}