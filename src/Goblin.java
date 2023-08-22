import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class Goblin extends Creature implements MouseListener {

    public static final int MINER = 0;
    public static final int GUARD = 1;
    public static final int CRAFTER = 2;
    private final int MOVEMENT_SPEED = 5;
    private final int UPPER_BOUND = 1000;
    private static final String DEFAULT_IMAGE = "images/goblin2.png";
    private static final String MINING_IMAGE = "images/goblin-mine.gif";
    private static final String GUARDING_IMAGE = "images/goblin-guard.png";
    private static final String CRAFTING_IMAGE = "images/goblin-crafter.gif";
    private static final String SLEEPING_IMAGE = "images/goblin-sleep.gif";
    private int currentRole;

    private int sleepLevel;
    private int funLevel;
    private int fearLevel;
    private int xCor, yCor, xDirection, yDirection, xSteps, ySteps;
    private boolean isSleeping, arrivedX, arrivedY;
    private Random random = new Random();

    public Goblin(int h, String img, int x, int y, int role) {
        super(h, img, x, y);
        this.sleepLevel = random.nextInt(UPPER_BOUND);
        this.funLevel = random.nextInt(UPPER_BOUND);
        this.fearLevel = random.nextInt(UPPER_BOUND);
        this.currentRole = role;
        this.isSleeping = false;
    }

    public void goblinBehavior() {
        if (this.getSleepLevel() <= 0 && !this.isSleeping) {
            this.startSleep();
        }
        else if (currentRole == MINER && !this.isSleeping) {
            this.startMining();
        } else if (currentRole == GUARD && !this.isSleeping) {
            this.startGuarding();
        } else if (currentRole == CRAFTER && !this.isSleeping) {
            this.startCrafting();
        }

        if (!this.isSleeping) {
            this.sleepLevel -= 1;
        } else {
            this.sleepLevel += 1;
            if (this.getSleepLevel() > 900) {
                this.isSleeping = false;
                this.setCreatureImage(DEFAULT_IMAGE);
            }
        }
    }

    private void startSleep() {
        this.isSleeping = true;
        this.setCreatureImage(SLEEPING_IMAGE);
    }

    private void startMining() {
        int xDistanceFromRocks = 50 - this.getX();
        int yDistanceFromRocks = 50 - this.getY();
        this.arrivedX = false;
        this.arrivedY = false;

        if (this.xCor >= xDistanceFromRocks) {
            this.setX(this.getX() - 5);
        } else {
            this.arrivedX = true;
        }
        if (this.yCor >= yDistanceFromRocks) {
            this.setY(this.getY() - 5);
        } else {
            this.arrivedY = true;
        }

        if (this.arrivedX && this.arrivedY) {
            this.setCreatureImage(MINING_IMAGE);
        }
    }

    private void startGuarding() {
        int xDistanceFromEntrance = 500 - this.getX();
        int yDistanceFromEntrance = 300 - this.getY();
        this.arrivedX = false;
        this.arrivedY = false;


        if (this.xCor <= xDistanceFromEntrance) {
            this.setX(this.getX() + 5);
        } else {
            this.arrivedX = true;
        }
        if (this.yCor <= yDistanceFromEntrance) {
            this.setY(this.getY() + 5);
        } else {
            this.arrivedY = true;
        }

        if (this.arrivedX && this.arrivedY) {
            this.setCreatureImage(GUARDING_IMAGE);
        }
    }

    public void startCrafting() {
        int xDistanceFromGrass = 100 - this.getX();
        int yDistanceFromGrass = 460 - this.getY();
        this.arrivedX = false;
        this.arrivedY = false;

        this.xSteps = xDistanceFromGrass / 5;
        this.ySteps = yDistanceFromGrass / 5;

        if (xSteps < 0) {
            this.setX(this.getX() - MOVEMENT_SPEED);
            this.xSteps += 1;
        } else if (xSteps > 0) {
            this.setX(this.getX() + MOVEMENT_SPEED);
            this.xSteps += 1;
        } else {
            this.arrivedX = true;
        }

        if (ySteps < 0) {
            this.setY(this.getY() - MOVEMENT_SPEED);
            this.ySteps += 1;
        } else if (ySteps > 0) {
            this.setY(this.getY() + MOVEMENT_SPEED);
            this.ySteps += 1;
        } else {
            this.arrivedY = true;
        }

        if (this.arrivedX && this.arrivedY) {
            this.setCreatureImage(CRAFTING_IMAGE);
        }
    }

    public int getSleepLevel() {
        return sleepLevel;
    }

    public int getFunLevel() {
        return funLevel;
    }

    public int getFearLevel() {
        return fearLevel;
    }

    public void setCurrentRole(int r) {
        this.currentRole = r;
    }



    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        System.out.println(this.currentRole + " was clicked!");
    }


    public void mousePressed(MouseEvent e) {

    }


    public void mouseReleased(MouseEvent e) {

    }


    public void mouseEntered(MouseEvent e) {

    }


    public void mouseExited(MouseEvent e) {

    }
}
