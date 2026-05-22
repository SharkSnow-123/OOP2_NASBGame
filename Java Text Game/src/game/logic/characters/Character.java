package game.logic.characters;

import game.logic.skills.Skill;

public abstract class Character
{
    public static final Skill BasicAttack = new Skill.Attack();

    public final String name;
    public int HP, MP, DEF, ATK, maxHP, maxMP, WIS, INT;
    public static int baseHP = 100, baseMP = 100, baseDEF = 50, baseATK = 50, baseWIS = 50, baseINT = 50;

    public Skill attack, skill1, Ultimate;


    // Kini nga constructor mag-set sa basic stats ug skills sa usa ka character.
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

    // Kini nga function mokuha sa pangalan sa character.
    public String getName()
    {
        return name;
    }

    // Kini nga function mo-check kung pildi/down na ang character.
    public boolean isDown() {
        return HP <= 0;
    }

    // Kini nga function mo-check kung igo pa ba ang MP para sa skill.
    public boolean hasEnoughMp(int amount) {
        return MP >= amount;
    }

    // Kini nga function mokaltas ug HP kung maigo ang character.
    public int takeDamage(int amount) {
        int damage = Math.max(0, amount);
        int before = HP;
        HP = Math.max(0, HP - damage);
        return before - HP;
    }

    // Kini nga function mopuno ug HP pero dili molapas sa maxHP.
    public int heal(int amount) {
        int healAmount = Math.max(0, amount);
        int before = HP;
        HP = Math.min(maxHP, HP + healAmount);
        return HP - before;
    }

    // Kini nga function mopataas sa maximum HP sa character.
    public void increaseMaxHp(int amount) {
        maxHP += Math.max(0, amount);
        HP = Math.min(maxHP, HP + Math.max(0, amount));
    }

    // Kini nga function mogasto ug MP kung igo ang current MP.
    public boolean spendMp(int amount) {
        if (!hasEnoughMp(amount)) {
            return false;
        }

        MP -= amount;
        return true;
    }

    // Kini nga function mobawi ug MP pero dili molapas sa maxMP.
    public int restoreMp(int amount) {
        int before = MP;
        MP = Math.min(maxMP, MP + Math.max(0, amount));
        return MP - before;
    }

    public static class Careza extends Character
    {
        // Kini nga constructor mohimo kang Careza nga taas ug defense.
        public Careza() {
            super("Careza", 150, baseMP, 100, baseATK, baseWIS, baseINT, new Skill.TSquare(), new Skill.SuccessfullFloorPlan());
        }

    }

    public static class Cyrus extends Character
    {
        // Kini nga constructor mohimo kang Cyrus nga taas ug HP.
        public Cyrus()
        {
            super("Cyrus", 200, baseMP, baseDEF, baseATK, baseWIS, baseINT, new Skill.FirstAidKit(), new Skill.SyringeRevive());
        }
    }

    public static class Briar extends Character
    {
        // Kini nga constructor mohimo kang Briar nga taas ug attack.
        public Briar()
        {
            super("Briar", baseHP, baseMP, baseDEF, 100, baseWIS, baseINT, new Skill.CellPhone(), new Skill.Transformation());
        }
    }

    public static class Dirk extends Character
    {
        // Kini nga constructor mohimo kang Dirk nga taas ug intellect.
        public Dirk()
        {
            super("Dirk", baseHP, baseMP, baseDEF, baseATK, baseWIS, 100, new Skill.InfoDump(), new Skill.HardHats());
        }
    }

    public static class Brad extends Character
    {
        // Kini nga constructor mohimo kang Brad nga taas ug wisdom.
        public Brad()
        {
            super("Brad", baseHP, baseMP, baseDEF, baseATK, 100, baseINT, new Skill.PresidentBook(), new Skill.ExistentialCrisis());
        }
    }

    public static class HomeworkMonster extends Character
    {
        // Kini nga constructor mohimo sa first enemy: Homework Monster.
        public HomeworkMonster()
        {
            super("Homework_Monster", 200, 50, 40, 50, 50, 50, new Skill.CellPhone(), new Skill.InfoDump());
        }
    }

    public static class MidtermMonster extends Character
    {
        // Kini nga constructor mohimo sa second enemy: Midterm Monster.
        public MidtermMonster()
        {
            super("Midterm_Monster", 400,50,40,60,50,50, new Skill.CellPhone(), new Skill.InfoDump());
        }
    }

    public static class Finals_Monster extends Character
    {
        // Kini nga constructor mohimo sa final enemy: Finals Monster.
        public Finals_Monster()
        {
            super("Finals_Monster", 600, 50, 50,70,60,60, new Skill.CellPhone(), new Skill.InfoDump());
        }
    }

}
