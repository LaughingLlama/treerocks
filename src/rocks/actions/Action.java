package rocks.actions;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import rocks.actions.requirements.Requirement;


public abstract class Action implements EventHandler<ActionEvent> {
  final private String name;
  final private Requirement req;

  public Action( String action, Requirement requirement ) {
    this.name = action;
    this.req = requirement;
  }

  public String name() {
    return name;
  }

  public Requirement requirement() {
    return req;
  }

}
