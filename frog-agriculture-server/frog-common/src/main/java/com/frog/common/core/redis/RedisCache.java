package com.frog.common.core.redis;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import static java.util.regex.Pattern.compile;

/**
 * spring redis 工具类
 *
 * @author ruoyi
 **/
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Component
public class RedisCache {
    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public long deleteObject(final Collection collection) {
        return redisTemplate.delete(collection);
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    public <T> long setVCacheList(final String key, final List<T> value) {
        Long count = 0L;
        for (T t : value) {
            count += redisTemplate.opsForList().rightPush(key, t);
        }
        return count;
    }
    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 删除Hash中的数据
     *
     * @param key
     * @param hkey
     */
    public void delCacheMapValue(final String key, final String hkey) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.delete(key, hkey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 是否存在key
     *
     * @param key 缓存key
     * @return true：存在key ；false：key不存在或者已过期
     */
    public boolean containsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 根据前缀获取所有的key
     * 例如：pro_*
     */
    public Set<String> getListKeyByPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix.concat("*"));
        return keys;
    }

    /**
     * 匹配获取键值对，ScanOptions.NONE为获取全部键对
     *
     * @param key
     * @param options
     * @return
     */
    public Cursor<Map.Entry<Object, Object>> hashScan(String key, ScanOptions options) {
        return redisTemplate.opsForHash().scan(key, options);
    }

    /**
     * 获取所有键值对集合
     *
     * @param key
     */
    public Map hashEntity(String key) {
        return redisTemplate.boundHashOps(key).entries();
    }

    /**
     * 以map集合的形式添加键值对
     *
     * @param key
     * @param maps
     */
    public void hashPutAll(String key, Map<String, String> maps) {
        redisTemplate.opsForHash().putAll(key, maps);
    }

    /**
     * 批量获取设备物模型值
     *
     * @param keys          键的集合
     * @param hkeyCondition 筛选字段
     * @return
     */
    public Map<String, Map> hashGetAllByKeys(Set<String> keys, String hkeyCondition) {
        return (Map<String, Map>) redisTemplate.execute((RedisCallback) con -> {
            Iterator<String> it = keys.iterator();
            Map<String, Map> mapList = new HashMap<>();
            while (it.hasNext()) {
                String key = it.next();
                Map<byte[], byte[]> result = con.hGetAll(key.getBytes());
                Map ans;
                if (CollectionUtils.isEmpty(result)) {
                    return new HashMap<>(0);
                }
                ans = new HashMap<>(result.size());
                for (Map.Entry entry : result.entrySet()) {
                    String field = new String((byte[]) entry.getKey());
                    if (!"".equals(hkeyCondition)) {
                        if (field.endsWith(hkeyCondition)) {
                            ans.put(new String((byte[]) entry.getKey()), new String((byte[]) entry.getValue()));
                        }
                    } else {
                        ans.put(new String((byte[]) entry.getKey()), new String((byte[]) entry.getValue()));
                    }
                }
                mapList.put(key, ans);
            }
            return mapList;
        });
    }

    /**
     * 批量获取匹配触发器的物模型值（定时告警使用）
     *
     * @param keys         键的集合
     * @param operator     操作符
     * @param triggerValue 触发的值
     * @return
     */
    public Map<String, String> hashGetAllMatchByKeys(Set<String> keys, String operator, String id, String triggerValue) {
        return (Map<String, String>) redisTemplate.execute((RedisCallback) con -> {
            Iterator<String> it = keys.iterator();
            Map<String, String> mapList = new HashMap<>();
            while (it.hasNext()) {
                String key = it.next();
                Map<byte[], byte[]> result = con.hGetAll(key.getBytes());
                if (CollectionUtils.isEmpty(result)) {
                    return new HashMap<>(0);
                }
                for (Map.Entry entry : result.entrySet()) {
                    String field = new String((byte[]) entry.getKey());
                    // 获取物模型值并且匹配规则
                    if (field.equals(id + "#V")) {
                        String value = new String((byte[]) entry.getValue()).replace("\"", "");
                        if (ruleResult(operator, value, triggerValue)) {
                            mapList.put(key, value);
                        }
                    }
                }
            }
            return mapList;
        });
    }

    /**
     * 根据key集合获取字符串
     *
     * @param keys 键的集合
     * @return
     */
    public Map<String, String> getStringAllByKeys(Set<String> keys) {
        return (Map<String, String>) redisTemplate.execute((RedisCallback) con -> {
            Iterator<String> it = keys.iterator();
            Map<String, String> mapList = new HashMap<>();
            while (it.hasNext()) {
                String key = it.next();
                byte[] result = con.get(key.getBytes());
                if (result == null) {
                    return new HashMap<>(0);
                }
                String ans = new String((byte[]) result);
                mapList.put(key, ans);
            }
            return mapList;
        });
    }

    /**
     * 根据条件返回所有键
     * @param query
     * @return
     */
    public List<Object> scan(String query) {
        Set<String> keys = (Set<String>) redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(query).count(1000).build());
            while (cursor.hasNext()) {
                keysTmp.add(new String(cursor.next()));
            }
            return keysTmp;
        });
        return new ArrayList<>(keys);
    }

    /**
     * 规则匹配结果
     *
     * @param operator     操作符
     * @param value        上报的值
     * @param triggerValue 触发器的值
     * @return
     */
    private boolean ruleResult(String operator, String value, String triggerValue) {
        boolean result = false;
        if("".equals(value)){
            return result;
        }
        // 操作符比较
        switch (operator) {
            case "=":
                result = value.equals(triggerValue);
                break;
            case "!=":
                result = !value.equals(triggerValue);
                break;
            case ">":
                if (isNumeric(value) && isNumeric(triggerValue)) {
                    result = Double.parseDouble(value) > Double.parseDouble(triggerValue);
                }
                break;
            case "<":
                if (isNumeric(value) && isNumeric(triggerValue)) {
                    result = Double.parseDouble(value) < Double.parseDouble(triggerValue);
                }
                break;
            case ">=":
                if (isNumeric(value) && isNumeric(triggerValue)) {
                    result = Double.parseDouble(value) >= Double.parseDouble(triggerValue);
                }
                break;
            case "<=":
                if (isNumeric(value) && isNumeric(triggerValue)) {
                    result = Double.parseDouble(value) <= Double.parseDouble(triggerValue);
                }
                break;
            case "contain":
                result = value.contains(triggerValue);
                break;
            case "notcontain":
                result = !value.contains(triggerValue);
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 判断字符串是否为整数或小数
     */
    private boolean isNumeric(String str) {
        Pattern pattern = compile("[0-9]*\\.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于 0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }
}
