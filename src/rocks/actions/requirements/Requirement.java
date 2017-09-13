package rocks.actions.requirements;

import rocks.items.Item;


public interface Requirement {
  boolean meetsRequirement( Item item );
}


