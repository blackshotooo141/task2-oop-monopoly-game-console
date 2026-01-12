package ru.cs.vsu.oop.task2.blackshotooTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.cs.vsu.oop.task2.blackshotoo141.Board;
import ru.cs.vsu.oop.task2.blackshotoo141.Color;
import ru.cs.vsu.oop.task2.blackshotoo141.Property;
import ru.cs.vsu.oop.task2.blackshotoo141.PropertyType;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса Board
 */
public class BoardTest {
    private Board board;

    /**
     * Настройка перед каждым тестом
     */
    @BeforeEach
    void setUp() {
        board = new Board();
    }

    /**
     * Тест создания игрового поля
     */
    @Test
    void testBoardCreation() {
        assertNotNull(board);
        assertTrue(board.getSize() > 0);
    }

    /**
     * Тест получения клетки по позиции
     */
    @Test
    void testGetPropertyAt() {
        Property start = board.getPropertyAt(0);
        assertNotNull(start);
        assertEquals("Старт", start.getName());
        assertEquals(PropertyType.GO, start.getType());

        Property jail = board.getPropertyAt(10);
        assertNotNull(jail);
        assertEquals("Тюрьма", jail.getName());
        assertEquals(PropertyType.JAIL, jail.getType());
    }

    /**
     * Тест получения недвижимости по цвету
     */
    @Test
    void testGetPropertiesByColor() {
        List<Property> brownProperties = board.getPropertiesByColor(Color.BROWN);
        assertNotNull(brownProperties);
        assertEquals(2, brownProperties.size()); // Две коричневые улицы

        for (Property prop : brownProperties) {
            assertEquals(Color.BROWN, prop.getColor());
            assertEquals(PropertyType.PROPERTY, prop.getType());
        }
    }

    /**
     * Тест граничных случаев
     */
    @Test
    void testEdgeCases() {
        // Тест неверной позиции (отрицательная)
        Property negativePos = board.getPropertyAt(-1);
        assertNull(negativePos);

        // Тест неверной позиции (больше размера)
        Property largePos = board.getPropertyAt(1000);
        assertNull(largePos);
    }

    /**
     * Тест типов клеток на поле
     */
    @Test
    void testPropertyTypes() {
        // Проверяем, что на поле есть все типы клеток
        boolean hasGo = false;
        boolean hasJail = false;
        boolean hasChance = false;
        boolean hasTax = false;

        for (int i = 0; i < board.getSize(); i++) {
            Property prop = board.getPropertyAt(i);
            if (prop.getType() == PropertyType.GO) hasGo = true;
            if (prop.getType() == PropertyType.JAIL) hasJail = true;
            if (prop.getType() == PropertyType.CHANCE) hasChance = true;
            if (prop.getType() == PropertyType.TAX) hasTax = true;
        }

        assertTrue(hasGo);
        assertTrue(hasJail);
        assertTrue(hasChance);
        assertTrue(hasTax);
    }
}
