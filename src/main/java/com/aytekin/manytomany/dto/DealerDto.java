package com.aytekin.manytomany.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DealerDto {
    private Long id;
    private String name;

    @JsonManagedReference
    private List<ProductDto> productList = new ArrayList<>();
}
