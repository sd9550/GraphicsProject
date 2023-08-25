import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Board extends JPanel implements ActionListener, KeyListener {

    private final int BLOCK_SIZE = 60;
    private final int N_BLOCKS = 10;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    // 0 = dirt, 1 = rock, 2 = torch, 3/4 = grass, 5 = pot
    private final short[] worldData = {
            10, 6, 6, 6, 6, 6, 6, 6, 6, 9,
            8, 1, 0, 0, 0, 0, 0, 0, 0, 7,
            8, 1, 0, 0, 0, 0, 0, 0, 0, 7,
            8, 0, 0, 0, 0, 0, 0, 0, 0, 7,
            8, 0, 0, 0, 0, 0, 0, 0, 0, 2,
            8, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            8, 0, 0, 0, 0, 0, 0, 0, 0, 2,
            8, 3, 4, 3, 0, 0, 0, 0, 0, 7,
            8, 4, 3, 4, 0, 0, 0, 0, 0, 7,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static final int BOARD_WIDTH = 610;
    public static final int BOARD_HEIGHT = 630;
    public static final int STATUS_HEIGHT = 50;
    private static final int STATUS_HEIGHT_DIFF = STATUS_HEIGHT * 2;
    private final String DEFAULT_GOBLIN_LABEL = "Goblin ? | Health: ?? | Energy: ????? | Role: ????";
    private Image firstGrass, secondGrass, dirt, rock, torch, pot, selected;
    private Image wallTop, wallLeft, wallRight, wallTopLeft, wallTopRight;
    //private Goblin firstGoblin, secondGoblin, thirdGoblin, fourthGoblin;
    private Inventory inventory;
    private Timer timer;
    private int currentDay, dayTimer;
    private JLabel dayLabel, goblinLabel, inventoryLabel;
    private ArrayList<Goblin> goblins = new ArrayList<>();
    private boolean testSpawnEnemy = true;
    private Slime redSlime;

    public Board() {
        inventory = new Inventory();
        loadImages();
        loadCreatures();
        loadUI();
        loadValues();
        loadMusic();
    }

    private void loadImages() {
        dirt = new ImageIcon("images/terrain/dirt.jpg").getImage();
        firstGrass = new ImageIcon("images/terrain/grass1.png").getImage();
        secondGrass = new ImageIcon("images/terrain/grass2.png").getImage();
        rock = new ImageIcon("images/terrain/rock4.png").getImage();
        torch = new ImageIcon("images/Torch.png").getImage();
        pot = new ImageIcon("images/pot.png").getImage();
        selected = new ImageIcon("images/selected.png").getImage();
        wallTop = new ImageIcon("images/wall/wall-top.png").getImage();
        wallLeft = new ImageIcon("images/wall/wall-left.png").getImage();
        wallRight = new ImageIcon("images/wall/wall-right.png").getImage();
        wallTopLeft = new ImageIcon("images/wall/wall-top-left.png").getImage();
        wallTopRight = new ImageIcon("images/wall/wall-top-right.png").getImage();
    }

    private void loadCreatures() {
//        firstGoblin = new Goblin(10, "images/creatures/goblin2.png", 300, 300, Goblin.MINER);
//        secondGoblin = new Goblin(10, "images/creatures/goblin2.png", 300, 300, Goblin.GUARD);
//        thirdGoblin = new Goblin(10, "images/creatures/goblin2.png", 300, 300, Goblin.CRAFTER);
//        fourthGoblin = new Goblin(10, "images/creatures/goblin2.png", 300, 300, Goblin.GATHERER);
        redSlime = new Slime(10, "images/creatures/red-slime.gif", 550, 400);

        goblins.add(new Goblin(10, "images/creatures/goblin2.png", 300, 300, Goblin.MINER));
        goblins.add(new Goblin(10, "images/creatures/goblin2.png", 300, 300, Goblin.GUARD));
        goblins.add(new Goblin(10, "images/creatures/goblin2.png", 300, 300, Goblin.CRAFTER));
        goblins.add(new Goblin(10, "images/creatures/goblin2.png", 300, 400, Goblin.GATHERER));
    }

    private void loadUI() {
        setFocusable(true);
        addKeyListener(this);
        setBackground(Color.BLACK);
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        dayLabel = new JLabel();
        dayLabel.setFont(new Font("Verdana", Font.PLAIN, 32));
        dayLabel.setForeground(Color.WHITE);
        dayLabel.setText("Day 1");
        goblinLabel = new JLabel();
        goblinLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        goblinLabel.setForeground(Color.WHITE);
        goblinLabel.setText(DEFAULT_GOBLIN_LABEL);
        inventoryLabel = new JLabel();
        inventoryLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        inventoryLabel.setForeground(Color.WHITE);
        inventoryLabel.setText("Rocks: " + inventory.getTotalRocks() + " Wood: " + inventory.getTotalWood());
        add(dayLabel);
        add(goblinLabel);
        add(inventoryLabel);
    }

    private void loadValues() {
        currentDay = 1;
        dayTimer = 0;
        timer = new Timer(50, this);
        timer.start();
    }

    private void loadMusic() {
        Thread t = new Thread(Sound::new);
        t.start();
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
        // paints the level based on number in array
        for (y = STATUS_HEIGHT_DIFF; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {
                if (worldData[i] == 0) {
                    g2d.drawImage(dirt, x, y, this);
                } else if (worldData[i] == 1) {
                    g2d.drawImage(dirt, x, y, this);
                    g2d.drawImage(rock, x - 40, y, this);
                } else if (worldData[i] == 2) {
                    g2d.drawImage(dirt, x, y, this);
                    g2d.drawImage(torch, x, y, this);
                } else if (worldData[i] == 3) {
                    g2d.drawImage(dirt, x, y, this);
                    g2d.drawImage(firstGrass, x, y, this);
                } else if (worldData[i] == 4) {
                    g2d.drawImage(dirt, x, y, this);
                    g2d.drawImage(secondGrass, x, y, this);
                } else if (worldData[i] == 5) {
                    g2d.drawImage(dirt, x, y, this);
                    g2d.drawImage(pot, x, y, this);
                } else if (worldData[i] == 6) {
                    g2d.drawImage(wallTop, x, y, this);
                } else if (worldData[i] == 7) {
                    g2d.drawImage(dirt, x, y, this);
                    g2d.drawImage(wallRight, x, y, this);
                } else if (worldData[i] == 8) {
                    g2d.drawImage(dirt, x, y, this);
                    g2d.drawImage(wallLeft, x, y, this);
                } else if (worldData[i] == 9) {
                    g2d.drawImage(dirt, x, y, this);
                    g2d.drawImage(wallTopRight, x, y, this);
                } else if (worldData[i] == 10) {
                    g2d.drawImage(dirt, x, y, this);
                    g2d.drawImage(wallTopLeft, x, y, this);
                }
                i++;
            }
        }

        if (testSpawnEnemy) {
            g2d.drawImage(redSlime.getCreatureImage(), redSlime.getX(), redSlime.getY(), this);
        }
    }

    private void drawCreatures(Graphics2D g2d) {
        for (Goblin goblin : goblins) {
            g2d.drawImage(goblin.getCreatureImage(), goblin.getX(), goblin.getY(), this);
            if (goblin.getSelected())
                g2d.drawImage(selected, goblin.getX(), goblin.getY(), this);
        }
    }

    // timer triggers this every 50 ms to repaint and move around goblins as needed
    public void actionPerformed(ActionEvent e) {
        dayTimer += 1;

        for (Goblin goblin : goblins) {
            goblin.goblinBehavior();
        }

        repaint();

        if (dayTimer % 1400 == 0) {
            currentDay += 1;
            dayLabel.setText("Day " + currentDay);
            dayTimer = 0;
        } else if (dayTimer % 100 == 0) {
            inventoryLabel.setText("Rocks: " + inventory.getTotalRocks() + " Wood: " + inventory.getTotalWood());
        }
    }

    // updates the label when a number is pressed
    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        int keyNumber = Character.getNumericValue(keyChar);
        int index = keyNumber - 1;
        try {
            for (Goblin goblin : goblins)
                goblin.setDeselected();
            goblinLabel.setText("Goblin " + keyNumber + " | " + "Health: " + goblins.get(index).getHealth() + " | Energy: " + goblins.get(index).getSleepLevelString() + " | Role: " + goblins.get(index).getCurrentRole());
            goblins.get(index).setSelected();
        } catch (IndexOutOfBoundsException ex) {
            goblinLabel.setText(DEFAULT_GOBLIN_LABEL);
            for (Goblin goblin : goblins)
                goblin.setDeselected();
        } catch (Exception ex) {
            goblinLabel.setText(DEFAULT_GOBLIN_LABEL);
            for (Goblin goblin : goblins)
                goblin.setDeselected();
            System.out.println("Error: " + ex);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}

