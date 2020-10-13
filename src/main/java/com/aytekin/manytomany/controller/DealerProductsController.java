package com.aytekin.manytomany.controller;


import com.aytekin.manytomany.dto.DealerDto;
import com.aytekin.manytomany.dto.ProductDto;
import com.aytekin.manytomany.service.DealerService;
import com.aytekin.manytomany.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/dealers/{dealerId}/products")
public class DealerProductsController {

    private final ProductService productService;
    private final DealerService dealerService;

    public DealerProductsController(ProductService productService, DealerService dealerService) {
        this.productService = productService;
        this.dealerService = dealerService;
    }

    @GetMapping
    @ApiOperation(value = "Get all products of dealer", response = ProductDto.class, responseContainer = "List")
    public ResponseEntity<Page<ProductDto>> getAllProducts(@PathVariable Long dealerId, Pageable pageable) {

        DealerDto dealer = dealerService.getDealerById(dealerId);
        // Getting all products in application...
        final Page<ProductDto> products = productService.getAllProducts(dealer, pageable);

        return ResponseEntity.ok(products);
    }

    @PostMapping(path = "/{productId}")
    @ApiOperation(value = "Add product to dealer", response = DealerDto.class)
    public ResponseEntity<DealerDto> addProduct(@PathVariable Long dealerId, @PathVariable Long productId) {

        final ProductDto product = productService.getProductById(productId);

        // Associating product with dealer...
        DealerDto dealer = dealerService.addProduct(dealerId, product);

        return ResponseEntity.status(HttpStatus.CREATED).body(dealer);
    }

    @DeleteMapping(path = "/{productId}")
    @ApiOperation(value = "remove product to dealer")
    public ResponseEntity<Void> removeProduct(@PathVariable Long dealerId, @PathVariable Long productId) {

        final ProductDto product = productService.getProductById(productId);

        // Dis-associating product with dealer...
        dealerService.removeProduct(dealerId, product);

        return ResponseEntity.ok().build();
    }
}
