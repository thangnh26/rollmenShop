package com.example.shopGiay.controller;

import com.example.shopGiay.dto.ProductDetailForm;
import com.example.shopGiay.dto.ProductDetailFormWrapper;
import com.example.shopGiay.dto.ProductDetailRequest;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
public class ProductsController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private SoleService soleService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private SoleRepository soleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ColorRepository colorRepository;

    @GetMapping("/products")
    public String adminProducts(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int sizePage = size.orElse(5);
        // Đảm bảo currentPage không nhỏ hơn 1
        if (currentPage < 1) {
            currentPage = 1;
        }
        Pageable pageable = PageRequest.of(currentPage - 1, sizePage);
        Page<Product> listProduct = productService.findAllOrderById(pageable);
        model.addAttribute("listProduct", listProduct);

        int totalPage = listProduct.getTotalPages();
        if (totalPage > 0) {
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPage);
            if (totalPage > 5) {
                if (end == totalPage) {
                    start = end - 5;
                } else if (start == 1) {
                    end = start + 5;
                }
            }
            List<Integer> pageNumber = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumber", pageNumber);
        }
        return "admin/product/products";
    }

    @GetMapping("/createProduct")
    public String adminCreateProduct(Model model) {
        model.addAttribute("newProduct", new Product());
        model.addAttribute("listCategory", categoryRepository.findByStatusActive());
        model.addAttribute("listBrand", brandRepository.findByStatusActive());
        model.addAttribute("listMaterial", materialRepository.findByStatusActive());
        model.addAttribute("listSole", soleRepository.findByStatusActive());
        model.addAttribute("listSize", sizeRepository.findByStatusActive());
        model.addAttribute("listColor", colorRepository.findByStatusActive());
        return "admin/product/createProduct";
    }

    @PostMapping("/createProduct")
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

    @GetMapping("/product/update/{id}")
    public String updateProductForm(Model model, @PathVariable("id") Integer id) {
        Product product = productService.getDetailProductById(id);
        if (product == null) {
            return "error/404";  // Redirect to a 404 page if the product is not found
        }
        product.getStatus();
        model.addAttribute("product", product);
        model.addAttribute("listCategory", categoryRepository.findByStatusActive());
        model.addAttribute("listBrand", brandRepository.findByStatusActive());
        model.addAttribute("listMaterial", materialRepository.findByStatusActive());
        model.addAttribute("listSole", soleRepository.findByStatusActive());
        return "admin/product/updateProduct";
    }

    @PostMapping("/product/update")
    public String updateProduct(@ModelAttribute("product") Product product,
                                @RequestParam("categoryId") Integer categoryId,
                                @RequestParam("brandId") Integer brandId,
                                @RequestParam("materialId") Integer materialId,
                                @RequestParam("soleId") Integer soleId,
                                @RequestParam("existingThumbnail") String existingThumbnail,
                                @RequestParam("thumbnailUrl") MultipartFile thumbnailUrl,
                                RedirectAttributes redirectAttributes) {
        try {
            product.setCategory(categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID")));
            product.setBrand(brandRepository.findById(brandId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid brand ID")));
            product.setMaterial(materialRepository.findById(materialId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid material ID")));
            product.setSole(soleRepository.findById(soleId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid sole ID")));

            // Handle thumbnail update
            if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
                // If a new image is uploaded, update the thumbnail
                String filename = StringUtils.cleanPath(thumbnailUrl.getOriginalFilename());
                product.setThumbnail("~/img/product/" + filename);

                String uploadDir = "./src/main/resources/static/img/product/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                try (InputStream inputStream = thumbnailUrl.getInputStream()) {
                    Path filePath = uploadPath.resolve(filename);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
            } else {
                // If no new image is uploaded, retain the existing image
                product.setThumbnail(existingThumbnail);
            }

            if (product.getStatus() == null) {
                product.setStatus(1);  // Set a default status if not provided
            }

            productService.saveOrUpdateProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update product. Please try again.");
            return "redirect:/admin/product/update/" + product.getId();
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/product/{id}")
    public String productDetail(Model model, @PathVariable("id") Integer id) {
        List<ProductDetail> productDetails = productService.getListDetailProductById(id);
        if (productDetails == null || productDetails.isEmpty()) {
            return "error/404";  // Redirect to a 404 page if the product details are not found
        }
        model.addAttribute("product", productDetails);
        model.addAttribute("id", id);
        return "admin/product/productDetail";
    }

    @GetMapping("/product/add-product-detail")
    public String addProductDetailForm(Model model, @RequestParam("productId") Integer productId) {
        List<Size> sizeList = sizeRepository.findAll();
        List<Color> colorList = colorRepository.findAll();
        model.addAttribute("size", sizeList);
        model.addAttribute("color", colorList);
        model.addAttribute("productId", productId);
        return "admin/product/addProductDetail";
    }

    @PostMapping("/product/add-product-detail")
    public String addProductDetail(@RequestParam("productId") Integer productId,
                                   @RequestParam("price") BigDecimal price,
                                   @RequestParam("quantity") Integer quantity,
                                   @RequestParam("sizeId") Integer sizeId,
                                   @RequestParam("colorId") Integer colorId,
                                   RedirectAttributes redirectAttributes) {
        try {
            ProductDetail existingDetail = productDetailRepository.getOne(productId, colorId, sizeId);
            if (existingDetail != null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Product detail already exists.");
                return "redirect:/admin/product/" + productId;
            }

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
            Size size = sizeRepository.findById(sizeId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid size ID"));
            Color color = colorRepository.findById(colorId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid color ID"));

            ProductDetail productDetail = new ProductDetail();
            productDetail.setProduct(product);
            productDetail.setSize(size);
            productDetail.setColor(color);
            productDetail.setPrice(price);
            productDetail.setQuantity(quantity);
            productDetail.setStatus(1);

            productDetailRepository.save(productDetail);
            redirectAttributes.addFlashAttribute("successMessage", "Product detail added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add product detail. Please try again.");
        }
        return "redirect:/admin/product/" + productId;
    }

    // Helper methods for product creation
    private void createProductAndDetails(ProductDetailForm productDTO, List<Integer> sizeIds, List<Integer> colorIds) throws IOException {
        Product product = createProduct(productDTO);
        if (sizeIds == null || colorIds == null || sizeIds.isEmpty() || colorIds.isEmpty()) {
            throw new IllegalArgumentException("Size IDs and Color IDs cannot be null or empty");
        }

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
        Product product = new Product();
        String filename = StringUtils.cleanPath(productDTO.getThumbnailUrl().getOriginalFilename());

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());

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

        product.setThumbnail("~/img/product/" + filename);
        product.setStatus(1);

        productRepository.save(product);

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
    @PostMapping("/updateProduct")
    public String updateProducts(@RequestParam("productIds") List<Integer> productIds,
                                 @RequestParam("prices") List<BigDecimal> prices,
                                 @RequestParam("quantities") List<Integer> quantities,
                                 RedirectAttributes redirectAttributes) {
        try {
            for (int i = 0; i < productIds.size(); i++) {
                productService.updateProductDetails(productIds.get(i), prices.get(i), quantities.get(i));
            }
            redirectAttributes.addFlashAttribute("successMessage", "Products updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update products. Please try again.");
        }
        return "redirect:/admin/products";
    }

}
