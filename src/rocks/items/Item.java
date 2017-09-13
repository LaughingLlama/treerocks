package rocks.items;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import rocks.actions.Action;
import rocks.actions.Actions;

import java.util.*;


public class Item extends TreeItem<String> {
  private static final Map<UUID,Item> allItems = new HashMap<>();

  final private UUID id;
  final private ArrayList<String> tags = new ArrayList<>();

  public Item(
    String... itemTags
  ) {
    super( itemTags[0] == null ? "something" : itemTags[0] );
    this.id = UUID.randomUUID();
    this.tags.addAll( Arrays.asList( itemTags ) );
    allItems.put( id, this );
  }


  public boolean is( String tag ) {
    return tags.contains( tag );
  }

  public boolean isAll( String... all ) {
    for( String tag : all ) {
      if( !tags.contains( tag ) ) {
        return false;
      }
    }
    return true;
  }


  private ContextMenu getMenu() {
    ContextMenu menu = new ContextMenu();
    for( Action action : Actions.getAvailableActions( this ) ) {
      MenuItem menuItem = new MenuItemForItemID( action.name(), id );
      menuItem.setOnAction( action );
      menu.getItems().add( menuItem );
    }
    return menu;
  }

  @Override public String toString() {
    return tags.get( 0 ) + " " + id;
  }

  public static Item getByID( UUID id ) {
    return allItems.get( id );
  }

  public static Item getByMenuItem( MenuItemForItemID source ) {
    return allItems.get( source.itemID );
  }


  public static final class Cell extends TreeCell<String> {
    @Override public void updateItem( String item, boolean empty ) {
      super.updateItem( item, empty );

      if( empty ) {
        setText( null );
        setGraphic( null );
      } else {
        setText( getItem() == null ? "" : getItem() );
        setGraphic( getTreeItem().getGraphic() );
        setContextMenu( ( (Item) getTreeItem() ).getMenu() );
      }
    }
  }


  public static final class MenuItemForItemID extends MenuItem {
    final UUID itemID;

    private MenuItemForItemID( String name, UUID id ) {
      super( name );
      this.itemID = id;
    }
  }

}
