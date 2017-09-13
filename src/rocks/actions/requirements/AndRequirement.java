package rocks.actions.requirements;

import rocks.items.Item;

public class AndRequirement implements Requirement {
  private Requirement[] allOfRequirements;

  public AndRequirement( Requirement... requirements ) {
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
}
