import java.util.Random;

public class Goblin extends Creature {

    public static final int MINER = 0;
    public static final int GUARD = 1;
    public static final int CRAFTER = 2;
    public static final int GATHERER = 3;
    private final int MOVEMENT_SPEED = 5;
    private final int UPPER_BOUND = 1000;
    private static final String DEFAULT_IMAGE = "images/creatures/goblin2.png";
    private static final String MINING_IMAGE = "images/creatures/goblin-mine.gif";
    private static final String GUARDING_IMAGE = "images/creatures/goblin-guard.png";
    private static final String CRAFTING_IMAGE = "images/creatures/goblin-crafter.gif";
    private static final String SLEEPING_IMAGE = "images/creatures/sleep.gif";
    private static final String WALK_LEFT = "images/creatures/walk-left.gif";
    private static final String WALK_RIGHT = "images/creatures/walk-right.gif";
    public static final String ATTACK_IMAGE = "images/creatures/attack.gif";
    private int currentRole;
    private int miningProgress;
    private int sleepLevel, waitLevel;
    private int xSteps, ySteps, xDistanceFromLoc, yDistanceFromLoc;
    private boolean isSleeping, arrivedX, arrivedY, isSelected, isCraftingAltar;
    private final Random random = new Random();
    private PersonalInventory inv;

    public Goblin(int h, String img, int x, int y, int role) {
        super(h, img, x, y);
        this.inv = new PersonalInventory();
        this.sleepLevel = random.nextInt(UPPER_BOUND);
        this.waitLevel = 0;
        this.currentRole = role;
        this.isSleeping = false;
        this.isSelected = false;
        this.miningProgress = 0;
        this.arrivedX = false;
        this.arrivedY = false;
        this.isCraftingAltar = false;
        this.xDistanceFromLoc = 0;
        this.yDistanceFromLoc = 0;
    }

    public void goblinBehavior() {
        if (this.getSleepLevel() <= 0 && !this.isSleeping) {
            this.startSleep();
        }
        else if (currentRole == MINER && !this.isSleeping) {
            if (this.miningProgress > UPPER_BOUND) {
                this.resetArrived();
                this.startWalking(TerrainLocations.GOODS_AREA);
                if (this.arrivedX && this.arrivedY)
                    this.returnGoods();
            }
            else {
                if (!this.arrivedX || !this.arrivedY)
                    this.startWalking(TerrainLocations.ROCKS);
                else {
                    this.startMining();
                }
            }
        } else if (currentRole == GUARD && !this.isSleeping) {
            if (!this.arrivedX || !this.arrivedY)
                this.startWalking(TerrainLocations.ENTRANCE);
            else
                this.startGuarding();
        } else if (currentRole == CRAFTER && !this.isSleeping) {
            if (this.isCraftingAltar) {
                    this.startWalking(TerrainLocations.ALTAR);
                    if (this.getX() == TerrainLocations.ALTAR_X && this.getY() == TerrainLocations.ALTAR_Y) {
                        this.setCreatureImage(CRAFTING_IMAGE);
                        this.startWaiting();
                        if (this.waitLevel > 200) {
                            Crafting.altarWasCrafted = true;
                            this.waitLevel = 0;
                            this.isCraftingAltar = false;
                            this.resetArrived();
                        }
                    }
            } else {
                if (!this.arrivedX || !this.arrivedY)
                    this.startWalking(TerrainLocations.CRAFTING_AREA);
                else
                    this.startCrafting();
            }
        } else if (currentRole == GATHERER && !this.isSleeping) {
            if (!this.arrivedX || !this.arrivedY) {
                this.startWalking(TerrainLocations.OUTSIDE);
            }
            else {
                this.startWaiting();
                if (this.waitLevel >= 50) {
                    this.startWalking(TerrainLocations.GOODS_AREA);
                    if (this.getX() == TerrainLocations.GOODS_X && this.getY() == TerrainLocations.GOODS_Y) {
                        this.returnGoods();
                        this.waitLevel = 0;
                    }
                }
            }
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

    private void startWalking(int loc) {
        if (loc == TerrainLocations.ROCKS) {
            xDistanceFromLoc = TerrainLocations.ROCK_X - this.getX();
            yDistanceFromLoc = TerrainLocations.ROCK_Y - this.getY();
        } else if (loc == TerrainLocations.ENTRANCE) {
            xDistanceFromLoc = TerrainLocations.ENTRANCE_X - this.getX();
            yDistanceFromLoc = TerrainLocations.ENTRANCE_Y - this.getY();
        } else if (loc == TerrainLocations.CRAFTING_AREA) {
            xDistanceFromLoc = TerrainLocations.CRAFT_X - this.getX();
            yDistanceFromLoc = TerrainLocations.CRAFT_Y - this.getY();
        } else if (loc == TerrainLocations.GOODS_AREA) {
            this.setCreatureImage(DEFAULT_IMAGE);
            xDistanceFromLoc = TerrainLocations.GOODS_X - this.getX();
            yDistanceFromLoc = TerrainLocations.GOODS_Y - this.getY();
        } else if (loc == TerrainLocations.OUTSIDE) {
            xDistanceFromLoc = 650 - this.getX();
            yDistanceFromLoc = 350 - this.getY();
        } else if (loc == TerrainLocations.ALTAR) {
            xDistanceFromLoc = TerrainLocations.ALTAR_X - this.getX();
            yDistanceFromLoc = TerrainLocations.ALTAR_Y - this.getY();
        }

        this.xSteps = xDistanceFromLoc / MOVEMENT_SPEED;
        this.ySteps = yDistanceFromLoc / MOVEMENT_SPEED;

        if (xSteps < 0) {
            this.setX(this.getX() - MOVEMENT_SPEED);
            this.setCreatureImage(WALK_LEFT);
        } else if (xSteps > 0) {
            this.setX(this.getX() + MOVEMENT_SPEED);
            this.setCreatureImage(WALK_RIGHT);
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
    }

    private void startMining() {
        this.setCreatureImage(MINING_IMAGE);
        this.miningProgress += 10;
    }

    private void startGuarding() {
        this.setCreatureImage(GUARDING_IMAGE);
    }

    public void startCrafting() {
        this.setCreatureImage(CRAFTING_IMAGE);
    }

    private void returnGoods() {
        if (this.currentRole == MINER) {
            this.inv.addRocksToMainInventory(10);
            this.miningProgress = 0;
        }
        else
            this.inv.addWoodToMainInventory(10);

        this.resetArrived();
    }

    public void startWaiting() {
        this.waitLevel += 1;
    }

    public int getWaiting() {
        return this.waitLevel;
    }

    private void resetArrived() {
        this.arrivedX = false;
        this.arrivedY = false;
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

    public void setCurrentRole(int r) {
        this.currentRole = r;
    }

    public String getCurrentRole() {
        if (this.currentRole == MINER)
            return "Miner";
        else if (this.currentRole == GUARD)
            return "Guard";
        else if (this.currentRole == CRAFTER)
            return "Crafter";
        else
            return "Gatherer";
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

    public void setAwake() {
        if (this.isSleeping)
            this.isSleeping = false;
    }

    public boolean isCraftingAltar() {
        return isCraftingAltar;
    }

    public void setCraftingAltar(boolean craftingAltar) {
        isCraftingAltar = craftingAltar;
    }
}
