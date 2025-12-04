package ma.ecommerce.frontservice.web;

import jakarta.validation.Valid;
import ma.ecommerce.frontservice.dto.CreateOrderResponse;
import ma.ecommerce.frontservice.dto.ProductDto;
import ma.ecommerce.frontservice.exception.FrontendException;
import ma.ecommerce.frontservice.service.ShopService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/")
    public String home(Model model) {
        try {
            List<ProductDto> products = shopService.getProducts();
            model.addAttribute("products", products);
        } catch (FrontendException ex) {
            model.addAttribute("products", Collections.emptyList());
            model.addAttribute("errorMessage", ex.getMessage());
        }
        return "index";
    }

    @GetMapping("/products/{id}")
    public String product(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ProductDto product = shopService.getProduct(id);
            OrderForm orderForm = new OrderForm();
            orderForm.setProductId(id);
            orderForm.setQuantity(1);
            orderForm.setPaymentMethod("CARD");
            model.addAttribute("product", product);
            model.addAttribute("orderForm", orderForm);
            return "product";
        } catch (FrontendException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/orders")
    public String createOrder(@Valid @ModelAttribute("orderForm") OrderForm orderForm,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        ProductDto product;
        try {
            product = shopService.getProduct(orderForm.getProductId());
        } catch (FrontendException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("product", product);
            return "product";
        }

        try {
            CreateOrderResponse response = shopService.placeOrder(orderForm);
            redirectAttributes.addFlashAttribute("product", product);
            redirectAttributes.addFlashAttribute("orderResponse", response);
            redirectAttributes.addFlashAttribute("customerEmail", orderForm.getEmail());
            return "redirect:/orders/confirmation";
        } catch (FrontendException ex) {
            model.addAttribute("product", product);
            model.addAttribute("errorMessage", ex.getMessage());
            return "product";
        }
    }

    @GetMapping("/orders/confirmation")
    public String confirmation(Model model) {
        if (!model.containsAttribute("orderResponse")) {
            return "redirect:/";
        }
        return "order-result";
    }
}
