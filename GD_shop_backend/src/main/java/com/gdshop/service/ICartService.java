package com.gdshop.service;

import com.gdshop.dto.Result;

public interface ICartService {
    Result addToCart(String sessionId, Long skuId, Integer quantity);
    Result queryCart(String sessionId);
    Result removeFromCart(String sessionId, Long skuId);
    Result updateQuantity(String sessionId, Long skuId, Integer quantity);
    Result clearCart(String sessionId);
}
