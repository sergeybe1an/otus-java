package ru.otus;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Банкомат
 */
public class Atm {

    private final Map<Denominations, AtmSlot> slots;
    private final int ZERO = 0;

    /**
     * Ячейки являются частью банкомата, поэтому тесное связывание
     * @param denominations Список номиналов купюр
     */
    public Atm(Set<Denominations> denominations) {
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

    /**
     * Внесение банкнот
     * @param banknotesMap Номинал банкноты-Количество
     */
    public void contributeBanknotes(Map<Denominations, Integer> banknotesMap) {
        validateContributingBanknotes(banknotesMap.keySet());

        for (Map.Entry<Denominations, Integer> banknotes : banknotesMap.entrySet()) {
            var slot = slots.get(banknotes.getKey());
            slot.contributeBanknotes(banknotes.getValue());
        }
    }

    /**
     * Получение банкнот
     * @param money Количество запрашиваемых денег
     */
    public void receiveBanknotes(long money) {
        validateReceivingBanknotes(money);

        for (Map.Entry<Denominations, Integer> banknotes : calculateBanknotesQuantity(money).entrySet()) {
            var slot = slots.get(banknotes.getKey());
            slot.receiveBanknotes(banknotes.getValue());
        }
    }

    /**
     * Получение остатка всех денег в банкомате
     */
    public long getBanknotesSum() {
        return slots.values().stream()
            .map(slot -> slot.getBanknotesQuantity() * slot.getDenomination().getValue())
            .map(Integer::longValue)
            .reduce(0L, Long::sum);
    }

    private void validateContributingBanknotes(Set<Denominations> banknotes) {
        if (!banknotes.stream().allMatch(slots::containsKey)) {
            throw new IllegalArgumentException("Вы пытаетесь вставить купюру, которая не поддерживаются банкоматом!");
        }
    }

    private void validateReceivingBanknotes(long money) {
        if (money < ZERO) {
            throw new IllegalArgumentException("Нельзя снять отрицательное количество денег!");
        }

        if (money > getBanknotesSum()) {
            throw new IllegalArgumentException("Вы пытаетесь снять больше денег, чем есть в банкомате!");
        }
    }

    private Map<Denominations, Integer> calculateBanknotesQuantity(long money) {
        Map<Denominations, Integer> receivingBanknotes = new TreeMap<>();
        for (Map.Entry<Denominations, AtmSlot> banknotes : slots.entrySet()) {
            var slot = banknotes.getValue();
            var neededBanknotes = (int) (money / slot.getDenomination().getValue());

            if (money % slot.getDenomination().getValue() == ZERO) {
                if (slot.getBanknotesQuantity() >= neededBanknotes) {
                    receivingBanknotes.put(slot.getDenomination(), neededBanknotes);
                    money = ZERO;
                    break;
                } else {
                    money -= (long) slot.getBanknotesQuantity() * slot.getDenomination().getValue();
                    receivingBanknotes.put(slot.getDenomination(), slot.getBanknotesQuantity());
                }

            } else {
                receivingBanknotes.put(slot.getDenomination(), Math.min(slot.getBanknotesQuantity(), neededBanknotes));
                money -= (long) neededBanknotes * slot.getDenomination().getValue();
            }
        }

        if (money > ZERO) {
            throw new IllegalArgumentException("Недостаточно банкнот для выдачи запрашиваемой суммы!");
        }
        return receivingBanknotes;
    }
}
