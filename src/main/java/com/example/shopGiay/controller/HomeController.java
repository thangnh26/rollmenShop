package com.example.shopGiay.controller;

import com.example.shopGiay.dto.*;
import com.example.shopGiay.model.*;
import com.example.shopGiay.repository.ProductDetailRepository;
import com.example.shopGiay.repository.RolesRepository;
import com.example.shopGiay.repository.UserRepository;
import com.example.shopGiay.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;
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
    private final ProductDetailRepository productDetailRepository;


    public HomeController(BrandService brandService, ProductService productService, CategoryService categoryService, MaterialService materialService, SoleService soleService, CommentService commentService, ShoppingCartService shoppingCartService, ColorService colorService, UserRepository userRepo, RolesRepository rolesRepository, OrderService orderService, OrderDetailService orderDetailService, CartService cartService, ProductDetailRepository productDetailRepository) {
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
        this.productDetailRepository = productDetailRepository;
    }


    @GetMapping("/")
    public String homePage(Model model, HttpServletRequest request) {
        String currentUrl = request.getRequestURI();
        model.addAttribute("currentUrl", currentUrl);
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

        //sp bán chạy
        List<ProductDto> productHot = productService.getProductHot();
        model.addAttribute("hot", productHot);
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
        ProductDto productDto;
        try {
            productDto = productService.getDetailProductById(id);
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
        String formatPrice = df.format(productDto.getPrice());

        // Loại bỏ dấu phẩy trước khi chuyển đổi lại thành BigDecimal
        String plainStringPrice = formatPrice.replace(",", "");
        BigDecimal formatBig = new BigDecimal(plainStringPrice);
        productDto.setPrice(formatBig);

        model.addAttribute("product", productDto);
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
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = null;
            if (principal instanceof UserDetails) {
                email = ((UserDetails) principal).getUsername();
            } else if (principal instanceof String) {
                email = (String) principal;
            }

            // Lấy User từ email
            Customer user = userRepo.findByEmail(email);
            model.addAttribute("user", user);
        }
        model.addAttribute("listSize", listSizeByProduct);
        List<ProductColorResponse> listColor = productService.listColor(id);
        model.addAttribute("listColor", listColor);

        return "single-product";
    }


    @GetMapping("/product")
    public String showListProduct(Model model, @RequestParam(name = "name", required = false) String keyword,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size,
                                  @RequestParam("category") Optional<Integer> category,
                                  @RequestParam("material") Optional<Integer> material,
                                  @RequestParam("sole") Optional<Integer> sole, HttpServletRequest request) {
        String currentUrl = request.getRequestURI();
        model.addAttribute("currentUrl", currentUrl);
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
    public String list(Model model, HttpServletRequest request) {
        String currentUrl = request.getRequestURI();
        model.addAttribute("currentUrl", currentUrl);
        //Lấy các thương hiệu
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        }

        // Lấy User từ email
        Customer user = userRepo.findByEmail(email);
        List<Brand> brandsReputation = brandService.getAllBrands();
        Result result = null;
        model.addAttribute("listBrandsReputation", brandsReputation);
        model.addAttribute("result", result);
        model.addAttribute("listItem", shoppingCartService.getAllItems(user.getId()));
        model.addAttribute("total", shoppingCartService.getAmount());
        return "cart";
    }

    @PostMapping("/add-cart")
    public String addCart(Model model, @ModelAttribute AddCart cartItem) {


        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        }

        // Lấy User từ email
        Customer user = userRepo.findByEmail(email);
        Result result = shoppingCartService.add(cartItem, user.getId());
        List<Brand> brandsReputation = brandService.getAllBrands();
        model.addAttribute("listBrandsReputation", brandsReputation);
        model.addAttribute("result", result);
        model.addAttribute("listItem", shoppingCartService.getAllItems(user.getId()));
        model.addAttribute("total", shoppingCartService.getAmount());
        return "cart";
    }

    @PostMapping("/buy-now")
    public String buyNow(@ModelAttribute OrderRequest orderRequest, Model model) {
        //Lấy các thương hiệu
        List<Brand> brandsReputation = brandService.getAllBrands();
        model.addAttribute("listBrandsReputation", brandsReputation);
//        String email = "tranducdo@gmail.com";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        }

        // Lấy User từ email
        Customer user = userRepo.findByEmail(email);
        model.addAttribute("user", userRepo.findByEmail(user.getEmail()));
        ProductDetail productDetail = productDetailRepository.getOneProductDetail(orderRequest.getProId(), orderRequest.getColorId(), orderRequest.getSizeId());
        model.addAttribute("listItem", productDetail);
        model.addAttribute("quantity", orderRequest.getQuantity());
        model.addAttribute("proId", orderRequest.getProId());
        model.addAttribute("sizeId", orderRequest.getSizeId());
        model.addAttribute("colorId", orderRequest.getColorId());
        model.addAttribute("total", productDetail.getPrice().multiply(new BigDecimal(orderRequest.getQuantity())));
        return "checkoutBuyNow";
    }

    @GetMapping("/checkout")
    public String checkoutCart(Model model) {
        //Lấy các thương hiệu
        List<Brand> brandsReputation = brandService.getAllBrands();
        model.addAttribute("listBrandsReputation", brandsReputation);
//        String email = "tranducdo@gmail.com";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        }

        // Lấy User từ email
        Customer user = userRepo.findByEmail(email);
        model.addAttribute("user", userRepo.findByEmail(user.getEmail()));
        model.addAttribute("listItem", shoppingCartService.getAllItems(user.getId()));
        model.addAttribute("total", shoppingCartService.getAmount());
        return "checkout";
    }

    @PostMapping("/checkout")
    public String createOrder(@RequestParam("name_receiver") String nameReceiver, @RequestParam("phone_number_receiver") String phoneReceiver, @RequestParam(value = "address_receiver", defaultValue = "qn") String addressReceiver, @RequestParam("note") String note, @RequestParam("price") String price, @RequestParam("userId") Integer id, @RequestParam(value = "proId",required = false) int proId, @RequestParam(value = "sizeId",required = false) int sizeId, @RequestParam(value = "colorId",required = false) int colorId, @RequestParam(value = "quantity",required = false) int quantity, @RequestParam(value = "check",required = false) int check) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        }

        // Lấy User từ email
        Customer user = userRepo.findByEmail(email);

        Order order = orderService.createOrder(nameReceiver, phoneReceiver, addressReceiver, note, price, id);
        Cart cart = cartService.getOneByUserId(user.getId());
        if (check == 1) {
            orderDetailService.createOrderDetail(proId, order.getId(), quantity, sizeId, colorId);
            return "redirect:/";
        } else {
            Collection<CartItem> cartItemMap = shoppingCartService.getAllItemsByCartId(cart.getId());
            for (var item : cartItemMap
            ) {
                ProductDto productDto = productService.getDetailProductById(item.getProductDetail().getProduct().getId());
                Product product = new ModelMapper().map(productDto, Product.class);
                orderDetailService.createOrderDetail(product.getId(), order.getId(), item.getQuantity(), item.getProductDetail().getSize().getId(), item.getProductDetail().getColor().getId());
            }
            shoppingCartService.clear(cart.getId());
            return "redirect:/cart";
        }
    }

    @GetMapping("/delete")
    public String deleteProInCart(@RequestParam("proId") int proId) {
        shoppingCartService.deletePro(proId);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        }

        // Lấy User từ email
        Customer user = userRepo.findByEmail(email);
        Cart cart = cartService.getOneByUserId(user.getId());
        shoppingCartService.clear(cart.getId());
        return "redirect:/cart";
    }

    @PostMapping("/process_register")
    public String handleRegister(@RequestParam("email") String email,
                                 @RequestParam("password") String password,
                                 @RequestParam("firstName") String firstName,
                                 @RequestParam("lastName") String lastName,
                                 @RequestParam("address") String address,
                                 @RequestParam("phoneNumber") String phoneNumber,
                                 Model model) {
        if (userRepo.findByEmail(email) != null) {
            // Email đã tồn tại, thông báo lỗi
            model.addAttribute("error", "Email đã được sử dụng");
            return "login";
        }

        // Tạo người dùng mới
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password); // Không mã hóa mật khẩu
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setAddress(address);
        newUser.setPhoneNumber(phoneNumber);
        newUser.setStatus(true); // Hoặc set tùy thuộc vào logic của bạn

        userRepo.save(newUser);

        // Đăng ký thành công, chuyển hướng về trang đăng nhập
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model, @RequestParam("error") Optional<String> error,
                                Principal principal) {
        model.addAttribute("user", new User());
        if (error.isPresent()) {
            model.addAttribute("error", error);
        }
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }

        return "login";
    }

    @GetMapping("/user/profile")
    public String showUserProfile(Model model, HttpSession session) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        Customer user = userRepo.findByEmail(email);
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            model.addAttribute("user", user);
            return "user_profile";
        } else {
            model.addAttribute("error", "User not found");
            return "error";
        }
    }


    @GetMapping("/logout")
    public String logout() {
        // Xử lý logout
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            auth.setAuthenticated(false);
        }
        return "redirect:/login";
    }

}
