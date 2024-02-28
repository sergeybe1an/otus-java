package ru.otus;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import ru.otus.storage.AtmSlot;
import ru.otus.storage.Storage;

/**
 * Банкомат
 */
public class AtmImpl implements Atm {

    private final Storage<Denominations, AtmSlot> storage;
    private final int ZERO = 0;

    public AtmImpl(Storage<Denominations, AtmSlot> storage) {
        this.storage = storage;
    }

    /**
     * Внесение банкнот
     * @param banknotesMap Номинал банкноты-Количество
     */
    public void contributeBanknotes(Map<Denominations, Integer> banknotesMap) {
        validateContributingBanknotes(banknotesMap.keySet());

        for (Map.Entry<Denominations, Integer> banknotes : banknotesMap.entrySet()) {
            var slot = storage.get(banknotes.getKey());
            slot.contributeBanknotes(banknotes.getValue());
        }
    }

    /**
     * Получение банкнот
     * @param money Количество запрашиваемых денег
     * @return По выданым купюрам - Номинал купюры и их количесво
     */
    public Map<Denominations, Long> receiveBanknotes(long money) {
        validateReceivingBanknotes(money);

        Map<Denominations, Long> banknotesForReceiving = calculateBanknotesQuantity(money);

        for (Map.Entry<Denominations, Long> banknotes : banknotesForReceiving.entrySet()) {
            var slot = storage.get(banknotes.getKey());
            slot.receiveBanknotes(banknotes.getValue());
        }

        return banknotesForReceiving;
    }

    /**
     * Получение остатка всех денег в банкомате
     */
    public long getBanknotesSum() {
        return storage.values().stream()
            .map(slot -> slot.getBanknotesQuantity() * slot.getDenomination().getValue())
            .reduce(0L, Long::sum);
    }

    private void validateContributingBanknotes(Set<Denominations> banknotes) {
        if (!banknotes.stream().allMatch(storage::containsKey)) {
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

    private Map<Denominations, Long> calculateBanknotesQuantity(long money) {
        Map<Denominations, Long> receivingBanknotes = new TreeMap<>();
        for (Map.Entry<Denominations, AtmSlot> banknotes : storage.entrySet()) {
            var slot = banknotes.getValue();
            var neededBanknotes = money / slot.getDenomination().getValue();

            if (money % slot.getDenomination().getValue() == ZERO) {
                if (slot.getBanknotesQuantity() >= neededBanknotes) {
                    receivingBanknotes.put(slot.getDenomination(), neededBanknotes);
                    money = ZERO;
                    break;
                } else {
                    money -= slot.getBanknotesQuantity() * slot.getDenomination().getValue();
                    receivingBanknotes.put(slot.getDenomination(), slot.getBanknotesQuantity());
                }

            } else {
                receivingBanknotes.put(slot.getDenomination(), Math.min(slot.getBanknotesQuantity(), neededBanknotes));
                money -= neededBanknotes * slot.getDenomination().getValue();
            }
        }

        if (money > ZERO) {
            throw new IllegalArgumentException("Недостаточно банкнот для выдачи запрашиваемой суммы!");
        }
        return receivingBanknotes;
    }
}
