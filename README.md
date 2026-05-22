# NASB Game

NASB Game is a Java Swing turn-based battle game about the Non Academic School Brawl squad fighting through waves of schoolwork monsters. The game opens with an animated title screen, includes a description page, and then moves into a party-vs-monster battle flow.

## Features

- Java Swing desktop UI
- Animated title and loading screens
- Turn-based combat system
- Five playable NAS characters with unique stats and skills
- Multiple enemy waves: Homework Monster, Midterm Monster, and Finals Monster
- Image assets for characters and enemies

## Characters

| Character | Role | Strength | Skill | Ultimate |
| --- | --- | --- | --- | --- |
| Careza | Tuition Payer | Defense | T Square | Successful Floor Plan |
| Cyrus | NAS MDC | HP | First Aid Kit | Syringe Revive |
| Briar | NAS CPE | Attack | Cellphone | Transformation |
| Dirk | NAS CE | Intellect | Info Dump | Hard Hats |
| Brad | NAS President | Wisdom | President Book | Existential Crisis |

## Project Structure

```text
Java Text Game/
  images/                  Character and enemy images
  src/game/logic/          Core game logic
    characters/            Character classes and factory
    combat/                Turn system and action results
    skills/                Skill definitions
  src/game/ui/             Swing screens and panels
```

The main entry point is:

```text
Java Text Game/src/game/ui/GUI.java
```

## Requirements

- Java Development Kit (JDK) 17 or newer
- IntelliJ IDEA or another Java IDE, optional

## Run in IntelliJ IDEA

1. Open the `Java Text Game` folder as the project.
2. Make sure the `src` folder is marked as a source root.
3. Run `game.ui.GUI`.

## Run in VS Code

1. Open the `Java Text Game` folder in VS Code.
2. Install the Java Extension Pack if VS Code asks for Java support.
3. Open the Run and Debug panel.
4. Choose `Run NASB Game`.

## Run from the Command Line

From the repository root:

```powershell
cd "Java Text Game"
mkdir build\classes
javac -d build\classes (Get-ChildItem -Recurse -Filter *.java -Path src).FullName
Copy-Item -Recurse images build\classes\images
java -cp build\classes game.ui.GUI
```

## Notes

- The game uses local image files from the `images` folder.
- Build output is ignored through `.gitignore`.
- The current project is a desktop app, so it runs in a Java window rather than in the browser.
