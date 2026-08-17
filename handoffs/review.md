# Games Code Review Handoff

Date: 2026-08-16

Source repository: https://github.com/rtayek/games

Reviewed branch: master

Reviewed commit: a610badd39e9619d89268ff6031de5dbba8ca4ea

Review status: Complete. No source changes were made.

## Purpose

This handoff records the actionable findings from a full review of the current Games repository. The review covered every production and test source file, the Gradle build, static-analysis configuration, Eclipse metadata, Git file modes, and the existing project guidance.

The project is a compact Java 25 and Swing prototype containing GardenMatch, MergeMansion, and WordSprout.

## Recommended Work Order

1. Fix the MergeMansion same-cell drag crash.
2. Restore executable permissions on the Unix scripts.
3. Return Gradle output to the standard build directory.
4. Guarantee that GardenMatch boards have at least one legal move.
5. Validate WordSprout answers against the available wheel letters.
6. Decide whether Checkstyle should report warnings or enforce the rules.

## Must Fix

### 1. Same-cell merge deletes the item and crashes

Files:

- src/games/merge/MergeBoard.java, lines 23-41
- src/games/merge/MergeMansionGame.java, lines 27-37
- tst/games/merge/MergeBoardTest.java

A player can select an item and click the same cell again.

MergeBoard.drag treats the source item as a valid merge target because source and target refer to the same ItemNode. It writes the upgraded item into the target cell and then clears the source cell. Because the two positions are equal, the upgraded item is immediately erased.

MergeMansionGame.drag then tries to read the level from the empty target cell and throws NullPointerException. The board has already lost the item.

Required correction:

- Reject a drag when from.equals(to).
- Return false without changing the board, moves, or score.
- Add a regression test covering the game-level method, not only MergeBoard.

Suggested test behavior:

- Place one seed at position (0, 0).
- Drag from (0, 0) to (0, 0).
- Assert false.
- Assert that the original item remains.
- Assert that moves and score remain zero.

### 2. Unix build entry points are not executable

Files:

- gradlew
- test.sh

Both files are committed with Git mode 100644. On Unix and WSL:

    ./gradlew check

fails with:

    ./gradlew: Permission denied

test.sh also calls ./gradlew and therefore cannot work normally.

Required correction:

    chmod +x gradlew test.sh
    git add --chmod=+x gradlew test.sh

Verify that Git records both files with mode 100755.

## Should Fix

### 3. Gradle uses a nonstandard, unignored build directory

Files:

- build.gradle.kts, line 21
- .gitignore, lines 33-38

The build sets:

    layout.buildDirectory.set(file(".gradle-build"))

The repository ignores build/ but not .gradle-build/. Running Gradle therefore creates untracked output.

The project decision is to use Gradle's normal build directory. Remove the build-directory override. The existing build/ ignore rules will then work.

### 4. GardenMatch can produce a board with no legal move

Files:

- src/games/match3/BoardController.java
- tst/games/match3/BoardControllerTest.java, lines 18-25

Board creation and refill prevent immediate three-tile matches, but they never verify that at least one adjacent swap can create a match.

A board can contain no immediate match and still have no legal swap. For example, an 8-by-8 repeating Latin pattern based on (row + column) modulo the five tile types has neither an existing triple nor a legal matching swap.

The test named createsPlayableBoardWithoutImmediateMatches checks only that there is no current match. It does not establish playability.

Required correction:

- Add a side-effect-free hasLegalMove method.
- After initial generation and after cascades settle, regenerate or shuffle until a legal move exists.
- Add deterministic tests for a deadlocked fixture and its recovery.

Keep match detection and legal-move detection in the pure Java domain layer.

### 5. WordSprout levels can contain impossible answers

File:

- src/games/wordconnect/WordLevel.java, lines 15-20 and 48-60

WordLevel normalizes the wheel letters and answers independently. It accepts answers containing letters that are unavailable, or more copies of a letter than the wheel supplies.

That violates the level model and will make a wheel-driven level impossible to complete.

Required correction:

- Treat the wheel as a multiset of letters.
- Validate every normalized answer against that multiset in the constructor or level loader.
- Reject an invalid level before gameplay begins.
- Add tests for an unavailable letter and an excessive repeated letter.

The current typed-text interface masks this defect because it does not yet require submissions to come from LetterWheelController.

## Consider This

### 6. Checkstyle currently reports but does not enforce

Files:

- config/checkstyle/checkstyle.xml
- build.gradle.kts

The Checker severity is warning. Gradle's Checkstyle task allows warnings by default, so isIgnoreFailures = false does not make warning-level violations fail the build.

If Checkstyle is intended as a build gate, set the permitted warning count to zero or promote the desired rules to error severity. If it is intentionally advisory, leave it as-is and document that decision.

## Positive Observations

- Business logic is separated from Swing widgets.
- Random is injected into GardenMatch, making deterministic tests possible.
- Match clearing and cascade scoring are straightforward and readable.
- MergeBoard and WordLevel are small enough to test without UI machinery.
- The source and test layouts follow the project's preferred src/ and tst/ structure.
- The existing tests cover the primary success and rejection paths.

## Validation Performed

- Inspected every production and test Java file on master.
- Inspected Gradle, static-analysis, wrapper, Eclipse, and ignore configuration.
- Cloned commit a610badd39e9619d89268ff6031de5dbba8ca4ea.
- Confirmed with git ls-files --stage that gradlew and test.sh have mode 100644.
- Reproduced the immediate ./gradlew permission failure.
- Confirmed the working tree remained unchanged.

After invoking the wrapper through sh, independent execution of the full Gradle check was blocked because this review environment could not download the Gradle 9.1.0 distribution. No claim is made that the current automated tests pass or fail.

## Completion Criteria

The review findings are resolved when:

- Same-cell dragging is rejected without mutation or exception.
- gradlew and test.sh are executable after a fresh Unix clone.
- Gradle uses build/.
- Every settled GardenMatch board has a legal move.
- Every WordSprout answer is constructible from the wheel letters.
- The intended Checkstyle enforcement behavior is explicit.
- The complete Gradle test and check tasks pass in the normal development environment.
