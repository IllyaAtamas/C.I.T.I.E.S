package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Main {

    private JFrame welcomeWindow;
    private JFrame gameWindow;
    private JLabel scoreLabel;
    private JTextField inputField;
    private JLabel computerResponseLabel;
    private Set<String> cities;
    private int score;

    public Main() {
        loadCities();
        createWelcomeWindow();
    }

    private void loadCities() {
        cities = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("cities.txt"))) {
            String city;
            while ((city = br.readLine()) != null) {
                cities.add(city.toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createWelcomeWindow() {
        welcomeWindow = new JFrame("Вітаємо!");
        welcomeWindow.setSize(400, 500);
        welcomeWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welcomeWindow.setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Вітаємо у грі C.I.T.I.E.S!");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);

        JButton startButton = new JButton("Почати");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                welcomeWindow.dispose();
                startGame();
            }
        });

        JButton leaderboardButton = new JButton("Таблиця рекордів");
        leaderboardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLeaderboard();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        panel.add(welcomeLabel);
        panel.add(startButton);
        panel.add(leaderboardButton);

        welcomeWindow.add(panel);
        welcomeWindow.setVisible(true);
    }

    private void startGame() {
        gameWindow = new JFrame("C.I.T.I.E.S");
        gameWindow.setSize(400, 500);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setLocationRelativeTo(null);

        JLabel inputLabel = new JLabel("Введіть назву міста:");
        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeMove();
            }
        });

        JButton submitButton = new JButton("Зробити хід");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeMove();
            }
        });

        JButton quitButton = new JButton("Вийти");
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                endGame();
            }
        });

        scoreLabel = new JLabel("Очки: 0");

        computerResponseLabel = new JLabel();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));
        panel.add(inputLabel);
        panel.add(inputField);
        panel.add(submitButton);
        panel.add(quitButton);
        panel.add(scoreLabel);

        gameWindow.add(panel, BorderLayout.CENTER);
        gameWindow.add(computerResponseLabel, BorderLayout.SOUTH);
        gameWindow.setVisible(true);

        score = 0;
    }

    private void makeMove() {
        String userInput = inputField.getText().trim().toLowerCase();
        if (isValidCity(userInput)) {
            if (score == 0) {
                computerResponseLabel.setText("Комп'ютер: " + getCityStartingWithLastLetter(userInput));
                score++;
                scoreLabel.setText("Очки: " + score);
            } else {
                String computerResponse = getCityStartingWithLastLetter(userInput);
                if (computerResponse != null) {
                    computerResponseLabel.setText("Комп'ютер: " + computerResponse);
                    score++;
                    scoreLabel.setText("Очки: " + score);
                } else {
                    endGame();
                }
            }
        } else {
            JOptionPane.showMessageDialog(gameWindow, "Невідоме місто, перевірте правильність його написання.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        inputField.setText("");
    }

    private boolean isValidCity(String city) {
        return city.matches("[а-яА-ЯіІїЇ]+");
        // Перевірка на валідність введеного слова
    }

    private String getCityStartingWithLastLetter(String city) {
        String lastLetter = city.substring(city.length() - 1);
        // Остання літера введеного слова
        String responseLetter = lastLetter.equals("ь") ? city.substring(city.length() - 2, city.length() - 1) : lastLetter;
        List<String> validCities = new ArrayList<>();
        for (String c : cities) {
            if (!c.equalsIgnoreCase(city) && c.startsWith(responseLetter)) {
                validCities.add(c);
            }
        }
        if (!validCities.isEmpty()) {
            return validCities.get(new Random().nextInt(validCities.size()));
        }
        return null;
    }

    private void endGame() {
        gameWindow.dispose();
        String message;
        if (cities.isEmpty()) {
            message = "Вітаю, ти перемогли!";
        } else {
            message = "Гру завершено! Ваш результат: " + score;
        }
        JOptionPane.showMessageDialog(null, message, "Гра завершена", JOptionPane.INFORMATION_MESSAGE);
        saveScore(score);
    }

    private void showLeaderboard() {
    }

    private void saveScore(int score) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }
}