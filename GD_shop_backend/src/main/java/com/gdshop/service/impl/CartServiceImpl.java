package com.gdshop.service.impl;

import com.gdshop.dto.Result;
import com.gdshop.entity.Sku;
import com.gdshop.service.ICartService;
import com.gdshop.service.ISkuService;
import com.gdshop.utils.RedisConstants;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements ICartService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ISkuService skuService;

    @Override
    public Result addToCart(String sessionId, Long skuId, Integer quantity) {
        String cartKey = RedisConstants.CART_KEY + sessionId;
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        String existing = ops.get(cartKey, skuId.toString());
        if (existing != null) {
            int oldQty = Integer.parseInt(existing);
            quantity = oldQty + quantity;
        }
        ops.put(cartKey, skuId.toString(), quantity.toString());
        return Result.ok();
    }

    @Override
    public Result queryCart(String sessionId) {
        String cartKey = RedisConstants.CART_KEY + sessionId;
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(cartKey);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            Long skuId = Long.valueOf(entry.getKey().toString());
            int qty = Integer.parseInt(entry.getValue().toString());
            Sku sku = skuService.getById(skuId);
            if (sku != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("skuId", sku.getId());
                item.put("spuId", sku.getSpuId());
                item.put("name", sku.getName());
                item.put("specs", sku.getSpecs());
                item.put("price", sku.getPrice());
                item.put("image", sku.getImage());
                item.put("quantity", qty);
                item.put("stock", sku.getStock());
                result.add(item);
            }
        }
        return Result.ok(result);
    }

    @Override
    public Result removeFromCart(String sessionId, Long skuId) {
        stringRedisTemplate.opsForHash().delete(RedisConstants.CART_KEY + sessionId, skuId.toString());
        return Result.ok();
    }

    @Override
    public Result updateQuantity(String sessionId, Long skuId, Integer quantity) {
        if (quantity <= 0) {
            return removeFromCart(sessionId, skuId);
        }
        stringRedisTemplate.opsForHash().put(RedisConstants.CART_KEY + sessionId, skuId.toString(), quantity.toString());
        return Result.ok();
    }

    @Override
    public Result clearCart(String sessionId) {
        stringRedisTemplate.delete(RedisConstants.CART_KEY + sessionId);
        return Result.ok();
    }
}
