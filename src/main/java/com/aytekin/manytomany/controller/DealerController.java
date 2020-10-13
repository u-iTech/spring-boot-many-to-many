package com.aytekin.manytomany.controller;

import com.aytekin.manytomany.controller.request.CreateDealerRequest;
import com.aytekin.manytomany.dto.DealerDto;
import com.aytekin.manytomany.service.DealerService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/dealers")
public class DealerController {

    private final DealerService dealerService;
    private final ModelMapper mapper;

    public DealerController(DealerService dealerService, ModelMapper mapper) {
        this.dealerService = dealerService;
        this.mapper = mapper;
    }

    @GetMapping
    @ApiOperation(value = "Get all dealers", response = DealerDto.class, responseContainer = "List")
    public ResponseEntity<Page<DealerDto>> getAllDealers(Pageable pageable) {
        // Getting all dealers...
        final Page<DealerDto> dealers = dealerService.getAllDealers(pageable);

        return ResponseEntity.ok(dealers);
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "get dealer info by id", response = DealerDto.class)
    public ResponseEntity<DealerDto> getDealerById(@PathVariable Long id) {
        // Getting the requiring dealer or throwing exception if not found
        final DealerDto dealer = dealerService.getDealerById(id);
        return ResponseEntity.ok(dealer);
    }

    @PostMapping
    @ApiOperation(value = "create Dealer", response = DealerDto.class)
    public ResponseEntity<DealerDto> createDealer(@RequestBody CreateDealerRequest request) {

        // Creating a new dealer...
        final DealerDto dealer = dealerService.createDealer(mapper.map(request, DealerDto.class));

        return ResponseEntity.status(HttpStatus.CREATED).body(dealer);
    }

    @PutMapping(path = "/{id}")
    @ApiOperation(value = "update Dealer", response = DealerDto.class)
    public ResponseEntity<DealerDto> updateDealer(@PathVariable Long id, @RequestBody CreateDealerRequest request) {

        // Updating a dealer...
        DealerDto dealer = dealerService.updateDealer(id, mapper.map(request, DealerDto.class));

        return ResponseEntity.ok(dealer);
    }

    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "delete Dealer", response = DealerDto.class)
    public ResponseEntity<Void> deleteDealer(@PathVariable Long id) {

        // Deleting dealer
        dealerService.deleteDealer(id);

        return ResponseEntity.ok().build();
    }
}
