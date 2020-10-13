package com.aytekin.manytomany.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor

@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;


    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "productList", cascade = CascadeType.ALL)
    private List<Dealer> dealers = new ArrayList<>();

}
