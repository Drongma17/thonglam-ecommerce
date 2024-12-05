package com.thonglam.controller;


import com.thonglam.entity.Product;
import com.thonglam.payload.ProductDTO;
import com.thonglam.payload.ProductResponse;
import com.thonglam.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long categoryId) {

        ProductDTO savedProduct = productService.addProduct(categoryId, productDTO);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }


    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts() {
        ProductResponse productResponse = productService.getAllProducts();
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }


    @GetMapping("public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable Long categoryId) {
        ProductResponse productResponse = productService.searchByCategory(categoryId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword) {
        ProductResponse productResponse = productService.searchProductByKeyword(keyword);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }


    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO,
                                                    @PathVariable Long productId){
        ProductDTO updatedProductDTO = productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }


    @DeleteMapping("/admin/product/delete/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
       ProductDTO productService1= productService.deleteProduct(productId);
        return new ResponseEntity<>(productService1, HttpStatus.OK);
    }

@PutMapping("/product/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@Valid @PathVariable Long productId,
                                                         @RequestParam("image")MultipartFile image) throws IOException {
 ProductDTO updatedProduct = productService.updateProductImage(productId, image);
   return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}
