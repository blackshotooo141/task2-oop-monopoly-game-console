package ru.cs.vsu.oop.task2.blackshotoo141;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий игрока в Монополии
 */
public class Player {
    private String name;
    private int money;
    private int position;
    private List<Property> properties;
    private boolean inJail;
    private int jailTurns;
    private boolean isBankrupt;
    private int getOutOfJailCards;

    /**
     * Конструктор игрока
     * @param name Имя игрока
     * @param initialMoney Начальное количество денег
     */
    public Player(String name, int initialMoney) {
        this.name = name;
        this.money = initialMoney;
        this.position = 0;
        this.properties = new ArrayList<>();
        this.inJail = false;
        this.jailTurns = 0;
        this.isBankrupt = false;
        this.getOutOfJailCards = 0;
    }

    /**
     * Перемещает игрока на указанное количество клеток
     * @param spaces Количество клеток для перемещения
     * @param boardSize Размер игрового поля
     * @return true если игрок прошел через старт
     */
    public boolean move(int spaces, int boardSize) {
        int oldPosition = position;
        position = (position + spaces) % boardSize;

        // Если прошли через старт
        if (oldPosition > position) {
            return true;
        }
        return false;
    }

    /**
     * Перемещает игрока на указанную позицию
     * @param newPosition Новая позиция
     * @param boardSize Размер игрового поля
     * @param passGo Получает ли игрок деньги за проход через старт
     */
    public void moveTo(int newPosition, int boardSize, boolean passGo) {
        if (passGo && newPosition < position) {
            receiveMoney(200);
        }
        position = newPosition % boardSize;
    }

    /**
     * Проверяет, есть ли у игрока монополия указанного цвета
     * @param color Цвет группы недвижимости
     * @return true если у игрока монополия
     */
    public boolean hasMonopoly(Color color) {
        if (color == Color.RAILROAD || color == Color.UTILITY) {
            return false; // Ж/д и коммуналки считаются отдельно
        }

        int owned = 0;
        int total = getTotalPropertiesInColor(color);

        for (Property prop : properties) {
            if (prop.getColor() == color && !prop.isMortgaged()) {
                owned++;
            }
        }

        return owned == total;
    }

    /**
     * Подсчитывает количество ж/д дорог у игрока
     * @return Количество ж/д дорог
     */
    public int countRailroads() {
        int count = 0;
        for (Property prop : properties) {
            if (prop.getType() == PropertyType.RAILROAD && !prop.isMortgaged()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Подсчитывает количество коммунальных предприятий у игрока
     * @return Количество коммунальных предприятий
     */
    public int countUtilities() {
        int count = 0;
        for (Property prop : properties) {
            if (prop.getType() == PropertyType.UTILITY && !prop.isMortgaged()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Игрок получает деньги
     * @param amount Сумма денег
     */
    public void receiveMoney(int amount) {
        money += amount;
    }

    /**
     * Игрок платит деньги
     * @param amount Сумма денег
     * @return true если игрок смог заплатить, false если обанкротился
     */
    public boolean pay(int amount) {
        if (money >= amount) {
            money -= amount;
            return true;
        } else {
            // Пытаемся продать недвижимость или заложить
            if (canRaiseMoney(amount)) {
                // В реальной игре здесь была бы логика продажи/залога
                return false;
            } else {
                bankrupt();
                return false;
            }
        }
    }

    /**
     * Игрок покупает недвижимость
     * @param property Недвижимость для покупки
     * @return true если покупка успешна
     */
    public boolean buyProperty(Property property) {
        if (money >= property.getPrice() && property.getOwner() == null) {
            if (pay(property.getPrice())) {
                property.setOwner(this);
                properties.add(property);
                return true;
            }
        }
        return false;
    }

    /**
     * Отправляет игрока в тюрьму
     */
    public void goToJail() {
        inJail = true;
        jailTurns = 0;
    }

    /**
     * Освобождает игрока из тюрьмы
     */
    public void releaseFromJail() {
        inJail = false;
        jailTurns = 0;
    }

    /**
     * Объявляет игрока банкротом
     */
    public void bankrupt() {
        isBankrupt = true;
        // Возвращаем все недвижимости на рынок
        for (Property prop : properties) {
            prop.setOwner(null);
        }
        properties.clear();
    }

    /**
     * Проверяет, может ли игрок собрать указанную сумму
     * @param amount Требуемая сумма
     * @return true если может собрать сумму
     */
    private boolean canRaiseMoney(int amount) {
        int totalValue = money;
        for (Property prop : properties) {
            if (!prop.isMortgaged()) {
                totalValue += prop.getPrice() / 2; // Залоговая стоимость
            }
        }
        return totalValue >= amount;
    }

    /**
     * Получает общее количество недвижимостей в группе цвета
     * @param color Цвет группы
     * @return Общее количество недвижимостей в группе
     */
    private int getTotalPropertiesInColor(Color color) {
        switch (color) {
            case BROWN: return 2;
            case LIGHT_BLUE: return 3;
            case PINK: return 3;
            case ORANGE: return 3;
            case RED: return 3;
            case YELLOW: return 3;
            case GREEN: return 3;
            case DARK_BLUE: return 2;
            default: return 0;
        }
    }

    // Геттеры
    public String getName() { return name; }
    public int getMoney() { return money; }
    public int getPosition() { return position; }
    public List<Property> getProperties() { return properties; }
    public boolean isInJail() { return inJail; }
    public int getJailTurns() { return jailTurns; }
    public boolean isBankrupt() { return isBankrupt; }
    public int getGetOutOfJailCards() { return getOutOfJailCards; }

    // Сеттеры
    public void setInJail(boolean inJail) { this.inJail = inJail; }
    public void setJailTurns(int jailTurns) { this.jailTurns = jailTurns; }
    public void setGetOutOfJailCards(int cards) { this.getOutOfJailCards = cards; }
}