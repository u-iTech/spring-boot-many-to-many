package com.aytekin.manytomany.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;

    @JsonBackReference
    private List<DealerDto> dealers = new ArrayList<>();
}
