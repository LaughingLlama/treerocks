package rocks.actions;

import javafx.event.ActionEvent;
import rocks.PlayerView;
import rocks.actions.requirements.AndRequirement;
import rocks.actions.requirements.NotRequirement;
import rocks.actions.requirements.TagRequirement;
import rocks.items.Item;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Actions {

  public static List<Action> getAvailableActions( Item item ) {
    return definedActions.stream().filter(
      action -> action.requirement().meetsRequirement( item )
    ).collect( Collectors.toList() );
  }


  private static final List<Action> definedActions = Arrays.asList(


    new Action( "pick up",
      new AndRequirement(
        new NotRequirement( new TagRequirement( TagRequirement.Relationship.parent, "hands" ) ),
        new NotRequirement( new TagRequirement( TagRequirement.Relationship.parent, "root_tree_node" ) )
        // todo new ValueRequirement( "size", ValueRequirement.Comparator.LessThan, 3 )
      )
    ) {
      @Override public void handle( ActionEvent event ) {
        if( event.getSource() instanceof Item.MenuItemForItemID ) {
          Item item = Item.getByMenuItem( (Item.MenuItemForItemID) event.getSource() );
          if( item != null ) {
            PlayerView.ground.getChildren().remove( item );
            PlayerView.hands.getChildren().add( item );
            PlayerView.inventoryTreeView.refresh();
          }
        }
      }
    },


    new Action( "drop",
      new TagRequirement( TagRequirement.Relationship.parent, "hands" )
    ) {
      @Override public void handle( ActionEvent event ) {
        if( event.getSource() instanceof Item.MenuItemForItemID ) {
          Item item = Item.getByMenuItem( (Item.MenuItemForItemID) event.getSource() );
          if( item != null ) {
            PlayerView.hands.getChildren().remove( item );
            PlayerView.ground.getChildren().add( item );
            PlayerView.inventoryTreeView.refresh();
          }
        }
      }
    },


    new Action( "search",
      new TagRequirement( "ground" )
    ) {
      @Override public void handle( ActionEvent event ) {
        for( int i = 0; i < Math.random() * 4; ++i ) {
          PlayerView.ground.getChildren().add(
            new Item( "rock" )
          );
        }
      }
    }

  );

}
