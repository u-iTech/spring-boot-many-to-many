package com.aytekin.manytomany.service;

import com.aytekin.manytomany.dto.DealerDto;
import com.aytekin.manytomany.dto.ProductDto;
import com.aytekin.manytomany.entity.Dealer;
import com.aytekin.manytomany.entity.Product;
import com.aytekin.manytomany.exception.ResourceNotFoundException;
import com.aytekin.manytomany.repository.DealerRepository;
import com.aytekin.manytomany.util.MapperUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DealerService {

    private final DealerRepository dealerRepository;
    private final ModelMapper mapper;

    public DealerService(DealerRepository dealerRepository, ModelMapper mapper) {
        this.dealerRepository = dealerRepository;
        this.mapper = mapper;
    }

    public Page<DealerDto> getAllDealers(Pageable pageable) {

        Page<Dealer> dealers = dealerRepository.findAll(pageable);

        return MapperUtil.mapEntityPageIntoDtoPage(dealers, DealerDto.class);
    }

    public DealerDto getDealerById(Long id) {
        return mapper.map(getDealer(id), DealerDto.class);
    }

    private Dealer getDealer(Long id) {
        // Getting the requiring dealer or throwing exception if not found
        return dealerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dealer not found."));
    }

    public DealerDto createDealer(DealerDto dealerDto) {
        Dealer dealer = mapper.map(dealerDto, Dealer.class);
        dealerRepository.save(dealer);
        return mapper.map(dealer, DealerDto.class);
    }

    public DealerDto updateDealer(Long id, DealerDto dealerDto) {
        Dealer dealer = getDealer(id);
        dealer.setName(dealerDto.getName());
        dealerRepository.save(dealer);
        return mapper.map(dealer, DealerDto.class);
    }

    public void deleteDealer(Long id) {
        dealerRepository.delete(getDealer(id));
    }

    public boolean hasProduct(Dealer dealer, Long productId) {
        return dealer.getProductList().stream().anyMatch(x -> x.getId().equals(productId));
    }

    public DealerDto addProduct(Long dealerId, ProductDto productDto) {

        Dealer dealer = getDealer(dealerId);

        // Validating if association does not exist...
        if (hasProduct(dealer, productDto.getId())) {
            throw new IllegalArgumentException("dealer " + dealer.getId() + " already contains product " + productDto.getId());
        }

        Product product = mapper.map(productDto, Product.class);

        dealer.getProductList().add(product);
        dealerRepository.save(dealer);

        return mapper.map(dealer, DealerDto.class);
    }

    public void removeProduct(Long dealerId, ProductDto productDto) {

        Dealer dealer = getDealer(dealerId);

        // Validating if association does not exist...
        if (!hasProduct(dealer, productDto.getId())) {
            throw new IllegalArgumentException("dealer " + dealer.getId() + " does not contain product " + productDto.getId());
        }

        dealer.getProductList().removeIf(x -> x.getId().equals(productDto.getId()));
        dealerRepository.save(dealer);
    }
}
