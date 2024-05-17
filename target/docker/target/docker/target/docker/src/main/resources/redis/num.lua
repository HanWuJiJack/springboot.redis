---
--- Created by Gjing.
--- DateTime: 2019/6/21 10:49
---
--- 获取key
local key = KEYS[1]
--- 获取一个参数
local userID = ARGV[1]
--- 如果redis找不到这个key就去插入
--- 获取key
-- 是否存在
local existence = redis.call("hget", userID, key)
-- 如果存在 返回3
if existence == "1" then
    return 3
else
     redis.call("hset", userID, key,"1")
--      设置过时 时间秒
--      redis.call("expire", userID, 10)
end

-- lua里面 nil 返回的是 false
local inventory = redis.call("get", key)
if inventory == false then
   return 2
else
   if tonumber(inventory) > 0 then
    -- 库存减去1
        redis.call('incrby', key, -1);
        return 1
    else
        return 2
    end
end