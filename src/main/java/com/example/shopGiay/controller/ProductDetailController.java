package com.example.shopGiay.controller;

import com.example.shopGiay.model.ProductDetail;
import com.example.shopGiay.service.ColorService;
import com.example.shopGiay.service.ProductDetailService;
import com.example.shopGiay.service.ProductService;
import com.example.shopGiay.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/productDetails")
public class ProductDetailController {
    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private ColorService colorService;

    @Autowired
    private SizeService sizeService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listProductDetails(Model model) {
        List<ProductDetail> productDetails = productDetailService.getAllProductDetails();
        model.addAttribute("productDetails", productDetails);
        return "productDetail/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("productDetail", new ProductDetail());
        model.addAttribute("colors", colorService.findAll());
        model.addAttribute("sizes", sizeService.getAllSizes());
        model.addAttribute("products", productService.getAllProducts());
        return "productDetail/form";
    }

    @PostMapping
    public String saveProductDetail(@ModelAttribute("productDetail") ProductDetail productDetail) {
        productDetailService.saveProductDetail(productDetail);
        return "redirect:/productDetails";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        ProductDetail productDetail = productDetailService.getProductDetailById(id);
        model.addAttribute("productDetail", productDetail);
        model.addAttribute("colors", colorService.findAll());
        model.addAttribute("sizes", sizeService.getAllSizes());
        model.addAttribute("products", productService.getAllProducts());
        return "productDetail/form";
    }

    @PostMapping("/{id}")
    public String updateProductDetail(@PathVariable("id") Integer id, @ModelAttribute("productDetail") ProductDetail productDetail) {
        productDetail.setId(id);
        productDetailService.saveProductDetail(productDetail);
        return "redirect:/productDetails";
    }

    @GetMapping("/delete/{id}")
    public String deleteProductDetail(@PathVariable("id") Integer id) {
        productDetailService.deleteProductDetail(id);
        return "redirect:/productDetails";
    }
}
