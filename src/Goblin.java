import java.util.Random;

public class Goblin extends Creature {

    public static final int MINER = 0;
    public static final int GUARD = 1;
    public static final int CRAFTER = 2;
    private final int MOVEMENT_SPEED = 5;
    private final int UPPER_BOUND = 1000;
    private static final String DEFAULT_IMAGE = "images/creatures/goblin2.png";
    private static final String MINING_IMAGE = "images/creatures/goblin-mine.gif";
    private static final String GUARDING_IMAGE = "images/creatures/goblin-guard.png";
    private static final String CRAFTING_IMAGE = "images/creatures/goblin-crafter.gif";
    private static final String SLEEPING_IMAGE = "images/creatures/goblin-sleep.gif";
    private int currentRole;
    private int miningProgress;
    private int sleepLevel;
    private int funLevel;
    private int fearLevel;
    private int xSteps, ySteps;
    private boolean isSleeping, arrivedX, arrivedY, isSelected;
    private Random random = new Random();
    private PersonalInventory inv;

    public Goblin(int h, String img, int x, int y, int role) {
        super(h, img, x, y);
        this.inv = new PersonalInventory();
        this.sleepLevel = random.nextInt(UPPER_BOUND);
        this.funLevel = random.nextInt(UPPER_BOUND);
        this.fearLevel = random.nextInt(UPPER_BOUND);
        this.currentRole = role;
        this.isSleeping = false;
        this.isSelected = false;
        this.miningProgress = 0;
    }

    public void goblinBehavior() {
        if (this.getSleepLevel() <= 0 && !this.isSleeping) {
            this.startSleep();
        }
        else if (currentRole == MINER && !this.isSleeping) {
            if (this.miningProgress > UPPER_BOUND)
                this.returnGoods();
            else
                this.startMining();
        } else if (currentRole == GUARD && !this.isSleeping) {
            this.startGuarding();
        } else if (currentRole == CRAFTER && !this.isSleeping) {
            this.startCrafting();
        }

        if (!this.isSleeping) {
            this.sleepLevel -= 1;
        } else {
            this.sleepLevel += 5;
            if (this.getSleepLevel() >= UPPER_BOUND) {
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
        int yDistanceFromRocks = 150 - this.getY();
        this.arrivedX = false;
        this.arrivedY = false;

        this.xSteps = xDistanceFromRocks / 5;
        this.ySteps = yDistanceFromRocks / 5;

        if (xSteps < 0) {
            this.setX(this.getX() - MOVEMENT_SPEED);
        } else if (xSteps > 0) {
            this.setX(this.getX() + MOVEMENT_SPEED);
        } else {
            this.arrivedX = true;
        }

        if (ySteps < 0) {
            this.setY(this.getY() - MOVEMENT_SPEED);
        } else if (ySteps > 0) {
            this.setY(this.getY() + MOVEMENT_SPEED);
        } else {
            this.arrivedY = true;
        }

        if (this.arrivedX && this.arrivedY) {
            this.setCreatureImage(MINING_IMAGE);
            this.miningProgress += 10;
            System.out.println(miningProgress);
        }
    }

    private void startGuarding() {
        int xDistanceFromEntrance = 500 - this.getX();
        int yDistanceFromEntrance = 400 - this.getY();
        this.arrivedX = false;
        this.arrivedY = false;

        this.xSteps = xDistanceFromEntrance / 5;
        this.ySteps = yDistanceFromEntrance / 5;


        if (xSteps < 0) {
            this.setX(this.getX() - MOVEMENT_SPEED);
        } else if (xSteps > 0) {
            this.setX(this.getX() + MOVEMENT_SPEED);
        } else {
            this.arrivedX = true;
        }

        if (ySteps < 0) {
            this.setY(this.getY() - MOVEMENT_SPEED);
        } else if (ySteps > 0) {
            this.setY(this.getY() + MOVEMENT_SPEED);
        } else {
            this.arrivedY = true;
        }

        if (this.arrivedX && this.arrivedY) {
            this.setCreatureImage(GUARDING_IMAGE);
        }
    }

    public void startCrafting() {
        int xDistanceFromGrass = 100 - this.getX();
        int yDistanceFromGrass = 560 - this.getY();
        this.arrivedX = false;
        this.arrivedY = false;

        this.xSteps = xDistanceFromGrass / 5;
        this.ySteps = yDistanceFromGrass / 5;

        if (xSteps < 0) {
            this.setX(this.getX() - MOVEMENT_SPEED);
        } else if (xSteps > 0) {
            this.setX(this.getX() + MOVEMENT_SPEED);
        } else {
            this.arrivedX = true;
        }

        if (ySteps < 0) {
            this.setY(this.getY() - MOVEMENT_SPEED);
        } else if (ySteps > 0) {
            this.setY(this.getY() + MOVEMENT_SPEED);
        } else {
            this.arrivedY = true;
        }

        if (this.arrivedX && this.arrivedY) {
            this.setCreatureImage(CRAFTING_IMAGE);
        }
    }

    private void returnGoods() {
        this.setCreatureImage(DEFAULT_IMAGE);
        int xDistanceFromGrass = 150 - this.getX();
        int yDistanceFromGrass = 560 - this.getY();
        this.arrivedX = false;
        this.arrivedY = false;

        this.xSteps = xDistanceFromGrass / 5;
        this.ySteps = yDistanceFromGrass / 5;


        if (xSteps < 0) {
            this.setX(this.getX() - MOVEMENT_SPEED);
        } else if (xSteps > 0) {
            this.setX(this.getX() + MOVEMENT_SPEED);
        } else {
            this.arrivedX = true;
        }

        if (ySteps < 0) {
            this.setY(this.getY() - MOVEMENT_SPEED);
        } else if (ySteps > 0) {
            this.setY(this.getY() + MOVEMENT_SPEED);
        } else {
            this.arrivedY = true;
        }

        if (this.arrivedX && this.arrivedY) {
            this.inv.addRocksToMainInventory(10);
            this.miningProgress = 0;
        }
    }

    public int getSleepLevel() {
        return this.sleepLevel;
    }

    public String getSleepLevelString() {
        String sleepString;
        if (this.isSleeping)
            return "Asleep";
        if (this.sleepLevel < 300)
            sleepString = "Low";
        else if (this.sleepLevel < 600)
            sleepString = "Adequate";
        else
            sleepString = "Awake";
        return sleepString;
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

    public String getCurrentRole() {
        if (this.currentRole == MINER)
            return "Miner";
        else if (this.currentRole == GUARD)
            return "Guard";
        else
            return "Crafter";
    }

    public void setSelected() {
        this.isSelected = true;
    }

    public void setDeselected() {
        this.isSelected = false;
    }

    public boolean getSelected() {
        return this.isSelected;
    }

}
