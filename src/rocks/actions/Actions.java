package rocks.actions;

import rocks.items.Item;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


public class Actions {

  public static List<Action> getAvailableActions( Item item ) {
    return definedActions.stream().filter(
      action -> action.requirement().meetsRequirement( item )
    ).collect( Collectors.toList() );
  }

  private static final List<Action> definedActions = ActionParser.parseFile(
    Paths.get( "D:\\IDEA Projects\\Rocks\\res\\spec\\actions.spec" )
  );

}
