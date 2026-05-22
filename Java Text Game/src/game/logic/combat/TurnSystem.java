package game.logic.combat;

import game.logic.characters.Character;
import game.logic.skills.Skill;

import java.util.List;

public class TurnSystem
{
    private List<Character> players;
    private Character monster;
    private int chosen = 0;
    private boolean YourTurn = true;

    int turn = 1;
    Character Active;
    public static Skill BasicAttack = new Skill.Attack();
    private ActionResult lastEnemyResult = ActionResult.effect("");

    public TurnSystem(List<Character> players, Character monster)
    {
        this.players = players;
        this.monster = monster;
        this.Active = players.get(chosen);
    }

    //Main spine ani, checks if it's your turn or not
    public void nextTurn()
    {
        YourTurn = !YourTurn;

        if(YourTurn == true)
        {
            System.out.println("Your turn! Round " + turn);
        }
        else
        {
            System.out.println("Enemy turn! Round " + turn);
            EnemyAction(monster);
            turn++;

            YourTurn = !YourTurn;
        }


    }

    public ActionResult ActionChosen(Character attacker, Character target, Skill skillChosen)
    {
        if(YourTurn == false)
        {
            return ActionResult.failure("It is not your turn!");
        }

        this.Active = attacker;
        ActionResult result = skillChosen.use(attacker, target);

        if(!result.isSuccess() || hasEnded()) return result;

        nextTurn();
        return result;
    }

    public ActionResult EnemyAction(Character monster)
    {
        Skill attack = monster.attack;
        lastEnemyResult = attack.use(monster, Active);

        hasEnded();
        return lastEnemyResult;
    }

    public boolean hasEnded()
    {
        if(monster.HP <= 0)
            return true;

        boolean playersDead = true;
        for(Character player : players)
        {
            if(player.HP > 0)
            {
                playersDead = false;
                break;
            }
        }

        if(playersDead)
        {
            System.out.println("Game Over!");
            if (listener != null) listener.onGameOver();
            return true;
        }

        return false;
    }


    public boolean isYourTurn()
    {
        return YourTurn;
    }

    public ActionResult getLastEnemyResult() {
        return lastEnemyResult;
    }

    public interface GameOverListener {
        void onGameOver();
    }

    private GameOverListener listener;

    public void setGameOverListener(GameOverListener listener) {
        this.listener = listener;
    }

}
