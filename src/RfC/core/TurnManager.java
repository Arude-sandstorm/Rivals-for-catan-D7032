//package RfC.core;
//
//import RfC.components.IPlayerIO;
//import RfC.components.RuleSet;
//
//public class TurnManager {
//    private final IPlayerIO io;
//    private final RuleSet rules;
//
//    public TurnManager(IPlayerIO io, RuleSet rules) {
//        this.io = io;
//        this.rules = rules;
//    }
//
//    public void executeTurn(World world) {
//        Entity player = rules.getActivePlayer(world);
//        io.displayBoard(player, world);
//
//        rules.rollDice(world, player);
//        rules.handleEvents(world, player);
//        rules.handleProduction(world, player);
//        rules.handleActionPhase(world, player);
//        rules.handleReplenish(world, player);
//        rules.handleExchange(world, player);
//
//        rules.checkVictory(world, player);
//        rules.advancePlayerTurn(world);
//    }
//}
