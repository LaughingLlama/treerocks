package rocks;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;
import rocks.items.Item;
import rocks.items.Items;


public class Main extends Application {

  @Override public void start( final Stage stage ) throws Exception {

    Items.parse();

    TreeItem<String> root = new Item( Item.UNLIMITED, "root_tree_node" );
    root.setExpanded( true );
    root.getChildren().add( PlayerView.world );
    root.getChildren().add( PlayerView.hands );
    PlayerView.world.setExpanded( true );
    PlayerView.hands.setExpanded( true );

    PlayerView.inventoryTreeView.setRoot( root );
    PlayerView.inventoryTreeView.setShowRoot( false );
    PlayerView.inventoryTreeView.setCellFactory( p -> new Item.Cell() );

    BorderPane pane = new BorderPane( PlayerView.inventoryTreeView );

    pane.addEventFilter(
      KeyEvent.KEY_PRESSED,
      t -> {
        switch( t.getCode() ) {
          case ESCAPE:
            stage.hide();
        }
      }
    );

    stage.setScene( new Scene( pane, 240, 240 ) );
    stage.initStyle( StageStyle.TRANSPARENT );
    stage.show();
  }

  public static void main( String[] args ) {
    launch( args );
  }
}
