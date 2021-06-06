local order_id = KEYS[1]
local order_msg = KEYS[2]
local count = KEYS[3]
local key = string.format("retailer-order-%s", order_id)

redis.call('SET', key, order_msg)
redis.call('HSET', 'temp-ordercount', key, count)
return '\"OK\"'