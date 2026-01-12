package ru.cs.vsu.oop.task2.blackshotooTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.cs.vsu.oop.task2.blackshotoo141.Board;
import ru.cs.vsu.oop.task2.blackshotoo141.Color;
import ru.cs.vsu.oop.task2.blackshotoo141.Player;
import ru.cs.vsu.oop.task2.blackshotoo141.Property;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса Player
 */
public class PlayerTest {
    private Player player;
    private Board board;

    /**
     * Настройка перед каждым тестом
     */
    @BeforeEach
    void setUp() {
        player = new Player("Тестовый игрок", 1500);
        board = new Board();
    }

    /**
     * Тест создания игрока
     */
    @Test
    void testPlayerCreation() {
        assertEquals("Тестовый игрок", player.getName());
        assertEquals(1500, player.getMoney());
        assertEquals(0, player.getPosition());
        assertFalse(player.isInJail());
        assertFalse(player.isBankrupt());
        assertTrue(player.getProperties().isEmpty());
    }

    /**
     * Тест перемещения игрока
     */
    @Test
    void testMove() {
        boolean passedGo = player.move(5, 40);
        assertFalse(passedGo); // Не проходили через старт
        assertEquals(5, player.getPosition());
    }

    /**
     * Тест перемещения через старт
     */
    @Test
    void testMovePassGo() {
        player.move(35, 40); // Перемещаемся к концу поля
        boolean passedGo = player.move(10, 40); // Проходим через старт
        assertTrue(passedGo);
        assertEquals(5, player.getPosition()); // 35 + 10 = 45, 45 % 40 = 5
    }

    /**
     * Тест получения денег
     */
    @Test
    void testReceiveMoney() {
        player.receiveMoney(500);
        assertEquals(2000, player.getMoney());
    }

    /**
     * Тест платежа
     */
    @Test
    void testPay() {
        boolean success = player.pay(500);
        assertTrue(success);
        assertEquals(1000, player.getMoney());
    }

    /**
     * Тест платежа с недостатком средств (но без банкротства)
     */
    @Test
    void testPayInsufficientFunds() {
        boolean success = player.pay(2000);
        assertFalse(success);
        assertFalse(player.isBankrupt()); // В тестовой среде не должно вызвать банкротство
    }

    /**
     * Тест покупки недвижимости
     */
    @Test
    void testBuyProperty() {
        Property property = board.getPropertyAt(1); // Старая дорога
        boolean success = player.buyProperty(property);

        assertTrue(success);
        assertEquals(player, property.getOwner());
        assertEquals(1, player.getProperties().size());
        assertEquals(1500 - property.getPrice(), player.getMoney());
    }

    /**
     * Тест проверки монополии
     */
    @Test
    void testHasMonopoly() {
        // Покупаем все коричневые улицы (их 2)
        Property brown1 = board.getPropertyAt(1); // Старая дорога
        Property brown2 = board.getPropertyAt(3); // Белая улица

        player.buyProperty(brown1);
        player.buyProperty(brown2);

        assertTrue(player.hasMonopoly(Color.BROWN));
    }

    /**
     * Тест подсчета ж/д дорог
     */
    @Test
    void testCountRailroads() {
        // Покупаем несколько ж/д
        Property railroad1 = board.getPropertyAt(5); // Вокзал 1
        Property railroad2 = board.getPropertyAt(15); // Вокзал 2

        player.buyProperty(railroad1);
        player.buyProperty(railroad2);

        assertEquals(2, player.countRailroads());
    }

    /**
     * Тест отправки в тюрьму и освобождения
     */
    @Test
    void testJail() {
        assertFalse(player.isInJail());

        player.goToJail();
        assertTrue(player.isInJail());

        player.releaseFromJail();
        assertFalse(player.isInJail());
    }

    /**
     * Тест банкротства
     */
    @Test
    void testBankrupt() {
        player.bankrupt();
        assertTrue(player.isBankrupt());
        assertTrue(player.getProperties().isEmpty());
    }

    /**
     * Тест перемещения на конкретную позицию
     */
    @Test
    void testMoveTo() {
        player.moveTo(10, 40, true); // Перемещаемся с получением денег за проход через старт
        assertEquals(10, player.getPosition());
        assertEquals(1700, player.getMoney()); // +200 за проход через старт
    }
}
