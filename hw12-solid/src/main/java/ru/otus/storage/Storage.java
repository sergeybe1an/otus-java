package ru.otus.storage;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

public interface Storage<K, V> {

    V get(K key);

    Collection<V> values();

    Boolean containsKey(K key);

    Set<Entry<K, V>> entrySet();
}
