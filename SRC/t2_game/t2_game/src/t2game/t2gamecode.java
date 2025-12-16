package t2game;

import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.ArrayList;

public class t2gamecode {
    private JFrame frame;
    private JLabel imageLabel;
    private JRadioButton option1;
    private JRadioButton option2;
    private JButton nextButton;
    private ButtonGroup optionsGroup;
    
    private String[][] careers = {
        {"Doctor", "doctor.png", "#FFDDC1"},
        {"Soldier", "soldier.png", "#C1FFD7"},
        {"Teacher", "teacher.png", "#C1D7FF"},
        {"Police", "police.png", "#FFD1DC"},
        {"Scientist", "scientist.png", "#E1C1FF"},
        {"Chef", "chef.png", "#FFE7C1"},
        {"Builder", "builder.png", "#D1FFC1"},
        {"Racer", "racer.png", "#FFC1C1"},
        {"Fisherman", "fisherman.png", "#C1E7FF"},
        {"Athlete", "athlete.png", "#F0C1FF"}
    };
    
    private ArrayList<String[]> randomizedCareers;
    private int currentQuestion = 0;
    private int score = 0;
    private int highScore = 0;
    
    public t2gamecode() {
        startNewGame();
    }
    
    private void startNewGame() {
        randomizedCareers = new ArrayList<>();
        Collections.addAll(randomizedCareers, careers);
        Collections.shuffle(randomizedCareers);
        currentQuestion = 0;
        score = 0;
        
        if (frame == null) {
            frame = new JFrame("Career Guessing Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLayout(new BorderLayout());
            frame.getContentPane().setBackground(new Color(240, 248, 255));
            
            JPanel imagePanel = new JPanel();
            imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imagePanel.add(imageLabel);
            frame.add(imagePanel, BorderLayout.CENTER);
            
            JPanel optionsPanel = new JPanel();
            optionsPanel.setLayout(new GridLayout(1, 3));
            optionsPanel.setBackground(new Color(240, 248, 255));
            
            option1 = new JRadioButton();
            option2 = new JRadioButton();
            optionsGroup = new ButtonGroup();
            optionsGroup.add(option1);
            optionsGroup.add(option2);
            
            nextButton = new JButton("Next");
            
            Font font = new Font("Arial", Font.BOLD, 24);
            option1.setFont(font);
            option2.setFont(font);
            nextButton.setFont(font);
            
            option1.setBackground(new Color(173, 216, 230));
            option2.setBackground(new Color(152, 251, 152));
            
            option1.setPreferredSize(new Dimension(200, 100));
            option2.setPreferredSize(new Dimension(200, 100));
            
            optionsPanel.add(option1);
            optionsPanel.add(option2);
            optionsPanel.add(nextButton);
            frame.add(optionsPanel, BorderLayout.SOUTH);
            
            nextButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!option1.isSelected() && !option2.isSelected()) {
                        JOptionPane.showMessageDialog(frame, "Please select an option before proceeding.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    checkAnswer();
                }
            });
            frame.setVisible(true);
        }
        
        loadQuestion();
    }
    
    private void loadQuestion() {
        if (currentQuestion < 5) {
            String[] career = randomizedCareers.get(currentQuestion);
            ImageIcon icon = new ImageIcon(new ImageIcon(career[1]).getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH));
            imageLabel.setIcon(icon);
            imageLabel.getParent().setBackground(Color.decode(career[2])); // Set background color based on career
            
            String incorrectOption;
            do {
                incorrectOption = careers[(int) (Math.random() * careers.length)][0];
            } while (incorrectOption.equals(career[0]));
            
            if (Math.random() > 0.5) {
                option1.setText(career[0]);
                option2.setText(incorrectOption);
            } else {
                option1.setText(incorrectOption);
                option2.setText(career[0]);
            }
            
            optionsGroup.clearSelection();
        } else {
            if (score > highScore) {
                highScore = score;
            }
            int choice = JOptionPane.showOptionDialog(frame, "Game Over! Your score: " + score + "/5\nHigh Score: " + highScore, 
                "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, 
                new String[]{"Play Again", "Exit"}, "Play Again");
            
            if (choice == JOptionPane.YES_OPTION) {
                startNewGame();
            } else {
                frame.dispose();
            }
        }
    }
    
    private void checkAnswer() {
        boolean correct = (option1.isSelected() && option1.getText().equals(randomizedCareers.get(currentQuestion)[0])) ||
                          (option2.isSelected() && option2.getText().equals(randomizedCareers.get(currentQuestion)[0]));
        
        if (correct) {
            score++;
            playSound("correct.wav");
        } else {
            playSound("incorrect.wav");
        }
        
        currentQuestion++;
        loadQuestion();
    }
    
    private void playSound(String soundFile) {
        try {
            File file = new File(soundFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new t2gamecode();
    }
}
