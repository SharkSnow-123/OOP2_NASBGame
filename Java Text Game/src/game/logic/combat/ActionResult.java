package game.logic.combat;

public class ActionResult {
    private final boolean success;
    private final boolean hit;
    private final boolean critical;
    private final int damage;
    private final int healing;
    private final String message;

    // Kini nga constructor magtipig sa result sa usa ka action sa battle.
    private ActionResult(boolean success, boolean hit, boolean critical, int damage, int healing, String message) {
        this.success = success;
        this.hit = hit;
        this.critical = critical;
        this.damage = damage;
        this.healing = healing;
        this.message = message;
    }

    // Kini nga function mohimo ug failed result kung dili pwede ang action.
    public static ActionResult failure(String message) {
        return new ActionResult(false, false, false, 0, 0, message);
    }

    // Kini nga function mohimo ug result kung naigo ang target.
    public static ActionResult hit(int damage, boolean critical, String message) {
        return new ActionResult(true, true, critical, damage, 0, message);
    }

    // Kini nga function mohimo ug result kung misipyat ang attack.
    public static ActionResult miss(String message) {
        return new ActionResult(true, false, false, 0, 0, message);
    }

    // Kini nga function mohimo ug result kung healing ang nahitabo.
    public static ActionResult heal(int healing, String message) {
        return new ActionResult(true, true, false, 0, healing, message);
    }

    // Kini nga function mohimo ug result para sa effects nga walay direct damage.
    public static ActionResult effect(String message) {
        return new ActionResult(true, true, false, 0, 0, message);
    }

    // Kini nga function mosulti kung successful ba ang action.
    public boolean isSuccess() {
        return success;
    }

    // Kini nga function mosulti kung niigo ba ang action.
    public boolean isHit() {
        return hit;
    }

    // Kini nga function mosulti kung critical hit ba ang action.
    public boolean isCritical() {
        return critical;
    }

    // Kini nga function mokuha sa damage value.
    public int getDamage() {
        return damage;
    }

    // Kini nga function mokuha sa healing value.
    public int getHealing() {
        return healing;
    }

    // Kini nga function mokuha sa battle message nga ipakita sa UI.
    public String getMessage() {
        return message;
    }
}
