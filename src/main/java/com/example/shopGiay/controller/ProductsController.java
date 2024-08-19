package com.example.shopGiay.controller;


import com.example.shopGiay.dto.*;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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
    public String adminCreateProducts(@ModelAttribute ProductDetailForm productDTO,
                                      @RequestParam("sizeIds") List<Integer> sizeIds,
                                      @RequestParam("colorIds") List<Integer> colorIds,
                                      RedirectAttributes redirectAttributes) {
        try {
            createProductAndDetails(productDTO, sizeIds, colorIds);
            redirectAttributes.addFlashAttribute("successMessage", "Product created successfully with multiple details");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating product");
        }
        return "redirect:/admin/products";
    }

    private void createProductAndDetails(ProductDetailForm productDTO, List<Integer> sizeIds, List<Integer> colorIds) throws IOException {
        Product product = createProduct(productDTO);

        // Validate size and color lists
        if (sizeIds == null || colorIds == null || sizeIds.isEmpty() || colorIds.isEmpty()) {
            throw new IllegalArgumentException("Size IDs and Color IDs cannot be null or empty");
        }

        // Generate ProductDetailRequest for each combination of size and color
        for (Integer sizeId : sizeIds) {
            for (Integer colorId : colorIds) {
                ProductDetailRequest detailRequest = new ProductDetailRequest();
                detailRequest.setSizeId(sizeId);
                detailRequest.setColorId(colorId);
                detailRequest.setQuantity(productDTO.getQuantity());
                detailRequest.setPrice(productDTO.getPrice());

                createProductDetail(product, detailRequest);
            }
        }
    }

    private void createProductDetail(Product product, ProductDetailRequest detailRequest) {
        if (detailRequest.getSizeId() == null || detailRequest.getColorId() == null || detailRequest.getQuantity() == null || detailRequest.getPrice() == null) {
            throw new IllegalArgumentException("Product detail fields cannot be null");
        }

        ProductDetail productDetail = new ProductDetail();
        productDetail.setProduct(product);

        Size size = sizeRepository.findById(detailRequest.getSizeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid size ID"));
        productDetail.setSize(size);

        Color color = colorRepository.findById(detailRequest.getColorId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid color ID"));
        productDetail.setColor(color);

        productDetail.setQuantity(detailRequest.getQuantity());
        productDetail.setPrice(BigDecimal.valueOf(detailRequest.getPrice()));
        productDetail.setStatus(1);

        productDetailRepository.save(productDetail);
    }
    private Product createProduct(ProductDetailForm productDTO) throws IOException {
        // Create a new Product instance
        Product product = new Product();
        String filename = StringUtils.cleanPath(productDTO.getThumbnailUrl().getOriginalFilename());

        // Set product attributes
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());

        // Fetch and set product category, brand, material, and sole
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        product.setCategory(category);

        Brand brand = brandRepository.findById(productDTO.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid brand ID"));
        product.setBrand(brand);

        Material material = materialRepository.findById(productDTO.getMaterialId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid material ID"));
        product.setMaterial(material);

        Sole sole = soleRepository.findById(productDTO.getSoleId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid sole ID"));
        product.setSole(sole);

        // Set thumbnail URL and status
        product.setThumbnail("~/img/product/" + filename);
        product.setStatus(1);

        // Save product to the repository
        productRepository.save(product);

        // Save the thumbnail image
        String uploadDir = "./src/main/resources/static/img/product/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = productDTO.getThumbnailUrl().getInputStream()) {
            Path filePath = uploadPath.resolve(filename);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Cannot upload file: " + filename, e);
        }

        return product;
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

        Page<Product> listProduct = productService.searchProduct(keyword,pageable);//Lấy các
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
        List<ProductDetail> product = productService.getListDetailProductById(id);
        model.addAttribute("product",product);
        return "admin/product/productDetail";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@RequestParam("id") Integer id,
                                @RequestParam("price") BigDecimal price,
                                @RequestParam("quantity") Integer quantity,
                                Model model) {

        try {
            // Call the service layer to update the product
            productService.updateProductDetails(id, price, quantity);
            model.addAttribute("message", "Product updated successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update product. Please try again.");
            return "error/500";
        }
        // Redirect to the products page or any other relevant page
        return "redirect:/admin/products";
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
//    @PostMapping("/admin/product/update")
//    public String adminCreateProduct(@RequestParam("id") Integer id,
//                                     @RequestParam("name") String name,
//                                     @RequestParam("categoryId") Integer categoryId,
//                                     @RequestParam("brandId") Integer brandId,
//                                     @RequestParam("materialId") Integer materialId,
//                                     @RequestParam("soleId") Integer soleId
//    ) throws IOException {
//        ProductDto product = productService.getDetailProductById(id);
//        product.setName(name);
//        Category category = categoryRepository.getById(categoryId);
//        Brand brand = brandRepository.getById(brandId);
//        Material material = materialRepository.getById(materialId);
//        Sole sole = soleRepository.getById(soleId);
//
//        product.setCategory(category);
//        product.setBrand(brand);
//        product.setMaterial(material);
//        product.setSole(sole);
//        productRepository.save(product);
//        String uploadDir = "./src/main/resources/static/img/product/";
//        Path uploadPath = Paths.get(uploadDir);
//        return "redirect:/admin/products";
//    }
}
