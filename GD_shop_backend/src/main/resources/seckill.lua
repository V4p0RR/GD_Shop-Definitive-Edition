-- 秒杀资格判断Lua脚本

local activityId = ARGV[1]
local userId = ARGV[2]
local orderId = ARGV[3]

local stockKey = 'seckill:stock:' .. activityId
local orderKey = 'seckill:order:' .. activityId

-- 1. 判断库存是否充足
local stock = redis.call('get', stockKey)
if (not stock or tonumber(stock) <= 0) then
    return 1
end

-- 2. 判断用户是否已秒杀
if (redis.call('sismember', orderKey, userId) == 1) then
    return 2
end

-- 3. 扣减库存
redis.call('incrby', stockKey, -1)

-- 4. 记录用户
redis.call('sadd', orderKey, userId)

-- 5. 发送订单信息到队列，异步落库
redis.call('lpush', 'seckill:order:queue', activityId .. ':' .. userId .. ':' .. orderId)

-- 6. 返回0表示成功
return 0
