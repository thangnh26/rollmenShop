package com.example.shopGiay.controller;

import com.example.shopGiay.model.*;
import com.example.shopGiay.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private SoleService soleService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Category.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(categoryService.getCategoryById(Integer.parseInt(text)));
            }
        });

        binder.registerCustomEditor(Brand.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(brandService.getBrandById(Integer.parseInt(text)));
            }
        });

        binder.registerCustomEditor(Material.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(materialService.getMaterialById(Integer.parseInt(text)));
            }
        });

        binder.registerCustomEditor(Sole.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(soleService.getSoleById(Integer.parseInt(text)));
            }
        });
    }

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("soles", soleService.getAllSoles());
        model.addAttribute("materials", materialService.getAllMaterial());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "product/form";
    }

    @PostMapping
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("soles", soleService.getAllSoles());
        model.addAttribute("materials", materialService.getAllMaterial());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "product/form";
    }

    @PostMapping("/{id}")
    public String updateProduct(@PathVariable("id") Integer id, @ModelAttribute("product") Product product) {
        product.setId(id);
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
