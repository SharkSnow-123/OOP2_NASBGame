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

    public Skill(String name, int mpCost) {
        this.name = name;
        this.MPcost = mpCost;
    }

    public ActionResult use(Character user, Character target) {
        ActionResult check = canUse(user);
        if (!check.isSuccess()) {
            return check;
        }

        return ActionResult.effect(user.name + " used " + name + ".");
    }

    public int getMpCost() {
        return MPcost;
    }

    protected ActionResult canUse(Character user) {
        if (user.isDown()) {
            return ActionResult.failure(user.name + " is down!");
        }

        if (!user.hasEnoughMp(MPcost)) {
            return ActionResult.failure(user.name + " does not have enough MP for " + name + "!");
        }

        return ActionResult.effect("");
    }

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
        public Attack() {
            super("Attack", 0);
        }

        @Override
        public ActionResult use(Character user, Character target) {
            ActionResult result = damageWithChance(user, target, user.ATK - (target.DEF / 2), BASIC_HIT_CHANCE);
            if (result.isSuccess()) {
                user.restoreMp(BASIC_ATTACK_MP_GAIN);
            }
            return result;
        }
    }

    public static class TSquare extends Skill {
        public TSquare() {
            super("TSquare", 20);
        }

        @Override
        public ActionResult use(Character user, Character target) {
            int damage = user.ATK + (user.DEF / 5) - (target.DEF / 2);
            return damageWithChance(user, target, damage, SKILL_HIT_CHANCE);
        }
    }

    public static class SuccessfullFloorPlan extends Skill {
        public SuccessfullFloorPlan() {
            super("SuccessfullFloorPlan", 90);
        }

        @Override
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
        public FirstAidKit() {
            super("FirstAidKit", 20);
        }

        @Override
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
        public SyringeRevive() {
            super("SyringeRevive", 90);
        }

        @Override
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
        public CellPhone() {
            super("CellPhone", 20);
        }

        @Override
        public ActionResult use(Character user, Character target) {
            return damageWithChance(user, target, (user.ATK / 2) + 20, SKILL_HIT_CHANCE);
        }
    }

    public static class Transformation extends Skill {
        public Transformation() {
            super("Transformation", 90);
        }

        @Override
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
        public InfoDump() {
            super("InfoDump", 50);
        }

        @Override
        public ActionResult use(Character user, Character target) {
            return damageWithChance(user, target, (user.INT + 20) - (target.DEF / 2), SKILL_HIT_CHANCE);
        }
    }

    public static class HardHats extends Skill {
        public HardHats() {
            super("HardHats", 90);
        }

        @Override
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
        public PresidentBook() {
            super("PresidentBook", 20);
        }

        @Override
        public ActionResult use(Character user, Character target) {
            return damageWithChance(user, target, user.WIS - (target.DEF / 2), SKILL_HIT_CHANCE);
        }
    }

    public static class ExistentialCrisis extends Skill {
        public ExistentialCrisis() {
            super("ExistentialCrisis", 90);
        }

        @Override
        public ActionResult use(Character user, Character target) {
            return damageWithChance(user, target, (user.WIS * 2) - target.DEF, ULTIMATE_HIT_CHANCE);
        }
    }
}
