package com.aytekin.manytomany.controller;

import com.aytekin.manytomany.controller.request.CreateDealerRequest;
import com.aytekin.manytomany.dto.DealerDto;
import com.aytekin.manytomany.service.DealerService;
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
 * Unit tests for {@link DealerController}
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = DealerController.class)
@EnableSpringDataWebSupport
public class DealerControllerTest {

    private static final String URL = "/dealers";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DealerService dealerService;

    private final List<DealerDto> dealerDtoList = new ArrayList<>();
    private final DealerDto dealerDto = new DealerDto();

    @Before
    public void setUp() {

        dealerDto.setId(1L);
        dealerDto.setName("Dealer");

        DealerDto dealerDto2 = new DealerDto();
        dealerDto2.setId(2L);
        dealerDto2.setName("Dealer2");

        dealerDtoList.add(dealerDto);
        dealerDtoList.add(dealerDto2);
    }

    @Test
    public void getAllDealers_test() throws Exception {

        when(dealerService.getAllDealers(Mockito.any())).thenReturn(
                new PageImpl<>(dealerDtoList, PageRequest.of(1, 20), 2)
        );

        MvcResult expected = mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        JSONAssert.assertEquals(expected.getResponse().getContentAsString(),
                "{\"content\":[{\"id\":1,\"name\":\"Dealer\",\"productList\":[]},{\"id\":2,\"name\":\"Dealer2\",\"productList\":[]}],\"pageable\":{\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"offset\":20,\"pageSize\":20,\"pageNumber\":1,\"unpaged\":false,\"paged\":true},\"last\":true,\"totalPages\":2,\"totalElements\":22,\"size\":20,\"number\":1,\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"numberOfElements\":2,\"first\":false,\"empty\":false}",
                false);
    }

    @Test
    public void getDealerById_test() throws Exception {

        Mockito.when(dealerService.getDealerById(Mockito.eq(1L))).thenReturn(dealerDto);

        MvcResult expected = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        JSONAssert.assertEquals(expected.getResponse().getContentAsString(),
                "{\"id\":1,\"name\":\"Dealer\",\"productList\":[]}",
                false);
    }

    @Test
    public void createDealer_test() throws Exception {
        CreateDealerRequest request = CreateDealerRequest.builder().name("dealer1").build();
        Mockito.when(dealerService.createDealer(any())).thenReturn(dealerDto);

        Gson gson = new GsonBuilder().create();

        MvcResult expected = mockMvc.perform(MockMvcRequestBuilders.post(URL).content(gson.toJson(request)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        JSONAssert.assertEquals(expected.getResponse().getContentAsString(),
                "{\"id\":1,\"name\":\"Dealer\",\"productList\":[]}",
                false);
    }

    @Test
    public void updateDealer_test() throws Exception {
        dealerDto.setName("newDealer");
        CreateDealerRequest request = CreateDealerRequest.builder().name("newDealer").build();
        Mockito.when(dealerService.updateDealer(any(), any())).thenReturn(dealerDto);

        Gson gson = new GsonBuilder().create();

        MvcResult expected = mockMvc.perform(MockMvcRequestBuilders.put(URL + "/{id}", "1").content(gson.toJson(request)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        JSONAssert.assertEquals(expected.getResponse().getContentAsString(),
                "{\"id\":1,\"name\":\"newDealer\",\"productList\":[]}",
                false);
    }

    @Test
    public void deleteDealer_test() throws Exception {

        Mockito.doNothing().when(dealerService).deleteDealer(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", "1").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();


        Mockito.verify(dealerService).deleteDealer(1L);
    }
}
