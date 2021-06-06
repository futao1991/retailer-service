local record_id = KEYS[1]
local order_id = KEYS[2]

if (redis.call('HEXISTS', 'retailer-warehouse', record_id) == 0) then
return '\"ERROR_WAREHOUSE_NOTEXIST\"'
end

local total_num = redis.call('HGET', 'retailer-warehouse', record_id)
if (redis.call('HEXISTS', 'fronzen-warehouse', order_id) == 0) then
return '\"ERROR_WAREHOUSE_NOTEXIST\"'
end

local fronzen_num = redis.call('HGET', 'fronzen-warehouse', order_id)
redis.call('HSET', 'retailer-warehouse', record_id, total_num + fronzen_num)
redis.call('HDEL', 'fronzen-warehouse', order_id)
return '\"OK\"'
