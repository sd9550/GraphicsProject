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
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
            0, 3, 4, 3, 0, 0, 0, 0, 0, 0,
            0, 4, 3, 4, 5, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static final int BOARD_WIDTH = 610;
    public static final int BOARD_HEIGHT = 630;
    public static final int STATUS_HEIGHT = 50;
    private static final int STATUS_HEIGHT_DIFF = STATUS_HEIGHT * 2;
    private Image firstGrass, secondGrass, slime, dirt, rock, torch, pot, selected;
    //private short[] levelData = new short[N_BLOCKS * N_BLOCKS];
    private Goblin firstGoblin, secondGoblin, thirdGoblin;
    private Inventory inventory;
    private Sound sound;
    private Timer timer;
    private int currentDay, dayTimer;
    private JLabel dayLabel, goblinLabel, inventoryLabel;
    private ArrayList<Goblin> goblins = new ArrayList<>();

    public Board() {
        sound = new Sound();
        inventory = new Inventory();
        loadImages();
        loadCreatures();
        loadUI();
        loadValues();
        sound.playPeacefulMusic();
    }

    private void loadImages() {
        dirt = new ImageIcon("images/terrain/dirt.jpg").getImage();
        //tree = new ImageIcon("images/tree2.jpg").getImage();
        firstGrass = new ImageIcon("images/terrain/grass1.png").getImage();
        secondGrass = new ImageIcon("images/terrain/grass2.png").getImage();
        slime = new ImageIcon("images/slime3.png").getImage();
        rock = new ImageIcon("images/terrain/rock4.png").getImage();
        torch = new ImageIcon("images/Torch.png").getImage();
        pot = new ImageIcon("images/pot.png").getImage();
        //bottles = new ImageIcon("images/bottles.png").getImage();
        selected = new ImageIcon("images/selected.png").getImage();
    }

    private void loadCreatures() {
        firstGoblin = new Goblin(10, "images/creatures/goblin2.png", 300, 300, Goblin.MINER);
        secondGoblin = new Goblin(10, "images/creatures/goblin2.png", 300, 300, Goblin.GUARD);
        thirdGoblin = new Goblin(10, "images/creatures/goblin2.png", 300, 300, Goblin.CRAFTER);

        goblins.add(firstGoblin);
        goblins.add(secondGoblin);
        goblins.add(thirdGoblin);
    }

    private void loadUI() {
        setFocusable(true);
        addKeyListener(this);
        setBackground(Color.BLACK);
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        dayLabel = new JLabel();
        dayLabel.setFont(new Font("Verdana", Font.PLAIN, 32));
        dayLabel.setForeground(Color.WHITE);
        dayLabel.setText("Day " + currentDay);
        goblinLabel = new JLabel();
        goblinLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        goblinLabel.setForeground(Color.WHITE);
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

        for (y = STATUS_HEIGHT_DIFF; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {
                if (worldData[i] == 0) {
                    g2d.drawImage(dirt, x, y, this);
                } else if (worldData[i] == 1) {
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
                } else if (worldData[i] == 5) {
                    g2d.drawImage(dirt, x, y, this);
                    g2d.drawImage(pot, x, y, this);
                }
                i++;
            }
        }
    }

    private void drawCreatures(Graphics2D g2d) {
        for (Goblin goblin : goblins) {
            g2d.drawImage(goblin.getCreatureImage(), goblin.getX(), goblin.getY(), this);
            if (goblin.getSelected())
                g2d.drawImage(selected, goblin.getX(), goblin.getY(), this);
        }
    }

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

    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        int keyNumber = Character.getNumericValue(keyChar);
        int index = keyNumber - 1;
        try {
            for (Goblin goblin : goblins)
                goblin.setDeselected();
            goblinLabel.setText("Goblin " + keyNumber + " - " + "Health: " + goblins.get(index).getHealth() + " Sleep: " + goblins.get(index).getSleepLevelString() + " Role: " + goblins.get(index).getCurrentRole());
            goblins.get(index).setSelected();
        } catch (IndexOutOfBoundsException ex) {
            goblinLabel.setText("");
            for (Goblin goblin : goblins)
                goblin.setDeselected();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}

