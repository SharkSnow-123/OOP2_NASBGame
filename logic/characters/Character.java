package game.logic.characters;

import game.logic.skills.Skill;

public abstract class Character
{
    public static final Skill BasicAttack = new Skill.Attack();

    public final String name;
    public int HP, MP, DEF, ATK, maxHP, maxMP, WIS, INT;
    public static int baseHP = 100, baseMP = 100, baseDEF = 50, baseATK = 50, baseWIS = 50, baseINT = 50;

    public Skill attack, skill1, Ultimate;


    public Character(String name, int HP, int MP, int DEF, int ATK, int WIS, int INT, Skill skill1, Skill Ultimate)
    {
        this.name = name;
        this.HP = HP;
        this.MP = MP;
        this.DEF = DEF;
        this.ATK = ATK;
        this.WIS = WIS;
        this.INT = INT;
        maxHP = HP;
        maxMP = MP;

        this.attack = BasicAttack;
        this.skill1 = skill1;
        this.Ultimate = Ultimate;
    }

    public String getName()
    {
        return name;
    }

    public boolean isDown() {
        return HP <= 0;
    }

    public boolean hasEnoughMp(int amount) {
        return MP >= amount;
    }

    public int takeDamage(int amount) {
        int damage = Math.max(0, amount);
        int before = HP;
        HP = Math.max(0, HP - damage);
        return before - HP;
    }

    public int heal(int amount) {
        int healAmount = Math.max(0, amount);
        int before = HP;
        HP = Math.min(maxHP, HP + healAmount);
        return HP - before;
    }

    public void increaseMaxHp(int amount) {
        maxHP += Math.max(0, amount);
        HP = Math.min(maxHP, HP + Math.max(0, amount));
    }

    public boolean spendMp(int amount) {
        if (!hasEnoughMp(amount)) {
            return false;
        }

        MP -= amount;
        return true;
    }

    public int restoreMp(int amount) {
        int before = MP;
        MP = Math.min(maxMP, MP + Math.max(0, amount));
        return MP - before;
    }

    public static class Careza extends Character
    {
        public Careza() {
            super("Careza", 150, baseMP, 100, baseATK, baseWIS, baseINT, new Skill.TSquare(), new Skill.SuccessfullFloorPlan());
        }

    }

    public static class Cyrus extends Character
    {
        public Cyrus()
        {
            super("Cyrus", 200, baseMP, baseDEF, baseATK, baseWIS, baseINT, new Skill.FirstAidKit(), new Skill.SyringeRevive());
        }
    }

    public static class Briar extends Character
    {
        public Briar()
        {
            super("Briar", baseHP, baseMP, baseDEF, 100, baseWIS, baseINT, new Skill.CellPhone(), new Skill.Transformation());
        }
    }

    public static class Dirk extends Character
    {
        public Dirk()
        {
            super("Dirk", baseHP, baseMP, baseDEF, baseATK, baseWIS, 100, new Skill.InfoDump(), new Skill.HardHats());
        }
    }

    public static class Brad extends Character
    {
        public Brad()
        {
            super("Brad", baseHP, baseMP, baseDEF, baseATK, 100, baseINT, new Skill.PresidentBook(), new Skill.ExistentialCrisis());
        }
    }

    public static class HomeworkMonster extends Character
    {
        public HomeworkMonster()
        {
            super("Homework_Monster", 200, 50, 40, 50, 50, 50, new Skill.CellPhone(), new Skill.InfoDump());
        }
    }

    public static class MidtermMonster extends Character
    {
        public MidtermMonster()
        {
            super("Midterm_Monster", 400,50,40,60,50,50, new Skill.CellPhone(), new Skill.InfoDump());
        }
    }

    public static class Finals_Monster extends Character
    {
        public Finals_Monster()
        {
            super("Finals_Monster", 600, 50, 50,70,60,60, new Skill.CellPhone(), new Skill.InfoDump());
        }
    }

}
