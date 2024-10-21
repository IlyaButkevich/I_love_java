package com.example.demo20;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired; //Для сбора классов-бинсов
import org.springframework.stereotype.Service; //Для обнаружения зависимостей

@Service
public class GoodsService {
    @Autowired
    private GoodsRepository repo;

    public List<Goods> ListAll(String keyword) {
        if (keyword != null) {
            return repo.searchGoods(keyword);
        }
        return repo.findAll();
    }
    public void save(Goods goods) {
        repo.save(goods);
    }

    public Goods get(Long id) {
        return repo.findById(id).get();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}