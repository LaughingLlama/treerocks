package rocks.items;


public class Container extends Item {

  public static final int UNLIMITED = -1;

  private int storage; // todo

  public Container(
    int storageSize,
    String... itemTags
  ) {
    super( itemTags );
    this.storage = storageSize;
  }
}
