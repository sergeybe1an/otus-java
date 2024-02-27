package ru.otus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AtmTest {

    private final Set<Denominations> denominationsSet =
        new HashSet<>(Arrays.asList(Denominations.ONE_HUNDRED, Denominations.ONE_THOUSAND));

    @DisplayName("Тест внесения банкнот в терминал")
    @Test
    void contributeBanknotesTest() {
        Atm atm = new Atm(denominationsSet);

        Map<Denominations, Integer> banknotesMap = new TreeMap<>() {{
            put(Denominations.ONE_HUNDRED, 3);
            put(Denominations.ONE_THOUSAND, 2);
        }};
        atm.contributeBanknotes(banknotesMap);

        assertEquals(2300, atm.getBanknotesSum());
    }

    @DisplayName("Тест внесения неподдерживаемой банкноты терминалом")
    @Test
    void contributeWrongBanknotesTest() {
        Atm atm = new Atm(denominationsSet);

        Map<Denominations, Integer> banknotesMap = new TreeMap<>() {{
            put(Denominations.FIVE_HUNDRED, 3);
            put(Denominations.ONE_THOUSAND, 2);
        }};

        assertThrows(IllegalArgumentException.class, () -> atm.contributeBanknotes(banknotesMap));
    }

    @DisplayName("Тест снятия банкнот с терминала")
    @Test
    void receiveBanknotesTest() {
        Atm atm = new Atm(denominationsSet);

        Map<Denominations, Integer> banknotesMap = new TreeMap<>() {{
            put(Denominations.ONE_HUNDRED, 3);
            put(Denominations.ONE_THOUSAND, 2);
        }};
        atm.contributeBanknotes(banknotesMap);

        atm.receiveBanknotes(1000);

        assertEquals(1300, atm.getBanknotesSum());
    }

    @DisplayName("Тест снятия с терминала отрицательной суммы")
    @Test
    void receiveBanknotesNegativeSumTest() {
        Atm atm = new Atm(denominationsSet);

        Map<Denominations, Integer> banknotesMap = new TreeMap<>() {{
            put(Denominations.ONE_HUNDRED, 3);
            put(Denominations.ONE_THOUSAND, 2);
        }};
        atm.contributeBanknotes(banknotesMap);

        assertThrows(IllegalArgumentException.class, () -> atm.receiveBanknotes(-1000));
    }

    @DisplayName("Тест снятия с терминала неправильной суммы")
    @Test
    void receiveBanknotesWrongSumTest() {
        Atm atm = new Atm(denominationsSet);

        Map<Denominations, Integer> banknotesMap = new TreeMap<>() {{
            put(Denominations.ONE_HUNDRED, 3);
            put(Denominations.ONE_THOUSAND, 2);
        }};
        atm.contributeBanknotes(banknotesMap);

        assertThrows(IllegalArgumentException.class, () -> atm.receiveBanknotes(400));
    }

    @DisplayName("Тест снятия банкнот с терминала, длинный сценарий")
    @Test
    void receiveBanknotesLongScenario1_Test() {
        Atm atm = new Atm(denominationsSet);

        //добавляем 3*100 и 2*1000
        Map<Denominations, Integer> banknotesMap1 = new TreeMap<>() {{
            put(Denominations.ONE_HUNDRED, 3);
            put(Denominations.ONE_THOUSAND, 2);
        }};
        atm.contributeBanknotes(banknotesMap1);

        //получаем 1000, ожидаем, что вернется 1*1000, остаток 1*1000 и 3*100
        atm.receiveBanknotes(1000);

        assertEquals(1300, atm.getBanknotesSum());

        //добавляем 5*100 и 6*1000
        Map<Denominations, Integer> banknotesMap2 = new TreeMap<>() {{
            put(Denominations.ONE_HUNDRED, 5);
            put(Denominations.ONE_THOUSAND, 6);
        }};
        atm.contributeBanknotes(banknotesMap2);

        //получаем 6000, ожидаем, что вернется 6*1000, остаток 1*1000 и 8*100
        atm.receiveBanknotes(6000);

        assertEquals(1800, atm.getBanknotesSum());

        //добавляем 5*100, остаток 1*1000 и 13*100
        Map<Denominations, Integer> banknotesMap3 = new TreeMap<>() {{
            put(Denominations.ONE_HUNDRED, 5);
        }};
        atm.contributeBanknotes(banknotesMap3);

        assertEquals(2300, atm.getBanknotesSum());

        //получаем 2000, ожидаем, что вернется 1*1000 и 10*100, остаток 3*100
        atm.receiveBanknotes(2000);
        assertEquals(300, atm.getBanknotesSum());
    }
}
