//package RfC.game;
//
//import RfC.core.World;
//
//public class GameController {
//    private final World world;
//    private final TurnManager turnManager;
//    private final RuleSet ruleSet;
//
//    public GameController(World world, TurnManager turnManager, RuleSet ruleSet) {
//        this.world = world;
//        this.turnManager = turnManager;
//        this.ruleSet = ruleSet;
//    }
//
//    public void runGame() {
//        while (!ruleSet.isGameOver(world)) {
//            turnManager.executeTurn(world);
//        }
//        ruleSet.showResults(world);
//    }
//}
