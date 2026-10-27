package com.example.inventory;

import com.example.inventory.controller.StockController;
import com.example.inventory.model.Product;
import com.example.inventory.model.StockMovement;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.StockMovementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockController.class)
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StockMovementRepository repository;

    @MockitoBean
    private ProductRepository productRepository;

    private Product product(int qty) {
        Product p = new Product();
        p.setPid("P101");
        p.setName("Laptop");
        p.setQuantity(qty);
        return p;
    }

    @Test
    void sale_reducesStock() throws Exception {
        Product p = product(20);
        when(productRepository.findById("P101")).thenReturn(Optional.of(p));
        when(repository.save(any(StockMovement.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/api/stock").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":\"P101\",\"movementType\":\"Sale\",\"quantity\":5}"))
                .andExpect(status().isOk());
        assertEquals(15, p.getQuantity());
    }

    @Test
    void purchase_increasesStock() throws Exception {
        Product p = product(20);
        when(productRepository.findById("P101")).thenReturn(Optional.of(p));
        when(repository.save(any(StockMovement.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/api/stock").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":\"P101\",\"movementType\":\"Purchase\",\"quantity\":10}"))
                .andExpect(status().isOk());
        assertEquals(30, p.getQuantity());
    }

    @Test
    void sale_moreThanStock_isRejected() throws Exception {
        when(productRepository.findById("P101")).thenReturn(Optional.of(product(3)));

        mockMvc.perform(post("/api/stock").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":\"P101\",\"movementType\":\"Sale\",\"quantity\":5}"))
                .andExpect(status().isBadRequest());
        verify(repository, never()).save(any());
    }

    @Test
    void unknownProduct_isRejected() throws Exception {
        when(productRepository.findById("NOPE")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/stock").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":\"NOPE\",\"movementType\":\"Purchase\",\"quantity\":5}"))
                .andExpect(status().isBadRequest());
    }
}
