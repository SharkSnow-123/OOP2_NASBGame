package game.logic.characters;

public final class CharacterInfo {
    // Private constructor para utility class ra ni siya.
    private CharacterInfo() {
    }

    public static final String[] HERO_NAMES = {"Careza", "Cyrus", "Briar", "Dirk", "Brad"};

    // Kini nga function mobalik ug short description sa napili nga character.
    public static String descriptionFor(String characterName) {
        return switch (characterName) {
            case "Careza" ->
                    "Careza (TUITION PAYER) - HIGHEST DEFENSE<br>Skill 1: T SQUARE - Uses a T square for offense<br>ULTIMATE: SUCCESSFUL FLOOR PLAN - Summons a protective barrier";
            case "Cyrus" ->
                    "Cyrus (NAS MDC) - HIGHEST HP<br>Skill 1: FIRST AID KIT - Heals a single ally<br>ULTIMATE: SYRINGE REVIVE - Revives a downed ally";
            case "Briar" ->
                    "Briar (NAS CPE) - HIGHEST ATTACK<br>Skill 1: CELLPHONE - Fires electric byte strikes<br>ULTIMATE: TRANSFORMATION - Boosts armor and power";
            case "Dirk" ->
                    "Dirk (NAS CE) - HIGHEST INTELLECT<br>Skill 1: INFO DUMP - Confuses enemy, dealing INT damage<br>ULTIMATE: HARD HATS - Reduces enemy defence";
            case "Brad" ->
                    "Brad (NAS PRESIDENT) - HIGHEST WISDOM<br>Skill 1: NAS PRESIDENT BOOK - Confuses enemies with knowledge<br>ULTIMATE: EXISTENTIAL CRISIS - Heavy Mental attack";
            default -> "";
        };
    }
}
