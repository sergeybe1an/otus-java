package ru.otus.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.Denominations;

/**
 * Банковская ячейка
 */
public class AtmSlot {
    private static final Logger logger = LoggerFactory.getLogger(AtmSlot.class.getName());
    private final int ZERO = 0;
    private final Denominations denomination;
    private long banknotesQuantity;

    public AtmSlot(Denominations denomination) {
        this.denomination = denomination;
    }

    /**
     * Внесение банкнот
     * @param banknotesQuantity количество банкнот
     */
    public void contributeBanknotes(long banknotesQuantity) {
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
    public long receiveBanknotes(long banknotesQuantity) {
        if (this.banknotesQuantity < banknotesQuantity) {
            throw new IllegalArgumentException("Вы запрашиваете слишком большое количество банкнот!");
        } else {
            this.banknotesQuantity -= banknotesQuantity;
            logger.info("Выдано {} банкнот номиналом {}, осталось {}",
                banknotesQuantity, denomination.getValue(), this.banknotesQuantity);
            return banknotesQuantity;
        }
    }

    public Denominations getDenomination() {
        return denomination;
    }

    public long getBanknotesQuantity() {
        return banknotesQuantity;
    }
}
