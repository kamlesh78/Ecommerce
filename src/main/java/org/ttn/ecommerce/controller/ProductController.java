package org.ttn.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ttn.ecommerce.entities.product.Product;
import org.ttn.ecommerce.services.ProductService;
import org.ttn.ecommerce.services.SellerDaoService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    SellerDaoService sellerDaoService;

    @PostMapping("add/product/{categoryId}")
    public ResponseEntity<?> addProduct(@RequestBody Product product, @PathVariable("categoryId") Long categoryId, HttpServletRequest request){

        String email = sellerDaoService.emailFromToken(request);
        return  productService.addProduct(product,categoryId,email);
    }

}
