package com.aytekin.manytomany.repository;

import com.aytekin.manytomany.entity.Dealer;
import com.aytekin.manytomany.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByDealersIn(List<Dealer> dealers, Pageable pageable);
}
