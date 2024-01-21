package ru.otus;

import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private final TreeMap<Customer, String> customerMap = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> entry = customerMap.firstEntry();
        return entry != null ? copyEntry(entry) : null;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = customerMap.higherEntry(customer);
        return entry != null ? copyEntry(entry) : null;
    }

    public void add(Customer customer, String data) {
        customerMap.put(customer, data);
    }

    private Map.Entry<Customer, String> copyEntry(Map.Entry<Customer, String> entry) {
        return Map.entry(entry.getKey().clone(), entry.getValue());
    }
}
