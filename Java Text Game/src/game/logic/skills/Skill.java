package game.logic.skills;

import game.logic.combat.ActionResult;
import game.logic.characters.Character;

import java.util.Random;

public abstract class Skill {
    private static final Random RANDOM = new Random();
    private static final int BASIC_ATTACK_MP_GAIN = 15;
    private static final double BASIC_HIT_CHANCE = 0.85;
    private static final double SKILL_HIT_CHANCE = 0.80;
    private static final double ULTIMATE_HIT_CHANCE = 0.90;
    private static final double CRITICAL_CHANCE = 0.15;
    private static final double CRITICAL_MULTIPLIER = 1.5;
    private static final int MINIMUM_HIT_DAMAGE = 5;
    private static final int CAREZA_DEFENSE_CAP = 125;
    private static final int CAREZA_ULTIMATE_DEFENSE_BOOST = 15;
    private static final int CAREZA_ULTIMATE_HEAL_LIMIT = 50;

    public final String name;
    protected int MPcost;

    // Kini nga constructor mag-set sa skill name ug MP cost.
    public Skill(String name, int mpCost) {
        this.name = name;
        this.MPcost = mpCost;
    }

    // Kini nga function mao ang default gamit sa skill kung walay special effect.
    public ActionResult use(Character user, Character target) {
        ActionResult check = canUse(user);
        if (!check.isSuccess()) {
            return check;
        }

        return ActionResult.effect(user.name + " used " + name + ".");
    }

    // Kini nga function mokuha kung pila ka MP ang kinahanglan sa skill.
    public int getMpCost() {
        return MPcost;
    }

    // Kini nga function mo-check kung pwede ba mogamit ug skill ang character.
    protected ActionResult canUse(Character user) {
        if (user.isDown()) {
            return ActionResult.failure(user.name + " is down!");
        }

        if (!user.hasEnoughMp(MPcost)) {
            return ActionResult.failure(user.name + " does not have enough MP for " + name + "!");
        }

        return ActionResult.effect("");
    }

    // Kini nga function mokompyut ug damage, miss chance, ug critical hit.
    protected ActionResult damageWithChance(Character user, Character target, int baseDamage, double hitChance) {
        ActionResult check = canUse(user);
        if (!check.isSuccess()) {
            return check;
        }

        user.spendMp(MPcost);

        if (RANDOM.nextDouble() > hitChance) {
            return ActionResult.miss(user.name + "'s " + name + " missed!");
        }

        boolean critical = RANDOM.nextDouble() < CRITICAL_CHANCE;
        int finalDamage = Math.max(MINIMUM_HIT_DAMAGE, baseDamage);
        if (critical) {
            finalDamage = (int) Math.round(finalDamage * CRITICAL_MULTIPLIER);
        }

        int damageDone = target.takeDamage(finalDamage);
        String message = user.name + "'s " + name + " dealt " + damageDone + " damage.";
        if (critical) {
            message = "Critical hit! " + message;
        }

        return ActionResult.hit(damageDone, critical, message);
    }

    public static class Attack extends Skill {
        // Kini nga constructor mohimo sa basic attack skill.
        public Attack() {
            super("Attack", 0);
        }

        @Override
        // Kini nga function mogamit sa basic attack ug mobawi ug gamay nga MP.
        public ActionResult use(Character user, Character target) {
            ActionResult result = damageWithChance(user, target, user.ATK - (target.DEF / 2), BASIC_HIT_CHANCE);
            if (result.isSuccess()) {
                user.restoreMp(BASIC_ATTACK_MP_GAIN);
            }
            return result;
        }
    }

    public static class TSquare extends Skill {
        // Kini nga constructor mohimo sa T Square skill.
        public TSquare() {
            super("TSquare", 20);
        }

        @Override
        // Kini nga function mogamit sa defense-based attack ni Careza.
        public ActionResult use(Character user, Character target) {
            int damage = user.ATK + (user.DEF / 5) - (target.DEF / 2);
            return damageWithChance(user, target, damage, SKILL_HIT_CHANCE);
        }
    }

    public static class SuccessfullFloorPlan extends Skill {
        // Kini nga constructor mohimo sa ultimate ni Careza.
        public SuccessfullFloorPlan() {
            super("SuccessfullFloorPlan", 90);
        }

        @Override
        // Kini nga function modeal ug damage, modugang DEF, ug moheal kang Careza.
        public ActionResult use(Character user, Character target) {
            int damage = user.ATK + (user.DEF / 4) - (target.DEF / 2);
            ActionResult result = damageWithChance(user, target, damage, ULTIMATE_HIT_CHANCE);
            if (result.isHit()) {
                int defenseBefore = user.DEF;
                user.DEF = Math.min(CAREZA_DEFENSE_CAP, user.DEF + CAREZA_ULTIMATE_DEFENSE_BOOST);
                int defenseGained = user.DEF - defenseBefore;
                int missingHp = user.maxHP - user.HP;
                int healing = user.heal(Math.min(CAREZA_ULTIMATE_HEAL_LIMIT, missingHp));
                String message = result.getMessage() + " Careza gained " + defenseGained
                        + " DEF and recovered " + healing + " HP.";
                return ActionResult.hit(result.getDamage(), result.isCritical(), message);
            }
            return result;
        }
    }

