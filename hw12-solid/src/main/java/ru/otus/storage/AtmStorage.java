package ru.otus.storage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import ru.otus.Denominations;

/**
 * Хранилище ячеек банкомата
 */
public class AtmStorage implements Storage<Denominations, AtmSlot> {

    private final Map<Denominations, AtmSlot> slots;

    public AtmStorage(Set<Denominations> denominations) {
        Supplier<TreeMap<Denominations, AtmSlot>> mapSupplier = () -> new TreeMap<>(Comparator.reverseOrder());

        slots = denominations.stream()
            .map(AtmSlot::new)
            .collect(Collectors.toMap(
                AtmSlot::getDenomination,
                atmSlot -> atmSlot,
                (key1, key2) -> key2,
                mapSupplier
            ));
    }

    @Override
    public AtmSlot get(Denominations key) {
        return slots.get(key);
    }

    @Override
    public Collection<AtmSlot> values() {
        return slots.values();
    }

    @Override
    public Boolean containsKey(Denominations key) {
        return slots.containsKey(key);
    }

    @Override
    public Set<Entry<Denominations, AtmSlot>> entrySet() {
        return slots.entrySet();
    }
}
