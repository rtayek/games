# Casual Games Suite Project Handoff

**Date:** August 15, 2026

**Project:** Casual Games Suite (Java 25 + JavaFX)

**Target Platforms:** Desktop (Primary initial build), Android Mobile (Future port target)

---

## 1. Project Vision & Core Scope

Building a suite of three popular mobile casual puzzle and word games tailored for broad audiences (specifically designed to replicate the rewarding loops of hit mobile titles like *Wordscapes*, *Gardenscapes*, and item-merge games).

### The Three Core Games in the Package:

1. **WordSprout (Word Connect Puzzle):**
* **Mechanics:** Circular letter selection wheel where players drag/swipe paths across randomized letters to discover and fill dynamic crossword grid puzzles.
* **Visuals / Experience:** Cozy nature-themed aesthetic, smooth pop-in letter/word reveals, and satisfying scaling animations.


2. **GardenMatch (Match-3 & Renovation Meta):**
* **Mechanics:** 8x8 tile matrix with zero-immediate-match initialization, click/swap controllers, and reactive matching logic.
* **Visuals / Experience:** Vibrant floral tile sets and progression loops (earning tokens to build/grow an in-game garden).


3. **MergeMansion: Java Edition (Item Combining Puzzle):**
* **Mechanics:** Inventory grid supporting drag-and-drop merging of identical item levels ($N + N \rightarrow N+1$).
* **Visuals / Experience:** Tiered color scaling and tactile item state progression.



---

## 2. Technical Architecture & Design Blueprint

### Technology Stack

* **Language:** Java 25
* **UI Framework:** JavaFX (utilizing standard layout containers, CSS styling, and hardware-accelerated Prism pipelines).
* **Persistence:** SQLite-ready repository patterns (designed for desktop file paths or Android app database directories).

### Package Layout Specification

```text
games/
|-- core/
|   `-- GameEngine.java           # Shared asset manager, sound/fx hooks, lifecycle timing
|-- wordconnect/
|   |-- WordSproutApp.java        # Main JavaFX launcher
|   |-- LetterWheelController.java # Circular touch/mouse swipe selector
|   `-- CrosswordBoardPane.java   # Dynamic crossword board renderer & validator
|-- match3/
|   |-- GardenMatchApp.java       # Main JavaFX launcher
|   `-- BoardController.java      # Grid state matrix & match-3 logic
`-- merge/
    |-- MergeMansionApp.java      # Main JavaFX launcher
    `-- ItemNode.java             # Level-based inventory & merge logic

```

---

## 3. Android Portability & Future-Proofing Strategy

To ensure a seamless future migration to mobile (Android via Gluon Mobile / GraalVM native images or native Android UI bindings):

* **Strict Domain Separation:** Zero business logic, word validation, or match-3 matrix rules are coupled to JavaFX visual nodes (`Node`, `Scene`, `Stage`). Service and puzzle logic exist as pure Java classes.
* **Unified Gesture Handling:** Input controllers utilize pointer-friendly event structures that map cleanly between desktop mouse dragging and mobile touch events.
* **Resolution Independence:** All game boards and grids rely on responsive JavaFX layout panes (`VBox`, `HBox`, `GridPane`, `StackPane`) rather than hardcoded desktop window coordinates.

---

## 4. Immediate Action Queue for Next Chat

1. **Initialize Module Structure:** Create the `games` package tree and root build configuration.
2. **Complete WordSprout Gameplay Loop:** Integrate `LetterWheelController` and `CrosswordBoardPane` into `WordSproutApp` with a functional level-loading dictionary validator.
3. **Draft GardenMatch Board Interactions:** Expand `BoardController` to handle live tile swapping and match-clearing animations.
