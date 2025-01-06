package com.ie.cicd_project_2_servicelayer;

import com.ie.cicd_project_2_servicelayer.repository.NotificationLayerClient;
import com.ie.cicd_project_2_servicelayer.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepo;
    private final NotificationLayerClient notificationLayerClient;

    @Autowired
    public ProductService(ProductRepository productRepo,
                          NotificationLayerClient notificationLayerClient) {
        this.productRepo = productRepo;
        this.notificationLayerClient = notificationLayerClient;
    }

    public String createProduct(Product product) {
        Product savedProduct = productRepo.save(product);
        return notificationLayerClient.notifyProductCreated(savedProduct);
    }

    public String getAllProducts() {
        List<Product> allProducts = productRepo.findAll();
        return notificationLayerClient.notifyAllProducts(allProducts);
    }

    public String getProductById(Long id) {
        Product productById = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return notificationLayerClient.notifyProductById(productById);
    }

    public String updateProduct(Long id, Product productDetails) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setDescription(productDetails.getDescription());
        product.setQuantity(productDetails.getQuantity());

        Product updatedProduct = productRepo.save(product);
        return notificationLayerClient.notifyProductUpdated(updatedProduct);
    }

    public String deleteProduct(Long id) {
        productRepo.deleteById(id);
        return notificationLayerClient.notifyProductDeleted(id);
    }
}
