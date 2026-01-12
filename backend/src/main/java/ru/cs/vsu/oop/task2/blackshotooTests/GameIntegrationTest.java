package ru.cs.vsu.oop.task2.blackshotooTests;

import org.junit.jupiter.api.Test;
import ru.cs.vsu.oop.task2.blackshotoo141.Color;
import ru.cs.vsu.oop.task2.blackshotoo141.Player;
import ru.cs.vsu.oop.task2.blackshotoo141.Property;
import ru.cs.vsu.oop.task2.blackshotoo141.PropertyType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционные тесты для проверки взаимодействия компонентов
 */
public class GameIntegrationTest {

    /**
     * Тест полного цикла: игрок покупает недвижимость, строит дома,
     * другой игрок платит аренду
     */
    @Test
    void testPropertyOwnershipAndRent() {
        // Создаем игроков
        Player owner = new Player("Владелец", 2000);
        Player renter = new Player("Арендатор", 1000);

        // Создаем недвижимость
        Property property = new Property("Тестовая улица", Color.BROWN,
                PropertyType.PROPERTY, 100, 10, 50);

        // Владелец покупает недвижимость
        owner.buyProperty(property);
        assertEquals(owner, property.getOwner());
        assertEquals(1900, owner.getMoney());

        // Арендатор платит аренду
        int rent = property.calculateRent();
        renter.pay(rent);
        owner.receiveMoney(rent);

        assertEquals(1000 - rent, renter.getMoney());
        assertEquals(1900 + rent, owner.getMoney());
    }

    /**
     * Тест монополии и строительства
     */
    @Test
    void testMonopolyAndBuilding() {
        Player player = new Player("Тест", 2000);

        // Создаем две коричневые улицы (монополия)
        Property prop1 = new Property("Коричневая 1", Color.BROWN,
                PropertyType.PROPERTY, 100, 10, 50);
        Property prop2 = new Property("Коричневая 2", Color.BROWN,
                PropertyType.PROPERTY, 100, 10, 50);

        player.buyProperty(prop1);
        player.buyProperty(prop2);

        // Проверяем монополию
        assertTrue(player.hasMonopoly(Color.BROWN));

        // Строим дом на первой улице
        prop1.buyHouse();
        assertEquals(1, prop1.getHouses());
    }

    /**
     * Тест тюрьмы
     */
    @Test
    void testJailSystem() {
        Player player = new Player("Тест", 1500);

        // Отправляем в тюрьму
        player.goToJail();
        assertTrue(player.isInJail());

        // Эмулируем три хода в тюрьме
        player.setJailTurns(3);
        assertEquals(3, player.getJailTurns());

        // Освобождаем
        player.releaseFromJail();
        assertFalse(player.isInJail());
        assertEquals(0, player.getJailTurns());
    }
}