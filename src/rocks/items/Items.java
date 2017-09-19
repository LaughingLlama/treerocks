package rocks.items;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Items {

  public static final Map<String,List<ItemType>> definedItemTypes = new HashMap<>();

  public static void parse() {
    ItemParser.parseFile(
      Paths.get( "D:\\IDEA Projects\\Rocks\\res\\spec\\items.spec" )
    );
  }

}
