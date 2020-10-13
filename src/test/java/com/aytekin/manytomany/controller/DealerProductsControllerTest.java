package com.aytekin.manytomany.controller;

import com.aytekin.manytomany.dto.DealerDto;
import com.aytekin.manytomany.dto.ProductDto;
import com.aytekin.manytomany.service.DealerService;
import com.aytekin.manytomany.service.ProductService;
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
 * Unit tests for {@link DealerController}
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = DealerProductsController.class)
@EnableSpringDataWebSupport
public class DealerProductsControllerTest {

    private static final String URL = "/dealers/{dealerId}/products";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DealerService dealerService;

    @MockBean
    private ProductService productService;

    private final List<ProductDto> productDtoList = new ArrayList<>();
    private final DealerDto dealerDto = new DealerDto();
    private final ProductDto productDto = new ProductDto();

    @Before
    public void setUp() {

        dealerDto.setId(1L);
        dealerDto.setName("Dealer");

        productDto.setId(1L);
        productDto.setName("Product");

        ProductDto productDto2 = new ProductDto();
        productDto2.setId(2L);
        productDto2.setName("Product2");

        dealerDto.getProductList().add(productDto);
        productDtoList.add(productDto);
        productDtoList.add(productDto2);
    }

    @Test
    public void getAllProducts_test() throws Exception {
        when(dealerService.getDealerById(dealerDto.getId())).thenReturn(dealerDto);
        when(productService.getAllProducts(any(), any())).thenReturn(
                new PageImpl<>(productDtoList, PageRequest.of(1, 20), 2)
        );

        MvcResult expected = mockMvc.perform(MockMvcRequestBuilders.get(URL, "1").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        JSONAssert.assertEquals(expected.getResponse().getContentAsString(),
                "{\"content\":[{\"id\":1,\"name\":\"Product\"},{\"id\":2,\"name\":\"Product2\"}],\"pageable\":{\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"offset\":20,\"pageSize\":20,\"pageNumber\":1,\"unpaged\":false,\"paged\":true},\"totalPages\":2,\"totalElements\":22,\"last\":true,\"size\":20,\"number\":1,\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"numberOfElements\":2,\"first\":false,\"empty\":false}",
                false);
    }

    @Test
    public void addProduct_test() throws Exception {
        Mockito.when(productService.getProductById(productDto.getId())).thenReturn(productDto);
        Mockito.when(dealerService.addProduct(any(), any())).thenReturn(dealerDto);

        MvcResult expected = mockMvc.perform(MockMvcRequestBuilders.post(URL + "/{productId}", "1", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        JSONAssert.assertEquals(expected.getResponse().getContentAsString(),
                "{\"id\":1,\"name\":\"Dealer\",\"productList\":[{\"id\":1,\"name\":\"Product\"}]}",
                false);
    }

    @Test
    public void removeProduct_test() throws Exception {
        Mockito.when(productService.getProductById(productDto.getId())).thenReturn(productDto);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{productId}", "1", "1").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();


        Mockito.verify(dealerService).removeProduct(1L, productDto);

    }
}
