package com.example.newsapplication.product_grid_view;

public class ProductModel {
    private String productName;
    private String productImage;

    public ProductModel(String productName, String productImage) {
        this.productName = productName;
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImage() {
        return productImage;
    }
}
