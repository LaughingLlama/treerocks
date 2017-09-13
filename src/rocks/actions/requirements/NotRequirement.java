package rocks.actions.requirements;

import rocks.items.Item;

public class NotRequirement implements Requirement {
  private Requirement notRequirement;

  public NotRequirement( Requirement requirement ) {
    this.notRequirement = requirement;
  }

  @Override public boolean meetsRequirement( Item item ) {
    return !notRequirement.meetsRequirement( item );
  }
}
