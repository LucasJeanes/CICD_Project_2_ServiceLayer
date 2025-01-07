package com.ie.cicd_project_2_servicelayer;

import com.ie.cicd_project_2_servicelayer.repository.NotificationLayerClient;
import com.ie.cicd_project_2_servicelayer.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class ProductServiceTest {

    @Mock
    private NotificationLayerClient notificationLayerClient;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void openMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        Product product = new Product(1L,
                "Test Product",
                100.0,
                "Test_description",
                10);

        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(notificationLayerClient.notifyProductCreated(any(Product.class)))
                .thenReturn("Registered new product with ID: "
                        + product.getId()
                        + "\n " + product);

        String result = productService.createProduct(product);

        assertEquals("Registered new product with ID: 1\n " + product, result);
        verify(productRepository, times(1)).save(product);
        verify(notificationLayerClient, times(1))
                .notifyProductCreated(product);

    }


    @Test
    void testGetAllProducts() {
        Product product = new Product(1L,
                "Test Product",
                100.0,
                "Test_description",
                10);

        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));
        when(notificationLayerClient.notifyAllProducts(any()))
                .thenReturn("Retrieved all 1 products. \n"
                        + Collections.singletonList(product));

        String result = productService.getAllProducts();

        assertEquals("Retrieved all 1 products. \n"
                + Collections.singletonList(product), result);
        verify(productRepository, times(1)).findAll();
        verify(notificationLayerClient, times(1))
                .notifyAllProducts(Collections.singletonList(product));
    }

    @Test
    void testGetProductById() {
            Product product = new Product(1L,
                    "Test Product",
                    100.0,
                    "Test_description",
                    10);

            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(notificationLayerClient.notifyProductById(any(Product.class)))
                    .thenReturn("Retrieved product with ID: " + product.getId() + "\n " + product);

            String result = productService.getProductById(1L);

            assertEquals("Retrieved product with ID: 1\n " + product, result);
            verify(productRepository, times(1)).findById(1L);
            verify(notificationLayerClient, times(1)).notifyProductById(product);
    }

    @Test
    void testUpdateProduct() {
        Product existingProduct = new Product(1L,
                "Test Product",
                100.0,
                "Test_description",
                10);

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Test Product");
        updatedProduct.setPrice(150.0);
        updatedProduct.setDescription("Updated_description");
        updatedProduct.setQuantity(25);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            existingProduct.setName(savedProduct.getName());
            existingProduct.setPrice(savedProduct.getPrice());
            existingProduct.setDescription(savedProduct.getDescription());
            existingProduct.setQuantity(savedProduct.getQuantity());
            return existingProduct;
        });

        when(notificationLayerClient.notifyProductUpdated(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product notifiedProduct = invocation.getArgument(0);
                    return "Updated product with ID: " + notifiedProduct.getId() + "\n " + notifiedProduct;
                });

        String result = productService.updateProduct(1L, updatedProduct);

        assertEquals("Updated product with ID: 1\n " + existingProduct, result);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
        verify(notificationLayerClient, times(1)).notifyProductUpdated(existingProduct);

        assertEquals("Updated Test Product", existingProduct.getName());
        assertEquals(150.0, existingProduct.getPrice());
        assertEquals("Updated_description", existingProduct.getDescription());
        assertEquals(25, existingProduct.getQuantity());
    }

    @Test
    void testDeleteProduct() {
        Long idToDelete = 1L;

        when(notificationLayerClient.notifyProductDeleted(idToDelete))
                .thenReturn("Deleted product with ID: " + idToDelete);

        String result = productService.deleteProduct(idToDelete);

        assertEquals("Deleted product with ID: 1", result);

        verify(productRepository, times(1)).deleteById(idToDelete);
        verify(notificationLayerClient, times(1)).notifyProductDeleted(idToDelete);
    }
}