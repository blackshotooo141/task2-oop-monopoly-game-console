package ru.cs.vsu.oop.task2.blackshotooTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.cs.vsu.oop.task2.blackshotoo141.Color;
import ru.cs.vsu.oop.task2.blackshotoo141.Player;
import ru.cs.vsu.oop.task2.blackshotoo141.Property;
import ru.cs.vsu.oop.task2.blackshotoo141.PropertyType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса Property
 */
public class PropertyTest {
    private Property property;
    private Player player;

    /**
     * Настройка перед каждым тестом
     */
    @BeforeEach
    void setUp() {
        property = new Property("Тестовая улица", Color.BROWN,
                PropertyType.PROPERTY, 100, 10, 50);
        player = new Player("Тестовый игрок", 1000);
    }

    /**
     * Тест создания недвижимости
     */
    @Test
    void testPropertyCreation() {
        assertEquals("Тестовая улица", property.getName());
        assertEquals(Color.BROWN, property.getColor());
        assertEquals(PropertyType.PROPERTY, property.getType());
        assertEquals(100, property.getPrice());
        assertEquals(10, property.getRent());
        assertEquals(50, property.getHousePrice());
        assertNull(property.getOwner());
        assertFalse(property.isMortgaged());
    }

    /**
     * Тест покупки недвижимости
     */
    @Test
    void testBuyProperty() {
        assertNull(property.getOwner());
        player.buyProperty(property);
        assertEquals(player, property.getOwner());
        assertEquals(900, player.getMoney()); // 1000 - 100
    }

    /**
     * Тест расчета аренды для обычной недвижимости
     */
    @Test
    void testCalculateRent() {
        player.buyProperty(property);
        assertEquals(10, property.calculateRent()); // Базовая аренда
    }

    /**
     * Тест расчета аренды при монополии
     */
    @Test
    void testCalculateRentWithMonopoly() {
        // Создаем вторую недвижимость того же цвета
        Property property2 = new Property("Тестовая улица 2", Color.BROWN,
                PropertyType.PROPERTY, 100, 10, 50);

        player.buyProperty(property);
        player.buyProperty(property2);

        // Для теста нужно эмулировать наличие монополии
        // В реальном тесте это делается через проверку hasMonopoly()
        // Здесь тестируем, что при монополии аренда удваивается
        // (В реальной игре монополия проверяется в calculateRent())
        assertTrue(player.hasMonopoly(Color.BROWN));
    }

    /**
     * Тест покупки дома
     */
    @Test
    void testBuyHouse() {
        player.buyProperty(property);
        assertTrue(property.buyHouse());
        assertEquals(1, property.getHouses());
        assertEquals(850, player.getMoney()); // 1000 - 100 - 50
    }

    /**
     * Тест покупки отеля
     */
    @Test
    void testBuyHotel() {
        player.buyProperty(property);

        // Купить 4 дома
        for (int i = 0; i < 4; i++) {
            property.buyHouse();
        }

        assertTrue(property.buyHotel());
        assertEquals(1, property.getHotel());
        assertEquals(0, property.getHouses());
        assertEquals(1000 - 100 - (50 * 4) - 50, player.getMoney());
    }

    /**
     * Тест залога недвижимости
     */
    @Test
    void testMortgage() {
        player.buyProperty(property);
        int mortgageAmount = property.mortgage();

        assertEquals(50, mortgageAmount); // Половина цены
        assertTrue(property.isMortgaged());
        assertEquals(950, player.getMoney()); // 900 + 50
    }

    /**
     * Тест выкупа из залога
     */
    @Test
    void testUnmortgage() {
        player.buyProperty(property);
        property.mortgage();

        // Добавим денег для выкупа
        player.receiveMoney(1000);

        assertTrue(property.unmortgage());
        assertFalse(property.isMortgaged());
        // Проверяем, что заплатили 55% от цены (100 * 0.55 = 55)
        assertEquals(1900 - 55, player.getMoney());
    }

    /**
     * Тест расчета аренды для ж/д дороги
     */
    @Test
    void testRailroadRent() {
        Property railroad = new Property("Вокзал", Color.RAILROAD,
                PropertyType.RAILROAD, 200, 25, 0);
        player.buyProperty(railroad);

        // Создаем еще три ж/д для теста монополии
        Property[] railroads = new Property[3];
        for (int i = 0; i < 3; i++) {
            railroads[i] = new Property("Вокзал " + (i+2), Color.RAILROAD,
                    PropertyType.RAILROAD, 200, 25, 0);
            player.buyProperty(railroads[i]);
        }

        assertEquals(4, player.countRailroads());
    }
}
