package rocks.actions;

import javafx.event.ActionEvent;
import rocks.PlayerView;
import rocks.actions.requirements.AllRequirement;
import rocks.actions.requirements.NotRequirement;
import rocks.actions.requirements.Requirement;
import rocks.actions.requirements.TagRequirement;
import rocks.items.Item;
import rocks.items.ItemType;
import rocks.items.Items;
import rocks.util.Range;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static rocks.util.Relationship.map;


public class ActionParser {

  private static final Pattern percentPattern = Pattern.compile( "[0-9]+%" );
  private static final Pattern numberPattern = Pattern.compile( "[0-9]+x" );
  private static final Pattern rangePattern = Pattern.compile( "[0-9]+-[0-9]+x" );

  private static final Pattern tagRequirementPattern = Pattern.compile( "(.+) +(is not|is) +(.+)" );
  private static final Pattern moveItemStepPattern = Pattern.compile( "[0-9\\-x% ]* *move (.+) from (.+) to (.+)" );
  private static final Pattern createItemStepPattern = Pattern.compile( "[0-9\\-x% ]* *create (.+) in (.+)" );


  public static List<Action> parseFile( final Path actionSpecFile ) {
    final ArrayList<Action> actions = new ArrayList<>();
    try( Stream<String> stream = Files.lines( actionSpecFile ) ) {
      ArrayList<String> actionNames = new ArrayList<>();
      ArrayList<String> requirements = new ArrayList<>();
      ArrayList<String> actionSteps = new ArrayList<>();
      stream.forEach( s -> {
        if( !s.trim().isEmpty() ) {
          if( s.trim().charAt( 0 ) == '?' ) {
            requirements.add( s.trim().substring( 1 ).trim() );
          } else if( s.trim().charAt( 0 ) == ':' ) {
            actionSteps.add( s.trim().substring( 1 ).trim() );
          } else {
            actions.addAll( parseFeed( actionNames, requirements, actionSteps ) );
            requirements.clear();
            actionSteps.clear();
            actionNames.clear();
            actionNames.add( s.trim() );
          }
        }
      } );
      actions.addAll( parseFeed( actionNames, requirements, actionSteps ) );
    } catch( IOException e ) {
      e.printStackTrace();
    }
    return actions;
  }


  private static List<Action> parseFeed(
    ArrayList<String> actionNames,
    ArrayList<String> requirements,
    ArrayList<String> actionSteps
  ) {
    ArrayList<Action> newActions = new ArrayList<>();
    for( String actionName : actionNames ) {
      newActions.add(
        parse(
          actionName,
          requirements.toArray( new String[]{ } ),
          actionSteps.toArray( new String[]{ } )
        )
      );
    }
    return newActions;
  }


  private static Action parse(
    final String name,
    final String[] requirements,
    final String[] steps
  ) {
    Requirement req = new AllRequirement(
      Arrays.stream( requirements ).map( s -> {
          Matcher matcher = tagRequirementPattern.matcher( s );
          if( matcher.matches() ) {
            TagRequirement nestedRequirement = new TagRequirement(
              map.get( matcher.group( 1 ) ),
              matcher.group( 3 )
            );
            if( matcher.group( 2 ).equals( "is not" ) ) {
              return new NotRequirement( nestedRequirement );
            } else {
              return nestedRequirement;
            }
          } else {
            System.out.println( "uh oh Brooklyn" );
            return new AllRequirement();
          }
        }
      ).toArray( Requirement[]::new )
    );

    return new Action( name, req ) {
      @Override public void handle( ActionEvent event ) {
        for( String step : steps ) {
          int howManyTimes = 1;
          String[] split = step.split( " +", 2 );
          while( split.length > 1 && howManyTimes > 0 && (
            percentPattern.matcher( split[0] ).matches() ||
              numberPattern.matcher( split[0] ).matches() ||
              rangePattern.matcher( split[0] ).matches()
          ) ) {
            if( percentPattern.matcher( split[0] ).matches() ) {
              double percent = Double.parseDouble(
                split[0].trim().substring(
                  0, split[0].trim().length() - 1
                )
              );
              for( int i = howManyTimes; i > 0; --i ) {
                if( Math.random() > percent / 100.0 ) {
                  --howManyTimes;
                }
              }
            } else if( numberPattern.matcher( split[0] ).matches() ) {
              int count = Integer.parseInt(
                split[0].trim().substring(
                  0, split[1].trim().length() - 1
                )
              );
              howManyTimes *= count;
            } else if( rangePattern.matcher( split[0] ).matches() ) {
              howManyTimes *= Range.parse(
                split[0].trim().substring(
                  0, split[0].trim().length() - 1
                )
              ).getInt();
            }
            step = split[1];
            split = step.split( " +", 2 );
          }

          for( int i = 0; i < howManyTimes; ++i ) {

            Matcher moveItem = moveItemStepPattern.matcher( step );
            if( moveItem.matches() ) {
              if( event.getSource() instanceof Item.MenuItemForItemID ) {
                Item item = Item.getByMenuItem( (Item.MenuItemForItemID) event.getSource() );
                if( item != null ) {
                  Item from = (Item) ( item.getParent() );
                  switch( moveItem.group( 2 ) ) {
                    case "ground":
                      from = PlayerView.world;
                  }
                  Item to = PlayerView.world;
                  switch( moveItem.group( 3 ) ) {
                    case "hands":
                      to = PlayerView.hands;
                  }
                  from.getChildren().remove( item );
                  to.getChildren().add( item );
                  PlayerView.inventoryTreeView.refresh();
                }
              }
            }

            Matcher createItem = createItemStepPattern.matcher( step );
            if( createItem.matches() ) {
              Item in = PlayerView.world;
              switch( createItem.group( 2 ) ) {
                case "hands":
                  in = PlayerView.hands;
              }
              String itemName = createItem.group( 1 ).trim();
              if( Items.definedItemTypes.containsKey( itemName ) ) {
                in.getChildren().add( new Item(
                  "Candy Mountain Charlie"
                ) );
              } else {
                in.getChildren().add( new Item( createItem.group( 1 ) ) );
              }
            }

          }

        }
      }
    };
  }

}
