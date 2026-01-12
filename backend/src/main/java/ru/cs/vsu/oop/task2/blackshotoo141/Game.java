package monopoly;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Основной класс игры Монополия, управляющий игровым процессом
 */
public class Game {
    private Board board;
    private List<Player> players;
    private Player currentPlayer;
    private int currentPlayerIndex;
    private Scanner scanner;
    private boolean gameOver;

    /**
     * Конструктор игры
     */
    public Game() {
        board = new Board();
        players = new ArrayList<>();
        scanner = new Scanner(System.in);
        gameOver = false;
    }

    /**
     * Начинает новую игру
     */
    public void startNewGame() {
        System.out.println("=== МОНОПОЛИЯ ===");
        setupPlayers();
        currentPlayerIndex = 0;
        currentPlayer = players.get(currentPlayerIndex);

        while (!gameOver) {
            playTurn();
            nextPlayer();

            // Проверяем, остался ли только один игрок
            int activePlayers = 0;
            for (Player player : players) {
                if (!player.isBankrupt()) {
                    activePlayers++;
                }
            }

            if (activePlayers <= 1) {
                gameOver = true;
                System.out.println("Игра окончена!");
            }
        }
    }

    /**
     * Настраивает игроков перед началом игры
     */
    private void setupPlayers() {
        System.out.print("Введите количество игроков (2-4): ");
        int numPlayers = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера

        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Введите имя игрока " + i + ": ");
            String name = scanner.nextLine();
            players.add(new Player(name, 1500)); // Стартовый капитал 1500
        }
    }

    /**
     * Выполняет ход текущего игрока
     */
    private void playTurn() {
        if (currentPlayer.isBankrupt()) {
            return;
        }

        System.out.println("\n=== Ход игрока: " + currentPlayer.getName() + " ===");
        System.out.println("Деньги: $" + currentPlayer.getMoney());
        System.out.println("Позиция: " + getPositionName(currentPlayer.getPosition()));

        if (currentPlayer.isInJail()) {
            handleJailTurn();
            return;
        }

        System.out.println("Нажмите Enter, чтобы бросить кубики...");
        scanner.nextLine();

        int dice1 = rollDice();
        int dice2 = rollDice();
        int total = dice1 + dice2;

        System.out.println("Выпало: " + dice1 + " + " + dice2 + " = " + total);

        if (dice1 == dice2) {
            System.out.println("Дубль! Вы бросаете снова в этом ходу.");
        }

        // Перемещение
        boolean passedGo = currentPlayer.move(total, board.getSize());
        if (passedGo) {
            System.out.println("Вы прошли через Старт! Получаете $200");
            currentPlayer.receiveMoney(200);
        }

        // Обработка клетки
        handleCurrentPosition();

        // Дополнительный ход при дубле
        if (dice1 == dice2) {
            playTurn(); // Рекурсивный вызов для дополнительного хода
        }
    }

    /**
     * Обрабатывает текущую позицию игрока
     */
    private void handleCurrentPosition() {
        int position = currentPlayer.getPosition();
        Property property = board.getPropertyAt(position);

        System.out.println("Вы на клетке: " + getPositionName(position));

        switch (property.getType()) {
            case PROPERTY:
            case RAILROAD:
            case UTILITY:
                handleProperty(property);
                break;

            case TAX:
                handleTax(property);
                break;

            case CHANCE:
                handleChance();
                break;

            case COMMUNITY_CHEST:
                handleCommunityChest();
                break;

            case GO_TO_JAIL:
                System.out.println("Отправляйтесь в тюрьму!");
                currentPlayer.goToJail();
                currentPlayer.moveTo(10, board.getSize(), false); // Тюрьма на позиции 10
                break;

            default:
                // Для GO, JAIL, FREE_PARKING ничего не делаем
                break;
        }
    }

    /**
     * Обрабатывает недвижимость на текущей позиции
     * @param property Недвижимость на клетке
     */
    private void handleProperty(Property property) {
        if (property.getOwner() == null) {
            System.out.println("Недвижимость свободна!");
            System.out.println("Название: " + property.getName());
            System.out.println("Цена: $" + property.getPrice());

            System.out.print("Хотите купить? (y/n): ");
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("y")) {
                if (currentPlayer.buyProperty(property)) {
                    System.out.println("Вы купили " + property.getName());
                } else {
                    System.out.println("Недостаточно денег!");
                }
            }
        } else if (property.getOwner() != currentPlayer) {
            int rent = property.calculateRent();
            System.out.println("Эта недвижимость принадлежит " + property.getOwner().getName());
            System.out.println("Арендная плата: $" + rent);

            if (currentPlayer.pay(rent)) {
                property.getOwner().receiveMoney(rent);
                System.out.println("Вы заплатили арендную плату");
            } else {
                System.out.println("Вы не можете заплатить арендную плату!");
            }
        } else {
            System.out.println("Это ваша недвижимость");
            if (property.getType() == PropertyType.PROPERTY) {
                handlePropertyImprovement(property);
            }
        }
    }

    /**
     * Обрабатывает улучшение недвижимости (дома/отели)
     * @param property Улучшаемая недвижимость
     */
    private void handlePropertyImprovement(Property property) {
        if (!currentPlayer.hasMonopoly(property.getColor())) {
            return;
        }

        System.out.println("У вас монополия на этот цвет!");
        System.out.println("Дома на этой улице: " + property.getHouses());
        System.out.println("Отелей: " + property.getHotel());
        System.out.println("Цена дома: $" + property.getHousePrice());

        if (property.getHouses() < 4 && property.getHotel() == 0) {
            System.out.print("Купить дом? (y/n): ");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("y")) {
                if (property.buyHouse()) {
                    System.out.println("Дом куплен!");
                } else {
                    System.out.println("Недостаточно денег!");
                }
            }
        } else if (property.getHouses() == 4 && property.getHotel() == 0) {
            System.out.print("Купить отель? (y/n): ");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("y")) {
                if (property.buyHotel()) {
                    System.out.println("Отель куплен!");
                } else {
                    System.out.println("Недостаточно денег!");
                }
            }
        }
    }

    /**
     * Обрабатывает налог
     * @param property Клетка с налогом
     */
    private void handleTax(Property property) {
        if (property.getName().equals("Подоходный налог")) {
            System.out.println("Заплатите подоходный налог $200");
            if (!currentPlayer.pay(200)) {
                System.out.println("Вы не можете заплатить налог!");
            }
        }
    }

    /**
     * Обрабатывает карточку Шанс
     */
    private void handleChance() {
        System.out.println("Вытягиваете карточку Шанс...");
        String[] chances = {
                "Переместитесь на Старт",
                "Отправляйтесь в тюрьму",
                "Получите $50",
                "Заплатите $100",
                "Получите $100",
                "Переместитесь на ближайшую ж/д дорогу"
        };

        String chance = chances[(int)(Math.random() * chances.length)];
        System.out.println(chance);

        switch (chance) {
            case "Переместитесь на Старт":
                currentPlayer.moveTo(0, board.getSize(), true);
                break;
            case "Отправляйтесь в тюрьму":
                currentPlayer.goToJail();
                currentPlayer.moveTo(10, board.getSize(), false);
                break;
            case "Получите $50":
                currentPlayer.receiveMoney(50);
                break;
            case "Заплатите $100":
                currentPlayer.pay(100);
                break;
            case "Получите $100":
                currentPlayer.receiveMoney(100);
                break;
        }
    }

    /**
     * Обрабатывает карточку Общественной казны
     */
    private void handleCommunityChest() {
        System.out.println("Вытягиваете карточку Общественной казны...");
        String[] chests = {
                "Банковская ошибка в вашу пользу. Получите $200",
                "Врачебные услуги. Заплатите $50",
                "Получите наследство $100",
                "Выигрыш в конкурсе. Получите $10",
                "Рождественский фонд. Получите $100"
        };

        String chest = chests[(int)(Math.random() * chests.length)];
        System.out.println(chest);

        switch (chest) {
            case "Банковская ошибка в вашу пользу. Получите $200":
                currentPlayer.receiveMoney(200);
                break;
            case "Врачебные услуги. Заплатите $50":
                currentPlayer.pay(50);
                break;
            case "Получите наследство $100":
                currentPlayer.receiveMoney(100);
                break;
            case "Выигрыш в конкурсе. Получите $10":
                currentPlayer.receiveMoney(10);
                break;
            case "Рождественский фонд. Получите $100":
                currentPlayer.receiveMoney(100);
                break;
        }
    }

    /**
     * Обрабатывает ход игрока в тюрьме
     */
    private void handleJailTurn() {
        System.out.println("Вы в тюрьме!");
        System.out.println("Осталось ходов в тюрьме: " + (3 - currentPlayer.getJailTurns()));

        currentPlayer.setJailTurns(currentPlayer.getJailTurns() + 1);

        if (currentPlayer.getGetOutOfJailCards() > 0) {
            System.out.print("Использовать карточку 'Выйти из тюрьмы бесплатно'? (y/n): ");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("y")) {
                currentPlayer.setGetOutOfJailCards(currentPlayer.getGetOutOfJailCards() - 1);
                currentPlayer.releaseFromJail();
                System.out.println("Вы вышли из тюрьмы!");
                return;
            }
        }

        System.out.println("1. Попытаться выбросить дубль");
        System.out.println("2. Заплатить $50 за выход");
        System.out.print("Выберите действие: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            System.out.println("Бросаете кубики...");
            int dice1 = rollDice();
            int dice2 = rollDice();
            System.out.println("Выпало: " + dice1 + " + " + dice2 + " = " + (dice1 + dice2));

            if (dice1 == dice2) {
                System.out.println("Дубль! Вы выходите из тюрьмы!");
                currentPlayer.releaseFromJail();
            } else if (currentPlayer.getJailTurns() >= 3) {
                System.out.println("Вы отсидели 3 хода и должны заплатить $50");
                if (currentPlayer.pay(50)) {
                    currentPlayer.releaseFromJail();
                }
            }
        } else if (choice == 2) {
            if (currentPlayer.pay(50)) {
                currentPlayer.releaseFromJail();
                System.out.println("Вы заплатили $50 и вышли из тюрьмы");
            } else {
                System.out.println("У вас недостаточно денег!");
            }
        }
    }

    /**
     * Бросает кубик
     * @return Результат броска (1-6)
     */
    private int rollDice() {
        return (int)(Math.random() * 6) + 1;
    }

    /**
     * Переходит к следующему игроку
     */
    private void nextPlayer() {
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            currentPlayer = players.get(currentPlayerIndex);
        } while (currentPlayer.isBankrupt() && !gameOver);
    }

    /**
     * Получает название позиции на поле
     * @param position Позиция на поле
     * @return Название клетки
     */
    private String getPositionName(int position) {
        Property prop = board.getPropertyAt(position);
        if (prop != null) {
            return prop.getName();
        }
        return "Неизвестная позиция";
    }

/**
 * Основной метод для