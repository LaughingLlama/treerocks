package rocks.items;


import rocks.util.Range;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Value<T> {

  private static final Pattern valueSinglePattern =
    Pattern.compile( "(.+) +([0-9]+)" );
  private static final Pattern valueRangePattern =
    Pattern.compile( "(.+) +([0-9]+)-([0-9]+)" );

  private String name;
  private T value;


  public String name() {
    return name;
  }

  public T value() {
    return value;
  }


  public Value( String tagName, T typeValue ) {
    this.name = tagName;
    this.value = typeValue;
  }


  public Value<Number> parse( String s ) throws Exception {

    Matcher singleMatcher = valueSinglePattern.matcher( s );
    if( singleMatcher.matches() ) {
      return new Value<>(
        singleMatcher.group( 1 ),
        Double.parseDouble(
          singleMatcher.group( 2 )
        )
      );
    }

    Matcher rangeMatcher = valueRangePattern.matcher( s );
    if( rangeMatcher.matches() ) {
      return new Value<>(
        singleMatcher.group( 1 ),
        Range.getInt(
          Integer.parseInt( rangeMatcher.group( 2 ) ),
          Integer.parseInt( rangeMatcher.group( 3 ) )
        )
      );
    }

    throw new Exception( "Failed to get number in range" );
  }
}
