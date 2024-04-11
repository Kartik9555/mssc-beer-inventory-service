package com.example.practice.beer.inventory.service.services;

import com.example.practice.beer.inventory.service.config.JmsConfig;
import com.example.practice.beer.inventory.service.domain.BeerInventory;
import com.example.practice.model.BeerDTO;
import com.example.practice.beer.inventory.service.repositories.BeerInventoryRepository;
import com.example.practice.model.events.NewInventoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewInventoryListener {

    private final BeerInventoryRepository beerInventoryRepository;

    @JmsListener(destination = JmsConfig.NEW_INVENTORY_QUEUE)
    public void listen(NewInventoryEvent newInventoryEvent) {
        BeerDTO beerDTO = newInventoryEvent.getBeerDTO();
        log.debug("Received new inventory event: {}", beerDTO);
        beerInventoryRepository.save(
            BeerInventory.builder()
                .beerId(beerDTO.getId())
                .quantityOnHand(beerDTO.getQuantityOnHand())
                .upc(beerDTO.getUpc())
                .build()
        );
    }
}
