package ru.cs.vsu.oop.task2.blackshotoo141;

import java.awt.*;

/**
 * Класс, представляющий недвижимость (улицу, ж/д дорогу или коммунальное предприятие)
 */
public class Property {
    private String name;
    private Color color;
    private PropertyType type;
    private int price;
    private int rent;
    private int housePrice;
    private int houses;
    private int hotel;
    private Player owner;
    private boolean isMortgaged;

    /**
     * Конструктор для создания недвижимости
     * @param name Название недвижимости
     * @param color Цвет группы
     * @param type Тип недвижимости
     * @param price Цена покупки
     * @param rent Базовая арендная плата
     * @param housePrice Цена постройки дома
     */
    public Property(String name, Color color, PropertyType type,
                    int price, int rent, int housePrice) {
        this.name = name;
        this.color = color;
        this.type = type;
        this.price = price;
        this.rent = rent;
        this.housePrice = housePrice;
        this.houses = 0;
        this.hotel = 0;
        this.owner = null;
        this.isMortgaged = false;
    }

    /**
     * Вычисляет текущую арендную плату в зависимости от построек
     * @return Сумма арендной платы
     */
    public int calculateRent() {
        if (owner == null || isMortgaged) {
            return 0;
        }

        switch (type) {
            case PROPERTY:
                if (hotel > 0) return rent * 10; // Отель
                if (houses > 0) return rent * (houses + 1); // Дома
                if (owner.hasMonopoly(color)) return rent * 2; // Монополия
                return rent; // Базовая ставка

            case RAILROAD:
                int railroadsOwned = owner.countRailroads();
                switch (railroadsOwned) {
                    case 1: return 25;
                    case 2: return 50;
                    case 3: return 100;
                    case 4: return 200;
                    default: return 25;
                }

            case UTILITY:
                int utilitiesOwned = owner.countUtilities();
                int diceRoll = (int)(Math.random() * 6) + 1 +
                        (int)(Math.random() * 6) + 1;
                return utilitiesOwned == 1 ? diceRoll * 4 : diceRoll * 10;

            default:
                return rent;
        }
    }

    /**
     * Покупка дома на недвижимости
     * @return true если дом успешно куплен, false в противном случае
     */
    public boolean buyHouse() {
        if (type != PropertyType.PROPERTY || houses >= 4 || hotel > 0) {
            return false;
        }

        if (owner.getMoney() >= housePrice) {
            owner.pay(housePrice);
            houses++;
            return true;
        }
        return false;
    }

    /**
     * Покупка отеля на недвижимости
     * @return true если отель успешно куплен, false в противном случае
     */
    public boolean buyHotel() {
        if (type != PropertyType.PROPERTY || houses < 4 || hotel > 0) {
            return false;
        }

        if (owner.getMoney() >= housePrice) {
            owner.pay(housePrice);
            hotel++;
            houses = 0;
            return true;
        }
        return false;
    }

    /**
     * Залог недвижимости
     * @return Сумма, полученная за залог
     */
    public int mortgage() {
        if (!isMortgaged) {
            isMortgaged = true;
            return price / 2;
        }
        return 0;
    }

    /**
     * Выкуп недвижимости из залога
     * @return true если выкуп успешен, false в противном случае
     */
    public boolean unmortgage() {
        if (isMortgaged && owner.getMoney() >= price * 0.55) {
            owner.pay((int)(price * 0.55));
            isMortgaged = false;
            return true;
        }
        return false;
    }

    // Геттеры и сеттеры
    public String getName() { return name; }
    public Color getColor() { return color; }
    public PropertyType getType() { return type; }
    public int getPrice() { return price; }
    public int getRent() { return rent; }
    public int getHousePrice() { return housePrice; }
    public int getHouses() { return houses; }
    public int getHotel() { return hotel; }
    public Player getOwner() { return owner; }
    public void setOwner(Player owner) { this.owner = owner; }
    public boolean isMortgaged() { return isMortgaged; }
}
