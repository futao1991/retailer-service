local record_id = KEYS[1]
local record_num = KEYS[2]
local order_id = KEYS[3]

if (redis.call('HEXISTS', 'retailer-warehouse', record_id) == 0) then
    return '\"ERROR_WAREHOUSE_NOTEXIST\"'
end

local total_num = redis.call('HGET', 'retailer-warehouse', record_id)
if (tonumber(total_num) < tonumber(record_num)) then
    return '\"ERROR_NOT_ENOUGH_WAREHOUSE\"'
end

redis.call('HSET', 'retailer-warehouse', record_id, total_num - record_num)
redis.call('HSET', 'fronzen-warehouse', order_id, record_num)
return '\"OK\"'
