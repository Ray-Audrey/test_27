# Inventory Management - Spring Boot backend

Requirements: Java 17+, MongoDB running on `localhost:27017`.

```
./mvnw spring-boot:run        (Windows: mvnw.cmd spring-boot:run)
```

Runs on http://localhost:8080. Database `Inventory` (collections `Products`, `StockMovements`).
Sample data is added automatically on first run.

## API
| Method | Path | What |
|---|---|---|
| GET/POST | /api/products | list / add product |
| GET/PUT/DELETE | /api/products/{id} | one product |
| GET/POST | /api/stock | list / record stock movement (updates product quantity) |
| GET | /api/products/purchased | report 1 |
| GET | /api/products/large-movements | report 2 |
| GET | /api/products/electronics-movements | report 3 |
| GET | /api/products/premium-sales | report 4 |
| GET | /api/products/total-moved | report 5 |
| GET | /api/products/bulk-movements | report 6 |
| GET | /api/products/sorted-by-price | extra: sorting |
| GET | /api/products/net-movement | extra: purchases + returns - sales |
