package ru.cs.vsu.oop.task2.blackshotoo141;

/**
 * Перечисление цветов групп недвижимости в игре Монополия
 */
public enum Color {
    BROWN("Коричневый"),
    LIGHT_BLUE("Голубой"),
    PINK("Розовый"),
    ORANGE("Оранжевый"),
    RED("Красный"),
    YELLOW("Желтый"),
    GREEN("Зеленый"),
    DARK_BLUE("Синий"),
    RAILROAD("Железная дорога"),
    UTILITY("Коммунальное предприятие");

    private final String russianName;

    Color(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }
}
