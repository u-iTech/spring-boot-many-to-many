package com.aytekin.manytomany.service;

import com.aytekin.manytomany.dto.DealerDto;
import com.aytekin.manytomany.dto.ProductDto;
import com.aytekin.manytomany.entity.Dealer;
import com.aytekin.manytomany.entity.Product;
import com.aytekin.manytomany.exception.ResourceNotFoundException;
import com.aytekin.manytomany.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper mapper;

    private final List<Product> productList = new ArrayList<>();
    private final Product product = new Product();
    private final ProductDto productDto = new ProductDto();
    private final DealerDto dealerDto = new DealerDto();

    @Before
    public void setUp() {

        product.setId(1L);
        product.setName("product");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("product2");

        productList.add(product);
        productList.add(product2);

        productDto.setId(1L);
        productDto.setName("product");

        dealerDto.setId(1L);
        dealerDto.setName("dealer");


        when(mapper.map(productDto, Product.class)).thenReturn(product);
        when(mapper.map(product, ProductDto.class)).thenReturn(productDto);
    }

    @Test
    public void getAllProducts_test() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> pageProductList = new PageImpl<>(productList);

        when(productRepository.findAll(pageable)).thenReturn(pageProductList);

        Page<ProductDto> response = productService.getAllProducts(pageable);
        assertEquals(response.getSize(), productList.size());
    }


    @Test
    public void getAllProductsByDealer_test() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> pageProductList = new PageImpl<>(productList);

        when(productRepository.findByDealersIn(Collections.singletonList(mapper.map(dealerDto, Dealer.class)), pageable)).thenReturn(pageProductList);

        Page<ProductDto> response = productService.getAllProducts(dealerDto, pageable);

        assertEquals(response.getSize(), productList.size());
    }

    @Test
    public void getProductById_test() {

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        ProductDto response = productService.getProductById(product.getId());

        assertEquals(response.getId(), product.getId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getProductByIdNotFoundException_test() {

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        ProductDto response = productService.getProductById(4L);

        assertEquals(response.getId(), product.getId());
    }

    @Test
    public void createProduct_test() {

        when(productRepository.save(product)).thenReturn(product);

        ProductDto response = productService.createProduct(productDto);

        assertEquals(response.getId(), product.getId());
    }


    @Test
    public void updateProduct_test() {

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        ProductDto response = productService.updateProduct(productDto.getId(), productDto);

        assertEquals(response.getId(), product.getId());
    }

    @Test
    public void deleteProduct_test() {
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        productService.deleteProduct(productDto.getId());

        verify(productRepository, times(1)).delete(product);
    }
}
