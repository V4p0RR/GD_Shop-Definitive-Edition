package com.gdshop.controller;

import com.gdshop.dto.Result;
import com.gdshop.service.ICartService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Resource
    private ICartService cartService;

    @PostMapping("/add")
    public Result addToCart(@RequestBody Map<String, Object> params) {
        String sessionId = (String) params.get("sessionId");
        Long skuId = Long.valueOf(params.get("skuId").toString());
        Integer quantity = Integer.valueOf(params.get("quantity").toString());
        return cartService.addToCart(sessionId, skuId, quantity);
    }

    @GetMapping("/list")
    public Result queryCart(@RequestParam String sessionId) {
        return cartService.queryCart(sessionId);
    }

    @DeleteMapping("/remove")
    public Result removeFromCart(@RequestParam String sessionId, @RequestParam Long skuId) {
        return cartService.removeFromCart(sessionId, skuId);
    }

    @PutMapping("/update")
    public Result updateQuantity(@RequestBody Map<String, Object> params) {
        String sessionId = (String) params.get("sessionId");
        Long skuId = Long.valueOf(params.get("skuId").toString());
        Integer quantity = Integer.valueOf(params.get("quantity").toString());
        return cartService.updateQuantity(sessionId, skuId, quantity);
    }

    @DeleteMapping("/clear")
    public Result clearCart(@RequestParam String sessionId) {
        return cartService.clearCart(sessionId);
    }
}
