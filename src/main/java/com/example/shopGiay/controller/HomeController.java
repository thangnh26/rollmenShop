package com.example.shopGiay.controller;

import com.example.shopGiay.dto.*;
import com.example.shopGiay.model.*;
import com.example.shopGiay.repository.*;
import com.example.shopGiay.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;



    public HomeController(BrandService brandService, ProductService productService, CategoryService categoryService, MaterialService materialService, SoleService soleService, CommentService commentService, ShoppingCartService shoppingCartService, ColorService colorService, UserRepository userRepo, RolesRepository rolesRepository, OrderService orderService, OrderDetailService orderDetailService, CartService cartService, ProductDetailRepository productDetailRepository, AddressRepository addressRepository, CustomerRepository customerRepository) {
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
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
    }


    @GetMapping("/")
    public String homePage(Model model, HttpServletRequest request) {
        String currentUrl = request.getRequestURI();
        model.addAttribute("currentUrl", currentUrl);
        //Lấy các thương hiệu
        List<Brand> brandsReputation = brandService.getAllBrands();
        model.addAttribute("listBrandsReputation", brandsReputation);

        //Lấy 3 sản phẩm mới nhất
        List<Product> newProductsBanner = productService.getListNewProducts(3);
        model.addAttribute("newProducts", newProductsBanner);

        //Lấy 8 sản phẩm mới nhất
        List<Product> newProducts = productService.getListNewProducts(3);
        model.addAttribute("listNewProduct", newProducts);
//        List<ProductDto> newProducts = productService.getListNewProducts(8);
//        model.addAttribute("listNewProduct", newProducts);

        //sp bán chạy
        List<Product> productHot = productService.getProductHot();
        model.addAttribute("hot", productHot);


        List<BigDecimal> price = productDetailRepository.findListPricreByProductId(newProducts.stream().map(Product::getId).collect(Collectors.toList()));

        //Lấy 8 sản phẩm ngẫu nhiên
        List<Product> randomProducts = productService.getListNewProducts(3);
        List<BigDecimal> priceRandomProducts = productDetailRepository.findListPricreByProductId(randomProducts.stream().map(Product::getId).collect(Collectors.toList()));

        model.addAttribute("listRandomProduct", randomProducts);
        model.addAttribute("price", price);
        model.addAttribute("priceRandomProducts", priceRandomProducts);

        return "index";
    }
    @GetMapping("/add-address")
    public String showAddForm(Address address) {
        return "addAddress";
    }
    @PostMapping("/add-address")
    public String addAddress(@Valid Address address, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "addAddress";
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        }

        // Lấy User từ email
        Customer user = userRepo.findByEmail(email);
        address.setCustomer(user);
        addressRepository.save(address);
        return "redirect:/user/profile";
    }
    @GetMapping("/order-detail-customer")
    public String getOderDetail(@RequestParam("id")Integer id,Model model){
        List<OrderDetail> orderDetail = orderDetailService.getById(id);
        Order oder = orderDetailService.getOrderById(id);
        if (orderDetail == null || orderDetail.isEmpty()) {
            // Handle case when no order details are found
            return "error/404";
        }
        model.addAttribute("orderDetail",orderDetail);
        model.addAttribute("oder",oder);
        return "orderDetail";
    }

    @GetMapping("/order-detail-staff")
    public String getOderDetailStaff(@RequestParam("id")Integer id,Model model){
        List<OrderDetail> orderDetail = orderDetailService.getById(id);
        Order oder = orderDetailService.getOrderById(id);
        if (orderDetail == null || orderDetail.isEmpty()) {
            // Handle case when no order details are found
            return "error/404";
        }
        model.addAttribute("orderDetail",orderDetail);
        model.addAttribute("oder",oder);
        return "staff/orderDetail";
    }

    @GetMapping("/{id}")
    public String getDetailProduct(Model model, @PathVariable int id, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {

        //Lấy các thương hiệu
        List<Brand> brandsReputation = brandService.getAllBrands();
        model.addAttribute("listBrandsReputation", brandsReputation);

        //Lấy thông tin sản phẩm
        Product product;
//        try {
            product = productService.getDetailProductById(id);
//        } catch (Exception ex) {
//            return "error/404";
//        }

        //Phân trang comment
        int currentPage = page.orElse(1);
        int sizePage = size.orElse(4);
        Pageable pageable = PageRequest.of(currentPage - 1, sizePage);
        Page<Comment> listComment = commentService.findAllByProductId(id, pageable);
        ProductDetail productDetail = productDetailRepository.findPricreByProductId(product.getId());

        // Định dạng số với 2 chữ số sau dấu phẩy và dấu phẩy ngăn cách mỗi 3 chữ số
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);
        String formatPrice = df.format(productDetail.getPrice());

        // Loại bỏ dấu phẩy trước khi chuyển đổi lại thành BigDecimal
        String plainStringPrice = formatPrice.replace(",", "");
        BigDecimal formatBig = new BigDecimal(plainStringPrice);
        productDetail.setPrice(formatBig);

        model.addAttribute("product", product);
        model.addAttribute("productDetail", productDetail);
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
    @GetMapping("/product-detail/quantity")
    @ResponseBody
    public Map<String, Integer> getProductDetailQuantity(@RequestParam("sizeId") Integer sizeId,
                                                         @RequestParam("colorId") Integer colorId,
                                                         @RequestParam("productId") Integer productId) {
        ProductDetail productDetail = productDetailRepository.findBySizeIdAndColorIdAndProductId(sizeId, colorId, productId);

        Map<String, Integer> response = new HashMap<>();
        response.put("quantity", productDetail != null ? productDetail.getQuantity() : 0);

        return response;
    }



    @GetMapping("/product")
    public String showListProduct(Model model, @RequestParam(name = "name", required = false) String keyword,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size,
                                  @RequestParam("category") Optional<Integer> category,
                                  @RequestParam("material") Optional<Integer> material,
                                  @RequestParam("sole") Optional<Integer> sole, HttpServletRequest request,
                                  @PageableDefault(size = 8) Pageable pageable) {
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

//        Pageable pageable = PageRequest.of(currentPage - 1, sizePage);

        Page<Product> pageProduct = productService.searchProduct(keyword, pageable);//Lấy các
        List<BigDecimal> price = productDetailRepository.findListPricreByProductId(pageProduct.getContent().stream().map(Product::getId).collect(Collectors.toList()));
        model.addAttribute("listProduct", pageProduct);
        model.addAttribute("price", price);
        model.addAttribute("keyword", keyword);

        int totalPage = pageProduct.getTotalPages();
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
        if (user==null){
            return "redirect:/login";
        }
        List<Brand> brandsReputation = brandService.getAllBrands();
        Result result = null;
       List<CartItem> list= shoppingCartService.getAllItems(user.getId());
        List<ProductDetail> productDetails = productDetailRepository.findAllByProduct(list.stream()
                .map(item -> item.getProductDetail().getProduct().getId())
                .collect(Collectors.toList())
        );
        Set<Size> distinctSizes = new HashSet<>();
        Set<Color> distinctColors = new HashSet<>();
        for (ProductDetail detail : productDetails) {
            distinctColors.add(detail.getColor());
            distinctSizes.add(detail.getSize());
        }

        model.addAttribute("listBrandsReputation", brandsReputation);
        model.addAttribute("result", result);
        model.addAttribute("listItem", list);
        model.addAttribute("total", shoppingCartService.getAmount());
        model.addAttribute("sizes", distinctSizes);
        model.addAttribute("colors", distinctColors);
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
        if (user==null){
            return "redirect:/login";
        }
        Result result = shoppingCartService.add(cartItem, user.getId());
        List<Brand> brandsReputation = brandService.getAllBrands();
        model.addAttribute("listBrandsReputation", brandsReputation);
        model.addAttribute("result", result);
        model.addAttribute("listItem", shoppingCartService.getAllItems(user.getId()));
        model.addAttribute("total", shoppingCartService.getAmount());
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCartItem(
            @RequestParam("id") int productId,
            @RequestParam("color") int colorId,
            @RequestParam("size") int sizeId,
            @RequestParam("qty") int quantity,
            RedirectAttributes redirectAttributes) {

        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = null;
            if (principal instanceof UserDetails) {
                email = ((UserDetails) principal).getUsername();
            } else if (principal instanceof String) {
                email = (String) principal;
            }

            // Lấy User từ email
            Customer user = userRepo.findByEmail(email);
            if (user==null){
                return "redirect:/login";
            }
            Cart cart = cartService.getOneByUserId(user.getId());
            shoppingCartService.updateCartItem(cart.getId(),productId,colorId, sizeId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Cart item updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating cart item");
        }

        return "redirect:/cart"; // Redirect to the cart page or any other page as needed
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
        if (user==null){
            return "redirect:/login";
        }
        Address address = addressRepository.findByCustomerId(user.getId());
        List<Address> allAddressByCusId = addressRepository.findByCusId(user.getId());
        if (allAddressByCusId.size()==0){
            return "redirect:/add-address";
        }
        model.addAttribute("user", userRepo.findByEmail(user.getEmail()));
        ProductDetail productDetail = productDetailRepository.getOneProductDetail(orderRequest.getProId(), orderRequest.getColorId(), orderRequest.getSizeId());
        model.addAttribute("listItem", productDetail);
        model.addAttribute("quantity", orderRequest.getQuantity());
        model.addAttribute("proId", orderRequest.getProId());
        model.addAttribute("sizeId", orderRequest.getSizeId());
        model.addAttribute("colorId", orderRequest.getColorId());
        model.addAttribute("selectedAddressId",address.getId());
        model.addAttribute("addresses",allAddressByCusId);
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
        if (user==null){
            return "redirect:/login";
        }
        Address address = addressRepository.findByCustomerId(user.getId());
        List<Address> allAddressByCusId = addressRepository.findByCusId(user.getId());
        if (allAddressByCusId.size()==0){
            return "redirect:/add-address";
        }
        model.addAttribute("user", userRepo.findByEmail(user.getEmail()));
        model.addAttribute("listItem", shoppingCartService.getAllItems(user.getId()));
        model.addAttribute("total", shoppingCartService.getAmount());
        model.addAttribute("selectedAddressId",address.getId());
        model.addAttribute("addresses",allAddressByCusId);
        return "checkout";
    }

    @PostMapping("/checkout")
    public String createOrder(@RequestParam("name_receiver") String nameReceiver, @RequestParam("phone_number_receiver") String phoneReceiver, @RequestParam(value = "address_receiver") Integer addressReceiverRequest, @RequestParam("note") String note, @RequestParam("price") String price, @RequestParam("userId") Integer id, @RequestParam(value = "proId",required = false) int proId, @RequestParam(value = "sizeId",required = false) int sizeId, @RequestParam(value = "colorId",required = false) int colorId, @RequestParam(value = "quantity",required = false) int quantity, @RequestParam(value = "check",required = false) int check) {
        if(id ==null){
            return "redirect:/login";
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        }

        // Lấy User từ email
        Customer user = userRepo.findByEmail(email);
        if (user==null){
            return "redirect:/login";
        }
        Address address = addressRepository.findById(addressReceiverRequest).get();
        Order order = orderService.createOrder(nameReceiver, phoneReceiver, address.getNameAddress(), note, price, id);
            orderDetailService.createOrderDetail(proId, order.getId(), quantity, sizeId, colorId);
            return "success";

    }
    @PostMapping("/checkout-by-cart")
    public String createOrderByCart(@RequestParam("name_receiver") String nameReceiver, @RequestParam("phone_number_receiver") String phoneReceiver, @RequestParam(value = "address_receiver") Integer addressReceiverRequest, @RequestParam("note") String note, @RequestParam("price") String price, @RequestParam("userId") Integer id) {
        if(id ==null){
            return "redirect:/login";
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        }

        // Lấy User từ email
        Customer user = userRepo.findByEmail(email);
        if (user==null){
            return "redirect:/login";
        }
        Address address = addressRepository.findById(addressReceiverRequest).get();

        Order order = orderService.createOrder(nameReceiver, phoneReceiver, address.getNameAddress(), note, price, id);
        Cart cart = cartService.getOneByUserId(user.getId());
            Collection<CartItem> cartItemMap = shoppingCartService.getAllItemsByCartId(cart.getId());
            for (var item : cartItemMap
            ) {
                Product productDto = productService.getDetailProductById(item.getProductDetail().getProduct().getId());
                Product product = new ModelMapper().map(productDto, Product.class);
                orderDetailService.createOrderDetail(product.getId(), order.getId(), item.getQuantity(), item.getProductDetail().getSize().getId(), item.getProductDetail().getColor().getId());
            }
            shoppingCartService.clear(cart.getId());
            return "success";
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
        if (user==null){
            return "redirect:/login";
        }
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
        Customer newUser = new Customer();
        Random random = new Random();
        int randomDigits = 100 + random.nextInt(900);
        newUser.setCode("KH"+randomDigits);
        newUser.setEmail(email);
        newUser.setPassword(password); // Không mã hóa mật khẩu
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setPhoneNumber(phoneNumber);
//        newUser.setStatus(true); // Hoặc set tùy thuộc vào logic của bạn

        customerRepository.save(newUser);

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
            return "redirect:/login";
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
