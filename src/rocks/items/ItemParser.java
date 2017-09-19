package rocks.items;

import rocks.util.Range;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


public class ItemParser {

  private static final Pattern itemTypeReferencePattern = Pattern.compile( "(.*) +\\((.*)\\)" );


  public static Map<String,List<ItemType>> parseFile( final Path actionSpecFile ) {
    final Map<String,List<ItemType>> itemTypes = new HashMap<>();
    try( Stream<String> stream = Files.lines( actionSpecFile ) ) {
      ArrayList<String> names = new ArrayList<>();
      ArrayList<String> counts = new ArrayList<>();
      ArrayList<String> values = new ArrayList<>();
      stream.forEach( s -> {
        if( !s.trim().isEmpty() ) {
          if( s.trim().charAt( 0 ) == '>' ) {
            counts.add( s.trim().substring( 1 ).trim() );
          } else if( s.trim().charAt( 0 ) == ':' ) {
            values.add( s.trim().substring( 1 ).trim() );
          } else {
            itemTypes.putAll( parseFeed( names, counts, values ) );
            counts.clear();
            values.clear();
            names.clear();
            names.add( s.trim() );
          }
        }
      } );
      itemTypes.putAll( parseFeed( names, counts, values ) );
    } catch( IOException e ) {
      e.printStackTrace();
    }
    return itemTypes;
  }


  private static Map<String,List<ItemType>> parseFeed(
    ArrayList<String> names,
    ArrayList<String> counts,
    ArrayList<String> values
  ) {
    Map<String,List<ItemType>> newItemTypes = new HashMap<>();
    for( String itemTypeName : names ) {
      newItemTypes.put(
        itemTypeName,
        parse(
          itemTypeName,
          counts.toArray( new String[]{ } ),
          values.toArray( new String[]{ } )
        )
      );
    }
    return newItemTypes;
  }

  private static final Pattern percentPattern = Pattern.compile( "[0-9]+%" );

  private static List<ItemType> parse(
    String name,
    String[] countStrings,
    String[] valueStrings
  ) {
    List<ItemType> generatedTypes = new ArrayList<>();
    for( String count : countStrings ) {
      int howMany = Range.parse( count.split( " +" )[0] ).getInt();
      for( int i = 0; i < howMany; ++i ) {
        List<Value<Object>> values = new ArrayList<>();
        for( String each : valueStrings ) {
          boolean doIt = true;
          String[] chunkCheck = each.trim().split( " +", 2 );
          if( chunkCheck.length > 1 && percentPattern.matcher( chunkCheck[0] ).matches() ) {
            double percent = Double.parseDouble(
              chunkCheck[0].trim().substring(
                0, chunkCheck[0].trim().length() - 1
              )
            );
            if( Math.random() > percent / 100.0 ) {
              doIt = false;
            }
            each = chunkCheck[1];
          }

          if( doIt ) {
            if( each.trim().split( "\\|" ).length > 1 ) {
              String[] options = each.trim().split( "\\|" );
              each = options[(int) ( Math.random() * options.length )];
            }
            Matcher typeReference = itemTypeReferencePattern.matcher( each );
            if( typeReference.matches() ) {
              values.add(
                new Value<>(
                  typeReference.group( 1 ).trim(),
                  typeReference.group( 2 ).trim()
                )
              );
            } else {
              String[] parts = each.trim().split( " +", 2 );
              values.add(
                new Value<>(
                  parts[0],
                  parts.length > 1 ?
                    Range.parse( parts[1] ).getInt() :
                    null
                )
              );
            }
          }
        }
        generatedTypes.add(
          new ItemType(
            name,
            values
          )
        );
      }
    }
    return generatedTypes;
  }

}
