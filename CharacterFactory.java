package game.logic.characters;

public final class CharacterFactory {
    private CharacterFactory() {
    }

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
