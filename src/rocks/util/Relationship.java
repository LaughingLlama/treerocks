package rocks.util;

import rocks.actions.requirements.TagRequirement;

import java.util.HashMap;
import java.util.Map;


public class Relationship {

  public static final Map<String,TagRequirement.Relationship> map;

  static {
    map = new HashMap<>();
    map.put( "item", TagRequirement.Relationship.self );
    map.put( "child", TagRequirement.Relationship.child );
    map.put( "parent", TagRequirement.Relationship.parent );
  }

}
