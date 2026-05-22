package game.logic.combat;

public class ActionResult {
    private final boolean success;
    private final boolean hit;
    private final boolean critical;
    private final int damage;
    private final int healing;
    private final String message;

    private ActionResult(boolean success, boolean hit, boolean critical, int damage, int healing, String message) {
        this.success = success;
        this.hit = hit;
        this.critical = critical;
        this.damage = damage;
        this.healing = healing;
        this.message = message;
    }

    public static ActionResult failure(String message) {
        return new ActionResult(false, false, false, 0, 0, message);
    }

    public static ActionResult hit(int damage, boolean critical, String message) {
        return new ActionResult(true, true, critical, damage, 0, message);
    }

    public static ActionResult miss(String message) {
        return new ActionResult(true, false, false, 0, 0, message);
    }

    public static ActionResult heal(int healing, String message) {
        return new ActionResult(true, true, false, 0, healing, message);
    }

    public static ActionResult effect(String message) {
        return new ActionResult(true, true, false, 0, 0, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isHit() {
        return hit;
    }

    public boolean isCritical() {
        return critical;
    }

    public int getDamage() {
        return damage;
    }

    public int getHealing() {
        return healing;
    }

    public String getMessage() {
        return message;
    }
}
