package com.aytekin.manytomany.service;

import com.aytekin.manytomany.dto.DealerDto;
import com.aytekin.manytomany.dto.ProductDto;
import com.aytekin.manytomany.entity.Dealer;
import com.aytekin.manytomany.entity.Product;
import com.aytekin.manytomany.exception.ResourceNotFoundException;
import com.aytekin.manytomany.repository.DealerRepository;
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
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class DealerServiceTests {

    @InjectMocks
    private DealerService dealerService;

    @Mock
    private DealerRepository dealerRepository;

    @Mock
    private ModelMapper mapper;

    private final List<Dealer> dealerList = new ArrayList<>();
    private final Dealer dealer = new Dealer();
    private final DealerDto dealerDto = new DealerDto();

    @Before
    public void setUp() {

        Product product = new Product();
        product.setId(1L);
        product.setName("product");

        dealer.setId(1L);
        dealer.setName("dealer");
        dealer.getProductList().add(product);

        Dealer dealer2 = new Dealer();
        dealer2.setId(2L);
        dealer2.setName("dealer2");

        dealerList.add(dealer);
        dealerList.add(dealer2);

        ProductDto productDto = ProductDto.builder().id(1L).name("product").build();

        dealerDto.setId(1L);
        dealerDto.setName("dealer");
        dealerDto.getProductList().add(productDto);


        when(mapper.map(dealerDto, Dealer.class)).thenReturn(dealer);
        when(mapper.map(dealer, DealerDto.class)).thenReturn(dealerDto);
        when(mapper.map(productDto, Product.class)).thenReturn(product);
    }

    @Test
    public void getAllDealers_test() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Dealer> pageDealerList = new PageImpl<>(dealerList);

        when(dealerRepository.findAll(pageable)).thenReturn(pageDealerList);

        Page<DealerDto> response = dealerService.getAllDealers(pageable);
        assertEquals(response.getSize(), dealerList.size());
    }

    @Test
    public void getDealerById_test() {

        when(dealerRepository.findById(dealer.getId())).thenReturn(Optional.of(dealer));

        DealerDto response = dealerService.getDealerById(dealer.getId());

        assertEquals(response.getId(), dealer.getId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getDealerByIdNotFoundException_test() {

        when(dealerRepository.findById(dealer.getId())).thenReturn(Optional.of(dealer));

        DealerDto response = dealerService.getDealerById(4L);

        assertEquals(response.getId(), dealer.getId());
    }

    @Test
    public void createDealer_test() {

        when(dealerRepository.save(dealer)).thenReturn(dealer);

        DealerDto response = dealerService.createDealer(dealerDto);

        assertEquals(response.getId(), dealer.getId());
    }


    @Test
    public void updateDealer_test() {

        when(dealerRepository.findById(dealer.getId())).thenReturn(Optional.of(dealer));
        when(dealerRepository.save(dealer)).thenReturn(dealer);

        DealerDto response = dealerService.updateDealer(dealerDto.getId(), dealerDto);

        assertEquals(response.getId(), dealer.getId());
    }

    @Test
    public void deleteDealer_test() {
        when(dealerRepository.findById(dealer.getId())).thenReturn(Optional.of(dealer));

        dealerService.deleteDealer(dealerDto.getId());

        verify(dealerRepository, times(1)).delete(dealer);
    }

    @Test
    public void addProduct_test() {
        ProductDto productDto = ProductDto.builder().id(2L).name("product2").build();
        dealerDto.getProductList().add(productDto);

        when(dealerRepository.findById(dealer.getId())).thenReturn(Optional.of(dealer));

        DealerDto response = dealerService.addProduct(dealer.getId(), productDto);

        assertEquals(response.getProductList().size(), dealer.getProductList().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addProductIllegalArgumentException_test() {
        ProductDto productDto = ProductDto.builder().id(1L).name("product").build();

        when(dealerRepository.findById(dealer.getId())).thenReturn(Optional.of(dealer));

        DealerDto response = dealerService.addProduct(dealer.getId(), productDto);


        assertEquals(response.getProductList().size(), dealer.getProductList().size());
    }

    @Test
    public void removeProduct_test() {
        int size = dealer.getProductList().size();
        ProductDto productDto = ProductDto.builder().id(1L).name("product1").build();
        dealerDto.getProductList().remove(productDto);

        when(dealerRepository.findById(dealer.getId())).thenReturn(Optional.of(dealer));

        dealerService.removeProduct(dealer.getId(), productDto);

        assertEquals(dealer.getProductList().size(), size - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeProductIllegalArgumentException_test() {
        int size = dealer.getProductList().size();
        ProductDto productDto = ProductDto.builder().id(2L).name("product2").build();
        dealerDto.getProductList().remove(productDto);

        when(dealerRepository.findById(dealer.getId())).thenReturn(Optional.of(dealer));

        dealerService.removeProduct(dealer.getId(), productDto);

        assertEquals(dealer.getProductList().size(), size - 1);
    }
}
