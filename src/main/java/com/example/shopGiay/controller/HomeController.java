package com.example.shopGiay.controller;

import com.example.shopGiay.dto.*;
import com.example.shopGiay.model.*;
import com.example.shopGiay.repository.RolesRepository;
import com.example.shopGiay.repository.UserRepository;
import com.example.shopGiay.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class HomeController {

    private final BrandService brandService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final MaterialService materialService;
    private final SoleService soleService;
    private final CommentService commentService;
    private final ShoppingCartService shoppingCartService;
    private final ColorService colorService;
    private final UserRepository userRepo;
    private final RolesRepository rolesRepository;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final CartService cartService;

    public HomeController(BrandService brandService, ProductService productService, CategoryService categoryService, MaterialService materialService, SoleService soleService, CommentService commentService, ShoppingCartService shoppingCartService, ColorService colorService, UserRepository userRepo, RolesRepository rolesRepository, OrderService orderService, OrderDetailService orderDetailService, CartService cartService) {
        this.brandService = brandService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.materialService = materialService;
        this.soleService = soleService;
        this.commentService = commentService;
        this.shoppingCartService = shoppingCartService;
        this.colorService = colorService;
        this.userRepo = userRepo;
        this.rolesRepository = rolesRepository;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.cartService = cartService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        //Lấy các thương hiệu
        List<Brand> brandsReputation = brandService.getAllBrands();
        model.addAttribute("listBrandsReputation", brandsReputation);

        //Lấy 3 sản phẩm mới nhất
        List<ProductDto> newProductsBanner = productService.getListNewProducts(3);
        model.addAttribute("newProducts", newProductsBanner);

        //Lấy 8 sản phẩm mới nhất
        List<ProductDto> newProducts = productService.getListNewProducts(3);
        model.addAttribute("listNewProduct", newProducts);
//        List<ProductDto> newProducts = productService.getListNewProducts(8);
//        model.addAttribute("listNewProduct", newProducts);

        //Lấy 8 sản phẩm ngẫu nhiên
        List<ProductDto> randomProducts = productService.getListNewProducts(3);
        model.addAttribute("listRandomProduct", randomProducts);
//        List<ProductDto> randomProducts = productService.getRandomListProduct(8);
//        model.addAttribute("listRandomProduct",randomProducts);8

        return "index";
    }

    @GetMapping("/{id}")
    public String getDetailProduct(Model model, @PathVariable int id, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {

        //Lấy các thương hiệu
        List<Brand> brandsReputation = brandService.getAllBrands();
        model.addAttribute("listBrandsReputation", brandsReputation);

        //Lấy thông tin sản phẩm
        ProductDto product;
        try {
            product = productService.getDetailProductById(id);
        } catch (Exception ex) {
            return "error/404";
        }

        //Phân trang comment
        int currentPage = page.orElse(1);
        int sizePage = size.orElse(4);
        Pageable pageable = PageRequest.of(currentPage - 1, sizePage);
        Page<Comment> listComment = commentService.findAllByProductId(id, pageable);

        // Định dạng số với 2 chữ số sau dấu phẩy và dấu phẩy ngăn cách mỗi 3 chữ số
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);
        String formatPrice = df.format(product.getPrice());

        // Loại bỏ dấu phẩy trước khi chuyển đổi lại thành BigDecimal
        String plainStringPrice = formatPrice.replace(",", "");
        BigDecimal formatBig = new BigDecimal(plainStringPrice);
        product.setPrice(formatBig);

        model.addAttribute("product", product);
        model.addAttribute("listCommnet", listComment);

        int totalPage = listComment.getTotalPages();

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
            List<Integer> pagenummber = IntStream.rangeClosed(start, end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumber", pagenummber);
        }
//        //Lấy size có sẵn
        List<ProductSizeResponse> listSizeByProduct = productService.listSize(id);
//        List<Product_size> listSize = new ArrayList<>();
//        for (var item : listSizeByProduct
//        ) {
//            if(item.getQuantity() > 0){
//                listSize.add(item);
//            }
//
//        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() != "anonymousUser") {
            User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            model.addAttribute("user", user);
        }
        model.addAttribute("listSize", listSizeByProduct);
        List<ProductColorResponse> listColor = productService.listColor(id);
        model.addAttribute("listColor",listColor);

        return "single-product";
    }


    @GetMapping("/product")
    public String showListProduct(Model model, @RequestParam(name = "name", required = false) String keyword,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size,
                                  @RequestParam("category") Optional<Integer> category,
                                  @RequestParam("material") Optional<Integer> material,
                                  @RequestParam("sole") Optional<Integer> sole
    ) {


        List<Category> categoryReputation = categoryService.getAllCategory();
        model.addAttribute("listCategoryReputation", categoryReputation);

        List<Brand> brandsReputation = brandService.getAllBrands();
        model.addAttribute("listBrandsReputation", brandsReputation);

        List<Material> materialReputation = materialService.getAllMaterial();
        model.addAttribute("listMaterialsReputation", materialReputation);

        List<Sole> solesReputation = soleService.getAllSoles();
        model.addAttribute("listSolesReputation", solesReputation);


        //Tìm kiếm sản phẩm

        int currentPage = page.orElse(1);//Trang hiển thị
        int sizePage = size.orElse(8);//Kích thước sản phẩm trong 1 trang

        Pageable pageable = PageRequest.of(currentPage - 1, sizePage);

        Page<ProductDto> listProduct = productService.searchProduct(keyword, pageable);//Lấy các
        model.addAttribute("listProduct", listProduct);
        model.addAttribute("keyword", keyword);

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
            List<Integer> pageNumber = IntStream.rangeClosed(start, end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumber", pageNumber);
        }

        return "category";
    }

    @PostMapping("/comment")
    public String sendComment(@RequestParam("message") String comment, @RequestParam("productId") int productId) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        commentService.createComment(productId, user.getId(), comment);
        return "redirect:/" + productId;
    }

    @GetMapping("cart")
    public String list(Model model) {
        //Lấy các thương hiệu
        List<Brand> brandsReputation = brandService.getAllBrands();
        Result result = null;
        model.addAttribute("listBrandsReputation", brandsReputation);
        model.addAttribute("result", result);
        model.addAttribute("listItem", shoppingCartService.getAllItems());
        model.addAttribute("total", shoppingCartService.getAmount());
        return "cart";
    }

    @PostMapping("/add-cart")
    public String addCart(Model model,@ModelAttribute AddCart cartItem){
        Result result = shoppingCartService.add(cartItem);
        List<Brand> brandsReputation = brandService.getAllBrands();
        model.addAttribute("listBrandsReputation", brandsReputation);
        model.addAttribute("result", result);
        model.addAttribute("listItem", shoppingCartService.getAllItems());
        model.addAttribute("total", shoppingCartService.getAmount());
        return "cart";
    }

    @GetMapping("/checkout")
    public String checkoutCart(Model model){
        //Lấy các thương hiệu
        List<Brand> brandsReputation = brandService.getAllBrands();
        model.addAttribute("listBrandsReputation",brandsReputation);
        String email = "tranducdo@gmail.com";
//        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        model.addAttribute("user",userRepo.findByEmail(email));
        model.addAttribute("listItem",shoppingCartService.getAllItems());
        model.addAttribute("total",shoppingCartService.getAmount());
        return "checkout";
    }

    @PostMapping("/checkout")
    public String createOrder( @RequestParam("name_receiver") String nameReceiver,@RequestParam("phone_number_receiver") String phoneReceiver,@RequestParam("address_receiver") String addressReceiver,@RequestParam("note") String note, @RequestParam("price") String price, @RequestParam("userId") Integer id) {
        Order order = orderService.createOrder(nameReceiver,phoneReceiver,addressReceiver,note,price, id);
        Cart cart = cartService.getOneByUserId(1);
        Collection<CartItem> cartItemMap = shoppingCartService.getAllItemsByCartId(cart.getId());
        for (var item : cartItemMap
        ) {
            ProductDto productDto = productService.getDetailProductById(item.getProductDetail().getProduct().getId());
            Product product = new ModelMapper().map(productDto,Product.class);
            orderDetailService.createOrderDetail(product.getId(),order.getId(),item.getQuantity(),item.getProductDetail().getSize().getId(),item.getProductDetail().getColor().getId());
        }
        shoppingCartService.clear(cart.getId());
        return "redirect:/cart";
    }

    @PostMapping("/process_register")
    public String processRegister(User user ) {
        if( userRepo.findByEmail(user.getEmail()) != null){
            return "redirect:/login";
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setStatus(false);
        Set<Roles> roles = new HashSet<>();
        roles.add(rolesRepository.getById(1));
        user.setRoles(roles);
        userRepo.save(user);

        return "redirect:/login";
    }
    @GetMapping("/logout")
    public  String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/login";
    }

    @GetMapping("/loginn")
    public String showLoginPage(Model model, @RequestParam("error") Optional<String> error) {
        model.addAttribute("user", new User());
//        error.ifPresent(e -> model.addAttribute("error", e));
        return "login";
    }

    @PostMapping("/loginn")
    public String handleLogin(@RequestParam("email") String email,
                              @RequestParam("pass") String pass,
                              Model model) {
//        if (userRepo.countByEmail(email, pass) != 0) {
//            return "login";
//        } else {
//            model.addAttribute("error", "Tài khoản và mật khẩu không chính xác");
//            model.addAttribute("user", new User());
//            return "login";
//        }
        return "redirect:/";
    }

}
