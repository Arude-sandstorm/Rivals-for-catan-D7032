//package RfC.util;
//
//import RfC.components.IPlayerIO;
//import RfC.components.RuleSet;
//import RfC.core.TurnManager;
//import RfC.core.World;
//import RfC.game.GameController;
//
//public class Bootstrap {
//    public static GameController initializeGame() {
//        World world = new World();
//        RuleSet rules = new IntroductoryRuleSet();
//        IPlayerIO io = new ConsoleIO();
//        TurnManager tm = new TurnManager(io, rules);
//        rules.initialize(world);
//        return new GameController(world, tm, rules);
//    }
//}
