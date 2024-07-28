package com.example.shopGiay.controller;


import com.example.shopGiay.dto.ProductDto;
import com.example.shopGiay.model.*;
import com.example.shopGiay.repository.*;
import com.example.shopGiay.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ProductsController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    BrandService brandService;

    @Autowired
    MaterialService materialService;

    @Autowired
    SoleService soleService;

    @Autowired
    ProductService productService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    SoleRepository soleRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductDetailRepository productDetailRepository;

    @Autowired
    SizeRepository sizeRepository;

    @Autowired
    ColorRepository colorRepository;



    @GetMapping("/admin/products")
    public String adminProducts(Model model, @RequestParam("page")Optional<Integer> page, @RequestParam("size") Optional<Integer> size){

        int currentPage = page.orElse(1);
        int sizePage = size.orElse(10);

        Pageable pageable = PageRequest.of(currentPage - 1, sizePage);
        Page<Product> listProduct = productService.findAllOrderById(pageable);
        model.addAttribute("listProduct", listProduct);
        int totalPage = listProduct.getTotalPages();
        if (totalPage > 0 ){
            int start = Math.max(1,currentPage-2);
            int end = Math.min(currentPage + 2, totalPage);
            if( totalPage > 5 ){
                if( end == totalPage){
                    start = end - 5;
                }else if(start == 1){
                    end = start + 5;
                }
            }
            List<Integer> pagenummber = IntStream.rangeClosed(start,end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumber", pagenummber);
        }
        return "admin/product/products";
    }
    @GetMapping("/admin/createProduct")
    public String adminCreateProduct(Model model){

        model.addAttribute("newProduct",new Product());
        model.addAttribute("listCategory",categoryRepository.findAll());
        model.addAttribute("listBrand",brandRepository.findAll());
        model.addAttribute("listMaterial",materialRepository.findAll());
        model.addAttribute("listSole",soleRepository.findAll());
        model.addAttribute("listSize",sizeRepository.findAll());
        model.addAttribute("listColor",colorRepository.findAll());
        return "admin/product/createProduct";
    }

    @PostMapping("/admin/createProduct")
    public String adminCreateProduct(@RequestParam("name") String name,
                                     @RequestParam("categoryId") Integer categoryId,
                                     @RequestParam("brandId") Integer brandId,
                                     @RequestParam("materialId") Integer materialId,
                                     @RequestParam("soleId") Integer soleId,
                                     @RequestParam("sizeId") int idSize,
                                     @RequestParam("colorId") int idColor,
                                     @RequestParam("quantity") int quantity,
                                     @RequestParam("price") double price,
                                     @RequestParam("description") String description,
                                     @RequestParam("thumbnailUrl") MultipartFile multipartFile) throws IOException {
        Product product = new Product();
        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        product.setName(name);
        product.setDescription(description);
        Category category = categoryRepository.getById(categoryId);
        product.setCategory(category);
        Brand brand = brandRepository.getById(brandId);
        product.setBrand(brand);
        Material material = materialRepository.getById(materialId);
        product.setMaterial(material);
        Sole sole = soleRepository.getById(soleId);
        product.setSole(sole);
        product.setThumbnail("~/img/product/"+filename);
        ProductDetail productdetail = new ProductDetail();
        product.setStatus(true);
        productdetail.setProduct(product);
        Size size = sizeRepository.getById(idSize);
        productdetail.setSize(size);
        Color color = colorRepository.getById(idColor);
        productdetail.setColor(color);
        productdetail.setQuantity(quantity);
        productdetail.setPrice(price);
        productRepository.save(product);
        categoryRepository.save(category);
        brandRepository.save(brand);
        materialRepository.save(material);
        soleRepository.save(sole);
        productDetailRepository.save(productdetail);

        String uploadDir = "./src/main/resources/static/img/product/";
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        try{
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = uploadPath.resolve(filename);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw new IOException("Không thể tải file: "+filename);
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/products")
    public String showListProduct(Model model, @RequestParam( name = "name", required = false) String keyword,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size,
                                  @RequestParam("category") Optional<Integer> category,
                                  @RequestParam("material") Optional<Integer> material,
                                  @RequestParam("sole") Optional<Integer> sole
                                  ){


        List<Category> categoryReputation = categoryService.getAllCategory();
        model.addAttribute("listCategoryReputation",categoryReputation);

        List<Brand> brandsReputation = brandService.getAllBrands();
        model.addAttribute("listBrandsReputation",brandsReputation);

        List<Material> materialReputation = materialService.getAllMaterial();
        model.addAttribute("listMaterialsReputation",materialReputation);

        List<Sole> solesReputation = soleService.getAllSoles();
        model.addAttribute("listSolesReputation",solesReputation);


        List<Size> sizesReputation = sizeRepository.findAll();
        model.addAttribute("listSizesReputation",sizesReputation);


        //Tìm kiếm sản phẩm

        int currentPage = page.orElse(1);//Trang hiển thị
        int sizePage = size.orElse(8);//Kích thước sản phẩm trong 1 trang

        Pageable pageable = PageRequest.of(currentPage - 1,sizePage);

        Page<ProductDto> listProduct = productService.searchProduct(keyword,pageable);//Lấy các
        model.addAttribute("listProduct", listProduct);
        model.addAttribute("keyword",keyword);

        int totalPage = listProduct.getTotalPages();
        if( totalPage > 0 ){
            int start = Math.max(1,currentPage - 2);
            int end = Math.min(currentPage + 2,totalPage);
            if( totalPage > 5){
                if( end == totalPage ){
                    start = end - 5;
                }else if (start == 1){
                    end = start + 5;
                }
            }
            List<Integer> pageNumber = IntStream.rangeClosed(start,end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumber", pageNumber);
        }

        return "category";

    }

    @GetMapping("/admin/product/{id}")
    public String productDetail(Model model,@PathVariable("id") Integer id){
        Product product = productService.getDetailProductById(id);
        List<ProductDetail> listSize = productDetailRepository.findAllByProductId(id);
        model.addAttribute("product",product);
        model.addAttribute("listSize",listSize);
        return "admin/product/productDetail";
    }

//    @PostMapping("/admin/product/{id}/update")
//    public String updateSize(@PathVariable("id") int id, @RequestParam("productId") int productId, @RequestParam("qty") int quantity, @RequestParam("price") double price){
//        ProductDetail product_size = productDetailRepository.findByProductIdAndSizeId(productId,id);
//        int a = product_size.getQuantity();
//        double b = product_size.getPrice();
//        product_size.setQuantity(quantity);
//        product_size.setPrice(price);
//        Product product = productRepository.getById(productId);
//        productRepository.save(product);
//        productDetailRepository.save(product_size);
//        return "redirect:/admin/product/"+productId;
//    }
@GetMapping("/admin/product/update/{id}")
public String updateProduct(Model model,@PathVariable("id") Integer id){
    Product product = productService.getDetailProductById(id);
    model.addAttribute("product",product);
    model.addAttribute("listCategory",categoryRepository.findAll());
    model.addAttribute("listBrand",brandRepository.findAll());
    model.addAttribute("listMaterial",materialRepository.findAll());
    model.addAttribute("listSole",soleRepository.findAll());

    return "admin/product/updateProduct";
}
    @PostMapping("/admin/product/update")
    public String adminCreateProduct(@RequestParam("id") Integer id,
                                     @RequestParam("name") String name,
                                     @RequestParam("categoryId") Integer categoryId,
                                     @RequestParam("brandId") Integer brandId,
                                     @RequestParam("materialId") Integer materialId,
                                     @RequestParam("soleId") Integer soleId
    ) throws IOException {
        Product product = productService.getDetailProductById(id);
        product.setName(name);
        Category category = categoryRepository.getById(categoryId);
        Brand brand = brandRepository.getById(brandId);
        Material material = materialRepository.getById(materialId);
        Sole sole = soleRepository.getById(soleId);

        product.setCategory(category);
        product.setBrand(brand);
        product.setMaterial(material);
        product.setSole(sole);
        productRepository.save(product);
        String uploadDir = "./src/main/resources/static/img/product/";
        Path uploadPath = Paths.get(uploadDir);
        return "redirect:/admin/products";
    }
}
