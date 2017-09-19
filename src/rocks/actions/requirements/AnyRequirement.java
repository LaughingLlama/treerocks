package rocks.actions.requirements;

import rocks.items.Item;

import java.util.Arrays;

public class AnyRequirement implements Requirement {
  private Requirement[] anyOfRequirements;

  public AnyRequirement( Requirement... requirements ) {
    this.anyOfRequirements = requirements;
  }

  @Override public boolean meetsRequirement( Item item ) {
    for( Requirement each : anyOfRequirements ) {
      if( each.meetsRequirement( item ) ) {
        return true;
      }
    }
    return false;
  }

  @Override public String toString() {
    return "any " + Arrays.toString( anyOfRequirements );
  }

}
