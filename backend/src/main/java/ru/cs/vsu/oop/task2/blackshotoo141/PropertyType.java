package ru.cs.vsu.oop.task2.blackshotoo141;

/**
 * Типы клеток на игровом поле
 */
public enum PropertyType {
    PROPERTY("Недвижимость"),
    RAILROAD("Железная дорога"),
    UTILITY("Коммунальное предприятие"),
    CHANCE("Шанс"),
    COMMUNITY_CHEST("Общественная казна"),
    TAX("Налог"),
    GO("Старт"),
    JAIL("Тюрьма"),
    FREE_PARKING("Бесплатная парковка"),
    GO_TO_JAIL("Отправляйтесь в тюрьму");

    private final String russianName;

    PropertyType(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }
}

