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

    // Kini nga constructor mag-andam sa players, monster, ug unang active character.
    public TurnSystem(List<Character> players, Character monster)
    {
        this.players = players;
        this.monster = monster;
        this.Active = players.get(chosen);
    }

    //Main spine ani, checks if it's your turn or not
    // Kini nga function mobalhin sa turn sa player ug enemy.
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

    // Kini nga function modawat sa skill nga gipili sa player ug ipagamit kini.
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

    // Kini nga function mopahimo sa monster ug basic attack sa active player.
    public ActionResult EnemyAction(Character monster)
    {
        Skill attack = monster.attack;
        lastEnemyResult = attack.use(monster, Active);

        hasEnded();
        return lastEnemyResult;
    }

    // Kini nga function mo-check kung humana ba ang battle.
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


    // Kini nga function mosulti kung turn ba sa player karon.
    public boolean isYourTurn()
    {
        return YourTurn;
    }

    // Kini nga function mokuha sa pinaka-ulahi nga action result sa enemy.
    public ActionResult getLastEnemyResult() {
        return lastEnemyResult;
    }

    public interface GameOverListener {
        void onGameOver();
    }

    private GameOverListener listener;

    // Kini nga function mag-set ug listener nga tawagon kung game over na.
    public void setGameOverListener(GameOverListener listener) {
        this.listener = listener;
    }

}
