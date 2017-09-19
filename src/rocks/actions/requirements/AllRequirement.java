package rocks.actions.requirements;

import rocks.items.Item;

import java.util.Arrays;

public class AllRequirement implements Requirement {
  private Requirement[] allOfRequirements;

  public AllRequirement( Requirement... requirements ) {
    this.allOfRequirements = requirements;
  }

  @Override public boolean meetsRequirement( Item item ) {
    for( Requirement each : allOfRequirements ) {
      if( !each.meetsRequirement( item ) ) {
        return false;
      }
    }
    return true;
  }

  @Override public String toString() {
    return "all " + Arrays.toString( allOfRequirements );
  }
}
