package com.aytekin.manytomany.controller;

import com.aytekin.manytomany.controller.request.CreateProductRequest;
import com.aytekin.manytomany.dto.ProductDto;
import com.aytekin.manytomany.service.ProductService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ProductController}
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = ProductController.class)
@EnableSpringDataWebSupport
public class ProductControllerTest {

    private static final String URL = "/products";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private final List<ProductDto> productDtoList = new ArrayList<>();
    private final ProductDto productDto = new ProductDto();

    @Before
    public void setUp() {

        productDto.setId(1L);
        productDto.setName("Product");

        ProductDto productDto2 = new ProductDto();
        productDto2.setId(2L);
        productDto2.setName("Product2");

        productDtoList.add(productDto);
        productDtoList.add(productDto2);
    }

    @Test
    public void getAllProducts_test() throws Exception {

        when(productService.getAllProducts(Mockito.any())).thenReturn(
                new PageImpl<>(productDtoList, PageRequest.of(1, 20), 2)
        );

        MvcResult expected = mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        JSONAssert.assertEquals(expected.getResponse().getContentAsString(),
                "{\"content\":[{\"id\":1,\"name\":\"Product\"},{\"id\":2,\"name\":\"Product2\"}],\"pageable\":{\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"offset\":20,\"pageNumber\":1,\"pageSize\":20,\"paged\":true,\"unpaged\":false},\"totalElements\":22,\"last\":true,\"totalPages\":2,\"size\":20,\"number\":1,\"numberOfElements\":2,\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"first\":false,\"empty\":false}",
                false);
    }

    @Test
    public void getProductById_test() throws Exception {

        Mockito.when(productService.getProductById(Mockito.eq(1L))).thenReturn(productDto);

        MvcResult expected = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        JSONAssert.assertEquals(expected.getResponse().getContentAsString(),
                "{\"id\":1,\"name\":\"Product\"}",
                false);
    }

    @Test
    public void createProduct_test() throws Exception {
        CreateProductRequest request = CreateProductRequest.builder().name("product1").build();
        Mockito.when(productService.createProduct(any())).thenReturn(productDto);

        Gson gson = new GsonBuilder().create();

        MvcResult expected = mockMvc.perform(MockMvcRequestBuilders.post(URL).content(gson.toJson(request)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        JSONAssert.assertEquals(expected.getResponse().getContentAsString(),
                "{\"id\":1,\"name\":\"Product\"}",
                false);
    }

    @Test
    public void updateProduct_test() throws Exception {
        productDto.setName("newProduct");
        CreateProductRequest request = CreateProductRequest.builder().name("newProduct").build();
        Mockito.when(productService.updateProduct(any(), any())).thenReturn(productDto);

        Gson gson = new GsonBuilder().create();

        MvcResult expected = mockMvc.perform(MockMvcRequestBuilders.put(URL + "/{id}", "1").content(gson.toJson(request)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        JSONAssert.assertEquals(expected.getResponse().getContentAsString(),
                "{\"id\":1,\"name\":\"newProduct\"}",
                false);
    }

    @Test
    public void deleteProduct_test() throws Exception {

        Mockito.doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", "1").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();


        Mockito.verify(productService).deleteProduct(1L);
    }
}
