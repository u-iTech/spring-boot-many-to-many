package com.aytekin.manytomany.controller;


import com.aytekin.manytomany.controller.request.CreateProductRequest;
import com.aytekin.manytomany.dto.ProductDto;
import com.aytekin.manytomany.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/products")
public class ProductController {
    private final ProductService productService;
    private final ModelMapper mapper;

    public ProductController(ProductService productService, ModelMapper mapper) {
        this.productService = productService;
        this.mapper = mapper;
    }

    @GetMapping
    @ApiOperation(value = "Get all products", response = ProductDto.class, responseContainer = "List")
    public ResponseEntity<Page<ProductDto>> getAllProducts(Pageable pageable) {
        // Getting all products...
        final Page<ProductDto> products = productService.getAllProducts(pageable);

        return ResponseEntity.ok(products);
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "get product info by id", response = ProductDto.class)
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        // Getting the requiring product or throwing exception if not found
        final ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    @ApiOperation(value = "create product", response = ProductDto.class)
    public ResponseEntity<ProductDto> createProduct(@RequestBody CreateProductRequest request) {

        // Creating a new product...
        final ProductDto product = productService.createProduct(mapper.map(request, ProductDto.class));

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping(path = "/{id}")
    @ApiOperation(value = "update product", response = ProductDto.class)
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody CreateProductRequest request) {

        // Updating a product...
        ProductDto product = productService.updateProduct(id, mapper.map(request, ProductDto.class));

        return ResponseEntity.ok(product);
    }

    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "delete product")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        // Deleting product
        productService.deleteProduct(id);

        return ResponseEntity.ok().build();
    }
}
