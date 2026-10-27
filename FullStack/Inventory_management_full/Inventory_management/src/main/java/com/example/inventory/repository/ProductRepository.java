package com.example.inventory.repository;

import com.example.inventory.model.Product;
import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {

    // QUERY 1: Products that have been purchased (stock added)
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'StockMovements', 'localField': '_id', 'foreignField': 'productId', 'as': 'movements' } }",
            "{ '$unwind': '$movements' }",
            "{ '$match': { 'movements.movementType': 'Purchase' } }",
            "{ '$project': { '_id': 0, 'name': 1, 'movementType': '$movements.movementType' } }"
    })
    AggregationResults<Document> findPurchasedProducts();


    // QUERY 2: Products with a single movement of more than 50 units
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'StockMovements', 'localField': '_id', 'foreignField': 'productId', 'as': 'movements' } }",
            "{ '$unwind': '$movements' }",
            "{ '$match': { 'movements.quantity': { '$gt': 50 } } }",
            "{ '$project': { '_id': 0, 'name': 1, 'movementQuantity': '$movements.quantity' } }"
    })
    AggregationResults<Document> findLargeMovements();


    // QUERY 3: Electronics products and their stock movements
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'StockMovements', 'localField': '_id', 'foreignField': 'productId', 'as': 'movements' } }",
            "{ '$unwind': '$movements' }",
            "{ '$match': { 'category': 'Electronics' } }",
            "{ '$project': { '_id': 0, 'name': 1, 'category': 1, 'movementType': '$movements.movementType', 'movementQuantity': '$movements.quantity' } }"
    })
    AggregationResults<Document> findElectronicsMovements();


    // QUERY 4: Price > 1000 and sold
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'StockMovements', 'localField': '_id', 'foreignField': 'productId', 'as': 'movements' } }",
            "{ '$unwind': '$movements' }",
            "{ '$match': { 'price': { '$gt': 1000 }, 'movements.movementType': 'Sale' } }",
            "{ '$project': { '_id': 0, 'name': 1, 'price': 1, 'movementType': '$movements.movementType' } }"
    })
    AggregationResults<Document> findPremiumSales();


    // QUERY 5: Total quantity moved per product
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'StockMovements', 'localField': '_id', 'foreignField': 'productId', 'as': 'movements' } }",
            "{ '$unwind': '$movements' }",
            "{ '$group': { '_id': '$_id', 'name': { '$first': '$name' }, 'totalQuantityMoved': { '$sum': '$movements.quantity' } } }",
            "{ '$project': { '_id': 0, 'name': 1, 'totalQuantityMoved': 1 } }"
    })
    AggregationResults<Document> calculateTotalMoved();


    // QUERY 6: Products with at least 10 units in a single movement
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'StockMovements', 'localField': '_id', 'foreignField': 'productId', 'as': 'movements' } }",
            "{ '$unwind': '$movements' }",
            "{ '$match': { 'movements.quantity': { '$gte': 10 } } }",
            "{ '$project': { '_id': 0, 'name': 1, 'movementQuantity': '$movements.quantity' } }"
    })
    AggregationResults<Document> findBulkMovements();


    // EXTRA 1: Product name, price and movement type, sorted by price high to low
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'StockMovements', 'localField': '_id', 'foreignField': 'productId', 'as': 'movements' } }",
            "{ '$unwind': '$movements' }",
            "{ '$sort': { 'price': -1 } }",
            "{ '$project': { '_id': 0, 'name': 1, 'price': 1, 'movementType': '$movements.movementType' } }"
    })
    AggregationResults<Document> findSortedByPrice();


    // EXTRA 2: Net movement per product (Purchases + Returns - Sales)
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'StockMovements', 'localField': '_id', 'foreignField': 'productId', 'as': 'movements' } }",
            "{ '$unwind': '$movements' }",
            "{ '$group': { '_id': '$_id', 'name': { '$first': '$name' }, 'netMovement': { '$sum': { '$cond': [ { '$eq': ['$movements.movementType', 'Sale'] }, { '$multiply': ['$movements.quantity', -1] }, '$movements.quantity' ] } } } }",
            "{ '$project': { '_id': 0, 'name': 1, 'netMovement': 1 } }"
    })
    AggregationResults<Document> calculateNetMovement();
}
