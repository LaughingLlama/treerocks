package rocks;

import javafx.scene.control.TreeView;
import rocks.items.Container;


public class PlayerView {

  public static final TreeView<String> inventoryTreeView = new TreeView<>();

  public static final Container ground = new Container( Container.UNLIMITED, "ground" );
  public static final Container hands = new Container( 2, "hands" );

}
