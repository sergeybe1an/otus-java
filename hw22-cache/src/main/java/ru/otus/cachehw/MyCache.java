package ru.otus.cachehw;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyCache<K, V> implements HwCache<K, V> {

    private static Logger logger = LoggerFactory.getLogger(MyCache.class.getName());
    private final Map<K, V> weakMap = new WeakHashMap<>();
    private final Set<HwListener<K, V>> listeners = new HashSet<>();

    @Override
    public void put(K key, V value) {
        executeListeners(key, value, "put");
        weakMap.put(key, value);
    }

    @Override
    public void remove(K key) {
        executeListeners(key, get(key), "remove");
        weakMap.remove(key);
    }

    @Override
    public V get(K key) {
        V val = weakMap.get(key);
        executeListeners(key, val, "get");
        return val;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void executeListeners(K key, V value, String action) {
        listeners.forEach(listener -> {
            try {
                listener.notify(key, value, action);
            } catch (Throwable e) {
                logger.error("Error while executing listener with action {} for key {}: {}", action, key, e.getMessage());
            }
        });
    }
}
