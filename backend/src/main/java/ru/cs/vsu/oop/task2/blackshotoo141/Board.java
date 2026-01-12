package ru.cs.vsu.oop.task2.blackshotoo141;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий игровое поле Монополии
 */
public class Board {
    private List<Property> properties;

    /**
     * Конструктор игрового поля, создает все клетки
     */
    public Board() {
        properties = new ArrayList<>();
        initializeBoard();
    }

    /**
     * Инициализирует игровое поле стандартными клетками Монополии
     */
    private void initializeBoard() {
        // Старт
        properties.add(new Property("Старт", null, PropertyType.GO, 0, 0, 0));

        // Коричневые
        properties.add(new Property("Старая дорога", Color.BROWN, PropertyType.PROPERTY, 60, 2, 50));
        properties.add(new Property("Общественная казна", null, PropertyType.COMMUNITY_CHEST, 0, 0, 0));
        properties.add(new Property("Белая улица", Color.BROWN, PropertyType.PROPERTY, 60, 4, 50));

        // Налог
        properties.add(new Property("Подоходный налог", null, PropertyType.TAX, 0, 200, 0));

        // Ж/д дороги
        properties.add(new Property("Вокзал 1", Color.RAILROAD, PropertyType.RAILROAD, 200, 25, 0));

        // Голубые
        properties.add(new Property("Розовая улица", Color.LIGHT_BLUE, PropertyType.PROPERTY, 100, 6, 50));
        properties.add(new Property("Шанс", null, PropertyType.CHANCE, 0, 0, 0));
        properties.add(new Property("Оранжевая улица", Color.LIGHT_BLUE, PropertyType.PROPERTY, 100, 6, 50));
        properties.add(new Property("Белая улица", Color.LIGHT_BLUE, PropertyType.PROPERTY, 120, 8, 50));

        // Тюрьма
        properties.add(new Property("Тюрьма", null, PropertyType.JAIL, 0, 0, 0));

        // Розовые
        properties.add(new Property("Алая улица", Color.PINK, PropertyType.PROPERTY, 140, 10, 100));
        properties.add(new Property("Электростанция", Color.UTILITY, PropertyType.UTILITY, 150, 0, 0));
        properties.add(new Property("Желтая улица", Color.PINK, PropertyType.PROPERTY, 140, 10, 100));
        properties.add(new Property("Красная улица", Color.PINK, PropertyType.PROPERTY, 160, 12, 100));

        // Ж/д дороги
        properties.add(new Property("Вокзал 2", Color.RAILROAD, PropertyType.RAILROAD, 200, 25, 0));

        // Оранжевые
        properties.add(new Property("Оранжевая улица", Color.ORANGE, PropertyType.PROPERTY, 180, 14, 100));
        properties.add(new Property("Общественная казна", null, PropertyType.COMMUNITY_CHEST, 0, 0, 0));
        properties.add(new Property("Голубая улица", Color.ORANGE, PropertyType.PROPERTY, 180, 14, 100));
        properties.add(new Property("Зеленая улица", Color.ORANGE, PropertyType.PROPERTY, 200, 16, 100));

        // Бесплатная парковка
        properties.add(new Property("Бесплатная парковка", null, PropertyType.FREE_PARKING, 0, 0, 0));

        // Красные
        properties.add(new Property("Синяя улица", Color.RED, PropertyType.PROPERTY, 220, 18, 150));
        properties.add(new Property("Шанс", null, PropertyType.CHANCE, 0, 0, 0));
        properties.add(new Property("Фиолетовая улица", Color.RED, PropertyType.PROPERTY, 220, 18, 150));
        properties.add(new Property("Серая улица", Color.RED, PropertyType.PROPERTY, 240, 20, 150));

        // Ж/д дороги
        properties.add(new Property("Вокзал 3", Color.RAILROAD, PropertyType.RAILROAD, 200, 25, 0));

        // Желтые
        properties.add(new Property("Черная улица", Color.YELLOW, PropertyType.PROPERTY, 260, 22, 150));
        properties.add(new Property("Бирюзовая улица", Color.YELLOW, PropertyType.PROPERTY, 260, 22, 150));
        properties.add(new Property("Водопровод", Color.UTILITY, PropertyType.UTILITY, 150, 0, 0));
        properties.add(new Property("Золотая улица", Color.YELLOW, PropertyType.PROPERTY, 280, 24, 150));

        // Отправляйтесь в тюрьму
        properties.add(new Property("Отправляйтесь в тюрьму", null, PropertyType.GO_TO_JAIL, 0, 0, 0));

        // Зеленые
        properties.add(new Property("Серебряная улица", Color.GREEN, PropertyType.PROPERTY, 300, 26, 200));
        properties.add(new Property("Бронзовая улица", Color.GREEN, PropertyType.PROPERTY, 300, 26, 200));
        properties.add(new Property("Общественная казна", null, PropertyType.COMMUNITY_CHEST, 0, 0, 0));
        properties.add(new Property("Платиновая улица", Color.GREEN, PropertyType.PROPERTY, 320, 28, 200));

        // Ж/д дороги
        properties.add(new Property("Вокзал 4", Color.RAILROAD, PropertyType.RAILROAD, 200, 25, 0));

        // Синие
        properties.add(new Property("Изумрудная улица", Color.DARK_BLUE, PropertyType.PROPERTY, 350, 35, 200));
        properties.add(new Property("Шанс", null, PropertyType.CHANCE, 0, 0, 0));
        properties.add(new Property("Бриллиантовая улица", Color.DARK_BLUE, PropertyType.PROPERTY, 400, 50, 200));
    }

    /**
     * Получает недвижимость по позиции
     * @param position Позиция на поле
     * @return Недвижимость на указанной позиции
     */
    public Property getPropertyAt(int position) {
        if (position >= 0 && position < properties.size()) {
            return properties.get(position);
        }
        return null;
    }

    /**
     * Получает размер игрового поля
     * @return Количество клеток на поле
     */
    public int getSize() {
        return properties.size();
    }

    /**
     * Получает все недвижимости указанного цвета
     * @param color Цвет группы
     * @return Список недвижимостей указанного цвета
     */
    public List<Property> getPropertiesByColor(Color color) {
        List<Property> result = new ArrayList<>();
        for (Property prop : properties) {
            if (prop.getColor() == color) {
                result.add(prop);
            }
        }
        return result;
    }
}
