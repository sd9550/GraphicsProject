public class Slime extends Creature {

    public final static String DEATH_IMAGE = "images/creatures/red-slime-death.gif";
    private final int MOVEMENT_SPEED = 5;
    private int xDistanceFromLoc, yDistanceFromLoc;
    private int xSteps, ySteps;
    private boolean arrivedX, arrivedY, startedAttack;

    public Slime(int h, String img, int x, int y) {
        super(h, img, x, y);
        this.startedAttack = false;
    }

    public void slimeBehavior() {
        this.startWalking();
        if (this.arrivedX && this.arrivedY)
            this.startedAttack = true;
    }

    private void startWalking() {
        xDistanceFromLoc = (TerrainLocations.ENTRANCE_X + 50) - this.getX();
        yDistanceFromLoc = TerrainLocations.ENTRANCE_Y - this.getY();

        this.xSteps = xDistanceFromLoc / MOVEMENT_SPEED;
        this.ySteps = yDistanceFromLoc / MOVEMENT_SPEED;

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
    }

    public boolean getAttackStarted() {
        return this.startedAttack;
    }
}
