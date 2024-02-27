package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Банковская ячейка
 */
public class AtmSlot {
    private static final Logger logger = LoggerFactory.getLogger(AtmSlot.class.getName());
    private final int ZERO = 0;
    private final Denominations denomination;
    private int banknotesQuantity;

    public AtmSlot(Denominations denomination) {
        this.denomination = denomination;
    }

    /**
     * Внесение банкнот
     * @param banknotesQuantity количество банкнот
     */
    public void contributeBanknotes(int banknotesQuantity) {
        if (banknotesQuantity < ZERO) {
            throw new IllegalArgumentException("Количество банкнот не может быть меньше 0!");
        } else {
            this.banknotesQuantity += banknotesQuantity;
            logger.info("Внесено {} банкнот номиналом {}, теперь всего {}",
                banknotesQuantity, denomination.getValue(), this.banknotesQuantity);
        }
    }

    /**
     * Получение банкнот
     * @param banknotesQuantity количество банкнот
     */
    public void receiveBanknotes(int banknotesQuantity) {
        if (this.banknotesQuantity < banknotesQuantity) {
            throw new IllegalArgumentException("Вы запрашиваете слишком большое количество банкнот!");
        } else {
            this.banknotesQuantity -= banknotesQuantity;
            logger.info("Выдано {} банкнот номиналом {}, осталось {}",
                banknotesQuantity, denomination.getValue(), this.banknotesQuantity);
        }
    }

    public Denominations getDenomination() {
        return denomination;
    }

    public int getBanknotesQuantity() {
        return banknotesQuantity;
    }
}
