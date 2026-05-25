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

| Character | Role          | Strength  | Skill           | Ultimate              |
| --------- | ------------- | --------- | --------------- | --------------------- |
| Careza    | Tuition Payer | Defense   | T Square        | Successful Floor Plan |
| Cyrus     | NAS MDC       | HP        | First Aid Kit   | Syringe Revive        |
| Briar     | NAS CPE       | Attack    | Cellphone       | Transformation        |
| Dirk      | NAS CE        | Intellect | Info Dump       | Hard Hats             |
| Brad      | NAS President | Wisdom    | President Book  | Existential Crisis    |

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



