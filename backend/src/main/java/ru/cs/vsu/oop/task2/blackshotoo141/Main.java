package ru.cs.vsu.oop.task2.blackshotoo141;
/**
 * Главный класс для запуска игры Монополия
 */
public class Main {
    /**
     * Точка входа в программу
     * @param args Аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.startNewGame();
    }
}
