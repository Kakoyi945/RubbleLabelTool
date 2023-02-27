package com.label.rubblelabeltool.util;

import java.util.HashMap;
import java.util.Map;

public class BMap<K, V> {
    private final Map<K, V> KVmap = new HashMap<>();
    private final Map<V, K> VKmap = new HashMap<>();

    /**
     * 采用懒汉式方法创建sessionImageMap单例
     * 用于维护<图片id, session>关系
     * 保证每个session互斥地访问图片
     */
    private static volatile BMap<Integer, String> imageSessionMap;

    public static BMap<Integer, String> getImageSessionMap(){
        if(imageSessionMap == null){
            synchronized (BMap.class){
                if(imageSessionMap == null){
                    imageSessionMap = new BMap<>();
                }
            }
        }
        return imageSessionMap;
    }

    public void removeByKey(K key) {
        VKmap.remove(KVmap.remove(key));
    }

    public void removeByValue(V value) {
        KVmap.remove(VKmap.remove(value));

    }

    public boolean containsKey(K key) {
        return KVmap.containsKey(key);
    }

    public boolean containsValue(V value) {
        return VKmap.containsKey(value);
    }

    public void replace(K key, V value) {
        // 对于双射关系, 将会删除交叉项
        removeByKey(key);
        removeByValue(value);
        KVmap.put(key, value);
        VKmap.put(value, key);
    }

    public V getByKey(K key) {
        return KVmap.get(key);
    }

    public K getByValue(V value) {
        return VKmap.get(value);
    }
}
