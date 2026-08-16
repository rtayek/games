package games.match3;

import java.util.Random;

public final class GardenMatchApp {
    private GardenMatchApp() {
    }

    public static void main(String[] args) {
        GardenMatchGame game = new GardenMatchGame(new Random(7));
        System.out.println(game.board().render());
    }
}
