package ru.otus;

import java.util.Map;

public interface Atm {

    /**
     * Внесение банкнот
     * @param banknotesMap Номинал банкноты-Количество
     */
    void contributeBanknotes(Map<Denominations, Integer> banknotesMap);

    /**
     * Получение банкнот
     * @param money Количество запрашиваемых денег
     * @return По выданым купюрам - Номинал купюры и их количесво
     */
    Map<Denominations, Long> receiveBanknotes(long money);

    /**
     * Получение остатка всех денег в банкомате
     */
    long getBanknotesSum();
}
