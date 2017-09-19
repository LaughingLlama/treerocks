package rocks.items;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemType {

  final static private Map<String,List<ItemType>> allOfTypeName = new HashMap<>();

  final private String typeName;
  final private Map<String,Object> value;


  public ItemType( String name, List<Value<Object>> valueList ) {
    this.typeName = name;
    this.value = new HashMap<>();
    for( Value each : valueList ) {
      value.put( each.name(), each.value() );
    }
    if( !allOfTypeName.containsKey( typeName ) ) {
      allOfTypeName.put( typeName, new ArrayList<>() );
    }
    allOfTypeName.get( typeName ).add( this );
    System.out.println( "Generated " + this.toString() );
  }


  @Override public String toString() {
    StringBuilder builder = new StringBuilder( typeName + "_" + allOfTypeName.get( typeName ).size() + "\t" );
    for( String key : value.keySet() ) {
      if( value.get( key ) == null ) {
        builder.append( " " ).append( key );
      } else {
        builder.append( " " ).append( key ).append( "=" ).append( value.get( key ) );
      }
    }
    return builder.toString();
  }
}
