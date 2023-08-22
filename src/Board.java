import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Board extends JPanel implements ActionListener {

    private final int BLOCK_SIZE = 60;
    private final int N_BLOCKS = 10;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final short[] worldData = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                3, 4, 3, 0, 0, 0, 0, 0, 0, 0,
                                4, 3, 4, 0, 0, 0, 0, 0, 0, 0 };
    public static final int BOARD_WIDTH = 610;
    public static final int BOARD_HEIGHT = 630;
    private Image tree, firstGrass, secondGrass, slime, dirt, rock, torch;
    private short[] levelData = new short[N_BLOCKS * N_BLOCKS];
    private Goblin firstGoblin, secondGoblin;
    private Timer timer;
    private int currentDay, dayTimer;
    private JLabel dayLabel;
    private ArrayList<Goblin> goblins = new ArrayList<>();

    public Board() {
        loadImages();
        loadCreatures();
        loadUI();
        loadValues();
    }

    private void loadImages() {
        dirt = new ImageIcon("images/dirt.jpg").getImage();
        tree = new ImageIcon("images/tree2.jpg").getImage();
        firstGrass = new ImageIcon("images/grass1.png").getImage();
        secondGrass = new ImageIcon("images/grass2.png").getImage();
        slime = new ImageIcon("images/slime3.png").getImage();
        rock = new ImageIcon("images/rock4.png").getImage();
        torch = new ImageIcon("images/Torch.png").getImage();
    }

    private void loadCreatures() {
        firstGoblin = new Goblin(10, "images/goblin2.png", 300, 300, Goblin.MINER);
        secondGoblin = new Goblin(10, "images/goblin2.png", 300, 300, Goblin.GUARD);
        goblins.add(firstGoblin);
        goblins.add(secondGoblin);
    }

    private void loadUI() {
        dayLabel = new JLabel();
        dayLabel.setFont(new Font("Verdana", Font.PLAIN, 32));
        dayLabel.setText("Day " + currentDay);
        add(dayLabel);
    }

    private void loadValues() {
        currentDay = 1;
        dayTimer = 0;
        timer = new Timer(50,this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        drawWorld(g2d);
        drawCreatures(g2d);
    }

    private void drawWorld(Graphics2D g2d) {
        short i = 0;
        int x, y;

        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {
                if (worldData[i] == 0) {
                    g2d.drawImage(dirt, x, y, this);
                }
                else if (worldData[i] == 1) {
                    g2d.drawImage(dirt, x, y, this);
                    g2d.drawImage(rock, x, y, this);
                } else if (worldData[i] == 2) {
                    g2d.drawImage(dirt, x, y, this);
                    g2d.drawImage(torch, x, y, this);
                } else if (worldData[i] == 3) {
                    g2d.drawImage(dirt, x, y, this);
                    g2d.drawImage(firstGrass, x, y, this);
                } else if (worldData[i] == 4) {
                    g2d.drawImage(dirt, x, y, this);
                    g2d.drawImage(secondGrass, x, y, this);
                }
                i++;
            }
        }
    }

    private void drawCreatures(Graphics2D g2d) {
        for (int i = 0; i < goblins.size(); ++i) {

        }
        g2d.drawImage(firstGoblin.getCreatureImage(), firstGoblin.getX(), firstGoblin.getY(), this);
        g2d.drawImage(secondGoblin.getCreatureImage(), secondGoblin.getX(), secondGoblin.getY(), this);
    }

    public void actionPerformed(ActionEvent e) {
        dayTimer += 1;
        firstGoblin.goblinBehavior();
        secondGoblin.goblinBehavior();
        repaint();

        if (dayTimer % 1400 == 0) {
            System.out.println("Day timer was triggered!");
            currentDay += 1;
            dayLabel.setText("Day " + currentDay);
            dayTimer = 0;
        }
    }

}
