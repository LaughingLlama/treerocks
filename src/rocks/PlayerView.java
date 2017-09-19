package rocks;

import javafx.scene.control.TreeView;
import rocks.items.Item;


public class PlayerView {

  public static final TreeView<String> inventoryTreeView = new TreeView<>();

  public static final Item world = new Item( Item.UNLIMITED, "wilderness" );
  public static final Item hands = new Item( 2, "hands" );

}