    public static class FirstAidKit extends Skill {
        // Kini nga constructor mohimo sa healing skill ni Cyrus.
        public FirstAidKit() {
            super("FirstAidKit", 20);
        }

        @Override
        // Kini nga function moheal sa ally nga target.
        public ActionResult use(Character user, Character target) {
            ActionResult check = canUse(user);
            if (!check.isSuccess()) {
                return check;
            }

            user.spendMp(MPcost);
            int healing = target.heal(user.maxHP / 4);
            return ActionResult.heal(healing, user.name + " healed " + target.name + " for " + healing + " HP.");
        }
    }

    public static class SyringeRevive extends Skill {
        // Kini nga constructor mohimo sa ultimate support skill ni Cyrus.
        public SyringeRevive() {
            super("SyringeRevive", 90);
        }

        @Override
        // Kini nga function mopataas ug max HP sa user ug target.
        public ActionResult use(Character user, Character target) {
            ActionResult check = canUse(user);
            if (!check.isSuccess()) {
                return check;
            }

            user.spendMp(MPcost);
            user.increaseMaxHp(50);
            target.increaseMaxHp(50);
            return ActionResult.heal(100, user.name + " restored the team with SyringeRevive.");
        }
    }

    public static class CellPhone extends Skill {
        // Kini nga constructor mohimo sa CellPhone attack skill.
        public CellPhone() {
            super("CellPhone", 20);
        }

        @Override
        // Kini nga function modeal ug attack-based damage gamit ang CellPhone.
        public ActionResult use(Character user, Character target) {
            return damageWithChance(user, target, (user.ATK / 2) + 20, SKILL_HIT_CHANCE);
        }
    }

    public static class Transformation extends Skill {
        // Kini nga constructor mohimo sa transformation ultimate ni Briar.
        public Transformation() {
            super("Transformation", 90);
        }

        @Override
        // Kini nga function mopakusog sa ATK, DEF, ug HP sa user.
        public ActionResult use(Character user, Character target) {
            ActionResult check = canUse(user);
            if (!check.isSuccess()) {
                return check;
            }

            user.spendMp(MPcost);
            user.ATK *= 2;
            user.DEF += 30;
            user.increaseMaxHp(20);
            return ActionResult.effect(user.name + " transformed and raised ATK, DEF, and HP.");
        }
    }

    public static class InfoDump extends Skill {
        // Kini nga constructor mohimo sa intellect-based InfoDump skill.
        public InfoDump() {
            super("InfoDump", 50);
        }

        @Override
        // Kini nga function modeal ug INT-based damage sa target.
        public ActionResult use(Character user, Character target) {
            return damageWithChance(user, target, (user.INT + 20) - (target.DEF / 2), SKILL_HIT_CHANCE);
        }
    }

    public static class HardHats extends Skill {
        // Kini nga constructor mohimo sa HardHats ultimate ni Dirk.
        public HardHats() {
            super("HardHats", 90);
        }

        @Override
        // Kini nga function modeal ug damage, modugang DEF sa user, ug mokunhod sa DEF sa enemy.
        public ActionResult use(Character user, Character target) {
            ActionResult result = damageWithChance(user, target, user.INT, ULTIMATE_HIT_CHANCE);
            if (result.isHit()) {
                user.DEF += 50;
                target.DEF = Math.max(0, target.DEF - 30);
            }
            return result;
        }
    }

    public static class PresidentBook extends Skill {
        // Kini nga constructor mohimo sa wisdom-based PresidentBook skill.
        public PresidentBook() {
            super("PresidentBook", 20);
        }

        @Override
        // Kini nga function modeal ug WIS-based damage sa target.
        public ActionResult use(Character user, Character target) {
            return damageWithChance(user, target, user.WIS - (target.DEF / 2), SKILL_HIT_CHANCE);
        }
    }

    public static class ExistentialCrisis extends Skill {
        // Kini nga constructor mohimo sa ultimate mental attack ni Brad.
        public ExistentialCrisis() {
            super("ExistentialCrisis", 90);
        }

        @Override
        // Kini nga function modeal ug kusog nga WIS-based ultimate damage.
        public ActionResult use(Character user, Character target) {
            return damageWithChance(user, target, (user.WIS * 2) - target.DEF, ULTIMATE_HIT_CHANCE);
        }
    }
}
