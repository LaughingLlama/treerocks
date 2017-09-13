package rocks.actions.requirements;

import rocks.items.Item;

public class OrRequirement implements Requirement {
  private Requirement[] anyOfRequirements;

  public OrRequirement( Requirement... requirements ) {
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
}
