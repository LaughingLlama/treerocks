package rocks.util;


public class Range {

  public static int getInt( int one, int other ) {
    if( one == other ) {
      return one;
    } else {
      int maximum = one > other ? one : other;
      int minimum = one < other ? one : other;
      return (int) ( Math.random() * ( maximum - minimum + 1 ) ) + minimum;
    }
  }


  private final int one, other;


  public int getInt() {
    return getInt( one, other );
  }

  public Range( int a, int b ) {
    one = a;
    other = b;
  }

  public static Range parse( String s ) {
    String[] parts = s.trim().split( "-" );
    return new Range(
      Integer.parseInt( parts[0] ),
      Integer.parseInt( ( parts.length > 1 ) ? parts[1] : parts[0] )
    );
  }

}
