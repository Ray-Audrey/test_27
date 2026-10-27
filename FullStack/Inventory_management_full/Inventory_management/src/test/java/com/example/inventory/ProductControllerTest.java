package com.example.inventory;

import com.example.inventory.controller.ProductController;
import com.example.inventory.model.Product;
import com.example.inventory.repository.ProductRepository;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductRepository repository;

    private Product product(String id, String name, String category, double price, int qty) {
        Product p = new Product();
        p.setPid(id);
        p.setName(name);
        p.setCategory(category);
        p.setPrice(price);
        p.setQuantity(qty);
        return p;
    }

    private AggregationResults<Document> results(Document... docs) {
        return new AggregationResults<>(List.of(docs), new Document());
    }

    @Test
    void getAllProducts_returnsList() throws Exception {
        when(repository.findAll()).thenReturn(List.of(
                product("P101", "Laptop", "Electronics", 55000, 20),
                product("P102", "Mouse", "Electronics", 500, 150)));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }

    @Test
    void insertProduct_generatesIdWhenMissing() throws Exception {
        when(repository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Desk\",\"category\":\"Furniture\",\"price\":7000,\"quantity\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pid").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Desk"));
    }

    @Test
    void update_changesFields() throws Exception {
        when(repository.findById("P101")).thenReturn(Optional.of(product("P101", "Laptop", "Electronics", 55000, 20)));
        when(repository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(put("/api/products/P101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Laptop Pro\",\"category\":\"Electronics\",\"price\":60000,\"quantity\":18}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop Pro"))
                .andExpect(jsonPath("$.quantity").value(18));
    }

    @Test
    void delete_existingProduct() throws Exception {
        when(repository.existsById("P101")).thenReturn(true);

        mockMvc.perform(delete("/api/products/P101"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product Deleted Successfully"));
        verify(repository).deleteById("P101");
    }

    @Test
    void delete_missingProduct() throws Exception {
        when(repository.existsById("X")).thenReturn(false);

        mockMvc.perform(delete("/api/products/X"))
                .andExpect(content().string("Product not Found"));
        verify(repository, never()).deleteById(any());
    }

    @Test
    void purchasedReport_returnsDocuments() throws Exception {
        when(repository.findPurchasedProducts()).thenReturn(
                results(new Document("name", "Laptop").append("movementType", "Purchase")));

        mockMvc.perform(get("/api/products/purchased"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].movementType").value("Purchase"));
    }

    @Test
    void netMovementReport_returnsDocuments() throws Exception {
        when(repository.calculateNetMovement()).thenReturn(
                results(new Document("name", "Mouse").append("netMovement", 150)));

        mockMvc.perform(get("/api/products/net-movement"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].netMovement").value(150));
    }
}
