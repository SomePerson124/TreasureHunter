import java.util.Scanner;

/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all the display based on the messages it receives from the Town object. <p>
 *
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class TreasureHunter {
    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean samuraiMode;
    private boolean hardMode;
    private boolean easyMode;


    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter() {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        samuraiMode = false;
        hardMode = false;
        easyMode = false;
    }

    /**
     * Starts the game; this is the only public method
     */
    public void play() {
        welcomePlayer();
        enterTown();
        showMenu();
        endScenario();
        loseScenario();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer() {
        System.out.println("Welcome to TREASURE HUNTER! Lol.edm");
        System.out.println("Going hunting for the big treasure, eh?");
        System.out.print("What's your name, Hunter? ");
        String name = SCANNER.nextLine().toLowerCase();

        System.out.println("Choose a Difficulty:");
        System.out.println("(E)asy Mode");
        System.out.println("(N)ormal Mode");
        System.out.println("(H)ard Mode");
        String hard = SCANNER.nextLine().toLowerCase();
        if (hard.equals("h")) {
            hardMode = true;
        }
        if (hard.equals("s")) {
            samuraiMode = true;
        }
        // set hunter instance variable
        hunter = new Hunter(name, 10, samuraiMode);
        if (hard.equals("test")) {
            hunter.buyItem("water", 2);
            hunter.buyItem("rope", 2);
            hunter.buyItem("machete", 2);
            hunter.buyItem("horse", 1);
            hunter.buyItem("boat", 1);
            hunter.buyItem("boot", 1);
            hunter.buyItem("shovel", 1);
            hunter.changeGold(100);
        }
        if (hard.equals("e")) {
            easyMode = true;
            hunter.changeGold(10);
        }
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown() {
        double markdown = 0.5;
        double toughness = 0.4;
        if (hardMode) {
            // in hard mode, you get less money back when you sell items
            markdown = 0.25;

            // and the town is "tougher"
            toughness = 0.75;
        } else if (easyMode) {
            //in easy mode, you get full money back after selling items
            markdown = 1;

            //and the town is less "tough"
            toughness = 0.25;
        }


        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu() {
        String choice = "";
        while (!choice.equals("x") && hunter.getGold() >= 0 && !hunter.hasAllTreasure()) {
            System.out.println();
            System.out.println(currentTown.getLatestNews());
            System.out.println("***");
            System.out.println(hunter);
            System.out.println(currentTown);
            System.out.println("(B)uy something at the shop.");
            System.out.println("(S)ell something at the shop.");
            System.out.println("(M)ove on to a different town.");
            System.out.println("(L)ook for trouble!");
            System.out.println("(D)ig for gold!");
            System.out.println("(H)unt for treasure!");
            System.out.println("Give up the hunt and e(X)it.");
            System.out.println();
            System.out.print("What's your next move? ");
            choice = SCANNER.nextLine().toLowerCase();
            processChoice(choice);
        }
    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice) {
        if (choice.equals("b") || choice.equals("s")) {
            currentTown.enterShop(choice);
        } else if (choice.equals("m")) {
            if (currentTown.leaveTown()) {
                // This town is going away so print its news ahead of time.
                System.out.println(currentTown.getLatestNews());
                enterTown();
            }
        } else if (choice.equals("l")) {
            currentTown.lookForTrouble();
        } else if (choice.equals("h")) {
            currentTown.searchForTreasure();
        } else if (choice.equals("d")) {
            currentTown.digGold();
        } else if (choice.equals("x")) {
            System.out.println("Fare thee well, " + hunter.getHunterName() + "!");
        } else {
            System.out.println("Yikes! That's an invalid option! Try again.");
        }

    }

    public String getDifficulty() {
        if (easyMode) {
            return "easy";
        } else if (hardMode) {
            return "hard";
        } else {
            return "normal";
        }
    }

    private void endScenario() {
        if (hunter.hasAllTreasure()) {
            System.out.println();
            System.out.println("You found " + hunter.getLastTreasure() + "!");
            System.out.println("***");
            System.out.println("Congratulations, you have found the last of the three treasures, you win!");
            System.out.println("***");
        }
    }

    private void loseScenario() {
        if (hunter.getGold() < 0) {
            System.out.println();
            System.out.println(currentTown.getLatestNews());
            System.out.println("***");
            System.out.println(Colors.RED + "You couldnt pay up! Game Over" + Colors.RESET);
            System.out.println("***");
        }
    }
}