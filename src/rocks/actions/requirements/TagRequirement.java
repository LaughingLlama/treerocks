package rocks.actions.requirements;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import rocks.items.Item;


public class TagRequirement implements Requirement {

  public enum Relationship {self, parent, child}

  private Relationship relationship;
  private String[] requiredTags;


  public TagRequirement( String... tags ) {
    this( Relationship.self, tags );
  }

  public TagRequirement( Relationship relation, String... tags ) {
    this.relationship = relation;
    this.requiredTags = tags;
  }

  @Override public boolean meetsRequirement( Item item ) {
    switch( relationship ) {

      case self:
        return item.isAll( requiredTags );

      case parent:
        TreeItem<String> parent = item.getParent();
        return parent instanceof Item && ( (Item) parent ).isAll( requiredTags );

      case child:
        ObservableList<TreeItem<String>> children = item.getChildren();
        for( TreeItem<String> child : children ) {
          if( child instanceof Item && ( (Item) child ).isAll( requiredTags ) ) {
            return true;
          }
        }
        return false;

      default:
        System.out.println( "Unknown Tag Relationship" );
        return false;
    }
  }

  @Override public String toString() {
    return relationship + " is " + String.join( " ", requiredTags );
  }
}
