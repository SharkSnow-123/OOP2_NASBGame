package game.logic.characters;

public final class CharacterFactory {
    // Private constructor para dili ma-instantiate ang factory class.
    private CharacterFactory() {
    }

    // Kini nga function mopili ug mohimo sa hero base sa iyang pangalan.
    public static Character createHero(String name) {
        return switch (name) {
            case "Careza" -> new Character.Careza();
            case "Cyrus" -> new Character.Cyrus();
            case "Briar" -> new Character.Briar();
            case "Dirk" -> new Character.Dirk();
            case "Brad" -> new Character.Brad();
            default -> throw new IllegalArgumentException("Unknown character: " + name);
        };
    }
}
