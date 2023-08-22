public class Goblin extends Creature {

    public static final int MINER = 0;
    public static final int GUARD = 1;
    private static final String DEFAULT_IMAGE = "images/goblin2.png";
    private static final String MINING_IMAGE = "images/goblin-mine.gif";
    private static final String GUARDING_IMAGE = "images/goblin-guard.png";
    private static final String SLEEPING_IMAGE = "images/goblin-sleep.gif";
    private int currentRole;

    private int sleepLevel;
    private int funLevel;
    private int fearLevel;
    private int xCor, yCor, xDirection, yDirection;
    private boolean isSleeping, arrivedX, arrivedY;

    public Goblin(int h, String img, int x, int y, int role) {
        super(h, img, x, y);
        this.sleepLevel = 500;
        this.funLevel = 100;
        this.fearLevel = 100;
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


}
