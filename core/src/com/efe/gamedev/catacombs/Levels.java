package com.efe.gamedev.catacombs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.efe.gamedev.catacombs.entities.Boss;
import com.efe.gamedev.catacombs.entities.Catacomb;
import com.efe.gamedev.catacombs.entities.Chest;
import com.efe.gamedev.catacombs.entities.Door;
import com.efe.gamedev.catacombs.entities.Electricity;
import com.efe.gamedev.catacombs.entities.Exit;
import com.efe.gamedev.catacombs.entities.Guard;
import com.efe.gamedev.catacombs.entities.Item;
import com.efe.gamedev.catacombs.entities.Lever;
import com.efe.gamedev.catacombs.entities.Prisoner;
import com.efe.gamedev.catacombs.entities.Puzzle;
import com.efe.gamedev.catacombs.entities.Sign;
import com.efe.gamedev.catacombs.entities.SpeechBubble;
import com.efe.gamedev.catacombs.entities.Word;
import com.efe.gamedev.catacombs.entities.levelVerdict;
import com.efe.gamedev.catacombs.items.Diamond;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 12/28/2017.
 * This class keeps track of all the levels in the game.
 * It may look complicated, but all this is doing is checking what level it is and initializing all classes for the level and adding the new instances of the classes to their own arrays.
 */

public class Levels {

    public int currentLevel;
    public int furthestLevel;
    private Array<String> victoryPhrases;

    Levels() {
        //the levels are stored in an array, so the first level is level 0, second level is level 1, ect...
        currentLevel = 0;
        //phrases that are shown when you beat a level
        victoryPhrases = new Array<>();
        victoryPhrases.add("Beginner's Luck!");
        victoryPhrases.add("Nice Jumps!");
        victoryPhrases.add("Amazing Job!");
        victoryPhrases.add("You are almost ready!");
        victoryPhrases.add("Difficulty Increasing...");
        victoryPhrases.add("10 hours until Lock-Down!");
        victoryPhrases.add("9 hours until Lock-Down!");
        victoryPhrases.add("8 hours until Lock-Down!");
        victoryPhrases.add("7 hours until Lock-Down!");
        victoryPhrases.add("6 hours until Lock-Down!");
        victoryPhrases.add("5 hours until Lock-Down!");
        victoryPhrases.add("4 hours until Lock-Down!");
        victoryPhrases.add("3 hours until Lock-Down!");
        victoryPhrases.add("Dun, dun, dun.....");
        victoryPhrases.add("Yay! You finally escaped!");
        //15 levels created so far
    }

    //setup stuff for each level
    public void configureLevel(final Level configuredLevel) {
        //set furthest level
        furthestLevel = configuredLevel.gameplayScreen.game.getFurthestLevel();
        if (currentLevel >= furthestLevel) {
            furthestLevel = currentLevel;
        }
        //re-initializing stuff that is the same for each level
        configuredLevel.viewportPosition = new Vector2();
        configuredLevel.touchPosition = new Vector2();
        configuredLevel.lookPosition = new Vector2();
        configuredLevel.pressDown = false;
        configuredLevel.pressUp = false;
        configuredLevel.alarm = false;
        configuredLevel.shake = false;
        configuredLevel.usedDoor = false;

        configuredLevel.getPlayer().hasGold = false;
        configuredLevel.getPlayer().hasWeapon = false;
        configuredLevel.getPlayer().armRotate = 0;
        configuredLevel.getPlayer().fighting = false;
        configuredLevel.getPlayer().mouthState = Enums.MouthState.NORMAL;
        configuredLevel.getPlayer().danger = false;
        configuredLevel.getPlayer().health = 20;
        configuredLevel.getPlayer().useEnergy = false;
        configuredLevel.getPlayer().energy = 20;
        configuredLevel.getPlayer().recoverE = false;

        configuredLevel.victory = false;
        configuredLevel.defeat = false;
        configuredLevel.verdict = new levelVerdict(configuredLevel);

        configuredLevel.targetBox.target = false;
        configuredLevel.continueBubbles = true;
        configuredLevel.show = true;
        configuredLevel.targetBox.isArrow = false;
        configuredLevel.levelStarted = false;
        configuredLevel.inventory.paused = false;
        configuredLevel.inventory.newItem = false;
        configuredLevel.inventory.inventoryItems = new DelayedRemovalArray<>();
        configuredLevel.inventory.selectedItem = -1;
        configuredLevel.inventory.scoreDiamonds = new DelayedRemovalArray<>();
        configuredLevel.inventory.speechButton.speech = true;
        //initialize torch timer
        configuredLevel.torchLight = 10f;
        configuredLevel.startTime = TimeUtils.nanoTime();
        configuredLevel.torchFade = false;
        configuredLevel.torchUp = false;
        //reset player variables
        configuredLevel.getPlayer().getVelocity().set(new Vector2());
        configuredLevel.getPlayer().legRotate = 170;
        configuredLevel.getPlayer().armRotate = 0;
        configuredLevel.getPlayer().startTime = TimeUtils.nanoTime();
        configuredLevel.getPlayer().viewportPosition = new Vector2();
        configuredLevel.getPlayer().alert = false;
        configuredLevel.getPlayer().duck = false;
        configuredLevel.getPlayer().invisibility = false;
        configuredLevel.getPlayer().ghost = false;
        configuredLevel.getPlayer().shock = false;
        configuredLevel.getPlayer().heldItem.potion.full = true;
        configuredLevel.getPlayer().heldItem.itemType = "";
        configuredLevel.getPlayer().drinkPotion = false;
        configuredLevel.getPlayer().disguise = false;

        switch (currentLevel) {
            case 0:
                configuredLevel.touchLocked = true;
                /*LEVEL CONFIGURATIONS:*/
                //player always starts inside the currentCatacomb
                configuredLevel.catacombs = new Array<>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -100), "Closed", "Closed", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -15), "Locked", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 1;

                configuredLevel.items = new DelayedRemovalArray<>();
                configuredLevel.items.add(new Item(new Vector2(60, 10), configuredLevel.viewportPosition, "key"));

                configuredLevel.puzzles = new Array<>();

                configuredLevel.words = new Array<>();

                configuredLevel.chests = new Array<>();

                configuredLevel.guards = new Array<>();

                configuredLevel.levers = new Array<>();

                configuredLevel.doors = new Array<>();

                configuredLevel.signs = new Array<>();

                configuredLevel.wires = new Array<>();

                configuredLevel.prisoners = new Array<>();

                configuredLevel.bosses = new Array<>();

                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<>();
                //A SpeechBubble consists of these parameters: (mainText, targetText, numberOfOptions, option1, option2, option3, option4, function1, function2, function3, function4, level);
                configuredLevel.speechBubbles.add(new SpeechBubble("Where am I?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Some kind of tomb?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hey, there is something by my ear!", "Player:", 1, "Turn earpiece on", "", "", "",
                        () -> configuredLevel.currentBubble = 3,
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("BEEP! Greetings, my name is c7-x", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 22));
                configuredLevel.speechBubbles.add(new SpeechBubble("My mission is to help people in need", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 22));
                configuredLevel.speechBubbles.add(new SpeechBubble("Are you in need?", "c7-x:", 3, "Nope", "Well, yes", "Can you say that again?", "",
                        () -> {
                            configuredLevel.currentBubble = 6;
                            configuredLevel.continueBubbles = false;
                        },
                        () -> configuredLevel.currentBubble = 7,
                        () -> {
                            configuredLevel.currentBubble = 5;
                            configuredLevel.speechBubbles.get(configuredLevel.currentBubble).speechBubbleAlpha = 0;
                            configuredLevel.speechBubbles.get(configuredLevel.currentBubble).option1Alpha = 0;
                            configuredLevel.speechBubbles.get(configuredLevel.currentBubble).option2Alpha = 0;
                            configuredLevel.speechBubbles.get(configuredLevel.currentBubble).option3Alpha = 0;
                        },
                        () -> {}, configuredLevel, 22));
                configuredLevel.speechBubbles.add(new SpeechBubble("OK, shutting down", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.defeat = true;
                                configuredLevel.verdict.verdict = false;
                                configuredLevel.verdict.verdictPhrase = "You needed c7-x to help you!";
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Alright, what is your condition?", "c7-x:", 4, "I am Alive", "Good, but confused", "Not so good", "Horrible",
                        () -> configuredLevel.currentBubble = 8,
                        () -> configuredLevel.currentBubble = 9,
                        () -> configuredLevel.currentBubble = 9,
                        () -> configuredLevel.currentBubble = 13, configuredLevel, 22));
                configuredLevel.speechBubbles.add(new SpeechBubble("I can see that", "c7-x:", 1, "Try again", "", "", "",
                        () -> {
                                configuredLevel.currentBubble = 7;
                                configuredLevel.speechBubbles.get(configuredLevel.currentBubble).speechBubbleAlpha = 0;
                                configuredLevel.speechBubbles.get(configuredLevel.currentBubble).option1Alpha = 0;
                                configuredLevel.speechBubbles.get(configuredLevel.currentBubble).option2Alpha = 0;
                                configuredLevel.speechBubbles.get(configuredLevel.currentBubble).option3Alpha = 0;
                                configuredLevel.speechBubbles.get(configuredLevel.currentBubble).option4Alpha = 0;
                            },
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 22));
                configuredLevel.speechBubbles.add(new SpeechBubble("BEEP! Scanning your surroundings...", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 22));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hmm, It appears you are\n45.3 miles underground", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 22));
                configuredLevel.speechBubbles.add(new SpeechBubble("WHAT! How did I get here?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I don't know the answer to that question", "c7-x:", 4, "Ask for advice", "Troubleshoot", "Turn c7-x off", "Ask a question",
                        () -> configuredLevel.currentBubble = 22,
                        () -> configuredLevel.currentBubble = 15,
                        () -> {
                                configuredLevel.currentBubble = 6;
                                configuredLevel.continueBubbles = false;
                            },
                        () -> configuredLevel.currentBubble = 19, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I sense that you are not in any\nphysical pain", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("How can the status you give\nme be 'horrible'?", "c7-x:", 3, "It's an expression", "I was kidding", "Well, how else would you put it?", "",
                        () -> configuredLevel.currentBubble = 42,
                        () -> configuredLevel.currentBubble = 42,
                        () -> configuredLevel.currentBubble = 43,
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Can you teleport me out of here\nor something?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("No", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("How about clearing me\na path to the surface?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("That assignment is beyond my capabilities", "c7-x:", 3, "Ask for advice", "Look around", "Turn c7-x off", "",
                        () -> configuredLevel.currentBubble = 22,
                        () -> configuredLevel.currentBubble = 21,
                        () -> {
                                configuredLevel.currentBubble = 6;
                                configuredLevel.continueBubbles = false;
                            },
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Is your name really c7-x?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("My full name is c7-x345Qy78G224hb-5Y,\n But you can call me c7-x", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("This place is really creepy!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("What should I do, c7-x?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hmm, let me look at your surroundings...", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Calculating...", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("It appears that there is a key\nto your left. It may help you", "c7-x:", 1, "Pick up key", "", "", "",
                        () -> {
                                configuredLevel.touchLocked = false;
                                if (configuredLevel.items.size != 0) {
                                    configuredLevel.items.get(0).targeted = true;
                                }
                                configuredLevel.show = false;
                            },
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.touchLocked = true;
                                configuredLevel.currentBubble = 26;
                                configuredLevel.show = true;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Great Job!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now, to use the key you need\nto tap on it", "c7-x:", 1, "Tap on key", "", "", "",
                        () -> {
                                configuredLevel.touchLocked = false;
                                if (configuredLevel.inventory.inventoryItems.size != 0) {
                                    configuredLevel.inventory.inventoryItems.get(0).targeted = true;
                                }
                                configuredLevel.show = false;
                            },
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.touchLocked = true;
                                if (configuredLevel.inventory.inventoryItems.size != 0) {
                                    configuredLevel.inventory.inventoryItems.get(0).targeted = false;
                                }
                                configuredLevel.currentBubble = 28;
                                configuredLevel.show = true;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("At this pace you might actually survive!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 34));
                configuredLevel.speechBubbles.add(new SpeechBubble("Thanks?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 34));
                configuredLevel.speechBubbles.add(new SpeechBubble("By the way, how did YOU\nget this far underground?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 34));
                configuredLevel.speechBubbles.add(new SpeechBubble("BEEP! That information is classified", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 34));
                configuredLevel.speechBubbles.add(new SpeechBubble("What! By whom!?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 34));
                configuredLevel.speechBubbles.add(new SpeechBubble("Do you want me to\nhelp you or not?", "c7-x:", 2, "Yes", "No", "", "",
                        () -> configuredLevel.currentBubble = 34,
                        () -> {
                                configuredLevel.currentBubble = 6;
                                configuredLevel.continueBubbles = false;
                            },
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Next, you will need to unlock\nthat door in front of you", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("With what?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("The key!", "c7-x:", 1, "Unlock Door", "", "", "",
                        () -> {
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.position.set(new Vector2(41.5f, 10.5f));
                                configuredLevel.show = false;
                                configuredLevel.touchLocked = false;
                            },
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.touchLocked = true;
                                if (configuredLevel.player.jumpState == Enums.JumpState.GROUNDED) {
                                    configuredLevel.show = true;
                                    configuredLevel.currentBubble = 37;
                                }
                                configuredLevel.targetBox.target = false;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Good work!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 40));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now, what do you want me\nto do?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 40));
                configuredLevel.speechBubbles.add(new SpeechBubble("I think you can handle this\non your own!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 40));
                configuredLevel.speechBubbles.add(new SpeechBubble("You can move around by\ntapping the screen", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("All you need to do is to\nescape through this exit door!", "c7-x:", 1, "Escape the Level", "", "", "",
                        () -> {
                                configuredLevel.currentBubble = 44;
                                configuredLevel.touchLocked = false;
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                            },
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("In that case, we should\nstop wasting our time", "c7-x:", 3, "Ask for advice", "Look around", "Turn c7-x off", "",
                        () -> configuredLevel.currentBubble = 22,
                        () -> configuredLevel.currentBubble = 21,
                        () -> {
                                configuredLevel.currentBubble = 6;
                                configuredLevel.continueBubbles = false;
                            },
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I would call your\nsituation: 'A predicament'", "c7-x:", 3, "Ask for advice", "Look around", "Turn c7-x off", "",
                        () -> configuredLevel.currentBubble = 22,
                        () -> configuredLevel.currentBubble = 21,
                        () -> {
                                configuredLevel.currentBubble = 6;
                                configuredLevel.continueBubbles = false;
                            },
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                            configuredLevel.viewportPosition.set(new Vector2(configuredLevel.getPlayer().getPosition()));
                            configuredLevel.show = true;
                            configuredLevel.touchLocked = true;
                            configuredLevel.continueBubbles = true;
                            configuredLevel.currentBubble = 45;
                        }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("The pad that you just stepped\non is called the Exit Pad.", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("The Exit Pad unlocks the Exit Door", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now...       \nOnward!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                            configuredLevel.touchLocked = false;
                            configuredLevel.show = false;
                            configuredLevel.continueBubbles = false;
                            }, configuredLevel, -1));
                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(100, 150));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(-50, -82), new Vector2(-100, -84), configuredLevel);

                break;
            case 1:
                configuredLevel.touchLocked = true;

                configuredLevel.catacombs = new Array<>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(200, -385), "Closed", "Unlocked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(50, -300), "Closed", "Unlocked", "Closed", "Closed", "Unlocked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-100, -215), "Closed", "Locked", "Closed", "Closed", "Unlocked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-250, -130), "Closed", "Locked", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-400, -45), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 0;

                configuredLevel.items = new DelayedRemovalArray<>();
                configuredLevel.items.add(new Item(new Vector2(-190, -105), configuredLevel.viewportPosition, "key"));

                configuredLevel.puzzles = new Array<>();

                configuredLevel.words = new Array<>();

                configuredLevel.chests = new Array<>();
                configuredLevel.chests.add(new Chest(new Vector2(-40, -197), "", configuredLevel, () -> configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"))));

                configuredLevel.guards = new Array<>();

                configuredLevel.levers = new Array<>();

                configuredLevel.doors = new Array<>();

                configuredLevel.signs = new Array<>();

                configuredLevel.wires = new Array<>();

                configuredLevel.prisoners = new Array<>();

                configuredLevel.bosses = new Array<>();

                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<>();
                configuredLevel.speechBubbles.add(new SpeechBubble("You can pause the game by tapping the\n pause button in the bottom left corner", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("If you ever get stuck, you\ncan reset the level there!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("Well, I still don't know my location!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("Don't worry,\nI've searched my databases and...", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("I have found that each of these hexagons\nare called a Catacomb!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("We are in the Catacombs! An underground\ncemetery full of tombs and tunnels!", "c7-x:", 4, "Lovely", "How are you so optimistic?", "Get me out of here!", "Great",
                        () -> configuredLevel.currentBubble = 6,
                        () -> configuredLevel.currentBubble = 7,
                        () -> configuredLevel.currentBubble = 13,
                        () -> configuredLevel.currentBubble = 6, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("I wouldn't go that far, but\nit is pretty nice down here!", "c7-x:", 2, "Interrogate c7-x", "Ask for help", "", "",
                        () -> configuredLevel.currentBubble = 8,
                        () -> configuredLevel.currentBubble = 13,
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("It's in my programming!", "c7-x:", 2, "Tell me more...", "Help me escape!", "", "",
                        () -> configuredLevel.currentBubble = 8,
                        () -> configuredLevel.currentBubble = 13,
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("How am I the only human down here?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("I'm sorry, but I cannot answer that.", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("So you do know!?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("I know how you can escape", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("Fine.\nTell me.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("First, come up to the\nleft side of this Catacomb", "c7-x:", 1, "Okay", "", "", "",
                        () -> {
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                                configuredLevel.touchLocked = false;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.isArrow = true;
                                configuredLevel.targetBox.left = true;
                                configuredLevel.targetBox.position.set(new Vector2(250, -350));
                            },
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.currentBubble = 14;
                                configuredLevel.show = true;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.targetBox.target = false;
                                configuredLevel.touchLocked = true;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("That's the easy part...", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now you need to jump\nto the next Catacomb!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("To jump, you need to tap once in the very\ntop corner that you want to jump to", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Right now, you need to jump\nto the top left corner!", "c7-x:", 1, "Jump time!", "", "", "",
                        () -> {
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                                configuredLevel.touchLocked = false;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.isArrow = false;
                                configuredLevel.targetBox.itemWidth = 5;
                                configuredLevel.targetBox.position.set(new Vector2(170, -260));
                            },
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.currentBubble = 18;
                                configuredLevel.touchLocked = true;
                                configuredLevel.show = true;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.targetBox.target = false;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Pro skills!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I would jump for you,\nbut I don't have legs!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("It looks like there is another\nCatacomb on your left to be jumped", "c7-x:", 1, "On it!", "", "", "",
                        () -> {
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                                configuredLevel.touchLocked = false;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.isArrow = true;
                                configuredLevel.targetBox.left = true;
                                configuredLevel.targetBox.position.set(new Vector2(50, -170));
                            },
                        () -> {},
                        () -> {},
                        () -> {
                                if (configuredLevel.viewportPosition.x < 40) {
                                    configuredLevel.viewportPosition.x = 40;
                                }
                                configuredLevel.touchLocked = true;
                                configuredLevel.show = true;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.targetBox.target = false;
                                configuredLevel.currentBubble = 21;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Wow, is that a treasure chest!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 23));
                configuredLevel.speechBubbles.add(new SpeechBubble("We are in a tomb, so it might be!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 23));
                configuredLevel.speechBubbles.add(new SpeechBubble("Some chests will need items\nto be unlocked...", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("But this chest already is! Tap on it!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                if (!configuredLevel.speechBubbles.get(configuredLevel.currentBubble).textLoaded) {
                                    configuredLevel.touchPosition.set(new Vector2());
                                }
                                configuredLevel.continueBubbles = false;
                                if (!configuredLevel.chests.get(0).opened) {
                                    configuredLevel.touchLocked = false;
                                }
                                if (configuredLevel.touchPosition.x > configuredLevel.chests.get(0).position.x && configuredLevel.touchPosition.x < configuredLevel.chests.get(0).position.x + 40 && configuredLevel.touchPosition.y > configuredLevel.chests.get(0).position.y && configuredLevel.touchPosition.y < configuredLevel.chests.get(0).position.y + 26 && configuredLevel.speechBubbles.get(configuredLevel.currentBubble).textLoaded) {
                                    configuredLevel.show = false;
                                    configuredLevel.touchPosition.set(new Vector2(configuredLevel.chests.get(0).position.x + 2, configuredLevel.chests.get(0).position.y + 2));
                                }
                                if (configuredLevel.chests.get(0).opened) {
                                    configuredLevel.show = true;
                                    configuredLevel.touchLocked = true;
                                    configuredLevel.currentBubble = 25;
                                }
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("It gave me a key!", "Player:", configuredLevel.targetBox.target ? 0 : 1, "Proceed", "", "", "",
                        () -> {
                                configuredLevel.viewportPosition.set(-41, 0);
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.isArrow = true;
                                configuredLevel.show = false;
                                configuredLevel.targetBox.position.set(new Vector2(-60, -160));
                            },
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.touchLocked = true;
                                configuredLevel.show = true;
                                configuredLevel.targetBox.target = false;
                                configuredLevel.currentBubble = 26;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Some doors are located higher up!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.getPlayer().mouthState = Enums.MouthState.NORMAL;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.isArrow = false;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.targetBox.position.set(new Vector2(-97.5f, -104.5f));
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("To unlock them, all you need is a key", "c7-x:", configuredLevel.inventory.selectedItem == -1 ? 1 : 0, "Grab key", "", "", "",
                        () -> {
                                configuredLevel.touchLocked = false;
                                configuredLevel.show = false;
                                if (configuredLevel.inventory.selectedItem == -1 && configuredLevel.inventory.inventoryItems.size >= 1) {
                                    configuredLevel.inventory.inventoryItems.get(0).targeted = true;
                                }
                            },
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = true;
                                configuredLevel.touchLocked = true;
                                configuredLevel.inventory.inventoryItems.get(0).targeted = false;
                                configuredLevel.currentBubble = 28;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Try unlocking the door\n in the upper left corner!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                                if (configuredLevel.catacombs.get(2).getLockedDoors().get(1).equals("Unlocked")) {
                                    configuredLevel.targetBox.isArrow = true;
                                    configuredLevel.touchLocked = true;
                                    configuredLevel.targetBox.position.set(new Vector2(-100, -100));
                                    configuredLevel.currentBubble = 30;
                                    configuredLevel.show = true;
                                } else {
                                    configuredLevel.touchLocked = false;
                                }
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Not bad, now jump up to the\nCatacomb in the top left corner!", "c7-x:", 1, "Okay", "", "", "",
                        () -> configuredLevel.currentBubble = 31,
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = false;
                                if (configuredLevel.viewportPosition.x < -110) {
                                    configuredLevel.viewportPosition.x = -110;
                                }
                                configuredLevel.touchLocked = !configuredLevel.speechBubbles.get(configuredLevel.currentBubble).textLoaded;
                                if (configuredLevel.currentCatacomb == 3) {
                                    configuredLevel.touchLocked = true;
                                    configuredLevel.show = true;
                                    configuredLevel.targetBox.target = false;
                                    configuredLevel.continueBubbles = true;
                                    configuredLevel.currentBubble = 32;
                                }
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Exhilarating!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Remember, to escape the level...", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("You must first step on the Exit Pad\nthen go to the Exit Door!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hey, I see a key to my left!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Walk over to the key to grab it", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                if (configuredLevel.items.size >= 1) {
                                    configuredLevel.items.get(0).targeted = true;
                                    configuredLevel.touchLocked = false;
                                    configuredLevel.continueBubbles = false;
                                } else {
                                    configuredLevel.touchLocked = true;
                                    configuredLevel.currentBubble = 37;
                                }
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now use it to unlock the door\nin the top left corner!", "c7-x:", 1, "Unlock door", "", "", "",
                        () -> {
                                configuredLevel.touchLocked = false;
                                configuredLevel.show = false;
                                if (configuredLevel.inventory.selectedItem != -1) {
                                    configuredLevel.inventory.inventoryItems.get(0).targeted = false;
                                }
                                if (configuredLevel.inventory.selectedItem == -1 && configuredLevel.inventory.inventoryItems.size >= 1) {
                                    configuredLevel.inventory.inventoryItems.get(0).targeted = true;
                                }
                                if (configuredLevel.catacombs.get(3).getLockedDoors().get(1).equals("Locked")) {
                                    configuredLevel.targetBox.target = true;
                                    configuredLevel.targetBox.isArrow = false;
                                    configuredLevel.continueBubbles = false;
                                    configuredLevel.targetBox.position.set(new Vector2(-97.5f - 150, -104.5f + 85));
                                }
                            },
                        () -> {},
                        () -> {},
                        () -> {
                                if (configuredLevel.catacombs.get(3).getLockedDoors().get(1).equals("Unlocked")) {
                                    configuredLevel.targetBox.isArrow = true;
                                    configuredLevel.targetBox.position.set(new Vector2(-100 - 150, -100 + 85));
                                }
                                if (configuredLevel.currentCatacomb == 4) {
                                    configuredLevel.targetBox.target = false;
                                    configuredLevel.show = true;
                                    configuredLevel.currentBubble = 38;
                                }
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now, escape the level through\nthat Exit Door!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(300, -250));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(-350, -27), new Vector2(-170, -114), configuredLevel);
                break;
            case 2:
                configuredLevel.touchLocked = true;

                configuredLevel.catacombs = new Array<>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -185), "Closed", "Closed", "Closed", "Locked", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -15), "Closed", "Closed", "Closed", "Closed", "Unlocked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(150, -270), "Closed", "Locked", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(300, -185), "Locked", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(150, -100), "Locked", "Unlocked", "Closed", "Unlocked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(300, -15), "Unlocked", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(450, -100), "Closed", "Locked", "Closed", "Closed", "DoubleLocked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(600, -185), "Closed", "DoubleLocked", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(750, -100), "Locked", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 1;

                configuredLevel.items = new DelayedRemovalArray<>();
                configuredLevel.items.add(new Item(new Vector2(60, 10), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(60, -160), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(80, -160), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(370, -160), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(400, -160), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(430, -160), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(220, -245), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(730, -160), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(250, -70), configuredLevel.viewportPosition, "diamond"));
                configuredLevel.items.add(new Item(new Vector2(400, 15), configuredLevel.viewportPosition, "diamond"));
                configuredLevel.items.add(new Item(new Vector2(550, -70), configuredLevel.viewportPosition, "diamond"));

                configuredLevel.puzzles = new Array<>();

                configuredLevel.words = new Array<>();

                configuredLevel.chests = new Array<>();

                configuredLevel.guards = new Array<>();

                configuredLevel.levers = new Array<>();

                configuredLevel.doors = new Array<>();

                configuredLevel.signs = new Array<>();

                configuredLevel.wires = new Array<>();

                configuredLevel.prisoners = new Array<>();

                configuredLevel.bosses = new Array<>();

                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<>();
                configuredLevel.speechBubbles.add(new SpeechBubble("In the pause menu there\nare several useful buttons!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("One of them lets you see a\nmap of the level you are in", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Using what you have learned,\ngo and navigate the catacombs!", "c7-x:", 1, "Try it out!", "", "", "",
                        () -> {
                                configuredLevel.touchLocked = false;
                                configuredLevel.continueBubbles = false;
                                configuredLevel.show = false;
                            },
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.currentBubble = 3;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.position.set(new Vector2(191.5f, -74.5f));
                                configuredLevel.show = true;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("There are diamonds in some catacombs,\nwhich are very valuable!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.touchLocked = false;
                                configuredLevel.show = false;
                                configuredLevel.targetBox.target = false;
                                configuredLevel.currentBubble = 4;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Lets test your jumping skills!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = true;
                                configuredLevel.touchLocked = true;
                                configuredLevel.inventory.selectedItem = 0;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.position.set(new Vector2(341.5f, -159.5f));
                                configuredLevel.viewportPosition.set(new Vector2(290, 0)); //285
                                configuredLevel.continueBubbles = true;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Try unlocking the door\n in the upper right corner!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.touchLocked = false, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                                if (configuredLevel.catacombs.get(2).getLockedDoors().get(3).equals("Unlocked")) {
                                    configuredLevel.targetBox.isArrow = true;
                                    configuredLevel.targetBox.left = false;
                                    configuredLevel.targetBox.position.set(new Vector2(340, -150));
                                    configuredLevel.currentBubble = 7;
                                    configuredLevel.show = true;
                                }
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Superb! Now tap once in the very\ntop right corner to jump up there!", "c7-x:", 1, "Got it", "", "", "",
                        () -> configuredLevel.show = false,
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = true;
                                configuredLevel.targetBox.target = false;
                                configuredLevel.currentBubble = 8;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.touchLocked = true;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Genius!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Not as genius\nas I am, of course!", "c7-x:", 3, "You are a robot!", "Oh, really?", "No way!", "",
                        () -> configuredLevel.currentBubble = 14,
                        () -> configuredLevel.currentBubble = 10,
                        () -> configuredLevel.currentBubble = 10,
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Well then, what is.....\n9 cubed!", "c7-x:", 4, "28", "The cube of 9", "821", "729",
                        () -> configuredLevel.currentBubble = 13,
                        () -> configuredLevel.currentBubble = 11,
                        () -> configuredLevel.currentBubble = 13,
                        () -> configuredLevel.currentBubble = 12, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble(".........................................", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Correct!", "c7-x:", 1, "Continue the catacombs", "", "", "",
                        () -> {
                                configuredLevel.show = false;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.left = true;
                                configuredLevel.targetBox.position.set(new Vector2(170, -150));
                                configuredLevel.continueBubbles = false;
                                configuredLevel.touchLocked = false;
                            },
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.touchLocked = true;
                                configuredLevel.show = true;
                                configuredLevel.targetBox.target = false;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.currentBubble = 15;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Incorrect!", "c7-x:", 1, "Continue the catacombs", "", "", "",
                        () -> {
                                configuredLevel.show = false;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.left = true;
                                configuredLevel.targetBox.position.set(new Vector2(170, -150));
                                configuredLevel.continueBubbles = false;
                                configuredLevel.touchLocked = false;
                            },
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.touchLocked = true;
                                configuredLevel.show = true;
                                configuredLevel.targetBox.target = false;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.currentBubble = 15;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I am a robot, a small one!", "c7-x:", 1, "Continue the catacombs", "", "", "",
                        () -> {
                                configuredLevel.show = false;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.left = true;
                                configuredLevel.targetBox.position.set(new Vector2(170, -150));
                                configuredLevel.continueBubbles = false;
                                configuredLevel.touchLocked = false;
                            },
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.touchLocked = true;
                                configuredLevel.show = true;
                                configuredLevel.targetBox.target = false;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.currentBubble = 15;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("You can lock doors with\nkeys as well as unlock them", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("In order to jump to the next\ncatacomb...", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("You must lock the door underneath\nthe catacomb you want to jump to", "c7-x:", 1, "Lock door and jump out", "", "", "",
                        () -> {
                                configuredLevel.touchLocked = false;
                                configuredLevel.show = false;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.left = false;
                                configuredLevel.targetBox.isArrow = false;
                                configuredLevel.targetBox.position.set(new Vector2(151.5f + (60 * 0.6f), -99.5f));
                                configuredLevel.continueBubbles = false;
                            },
                        () -> {},
                        () -> {},
                        () -> {
                                if (!configuredLevel.touchLocked) {
                                    configuredLevel.targetBox.isArrow = true;
                                    configuredLevel.targetBox.position.set(new Vector2(190, -70));
                                    configuredLevel.currentBubble = 18;
                                }
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Great job!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = true;
                                configuredLevel.targetBox.target = false;
                                configuredLevel.continueBubbles = true;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now, find the exit and\nget out of this place!", "c7-x:", 1, "Okay!", "", "", "",
                        () -> {
                                configuredLevel.show = false;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.isArrow = true;
                                configuredLevel.targetBox.position.set(new Vector2(340, 20));
                                configuredLevel.continueBubbles = false;
                                configuredLevel.currentBubble = 20;
                            },
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("After you step on the exit pad\nyou still need to get to the door!", "c7-x:", 1, "Got it!", "", "", "",
                        () -> {
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                                configuredLevel.currentBubble = 1;
                            },
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = true;
                                configuredLevel.targetBox.position.set(new Vector2(750, -65));
                            }, configuredLevel, -1));
                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(100, 150));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(850, -82), new Vector2(680, -169), configuredLevel);
                break;
            case 3:
                configuredLevel.touchLocked = false;
                configuredLevel.show = false;
                configuredLevel.continueBubbles = false;

                configuredLevel.catacombs = new Array<>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(150, -100), "Closed", "Locked", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -15), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(300, -15), "Locked", "Closed", "Closed", "Closed", "DoubleLocked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(300, -185), "Closed", "Locked", "Closed", "Locked", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(450, -100), "Locked", "DoubleLocked", "Closed", "DoubleLocked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(450, -270), "Closed", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(600, -15), "DoubleLocked", "Unlocked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(600, -185), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(450, 70), "Closed", "Closed", "Closed", "Closed", "Unlocked", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 1;

                configuredLevel.items = new DelayedRemovalArray<>();
                configuredLevel.items.add(new Item(new Vector2(130, 10), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(530, -70), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(390, 10), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(370, -155), configuredLevel.viewportPosition, "diamond"));
                configuredLevel.items.add(new Item(new Vector2(720, 30), configuredLevel.viewportPosition, "diamond"));
                configuredLevel.items.add(new Item(new Vector2(510, 100), configuredLevel.viewportPosition, "diamond"));

                configuredLevel.puzzles = new Array<>();
                configuredLevel.puzzles.add(new Puzzle(new Vector2(660, 50), new Vector2(500, 130), "shapes3", new Enums.Shape[]{Enums.Shape.SQUARE, Enums.Shape.CIRCLE, Enums.Shape.TRIANGLE}, () -> {
                    configuredLevel.catacombs.get(6).getLockedDoors().set(5, "Unlocked");
                    configuredLevel.catacombs.get(7).getLockedDoors().set(2, "Unlocked");
                }, configuredLevel));

                configuredLevel.words = new Array<>();
                configuredLevel.words.add(new Word(new String[]{"K", "E", "Y"}, new Vector2[]{new Vector2(370, 50), new Vector2(395, 50), new Vector2(420, 50)}, configuredLevel));
                configuredLevel.words.add(new Word(new String[]{"K", "E", "Y"}, new Vector2[]{new Vector2(550, -35), new Vector2(420, -120), new Vector2(370, -120)}, configuredLevel));

                configuredLevel.chests = new Array<>();
                configuredLevel.chests.add(new Chest(new Vector2(250, -82), "", configuredLevel, () -> configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"))));
                configuredLevel.chests.add(new Chest(new Vector2(550, -252), "", configuredLevel, () -> {
                    configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                    configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                    configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                }));

                configuredLevel.guards = new Array<>();

                configuredLevel.levers = new Array<>();

                configuredLevel.doors = new Array<>();

                configuredLevel.signs = new Array<>();

                configuredLevel.wires = new Array<>();

                configuredLevel.prisoners = new Array<>();

                configuredLevel.bosses = new Array<>();

                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<>();
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = true;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.currentBubble = 1;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("c7x, How long until I escape this place?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("By my calculations.............\nI have no clue.", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("But, you are a robot!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.continueBubbles = false;
                                if (configuredLevel.currentCatacomb == 2) {
                                    configuredLevel.currentBubble = 4;
                                }
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("It looks like there will\nbe some puzzles along the way!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                if (configuredLevel.currentCatacomb == 4) {
                                    configuredLevel.show = false;
                                }
                                if (configuredLevel.currentCatacomb == 6 && configuredLevel.catacombs.get(6).getLockedDoors().get(0).equals("Unlocked")) {
                                    configuredLevel.catacombs.get(6).getLockedDoors().set(0, "Locked");
                                }
                            }, configuredLevel, -1));
                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(100, 150));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(710, -167), new Vector2(550, -84), configuredLevel);
                break;
            case 4:
                configuredLevel.touchLocked = false;
                configuredLevel.show = false;
                configuredLevel.continueBubbles = false;

                configuredLevel.catacombs = new Array<>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-400, -300), "Closed", "Closed", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-250, -215), "Locked", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-100, -300), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(50, -385), "Locked", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-100, -470), "Closed", "Closed", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 1;

                configuredLevel.items = new DelayedRemovalArray<>();
                configuredLevel.items.add(new Item(new Vector2(-285, -270), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(-330, -269), configuredLevel.viewportPosition, "dagger"));
                configuredLevel.items.add(new Item(new Vector2(-265, -270), configuredLevel.viewportPosition, "gold"));
                configuredLevel.items.add(new Item(new Vector2(-135, -185), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(-30, -270), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(10, -440), configuredLevel.viewportPosition, "diamond"));

                configuredLevel.puzzles = new Array<>();

                configuredLevel.words = new Array<>();
                configuredLevel.words.add(new Word(new String[]{"G", "O", "L", "D"}, new Vector2[]{new Vector2(15, -200), new Vector2(-15, -230), new Vector2(10, -250), new Vector2(-45, -230)}, configuredLevel));

                configuredLevel.chests = new Array<>();
                configuredLevel.chests.add(new Chest(new Vector2(140, -367), "gold", configuredLevel, () -> {
                    configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                    configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0)));
                    configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0)));
                    configuredLevel.inventory.deleteCurrentItem();
                }));

                configuredLevel.guards = new Array<>();
                configuredLevel.guards.add(new Guard(new Vector2(-150, -149), configuredLevel));

                configuredLevel.levers = new Array<>();

                configuredLevel.doors = new Array<>();

                configuredLevel.signs = new Array<>();

                configuredLevel.wires = new Array<>();

                configuredLevel.prisoners = new Array<>();

                configuredLevel.bosses = new Array<>();

                configuredLevel.speechBubbles = new Array<>();
                configuredLevel.speechBubbles.add(new SpeechBubble("Halt stranger!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = true;
                                configuredLevel.catacombs.get(1).getLockedDoors().set(0, "Locked");
                                configuredLevel.catacombs.get(1).getLockedDoors().set(4, "Locked");
                                configuredLevel.continueBubbles = true;
                                configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("You are not authorized to\nenter this area!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.getPlayer().mouthState = Enums.MouthState.NORMAL, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Well...", "Player:", 4, "Talk with Guard", "Run Away", "Bribe Guard", "Attack Guard",
                        () -> configuredLevel.currentBubble = 3,
                        () -> configuredLevel.currentBubble = 56,
                        () -> configuredLevel.currentBubble = 50,
                        () -> configuredLevel.currentBubble = 64, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("What are you doing here?", "Guard 1:", 4, "None of your business", "Escaping!", "I am lost", "What are YOU doing here?",
                        () -> configuredLevel.currentBubble = 27,
                        () -> configuredLevel.currentBubble = 34,
                        () -> configuredLevel.currentBubble = 4,
                        () -> configuredLevel.currentBubble = 28, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Really!?", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("Yep, I am not sure how\nI came here.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("...                  \nYou don't know where you are?", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("Well... Not really.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("How could you have traveled\nhere without knowing where you are?!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("This is the Catacombs, the most secure\nunderground prison in the world!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("Prison!!      \nDo they keep criminals here?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("All the worst criminals in the\nworld are kept down here!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("It is strange that you don't\nknow how you traveled here.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 13));
                configuredLevel.speechBubbles.add(new SpeechBubble("It is nearly impossible to venture\ndown here without being a...", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Prisoner!!!", "Guard 1:", 3, "I am not a criminal!", "Who, me?", "What!!!", "",
                        () -> configuredLevel.currentBubble = 15,
                        () -> configuredLevel.currentBubble = 15,
                        () -> configuredLevel.currentBubble = 15,
                        () -> configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Nice try, but you\ncan't fool me anymore!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I am alerting security!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = false;
                                configuredLevel.player.mouthState = Enums.MouthState.OPEN;
                                configuredLevel.guards.get(0).guardItem = "phone";
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("CODE RED! We have a\nprisoner on the loose!", "Security:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = true;
                                configuredLevel.alarm = true;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Run, player, RUN!!!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Where?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Unlock the door on your right!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                                configuredLevel.touchLocked = false;
                                configuredLevel.guards.get(0).alert = false;
                                configuredLevel.guards.get(0).chasePlayer = true;
                                configuredLevel.guards.get(0).talkToPlayer = false;
                                if (configuredLevel.guards.get(0).guardTouchesPlayer() && configuredLevel.guards.get(0).guardState == Enums.GuardState.ATTACK) {
                                    configuredLevel.currentBubble = 23;
                                    configuredLevel.show = true;
                                    configuredLevel.touchLocked = true;
                                }
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Got you!", "Guard 1:", 1, "Oh no!", "", "", "",
                        () -> {
                                configuredLevel.defeat = true;
                                configuredLevel.verdict.verdict = false;
                                configuredLevel.verdict.verdictPhrase = "You were captured by a guard!";
                            },
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hey! Where are you going!!!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.continueBubbles = true;
                                configuredLevel.alarm = false;
                                configuredLevel.show = true;
                                configuredLevel.touchLocked = false;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("That was a close one!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.catacombs.get(2).getLockedDoors().set(1, "Closed"), configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("None of my business!?", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 32));
                configuredLevel.speechBubbles.add(new SpeechBubble("I guard this prison for a living!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 32));
                configuredLevel.speechBubbles.add(new SpeechBubble("Prison!!      \nDo they keep criminals here?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 32));
                configuredLevel.speechBubbles.add(new SpeechBubble("This is the Catacombs, the most secure\nunderground prison in the world!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 32));
                configuredLevel.speechBubbles.add(new SpeechBubble("All the worst criminals in the\nworld are kept down here!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 32));
                configuredLevel.speechBubbles.add(new SpeechBubble("It is nearly impossible to venture\ndown here without being a...", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Prisoner!!!", "Guard 1:", 3, "Huh?", "I am not a criminal!", "But...", "",
                        () -> configuredLevel.currentBubble = 15,
                        () -> configuredLevel.currentBubble = 15,
                        () -> configuredLevel.currentBubble = 15,
                        () -> configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("...            \nYou're escaping?", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 42));
                configuredLevel.speechBubbles.add(new SpeechBubble("Uh... yeah.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 42));
                configuredLevel.speechBubbles.add(new SpeechBubble("I really want to\nget out of this place!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 42));
                configuredLevel.speechBubbles.add(new SpeechBubble("You have the audacity\n to leave this prison?!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 42));
                configuredLevel.speechBubbles.add(new SpeechBubble("Wait, this is a prison?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 42));
                configuredLevel.speechBubbles.add(new SpeechBubble("This is the Catacombs, the most secure\nunderground prison in the world!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 42));
                configuredLevel.speechBubbles.add(new SpeechBubble("All the worst criminals in the\nworld are kept down here.", "Guard 1:", 2, "But I haven't done anything wrong!", "I'm not a criminal!", "", "",
                        () -> configuredLevel.currentBubble = 41,
                        () -> configuredLevel.currentBubble = 41,
                        () -> {},
                        () -> {}, configuredLevel, 42));
                configuredLevel.speechBubbles.add(new SpeechBubble("All the prisoners say that!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 42));
                configuredLevel.speechBubbles.add(new SpeechBubble("Enough talking,\nI am alerting security!", "Guard 1:", 1, "Wait...", "", "", "",
                        () -> configuredLevel.currentBubble = 43,
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Will you let me go\nif I give you my gold?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hmm, let me see it.", "Guard 1:", 2, "Give guard gold", "Don't give guard gold", "", "",
                        () -> configuredLevel.currentBubble = 47,
                        () -> configuredLevel.currentBubble = 45,
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("How dare you!?", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.currentBubble = 17, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("...", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.inventory.selectedItem = -1;
                                for (int i = 0; i < configuredLevel.inventory.inventoryItems.size; i++) {
                                    if (configuredLevel.inventory.inventoryItems.get(i).itemType.equals("gold")) {
                                        configuredLevel.inventory.inventoryItems.removeIndex(i);
                                    }
                                }
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Actually, I've changed my mind.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.currentBubble = 17, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("What are you doing down here?!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 37));
                configuredLevel.speechBubbles.add(new SpeechBubble("Uh... Leaving?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 37));
                configuredLevel.speechBubbles.add(new SpeechBubble("...            \nYou're escaping?", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 37));
                configuredLevel.speechBubbles.add(new SpeechBubble("No, I just really want\nto get out of this place!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 37));
                configuredLevel.speechBubbles.add(new SpeechBubble("That's the same thing...", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 37));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.currentBubble = 37, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Sorry, can't talk now!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("huh?            \nWho are you?", "Guard 1:", 4, "None of your business", "Nobody", "Who are YOU?", "I don't know",
                        () -> configuredLevel.currentBubble = 27,
                        () -> configuredLevel.currentBubble = 60,
                        () -> configuredLevel.currentBubble = 58,
                        () -> configuredLevel.currentBubble = 60, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Well, I am a security guard...", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.currentBubble = 28, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("You must be somebody!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I would talk,\nbut I have to leave...", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("How could you have traveled\nhere without knowing who you are?!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.currentBubble = 9, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("What are you doing down here!?", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 71));
                configuredLevel.speechBubbles.add(new SpeechBubble("I am escaping and there is\nnothing you can do to stop me.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 71));
                configuredLevel.speechBubbles.add(new SpeechBubble("You have the audacity\n to leave this prison?!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 71));
                configuredLevel.speechBubbles.add(new SpeechBubble("Wait, this is a prison?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 71));
                configuredLevel.speechBubbles.add(new SpeechBubble("This is the Catacombs, the most secure\nunderground prison in the world!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 71));
                configuredLevel.speechBubbles.add(new SpeechBubble("All the worst criminals in the\nworld are kept down here.", "Guard 1:", 2, "But I haven't done anything wrong!", "I'm not a criminal!", "", "",
                        () -> configuredLevel.currentBubble = 70,
                        () -> configuredLevel.currentBubble = 70,
                        () -> {},
                        () -> {}, configuredLevel, 71));
                configuredLevel.speechBubbles.add(new SpeechBubble("All the prisoners say that!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Enough talking,\nI am alerting security!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = false;
                                configuredLevel.player.mouthState = Enums.MouthState.OPEN;
                                configuredLevel.guards.get(0).guardItem = "phone";
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("CODE RED! We have a\nprisoner on the loose!", "Security:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = true;
                                configuredLevel.alarm = true;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I won't let myself be captured!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                for (int i = 0; i < configuredLevel.inventory.inventoryItems.size; i++) {
                                    if (configuredLevel.inventory.inventoryItems.get(i).itemType.equals("dagger")) {
                                        configuredLevel.inventory.selectedItem = i;
                                    }
                                }
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Oh, really?", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.guards.get(0).guardItem = "dagger", configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Tap on the guard to attack him!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.continueBubbles = false;
                                configuredLevel.guards.get(0).guardItem = "dagger";
                                if (configuredLevel.guards.get(0).position.x >= configuredLevel.getPlayer().getPosition().x - 30 && configuredLevel.guards.get(0).position.x <= configuredLevel.getPlayer().getPosition().x + 30) {
                                    configuredLevel.guards.get(0).guardState = Enums.GuardState.FIGHT;
                                    configuredLevel.getPlayer().fighting = true;
                                } else {
                                    configuredLevel.guards.get(0).guardState = Enums.GuardState.ATTACK;
                                }
                                if (configuredLevel.guards.get(0).guardEnergy > 20) {
                                    configuredLevel.player.mouthState = Enums.MouthState.OPEN;
                                } else {
                                    configuredLevel.player.mouthState = Enums.MouthState.NORMAL;
                                }
                                if (configuredLevel.guards.get(0).guardEnergy >= 30) {
                                    if (configuredLevel.player.armRotate < 50) {
                                        configuredLevel.player.armRotate += 4f;
                                    } else {
                                        configuredLevel.show = true;
                                        configuredLevel.currentBubble = 77;
                                    }
                                }
                                //player lose
                                if (configuredLevel.guards.get(0).guardEnergy >= 30) {
                                    if (configuredLevel.player.armRotate < 60) {
                                        configuredLevel.player.armRotate += 4f;
                                    } else {
                                        configuredLevel.show = true;
                                        configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN;
                                        configuredLevel.currentBubble = 77;
                                    }
                                    configuredLevel.alarm = false;
                                }
                                //player win
                                if (configuredLevel.guards.get(0).guardEnergy <= 1) {
                                    if (configuredLevel.guards.get(0).armRotate < 60) {
                                        configuredLevel.guards.get(0).armRotate += 4f;
                                    } else {
                                        configuredLevel.show = true;
                                        configuredLevel.continueBubbles = true;
                                        configuredLevel.alarm = false;
                                        configuredLevel.currentBubble = 78;
                                    }
                                }
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Got you!", "Guard 1:", 1, "Oh no!", "", "", "",
                        () -> {
                                configuredLevel.defeat = true;
                                configuredLevel.verdict.verdict = false;
                                configuredLevel.verdict.verdictPhrase = "You were captured by a guard!";
                            },
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.getPlayer().heldItem.itemType = "";
                                configuredLevel.inventory.selectedItem = -1;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("No!!!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Don't mess with me again.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("That was a close one!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = false;
                                configuredLevel.touchLocked = false;
                            }, configuredLevel, -1));

                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(-300, -135));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(-50, -452), new Vector2(0, -284), configuredLevel);
                break;
            case 5:
                configuredLevel.touchLocked = false;
                configuredLevel.show = false;
                configuredLevel.continueBubbles = false;

                configuredLevel.catacombs = new Array<>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-600, 100), "Closed", "Closed", "Closed", "Closed", "Unlocked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-450, 15), "Closed", "Unlocked", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-200, -70), "Locked", "Locked", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-50, 15), "Locked", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-350, -155), "Unlocked", "Closed", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-600, -240), "Locked", "Closed", "Closed", "Unlocked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-750, -325), "Closed", "Closed", "Closed", "Locked", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-600, -410), "Closed", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 0;

                configuredLevel.items = new DelayedRemovalArray<>();
                configuredLevel.items.add(new Item(new Vector2(-535, 130), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(-135, -40), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(-690, -295), configuredLevel.viewportPosition, "diamond"));

                configuredLevel.puzzles = new Array<>();
                configuredLevel.puzzles.add(new Puzzle(new Vector2(50, 100), new Vector2(1000, 1000), "shapes2", new Enums.Shape[]{Enums.Shape.TRIANGLE, Enums.Shape.CIRCLE}, () -> {
                    configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "gold"));
                    configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                }, configuredLevel));

                configuredLevel.words = new Array<>();
                configuredLevel.words.add(new Word(new String[]{"E", "X", "I", "T"}, new Vector2[]{new Vector2(-490, -300), new Vector2(-370, -280), new Vector2(-400, -370), new Vector2(-360, -360)}, configuredLevel));

                configuredLevel.chests = new Array<>();
                configuredLevel.chests.add(new Chest(new Vector2(-110, -52), "key", configuredLevel, () -> {
                    configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                    configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0)));
                    configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0)));
                    configuredLevel.inventory.deleteCurrentItem();
                }));
                configuredLevel.chests.add(new Chest(new Vector2(-280, -137), "gold", configuredLevel, () -> {
                    configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                    configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                    configuredLevel.inventory.deleteCurrentItem();
                }));

                configuredLevel.guards = new Array<>();
                configuredLevel.guards.add(new Guard(new Vector2(-429, -184), configuredLevel));

                configuredLevel.levers = new Array<>();

                configuredLevel.doors = new Array<>();

                configuredLevel.signs = new Array<>();

                configuredLevel.wires = new Array<>();

                configuredLevel.prisoners = new Array<>();

                configuredLevel.bosses = new Array<>();

                configuredLevel.speechBubbles = new Array<>();
                configuredLevel.speechBubbles.add(new SpeechBubble("There are spikes on the ceiling!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = true;
                                configuredLevel.getPlayer().viewportPosition.set(new Vector2(configuredLevel.catacombs.get(1).position.x + (200 / 3.5f), configuredLevel.getPlayer().getPosition().y));
                                configuredLevel.touchLocked = true;
                                if (configuredLevel.inventory.inventoryItems.size != 0) {
                                    configuredLevel.catacombs.get(1).getLockedDoors().set(1, "Locked");
                                }
                                configuredLevel.continueBubbles = true;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Oh no! Those are Stalactites!!!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("They are going to fall!\nGet out of the way!!!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN;
                                configuredLevel.shake = true;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                if (configuredLevel.getPlayer().health <= 0) {
                                    configuredLevel.defeat = true;
                                    configuredLevel.verdict.verdict = false;
                                    configuredLevel.touchLocked = true;
                                    configuredLevel.verdict.verdictPhrase = "Try to avoid the Stalactites!";
                                }
                                configuredLevel.catacombs.get(1).drop = true;
                                configuredLevel.getPlayer().danger = true;
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                                configuredLevel.touchLocked = false;
                                if (configuredLevel.currentCatacomb == 2 && configuredLevel.getPlayer().jumpState == Enums.JumpState.GROUNDED) {
                                    configuredLevel.shake = false;
                                    configuredLevel.catacombs.get(1).drop = false;
                                    configuredLevel.getPlayer().danger = false;
                                    configuredLevel.catacombs.get(1).getLockedDoors().set(4, "Locked");
                                    configuredLevel.catacombs.get(2).getLockedDoors().set(1, "Locked");
                                    configuredLevel.currentBubble = 4;
                                }
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("We made it!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = true;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.touchLocked = true;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                                configuredLevel.touchLocked = false;
                                if (configuredLevel.currentCatacomb == 5 && configuredLevel.getPlayer().jumpState == Enums.JumpState.GROUNDED) {
                                    configuredLevel.viewportPosition.set(new Vector2(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 220, configuredLevel.getPlayer().getPosition().y));
                                    if (configuredLevel.getPlayer().getPosition().x < configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 222) {
                                        configuredLevel.currentBubble = 6;
                                    }
                                }
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I see a Guard!", "PLayer:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = true;
                                configuredLevel.getPlayer().alert = true;
                                configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.touchLocked = true;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = false;
                                configuredLevel.viewportPosition.set(new Vector2(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 240, configuredLevel.getPlayer().getPosition().y));
                                configuredLevel.touchPosition.set(new Vector2(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 220, configuredLevel.getPlayer().getPosition().y));
                                configuredLevel.getPlayer().mouthState = Enums.MouthState.NORMAL;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("......", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = true;
                                configuredLevel.guards.get(0).guardItem = "phone";
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Guard 24A-G678, do you read me?", "Security:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 24));
                configuredLevel.speechBubbles.add(new SpeechBubble("Affirmative, sir.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 24));
                configuredLevel.speechBubbles.add(new SpeechBubble("An escaped prisoner has been\nrecently spotted in your vicinity.", "Security:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 24));
                configuredLevel.speechBubbles.add(new SpeechBubble("We are initiating the\nCatacombs lock-down sequence.", "Security:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 24));
                configuredLevel.speechBubbles.add(new SpeechBubble("...                   \nAre you sure that's necessary, sir?", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 24));
                configuredLevel.speechBubbles.add(new SpeechBubble("Not typically, but this prisoner is...", "Security:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 24));
                configuredLevel.speechBubbles.add(new SpeechBubble("...Special.", "Security:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 24));
                configuredLevel.speechBubbles.add(new SpeechBubble("We cannot let him escape.", "Security:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 24));
                configuredLevel.speechBubbles.add(new SpeechBubble("Very well, sir.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 24));
                configuredLevel.speechBubbles.add(new SpeechBubble("Countdown has been initiated.\nLock-down occurs in 10 hours.", "Security:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 24));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "Security:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.show = false, configuredLevel, 24));
                configuredLevel.speechBubbles.add(new SpeechBubble("One more thing,\nIf you see the prisoner...", "Security:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.show = true, configuredLevel, 24));
                configuredLevel.speechBubbles.add(new SpeechBubble("Yes...", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 24));
                configuredLevel.speechBubbles.add(new SpeechBubble("Don't hesitate to use\nyour Stun Gun.", "Security:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 24));
                configuredLevel.speechBubbles.add(new SpeechBubble("Yes, sir.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 24));
                configuredLevel.speechBubbles.add(new SpeechBubble("Central Security out.", "Security:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "Security:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.show = false, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Be careful, player.", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.show = true, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "Security:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = false;
                                configuredLevel.touchLocked = false;
                                configuredLevel.continueBubbles = false;
                                if (configuredLevel.getPlayer().getPosition().x < configuredLevel.guards.get(0).position.x) {
                                    configuredLevel.currentBubble = 28;
                                }
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Huh?", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.show = true;
                                configuredLevel.guards.get(0).talkToPlayer = true;
                                configuredLevel.guards.get(0).alert = true;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.viewportPosition.x = configuredLevel.guards.get(0).position.x - 50;
                                configuredLevel.touchLocked = true;
                                configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Halt prisoner!\nDo not take another step!", "Guard 1:", 4, "Make me", "Whatever", "I don't think so", "Yes, sir!",
                        () -> configuredLevel.currentBubble = 33,
                        () -> configuredLevel.currentBubble = 30,
                        () -> configuredLevel.currentBubble = 33,
                        () -> configuredLevel.currentBubble = 31, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("You will regret the day\nthat you came across me...", "Guard 1:", 1, "Okay?", "", "", "",
                        () -> configuredLevel.currentBubble = 34,
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Finally!\nA person who recognizes my authority!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Actually, I was mocking you...", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("How dare you!!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Prepare to be stunned!\nLiterally.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.guards.get(0).guardItem = "stungun";
                                configuredLevel.viewportPosition.x = configuredLevel.guards.get(0).position.x - 80;
                                configuredLevel.getPlayer().health = 20;
                                configuredLevel.guards.get(0).alert = false;
                                configuredLevel.getPlayer().alert = true;
                                configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Tap on the player to\nduck the Stun Gun lasers!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                if (configuredLevel.guards.get(0).heldItem.stungun.lasersShot < 30) {
                                    configuredLevel.guards.get(0).heldItem.stungun.fire = true;
                                }
                                if (configuredLevel.getPlayer().health <= 0) {
                                    configuredLevel.defeat = true;
                                    configuredLevel.verdict.verdict = false;
                                    configuredLevel.verdict.verdictPhrase = "You were stunned by the lasers!";
                                }
                                configuredLevel.getPlayer().alert = false;
                                configuredLevel.getPlayer().danger = true;
                                configuredLevel.getPlayer().useEnergy = true;
                                configuredLevel.continueBubbles = false;
                                if (configuredLevel.guards.get(0).heldItem.stungun.lasersShot >= 30) {
                                    if (configuredLevel.guards.get(0).heldItem.lasers.size > 0) {
                                        configuredLevel.getPlayer().duck = true;
                                        configuredLevel.guards.get(0).heldItem.stungun.fire = false;
                                    } else {
                                        configuredLevel.currentBubble = 36;
                                    }
                                }
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble(".....", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.guards.get(0).heldItem.stungun.fire = false;
                                configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN;
                                configuredLevel.getPlayer().duck = false;
                                configuredLevel.getPlayer().danger = false;
                                configuredLevel.getPlayer().useEnergy = false;
                                configuredLevel.continueBubbles = true;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("What!! I'm out of Ammo!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Why does Central Security always\ngive us the cheap weapons?!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("This is your chance!\nUnlock the door to your left!", "c7-x:", 1, "Got it!", "", "", "",
                        () -> {
                                configuredLevel.currentBubble = 40;
                                configuredLevel.continueBubbles = false;
                                configuredLevel.show = false;
                                configuredLevel.touchLocked = false;
                            },
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.currentBubble = 41;
                                configuredLevel.touchLocked = true;
                                configuredLevel.show = true;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("You aren't going\nanywhere, prisoner!", "Guard 1:", 1, "Oh no!", "", "", "",
                        () -> {
                                configuredLevel.defeat = true;
                                configuredLevel.verdict.verdict = false;
                                configuredLevel.verdict.verdictPhrase = "You went the wrong way!";
                            },
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));

                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(-500, 235));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(-490, -392), new Vector2(-650, -309), configuredLevel);
                configuredLevel.exitDoor.show = false;
                break;
            case 6:
                configuredLevel.show = false;
                configuredLevel.continueBubbles = false;
                configuredLevel.touchLocked = false;
                /*LEVEL CONFIGURATIONS:*/
                //player always starts inside the currentCatacomb
                configuredLevel.catacombs = new Array<>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -100), "Locked", "Locked", "Closed", "Locked", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -15), "Locked", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(150, 70), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(300, -15), "Closed", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-300, -15), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-550, 70), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-300, -185), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-550, -270), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -185), "Closed", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(150, -270), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -355), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(300, -355), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -440), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 0;

                configuredLevel.items = new DelayedRemovalArray<>();
                configuredLevel.items.add(new Item(new Vector2(-240, -152), configuredLevel.viewportPosition, "gold"));
                configuredLevel.items.add(new Item(new Vector2(-380, 105), configuredLevel.viewportPosition, "diamond"));
                configuredLevel.items.add(new Item(new Vector2(460, 19), configuredLevel.viewportPosition, "gold"));
                configuredLevel.items.add(new Item(new Vector2(420, 19), configuredLevel.viewportPosition, "diamond"));

                configuredLevel.puzzles = new Array<>();
                configuredLevel.puzzles.add(new Puzzle(new Vector2(-250, 100), new Vector2(70, 100), "shapes3", new Enums.Shape[]{Enums.Shape.CIRCLE, Enums.Shape.SQUARE, Enums.Shape.CIRCLE}, () -> {
                    configuredLevel.catacombs.get(5).getLockedDoors().set(4, "Unlocked");
                    configuredLevel.catacombs.get(4).getLockedDoors().set(1, "Unlocked");
                    configuredLevel.puzzles.get(1).hintPosition.set(new Vector2(-70, 20));
                }, configuredLevel));
                configuredLevel.puzzles.add(new Puzzle(new Vector2(-430, 185), new Vector2(1000, 1000), "shapes5", new Enums.Shape[]{Enums.Shape.TRIANGLE, Enums.Shape.TRIANGLE, Enums.Shape.CIRCLE, Enums.Shape.TRIANGLE, Enums.Shape.TRIANGLE}, () -> configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "dagger")), configuredLevel));
                //new type of puzzle: Whack-a-button!
                configuredLevel.puzzles.add(new Puzzle(new Vector2(230, 145), new Vector2(1000, 1000), "whacker1", new Enums.Shape[]{}, () -> {
                    configuredLevel.catacombs.get(2).getLockedDoors().set(4, "Unlocked");
                    configuredLevel.catacombs.get(3).getLockedDoors().set(1, "Unlocked");
                }, configuredLevel));
                //bonus puzzle
                configuredLevel.puzzles.add(new Puzzle(new Vector2(370, -290), new Vector2(1000, 1000), "shapes2", new Enums.Shape[]{Enums.Shape.CIRCLE, Enums.Shape.TRIANGLE}, () -> configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0))), configuredLevel));

                configuredLevel.words = new Array<>();
                configuredLevel.words.add(new Word(new String[]{"2", "K", "E", "Y", "S"}, new Vector2[]{new Vector2(-80, -40), new Vector2(-80, 20), new Vector2(0, -40), new Vector2(0, 20), new Vector2(0, -10)}, configuredLevel));

                configuredLevel.chests = new Array<>();
                configuredLevel.chests.add(new Chest(new Vector2(-490, -252), "gold", configuredLevel, () -> { configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "sapphire")); configuredLevel.chests.get(1).fade = true; configuredLevel.chests.get(2).fade = true; configuredLevel.inventory.deleteCurrentItem(); }));
                configuredLevel.chests.add(new Chest(new Vector2(-420, -252), "gold", configuredLevel, () -> configuredLevel.chests.get(1).fade = true));
                configuredLevel.chests.add(new Chest(new Vector2(-350, -252), "gold", configuredLevel, () -> configuredLevel.chests.get(2).fade = true));
                configuredLevel.chests.add(new Chest(new Vector2(-490, 88), "dagger", configuredLevel, () -> { configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "ruby")); configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key")); configuredLevel.inventory.deleteCurrentItem(); }));
                configuredLevel.chests.add(new Chest(new Vector2(360, 3), "gold", configuredLevel, () -> { configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "emerald")); configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key")); configuredLevel.inventory.deleteCurrentItem(); }));
                configuredLevel.chests.add(new Chest(new Vector2(60, -167), "ruby", configuredLevel, () -> { configuredLevel.catacombs.get(8).getLockedDoors().set(4, "Unlocked"); configuredLevel.catacombs.get(9).getLockedDoors().set(1, "Unlocked"); configuredLevel.inventory.deleteCurrentItem(); }));
                configuredLevel.chests.add(new Chest(new Vector2(210, -252), "emerald", configuredLevel, () -> { configuredLevel.catacombs.get(9).getLockedDoors().set(0, "Unlocked"); configuredLevel.catacombs.get(10).getLockedDoors().set(3, "Unlocked"); configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key")); configuredLevel.inventory.deleteCurrentItem(); }));
                configuredLevel.chests.add(new Chest(new Vector2(60, -337), "sapphire", configuredLevel, () -> { configuredLevel.catacombs.get(12).getLockedDoors().set(3, "Unlocked"); configuredLevel.catacombs.get(10).getLockedDoors().set(0, "Unlocked"); configuredLevel.inventory.deleteCurrentItem(); }));

                configuredLevel.guards = new Array<>();

                configuredLevel.levers = new Array<>();
                configuredLevel.levers.add(new Lever(new Vector2(130, 3), () -> { configuredLevel.catacombs.get(1).getLockedDoors().set(3, "Unlocked"); configuredLevel.catacombs.get(2).getLockedDoors().set(0, "Unlocked"); }, () -> { configuredLevel.catacombs.get(1).getLockedDoors().set(3, "Closed"); configuredLevel.catacombs.get(2).getLockedDoors().set(0, "Closed"); }, configuredLevel));
                configuredLevel.levers.add(new Lever(new Vector2(-170, -167), () -> { configuredLevel.catacombs.get(7).getLockedDoors().set(3, "Unlocked"); configuredLevel.catacombs.get(6).getLockedDoors().set(0, "Unlocked"); }, () -> { configuredLevel.catacombs.get(7).getLockedDoors().set(3, "Closed"); configuredLevel.catacombs.get(6).getLockedDoors().set(0, "Closed"); }, configuredLevel));

                configuredLevel.doors = new Array<>();

                configuredLevel.signs = new Array<>();

                configuredLevel.wires = new Array<>();

                configuredLevel.prisoners = new Array<>();

                configuredLevel.bosses = new Array<>();
                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<>();
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 60, configuredLevel.getPlayer().getPosition().y + 20); configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.currentBubble = 1; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Woah! What is THAT?!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I think it is a\nnew type of puzzle!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Try to tap on that red ball!", "c7-x:", 1, "I will try my best!", "", "", "",
                        () -> { configuredLevel.show = false; configuredLevel.continueBubbles = false; },
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.touchLocked = false; configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.currentBubble = 4;}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Nice!    \nYou did it!!!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.continueBubbles = false; }, configuredLevel, -1));

                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(-50, 65));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(-100, -422), new Vector2(400, -339), configuredLevel);
                break;
            case 7:
                configuredLevel.show = false;
                configuredLevel.continueBubbles = false;
                configuredLevel.touchLocked = false;
                /*LEVEL CONFIGURATIONS:*/
                //player always starts inside the currentCatacomb
                configuredLevel.catacombs = new Array<>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-600, -100), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-450, -185), "Closed", "Locked", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-200, -100), "Locked", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-50, -185), "Locked", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-200, -270), "Unlocked", "Closed", "Closed", "Locked", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-350, -355), "Closed", "Closed", "Closed", "Unlocked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-50, -355), "Closed", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-200, -440), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-600, -440), "Unlocked", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-750, -525), "Closed", "Closed", "Closed", "Unlocked", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-750, -695), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-450, -695), "Closed", "Unlocked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-600, -610), "Closed", "Locked", "Closed", "Closed", "Unlocked", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 0;

                configuredLevel.items = new DelayedRemovalArray<>();
                configuredLevel.items.add(new Item(new Vector2(-540, -70), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(-470, -70), configuredLevel.viewportPosition, "invisibility"));
                configuredLevel.items.add(new Item(new Vector2(50, -155), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(-80, -240), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(-80, -410), configuredLevel.viewportPosition, "diamond"));
                configuredLevel.items.add(new Item(new Vector2(-120, -410), configuredLevel.viewportPosition, "diamond"));
                configuredLevel.items.add(new Item(new Vector2(-640, -665), configuredLevel.viewportPosition, "diamond"));

                configuredLevel.puzzles = new Array<>();
                configuredLevel.puzzles.add(new Puzzle(new Vector2(10, -85), new Vector2(-140, 0), "shapes3", new Enums.Shape[]{Enums.Shape.TRIANGLE, Enums.Shape.CIRCLE, Enums.Shape.CIRCLE}, () -> {
                    configuredLevel.catacombs.get(2).getLockedDoors().set(4, "Closed");
                    configuredLevel.catacombs.get(3).getLockedDoors().set(1, "Closed");
                }, configuredLevel));

                configuredLevel.words = new Array<>();
                configuredLevel.words.add(new Word(new String[]{"R","U","B","Y"}, new Vector2[]{new Vector2(40, -270), new Vector2(80, -280), new Vector2(-220, -270), new Vector2(-260, -280)}, configuredLevel));
                configuredLevel.words.add(new Word(new String[]{"K","E","Y"}, new Vector2[]{new Vector2(-400, -400), new Vector2(-500, -400), new Vector2(-650, -485)}, configuredLevel));

                configuredLevel.chests = new Array<>();
                configuredLevel.chests.add(new Chest(new Vector2(-110, -82), "", configuredLevel, () -> configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"))));
                configuredLevel.chests.add(new Chest(new Vector2(-140, -252), "ruby", configuredLevel, () -> { configuredLevel.catacombs.get(5).getLockedDoors().set(0, "Unlocked"); configuredLevel.catacombs.get(8).getLockedDoors().set(3, "Unlocked"); configuredLevel.inventory.deleteCurrentItem(); configuredLevel.inventory.selectedItem = -1; }));

                configuredLevel.guards = new Array<>();
                configuredLevel.guards.add(new Guard(new Vector2(-230, -119), configuredLevel));
                configuredLevel.guards.add(new Guard(new Vector2(-380, -35), configuredLevel));
                configuredLevel.guards.get(0).guardItem = "key";

                configuredLevel.levers = new Array<>();
                configuredLevel.levers.add(new Lever(new Vector2(30, -337), () -> { configuredLevel.catacombs.get(6).getLockedDoors().set(0, "Unlocked"); configuredLevel.catacombs.get(7).getLockedDoors().set(3, "Unlocked"); }, () -> { configuredLevel.catacombs.get(6).getLockedDoors().set(0, "Closed"); configuredLevel.catacombs.get(7).getLockedDoors().set(3, "Closed"); }, configuredLevel));
                configuredLevel.levers.add(new Lever(new Vector2(-500, -592), () -> { configuredLevel.catacombs.get(12).getLockedDoors().set(0, "Unlocked"); configuredLevel.catacombs.get(10).getLockedDoors().set(3, "Unlocked"); configuredLevel.catacombs.get(12).getLockedDoors().set(4, "Closed"); configuredLevel.catacombs.get(11).getLockedDoors().set(1, "Closed"); }, () -> { configuredLevel.catacombs.get(12).getLockedDoors().set(0, "Closed"); configuredLevel.catacombs.get(10).getLockedDoors().set(3, "Closed"); configuredLevel.catacombs.get(12).getLockedDoors().set(4, "Unlocked"); configuredLevel.catacombs.get(11).getLockedDoors().set(1, "Unlocked"); }, configuredLevel));

                configuredLevel.doors = new Array<>();

                configuredLevel.signs = new Array<>();

                configuredLevel.wires = new Array<>();

                configuredLevel.prisoners = new Array<>();

                configuredLevel.bosses = new Array<>();

                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<>();
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.getPlayer().alert = true; configuredLevel.viewportPosition.set(configuredLevel.getPlayer().getPosition().x - 5, configuredLevel.getPlayer().getPosition().y); configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN; configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.currentBubble = 1; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Yikes!    \nI see a guard!!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("It's the escaped prisoner!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {  if (configuredLevel.guards.get(0).guardTouchesPlayer()) { configuredLevel.defeat = true; configuredLevel.verdict.verdict = false; configuredLevel.verdict.verdictPhrase = "You were captured by a Guard!"; } configuredLevel.guards.get(0).alert = true; configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Run, player!!!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { if (configuredLevel.getPlayer().getPosition().x < configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 125) { configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN; configuredLevel.currentBubble = 4; } configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 120, configuredLevel.getPlayer().getPosition().y + 20); }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Nice try prisoner...\nSurrender yourself!", "Guard 2:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("You are trapped!", "c7-x:", 3, "I can see that!", "Help!!!", "What should I do?!", "",
                        () -> configuredLevel.currentBubble = 6,
                        () -> configuredLevel.currentBubble = 6,
                        () -> configuredLevel.currentBubble = 6,
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Wait!      \nI have a solution!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Quick! Tell me!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I have searched my vast\ndatabases and have found that...", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("The potion in your\ninventory is an...", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("What?!\nTell me!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Invisibility Potion!!!", "c7-x:", 1, "Drink the potion", "", "", "",
                        () -> { configuredLevel.getPlayer().alert = false; configuredLevel.inventory.selectedItem = 0; configuredLevel.inventory.inventoryItems.get(configuredLevel.inventory.selectedItem).potion.full = false; configuredLevel.getPlayer().heldItem.potion.full = false; configuredLevel.currentBubble = 12; },
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble(".....", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.getPlayer().invisibility = true; configuredLevel.guards.get(0).suspicious = true; configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN; configuredLevel.guards.get(1).mouthState = Enums.MouthState.OPEN; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Did you just see\nwhat I just saw?!", "Guard 2:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Yeah,    \nthat prisoner just disappeared!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.guards.get(0).suspicious = false, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I need to get\nout of here.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                //grab key from guard
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.touchLocked = false; configuredLevel.show = false; configuredLevel.continueBubbles = false; if (configuredLevel.guards.get(0).guardTouchesPlayer() &&
                                configuredLevel.touchPosition.x > (configuredLevel.guards.get(0).jumpState.equals(Enums.JumpState.GROUNDED) ? (configuredLevel.guards.get(0).position.x - Constants.HEAD_SIZE) - 12 : (configuredLevel.guards.get(0).position.x - Constants.HEAD_SIZE)) &&
                                configuredLevel.touchPosition.y > (configuredLevel.guards.get(0).position.y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f) &&
                                configuredLevel.touchPosition.x < (configuredLevel.guards.get(0).jumpState.equals(Enums.JumpState.GROUNDED) ? (Constants.PLAYER_WIDTH * 2f) + 12 : (Constants.PLAYER_WIDTH * 2f)) + (configuredLevel.guards.get(0).jumpState.equals(Enums.JumpState.GROUNDED) ? (configuredLevel.guards.get(0).position.x - Constants.HEAD_SIZE) - 12 : (configuredLevel.guards.get(0).position.x - Constants.HEAD_SIZE)) &&
                                configuredLevel.touchPosition.y < (Constants.PLAYER_HEIGHT * 2.5f) + (configuredLevel.guards.get(0).position.y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f) && configuredLevel.guards.get(0).guardItem.equals("key")) { configuredLevel.guards.get(0).guardItem = ""; configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key")); configuredLevel.currentBubble = 17; }
                        }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hey!      \nSomeone stole my key!!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.guards.get(0).suspicious = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Something strange is\ngoing on here...", "Guard 2:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.continueBubbles = false; if (configuredLevel.currentCatacomb == 2) { configuredLevel.currentBubble = 20; configuredLevel.show = true; configuredLevel.continueBubbles = true; } if (!configuredLevel.guards.get(1).alert) { configuredLevel.guards.get(1).guardState = Enums.GuardState.PATROLLING; } else { configuredLevel.show = true; configuredLevel.touchLocked = true; configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN; configuredLevel.currentBubble = 19; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("There you are, prisoner!!!", "Guard 2:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.continueBubbles = false; if (configuredLevel.speechBubbles.get(configuredLevel.currentBubble).textLoaded) { configuredLevel.defeat = true; configuredLevel.verdict.verdict = false; configuredLevel.verdict.verdictPhrase = "Next time, avoid the Guard!"; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Yikes,   \nthat was too close!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("It looks like the\nInvisibility Potion wore off.", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.getPlayer().invisibility = false, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.continueBubbles = false; if (configuredLevel.currentCatacomb == 8) { configuredLevel.currentBubble = 23; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Oh no!    \nMore Stalactites!!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.touchLocked = true; configuredLevel.shake = true; configuredLevel.show = true; configuredLevel.continueBubbles = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.touchLocked = false; configuredLevel.show = false; configuredLevel.continueBubbles = false; configuredLevel.catacombs.get(8).drop = true; configuredLevel.catacombs.get(9).drop = true; configuredLevel.getPlayer().danger = true; if (configuredLevel.currentCatacomb == 12) { configuredLevel.currentBubble = 25; } if (configuredLevel.getPlayer().health <= 0) { configuredLevel.touchLocked = true; configuredLevel.defeat = true; configuredLevel.verdict.verdict = false; configuredLevel.verdict.verdictPhrase = "Ouch! Better luck next time!!"; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I have survived...\nAgain!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.shake = false; configuredLevel.show = true; configuredLevel.continueBubbles = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.continueBubbles = false; configuredLevel.catacombs.get(8).drop = false; configuredLevel.catacombs.get(9).drop = false; configuredLevel.getPlayer().danger = false; }, configuredLevel, -1));

                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(-500, 65));
                //exit door +18 y, +16 y
                configuredLevel.exitDoor = new Exit(new Vector2(-350, -677), new Vector2(-690, -679), configuredLevel);
                break;
            case 8:
                configuredLevel.show = false;
                configuredLevel.continueBubbles = false;
                configuredLevel.touchLocked = false;
                /*LEVEL CONFIGURATIONS:*/
                //player always starts inside the currentCatacomb
                configuredLevel.catacombs = new Array<>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-200, -185), "Closed", "Closed", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(50, -100), "Locked", "Closed", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(200, -15), "Locked", "DoubleLocked", "Closed", "Unlocked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(350, 70), "Unlocked", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-50, 70), "Closed", "Closed", "Closed", "Closed", "DoubleLocked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-200, 155), "Closed", "Closed", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-50, 240), "Locked", "Closed", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(200, 325), "Locked", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-50, 410), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 0;

                configuredLevel.items = new DelayedRemovalArray<>();
                configuredLevel.items.add(new Item(new Vector2(0, -155), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(120, -70), configuredLevel.viewportPosition, "sapphire"));
                //new Item
                configuredLevel.items.add(new Item(new Vector2(320, 15), configuredLevel.viewportPosition, "doubleKey"));
                configuredLevel.items.add(new Item(new Vector2(-90, 185), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(-140, 185), configuredLevel.viewportPosition, "stungun"));
                configuredLevel.items.add(new Item(new Vector2(-115, 185), configuredLevel.viewportPosition, "shield"));
                configuredLevel.items.add(new Item(new Vector2(1000, 1000), configuredLevel.viewportPosition, "diamond"));

                configuredLevel.puzzles = new Array<>();
                //bonus puzzles
                configuredLevel.puzzles.add(new Puzzle(new Vector2(430, 170), new Vector2(1000, 1000), "shapes2", new Enums.Shape[]{Enums.Shape.TRIANGLE, Enums.Shape.TRIANGLE}, () -> configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0))), configuredLevel));
                configuredLevel.puzzles.add(new Puzzle(new Vector2(430, 140), new Vector2(1000, 1000), "shapes2", new Enums.Shape[]{Enums.Shape.CIRCLE, Enums.Shape.CIRCLE}, () -> configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0))), configuredLevel));
                //this following line of code is not actually a puzzle. I use it as a series of variables in speechBubble logic.
                configuredLevel.puzzles.add(new Puzzle(new Vector2(1000, 1000), new Vector2(1000, 1000), "shapes4", new Enums.Shape[]{Enums.Shape.CIRCLE, Enums.Shape.CIRCLE, Enums.Shape.CIRCLE, Enums.Shape.CIRCLE}, () -> { }, configuredLevel));
                //harder whacker puzzle
                configuredLevel.puzzles.add(new Puzzle(new Vector2(90, 500), new Vector2(1000, 1000), "whacker2", new Enums.Shape[]{}, () -> {
                    configuredLevel.exitDoor.setButtonPosition(new Vector2(150, 426));
                    for (int i = 0; i < configuredLevel.items.size; i++) {
                        if (configuredLevel.items.get(i).itemType.equals("diamond")) {
                            configuredLevel.items.get(i).position.set(new Vector2(10, 445));
                        }
                    }
                }, configuredLevel));
                //hint puzzle
                configuredLevel.puzzles.add(new Puzzle(new Vector2(1000, 1000), new Vector2(80, 150), "shapes3", new Enums.Shape[]{Enums.Shape.TRIANGLE, Enums.Shape.CIRCLE, Enums.Shape.TRIANGLE}, () -> { }, configuredLevel));

                configuredLevel.words = new Array<>();
                configuredLevel.words.add(new Word(new String[]{"P", "O", "T", "I", "O", "N"}, new Vector2[]{new Vector2(-40, -105), new Vector2(30, -105), new Vector2(-10, -105), new Vector2(120, -25), new Vector2(140, -25), new Vector2(1000, 1000)}, configuredLevel));
                configuredLevel.words.add(new Word(new String[]{"2", "K", "E", "Y", "S"}, new Vector2[]{new Vector2(260, 370), new Vector2(320, 440), new Vector2(340, 440), new Vector2(270, 440), new Vector2(280, 370)}, configuredLevel));

                configuredLevel.chests = new Array<>();
                configuredLevel.chests.add(new Chest(new Vector2(160, -82), "sapphire", configuredLevel, () -> { configuredLevel.words.get(0).letterPositions[5].set(new Vector2(160, -25)); configuredLevel.inventory.deleteCurrentItem(); }));

                configuredLevel.guards = new Array<>();
                configuredLevel.guards.add(new Guard(new Vector2(100, 300), configuredLevel));

                configuredLevel.levers = new Array<>();
                configuredLevel.levers.add(new Lever(new Vector2(50, 88), () -> {}, () -> {}, configuredLevel));
                configuredLevel.levers.add(new Lever(new Vector2(100, 88), () -> {}, () -> {}, configuredLevel));
                configuredLevel.levers.add(new Lever(new Vector2(150, 88), () -> {}, () -> {}, configuredLevel));

                configuredLevel.doors = new Array<>();

                configuredLevel.signs = new Array<>();

                configuredLevel.wires = new Array<>();

                configuredLevel.prisoners = new Array<>();

                configuredLevel.bosses = new Array<>();

                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<>();
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.inventory.selectedItem = 0; configuredLevel.getPlayer().mouthState = Enums.MouthState.NORMAL; if (!configuredLevel.inventory.newItem) { configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 80, configuredLevel.getPlayer().getPosition().y + 20); configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.currentBubble = 1; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Another Potion!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I think that this Potion\nmay let you walk through walls.", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("If the Potion is in your hand,\nyou can tap on it and drink it!", "c7-x:", 1, "Got it!", "", "", "",
                        () -> { configuredLevel.show = false; configuredLevel.continueBubbles = false; },
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.touchLocked = false;  if (configuredLevel.currentCatacomb == 2) { configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.currentBubble = 4; }}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Nice Work!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.getPlayer().drinkPotion = false; configuredLevel.getPlayer().ghost = false; configuredLevel.getPlayer().invisibility = false; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Wow!\nThat Potion wore off fast!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.touchLocked = false; configuredLevel.show = false; configuredLevel.continueBubbles = false; if (configuredLevel.currentCatacomb == 6) { configuredLevel.currentBubble = 7; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Another Guard!\nAck!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.catacombs.get(5).getLockedDoors().set(3, "Closed"); configuredLevel.catacombs.get(6).getLockedDoors().set(0, "Closed"); configuredLevel.getPlayer().alert = true; configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 70, configuredLevel.getPlayer().getPosition().y + 20); }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("You won't get away\nthis time, prisoner!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.guards.get(0).guardItem = "stungun"; configuredLevel.getPlayer().health = 20; configuredLevel.guards.get(0).health = 20; configuredLevel.getPlayer().heldItem.lasers = new DelayedRemovalArray<>(); configuredLevel.guards.get(0).heldItem.lasers = new DelayedRemovalArray<>(); }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Use the shield to block lasers\nand use the Stun Gun to fight back!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                            if (configuredLevel.guards.get(0).health > 0 && !configuredLevel.defeat) {
                                configuredLevel.guards.get(0).heldItem.stungun.fire = true;
                            }
                            //you lose
                            if (configuredLevel.getPlayer().health <= 0) {
                                configuredLevel.defeat = true;
                                configuredLevel.verdict.verdict = false;
                                configuredLevel.verdict.verdictPhrase = "You were stunned by the lasers!";
                            }
                            //make mouths be open if they are losing
                            if (configuredLevel.getPlayer().health <= 5) {
                                configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN;
                            }
                            if (configuredLevel.guards.get(0).health <= 5) {
                                configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN;
                            }
                            configuredLevel.getPlayer().alert = false;
                            configuredLevel.getPlayer().danger = true;
                            configuredLevel.guards.get(0).danger = true;
                            configuredLevel.getPlayer().useEnergy = true;
                            configuredLevel.continueBubbles = false;
                            if (configuredLevel.guards.get(0).health <= 0) {
                                configuredLevel.guards.get(0).heldItem.stungun.fire = false;
                                configuredLevel.guards.get(0).guardItem = "";
                                if (configuredLevel.guards.get(0).armRotate < 60) {
                                    configuredLevel.guards.get(0).armRotate += 4f;
                                } else {
                                    configuredLevel.currentBubble = 10;
                                }
                            }
                        }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Stop, stop!\nI surrender!!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.guards.get(0).danger = false; configuredLevel.guards.get(0).health = 20; configuredLevel.getPlayer().danger = false; configuredLevel.getPlayer().useEnergy = false; configuredLevel.getPlayer().health = 20; configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN; configuredLevel.continueBubbles = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Really?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.getPlayer().heldItem.itemType = "", configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I am going to ask\nyou a few questions.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Never!!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                            if (configuredLevel.guards.get(0).armRotate > 0) {
                                configuredLevel.guards.get(0).armRotate -= 4f;
                            }
                            configuredLevel.getPlayer().heldItem.itemType = "";
                            configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN;
                        }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I will never tell...", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.getPlayer().facing = Enums.Facing.RIGHT; configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 75, configuredLevel.getPlayer().getPosition().y + 20); configuredLevel.getPlayer().heldItem.itemType = "stungun"; if (configuredLevel.speechBubbles.get(configuredLevel.currentBubble).textLoaded) { configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN; configuredLevel.guards.get(0).alert = true; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Fine, but only a few.", "Guard 1:", 4, "Who are you?", "Who do you work for?", "Why am I here?", "How can I escape?",
                        () -> { configuredLevel.currentBubble = 16; configuredLevel.guards.get(0).alert = false; },
                        () -> { configuredLevel.currentBubble = 20; configuredLevel.guards.get(0).alert = false; },
                        () -> { configuredLevel.currentBubble = 26; configuredLevel.guards.get(0).alert = false; },
                        () -> { configuredLevel.currentBubble = 33; configuredLevel.guards.get(0).alert = false; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("My sentry code is:\nGuard 48Y-96K3.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("But, my real name is Jeremy.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.puzzles.get(2).solution[0] = Enums.Shape.TRIANGLE, configuredLevel, 19));
                configuredLevel.speechBubbles.add(new SpeechBubble("This is the area that\nI patrol during my shift.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 19));
                configuredLevel.speechBubbles.add(new SpeechBubble("Are you satisfied yet?", "Guard 1:", configuredLevel.puzzles.get(2).solution[0] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[1] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[2] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[3] == Enums.Shape.TRIANGLE ? 0 : 4,  configuredLevel.puzzles.get(2).solution[0] == Enums.Shape.TRIANGLE ? "" : "Who are you?", configuredLevel.puzzles.get(2).solution[1] == Enums.Shape.TRIANGLE ? "" : "Who do you work for?", configuredLevel.puzzles.get(2).solution[2] == Enums.Shape.TRIANGLE ? "" : "Why am I here?", configuredLevel.puzzles.get(2).solution[3] == Enums.Shape.TRIANGLE ? "" : "How can I escape?",
                        () -> configuredLevel.currentBubble = 16,
                        () -> configuredLevel.currentBubble = 20,
                        () -> configuredLevel.currentBubble = 26,
                        () -> configuredLevel.currentBubble = 33, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Well, I am employed by the\nbuilders of the Catacombs.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Who?!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.puzzles.get(2).solution[1] = Enums.Shape.TRIANGLE, configuredLevel, 25));
                configuredLevel.speechBubbles.add(new SpeechBubble("The people who turned these\nsubterranean tunnels into a prison!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 25));
                configuredLevel.speechBubbles.add(new SpeechBubble("Actually, these ancient tombs were\noriginally constructed by the Romans.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 25));
                configuredLevel.speechBubbles.add(new SpeechBubble("But recently, they have been\nreinforced by my employers...", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 25));
                configuredLevel.speechBubbles.add(new SpeechBubble("...And have been transformed into\nthe largest prison in the world!", "Guard 1:", configuredLevel.puzzles.get(2).solution[0] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[1] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[2] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[3] == Enums.Shape.TRIANGLE ? 0 : 4, configuredLevel.puzzles.get(2).solution[0] == Enums.Shape.TRIANGLE ? "" : "Who are you?", configuredLevel.puzzles.get(2).solution[1] == Enums.Shape.TRIANGLE ? "" : "Who do you work for?", configuredLevel.puzzles.get(2).solution[2] == Enums.Shape.TRIANGLE ? "" : "Why am I here?", configuredLevel.puzzles.get(2).solution[3] == Enums.Shape.TRIANGLE ? "" : "How can I escape?",
                        () -> configuredLevel.currentBubble = 16,
                        () -> configuredLevel.currentBubble = 20,
                        () -> configuredLevel.currentBubble = 26,
                        () -> configuredLevel.currentBubble = 33, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Well, this is a\nhigh-security prison...", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("You probably have committed\nsome horrendous crime!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.puzzles.get(2).solution[2] = Enums.Shape.TRIANGLE, configuredLevel, 32));
                configuredLevel.speechBubbles.add(new SpeechBubble("What?!      \nI would never do that!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN, configuredLevel, 32));
                configuredLevel.speechBubbles.add(new SpeechBubble("There are no alternatives.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 32));
                configuredLevel.speechBubbles.add(new SpeechBubble("Please listen to me!     \nI haven't done anything wrong!!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 32));
                configuredLevel.speechBubbles.add(new SpeechBubble("I don't remember\nanything before this!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 32));
                configuredLevel.speechBubbles.add(new SpeechBubble("I still don't believe you.", "Guard 1:", configuredLevel.puzzles.get(2).solution[0] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[1] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[2] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[3] == Enums.Shape.TRIANGLE ? 0 : 4, configuredLevel.puzzles.get(2).solution[0] == Enums.Shape.TRIANGLE ? "" : "Who are you?", configuredLevel.puzzles.get(2).solution[1] == Enums.Shape.TRIANGLE ? "" : "Who do you work for?", configuredLevel.puzzles.get(2).solution[2] == Enums.Shape.TRIANGLE ? "" : "Why am I here?", configuredLevel.puzzles.get(2).solution[3] == Enums.Shape.TRIANGLE ? "" : "How can I escape?",
                        () -> configuredLevel.currentBubble = 16,
                        () -> configuredLevel.currentBubble = 20,
                        () -> configuredLevel.currentBubble = 26,
                        () -> configuredLevel.currentBubble = 33, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Ha!! Escape?!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Yeah, I want to find the\nquickest route to the surface.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.puzzles.get(2).solution[3] = Enums.Shape.TRIANGLE, configuredLevel, 40));
                configuredLevel.speechBubbles.add(new SpeechBubble("No prisoner has EVER\nescaped the Catacombs!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 40));
                configuredLevel.speechBubbles.add(new SpeechBubble("I am not a prisoner!!!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 40));
                configuredLevel.speechBubbles.add(new SpeechBubble("It would take months to\nnavigate to the surface.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 40));
                configuredLevel.speechBubbles.add(new SpeechBubble("Wait a second!\nIf it takes months to do that...", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 40));
                configuredLevel.speechBubbles.add(new SpeechBubble("Then, how would YOU\nget to the surface?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 40));
                configuredLevel.speechBubbles.add(new SpeechBubble(".....     \nI have said too much.", "Guard 1:", configuredLevel.puzzles.get(2).solution[0] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[1] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[2] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[3] == Enums.Shape.TRIANGLE ? 0 : 4, configuredLevel.puzzles.get(2).solution[0] == Enums.Shape.TRIANGLE ? "" : "Who are you?", configuredLevel.puzzles.get(2).solution[1] == Enums.Shape.TRIANGLE ? "" : "Who do you work for?", configuredLevel.puzzles.get(2).solution[2] == Enums.Shape.TRIANGLE ? "" : "Why am I here?", configuredLevel.puzzles.get(2).solution[3] == Enums.Shape.TRIANGLE ? "" : "How can I escape?",
                        () -> configuredLevel.currentBubble = 16,
                        () -> configuredLevel.currentBubble = 20,
                        () -> configuredLevel.currentBubble = 26,
                        () -> configuredLevel.currentBubble = 33, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Okay,      \nI am done questioning you.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I'll be glad when Central\nSecurity finally catches you.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("But, I haven't committed\nany crimes!!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Most of the prisoners\nhere say that.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now, please hand over your Keys.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("NEVER!!!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.guards.get(0).guardItem = "stungun"; configuredLevel.getPlayer().health = 20; configuredLevel.guards.get(0).health = 20; configuredLevel.getPlayer().heldItem.lasers = new DelayedRemovalArray<>(); configuredLevel.guards.get(0).heldItem.lasers = new DelayedRemovalArray<>(); configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Protect yourself and\nget to the Guard!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                            configuredLevel.touchLocked = false;
                            if (configuredLevel.guards.get(0).health > 0 && !configuredLevel.defeat) {
                                configuredLevel.guards.get(0).heldItem.stungun.fire = true;
                            }
                            //you lose
                            if (configuredLevel.getPlayer().health <= 0) {
                                configuredLevel.defeat = true;
                                configuredLevel.verdict.verdict = false;
                                configuredLevel.verdict.verdictPhrase = "You need to use your shield!";
                            }
                            //make mouths be open if they are losing
                            if (configuredLevel.getPlayer().health <= 5) {
                                configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN;
                            }
                            configuredLevel.getPlayer().alert = false;
                            configuredLevel.getPlayer().danger = true;
                            configuredLevel.getPlayer().useEnergy = true;
                            configuredLevel.continueBubbles = false;
                            //player gets to guard in time
                            if (configuredLevel.guards.get(0).guardTouchesPlayer() && configuredLevel.getPlayer().health > 0) {
                                configuredLevel.guards.get(0).heldItem.stungun.fire = false;
                                configuredLevel.guards.get(0).guardItem = "";
                                if (configuredLevel.guards.get(0).armRotate < 60) {
                                    configuredLevel.guards.get(0).armRotate += 4f;
                                } else {
                                    configuredLevel.currentBubble = 48;
                                }
                            }
                        }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Fine, fine!!\nI'll give you my key!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.getPlayer().danger = false; configuredLevel.getPlayer().useEnergy = false; configuredLevel.guards.get(0).danger = false; configuredLevel.touchLocked = true; configuredLevel.continueBubbles = true; configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN; configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 120, configuredLevel.getPlayer().getPosition().y + 20);  }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble(".....", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { if (configuredLevel.inventory.inventoryItems.size < 4) { configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key")); } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("This time,    \nI am taking your Stun Gun.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { if (configuredLevel.inventory.inventoryItems.size < 5) { configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "stungun")); } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Let's go find the Exit.", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.continueBubbles = false; configuredLevel.touchLocked = false; if (configuredLevel.currentCatacomb == 8) { configuredLevel.currentBubble = 53; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Another Puzzle!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.touchLocked = true; configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 120, configuredLevel.getPlayer().getPosition().y + 20); }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.continueBubbles = false; if (configuredLevel.puzzles.get(3).solved) { configuredLevel.currentBubble = 55; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Well done!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.touchLocked = false; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.continueBubbles = false; }, configuredLevel, -1));
                configuredLevel.currentBubble = 0;
                //-100, -20, test: -100, 320
                configuredLevel.player.position.set(new Vector2(-100, -20));
                //exit door +18y, +16y
                configuredLevel.exitDoor = new Exit(new Vector2(290, 343), new Vector2(1000, 1000), configuredLevel);
                break;
            case 9:
                configuredLevel.show = false;
                configuredLevel.continueBubbles = false;
                configuredLevel.touchLocked = false;
                /*LEVEL CONFIGURATIONS:*/
                //player always starts inside the currentCatacomb
                configuredLevel.catacombs = new Array<>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -100), "Locked", "Unlocked", "Closed", "Unlocked", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-300, -15), "Closed", "Closed", "Closed", "Closed", "Unlocked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -15), "Unlocked", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-300, -185), "Closed", "Closed", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -185), "Closed", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));

                configuredLevel.catacombs.add(new Catacomb(new Vector2(-300, -535), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -620), "Locked", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(100, -705), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-300, -705), "DoubleLocked", "Closed", "Closed", "Locked", "DoubleLocked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-450, -790), "Closed", "Closed", "Closed", "Locked", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-300, -875), "Closed", "Locked", "Closed", "Locked", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -790), "Locked", "DoubleLocked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -960), "Closed", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 0;

                configuredLevel.items = new DelayedRemovalArray<>();
                configuredLevel.items.add(new Item(new Vector2(-200, -155), configuredLevel.viewportPosition, "diamond"));
                configuredLevel.items.add(new Item(new Vector2(100, -155), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(-170, -505), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(180, -675), configuredLevel.viewportPosition, "diamond"));
                configuredLevel.items.add(new Item(new Vector2(230, -675), configuredLevel.viewportPosition, "diamond"));
                configuredLevel.items.add(new Item(new Vector2(-180, -845), configuredLevel.viewportPosition, "ghost"));

                configuredLevel.puzzles = new Array<>();
                configuredLevel.puzzles.add(new Puzzle(new Vector2(80, 100), new Vector2(-230, -60), "shapes3", new Enums.Shape[]{Enums.Shape.CIRCLE, Enums.Shape.CIRCLE, Enums.Shape.TRIANGLE}, () -> configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key")), configuredLevel));
                configuredLevel.puzzles.add(new Puzzle(new Vector2(-230, 90), new Vector2(1000, 1000), "shapes2", new Enums.Shape[]{Enums.Shape.CIRCLE, Enums.Shape.TRIANGLE}, () -> {
                    if (configuredLevel.puzzles.get(2).solved) {
                        configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                    }
                }, configuredLevel));
                configuredLevel.puzzles.add(new Puzzle(new Vector2(-170, 100), new Vector2(1000, 1000), "shapes1", new Enums.Shape[]{Enums.Shape.CIRCLE}, () -> {
                    if (configuredLevel.puzzles.get(1).solved) {
                        configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                    }
                }, configuredLevel));

                configuredLevel.words = new Array<>();
                configuredLevel.words.add(new Word(new String[]{"D", "O", "O", "R"}, new Vector2[]{new Vector2(-200, 50), new Vector2(100, 50), new Vector2(-200, -110), new Vector2(100, -110)}, configuredLevel));
                configuredLevel.words.add(new Word(new String[]{"E", "X", "I", "T"}, new Vector2[]{new Vector2(-30, -900), new Vector2(-80, -850), new Vector2(-30, -850), new Vector2(-80, -900)}, configuredLevel));

                configuredLevel.chests = new Array<>();
                configuredLevel.chests.add(new Chest(new Vector2(-35, -602), "", configuredLevel, () -> { configuredLevel.chests.get(0).fade = true; configuredLevel.chests.get(1).fade = true; configuredLevel.catacombs.get(6).getLockedDoors().set(5, "Unlocked"); configuredLevel.catacombs.get(11).getLockedDoors().set(2, "Unlocked"); configuredLevel.shake = true; }));
                configuredLevel.chests.add(new Chest(new Vector2(10, -602), "", configuredLevel, () -> { configuredLevel.chests.get(0).fade = true; configuredLevel.chests.get(1).fade = true; configuredLevel.catacombs.get(6).getLockedDoors().set(5, "Unlocked"); configuredLevel.catacombs.get(11).getLockedDoors().set(2, "Unlocked"); configuredLevel.shake = true; }));
                configuredLevel.chests.add(new Chest(new Vector2(55, -602), "", configuredLevel, () -> { configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key")); configuredLevel.chests.get(0).fade = true; configuredLevel.chests.get(1).fade = true; }));

                configuredLevel.guards = new Array<>();
                configuredLevel.guards.add(new Guard(new Vector2(-370, -730), configuredLevel));
                configuredLevel.guards.get(0).guardItem = "key";
                configuredLevel.guards.get(0).asleep = true;

                configuredLevel.levers = new Array<>();
                configuredLevel.levers.add(new Lever(new Vector2(-220, -687), () -> { configuredLevel.catacombs.get(11).getLockedDoors().set(1, "Unlocked"); configuredLevel.catacombs.get(8).getLockedDoors().set(4, "Unlocked"); }, () -> { configuredLevel.catacombs.get(11).getLockedDoors().set(1, "DoubleLocked"); configuredLevel.catacombs.get(8).getLockedDoors().set(4, "DoubleLocked"); }, configuredLevel));
                configuredLevel.levers.add(new Lever(new Vector2(-180, -687), () -> { configuredLevel.catacombs.get(9).getLockedDoors().set(3, "Unlocked"); configuredLevel.catacombs.get(8).getLockedDoors().set(0, "Unlocked"); }, () -> { configuredLevel.catacombs.get(9).getLockedDoors().set(3, "DoubleLocked"); configuredLevel.catacombs.get(8).getLockedDoors().set(0, "DoubleLocked"); }, configuredLevel));

                configuredLevel.doors = new Array<>();
                configuredLevel.doors.add(new Door(new Vector2(-90, -82), 1, configuredLevel));
                configuredLevel.doors.add(new Door(new Vector2(-240, -517), 0, configuredLevel));
                configuredLevel.doors.get(0).show = false;

                configuredLevel.signs = new Array<>();
                configuredLevel.signs.add(new Sign(new Vector2(-80, -602)));

                configuredLevel.wires = new Array<>();

                configuredLevel.prisoners = new Array<>();

                configuredLevel.bosses = new Array<>();

                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<>();
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 110, configuredLevel.getPlayer().getPosition().y + 20); configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.currentBubble = 1; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hey!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I wonder what\nthis sign says...", "Player:", 2, "Read Sign", "Don't read Sign", "", "",
                        () -> configuredLevel.currentBubble = 3,
                        () -> configuredLevel.currentBubble = 5,
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Heed this warning...", "Sign:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("One chest grants what you seek,\nwhile the others insure doom.", "Sign:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.touchLocked = false; configuredLevel.show = false; configuredLevel.continueBubbles = false; if (configuredLevel.guards.get(0).guardTouchesPlayer() &&
                                configuredLevel.touchPosition.x > (configuredLevel.guards.get(0).jumpState.equals(Enums.JumpState.GROUNDED) ? (configuredLevel.guards.get(0).position.x - Constants.HEAD_SIZE) - 12 : (configuredLevel.guards.get(0).position.x - Constants.HEAD_SIZE)) &&
                                configuredLevel.touchPosition.y > (configuredLevel.guards.get(0).position.y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f) &&
                                configuredLevel.touchPosition.x < (configuredLevel.guards.get(0).jumpState.equals(Enums.JumpState.GROUNDED) ? (Constants.PLAYER_WIDTH * 2f) + 12 : (Constants.PLAYER_WIDTH * 2f)) + (configuredLevel.guards.get(0).jumpState.equals(Enums.JumpState.GROUNDED) ? (configuredLevel.guards.get(0).position.x - Constants.HEAD_SIZE) - 12 : (configuredLevel.guards.get(0).position.x - Constants.HEAD_SIZE)) &&
                                configuredLevel.touchPosition.y < (Constants.PLAYER_HEIGHT * 2.5f) + (configuredLevel.guards.get(0).position.y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f) && configuredLevel.guards.get(0).guardItem.equals("key")) { configuredLevel.guards.get(0).guardItem = ""; configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key")); configuredLevel.currentBubble = 6; }
                        }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Huh?", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.guards.get(0).asleep = false; configuredLevel.show = true; configuredLevel.continueBubbles = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Who goes there?!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.guards.get(0).alert = true; configuredLevel.guards.get(0).facing = Enums.Facing.RIGHT;
                            //initialize eyes
                            configuredLevel.guards.get(0).eyeLookLeft = new Vector2(-Constants.EYE_OFFSET + 2, Constants.EYE_OFFSET);
                            configuredLevel.guards.get(0).eyeLookRight = new Vector2(Constants.EYE_OFFSET + 2, Constants.EYE_OFFSET);
                            //initialize mouth
                            configuredLevel.guards.get(0).mouthOffset = new Vector2(0.6f, 0f);
                        }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Stop, prisoner!!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.continueBubbles = false; configuredLevel.guards.get(0).guardState = Enums.GuardState.ATTACK; if (configuredLevel.currentCatacomb == 10) { configuredLevel.currentBubble = 9; } if (configuredLevel.guards.get(0).guardTouchesPlayer()) { configuredLevel.defeat = true; configuredLevel.verdict.verdict = false; configuredLevel.verdict.verdictPhrase = "You were caught by a Guard!"; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.continueBubbles = false; configuredLevel.catacombs.get(10).getLockedDoors().set(1, "Locked"); configuredLevel.catacombs.get(9).getLockedDoors().set(4, "Locked"); }, configuredLevel, -1));
                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(-50, 65));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(-50, -942), new Vector2(-230, -859), configuredLevel);
                configuredLevel.exitDoor.show = false;
                break;
            case 10:
                configuredLevel.show = false;
                configuredLevel.continueBubbles = false;
                configuredLevel.touchLocked = false;
                /*LEVEL CONFIGURATIONS:*/
                //player always starts inside the currentCatacomb
                configuredLevel.catacombs = new Array<>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-300, -15), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -100), "Closed", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -15), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-300, -185), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));

                configuredLevel.catacombs.add(new Catacomb(new Vector2(-300, -525), "Closed", "Closed", "Closed", "Unlocked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -440), "Unlocked", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -525), "Closed", "Locked", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(150, -440), "Locked", "DoubleLocked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -355), "Closed", "Locked", "Closed", "Locked", "DoubleLocked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -270), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(150, -270), "Locked", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(400, -185), "Locked", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 0;

                configuredLevel.items = new DelayedRemovalArray<>();
                configuredLevel.items.add(new Item(new Vector2(-240, 15), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(-190, -155), configuredLevel.viewportPosition, "emerald"));
                configuredLevel.items.add(new Item(new Vector2(70, 15), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(-70, -410), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(120, -495), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(370, -410), configuredLevel.viewportPosition, "diamond"));

                configuredLevel.puzzles = new Array<>();
                configuredLevel.puzzles.add(new Puzzle(new Vector2(60, -275), new Vector2(-90, -360), "shapes4", new Enums.Shape[]{Enums.Shape.CIRCLE, Enums.Shape.SQUARE, Enums.Shape.TRIANGLE, Enums.Shape.CIRCLE}, () -> configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "ghost")), configuredLevel));

                configuredLevel.words = new Array<>();
                configuredLevel.words.add(new Word(new String[]{"G", "O", "L", "D"}, new Vector2[]{new Vector2(-70, -30), new Vector2(-90, -30), new Vector2(1000, 1000), new Vector2(1000, 1000)}, configuredLevel));
                configuredLevel.words.add(new Word(new String[]{"K", "E", "Y"}, new Vector2[]{new Vector2(280, -190), new Vector2(220, -190), new Vector2(250, -190)}, configuredLevel));

                configuredLevel.chests = new Array<>();
                configuredLevel.chests.add(new Chest(new Vector2(-195, 3), "gold", configuredLevel, () -> { configuredLevel.inventory.deleteCurrentItem();  configuredLevel.catacombs.get(0).getLockedDoors().set(5, "Unlocked"); configuredLevel.catacombs.get(3).getLockedDoors().set(2, "Unlocked"); configuredLevel.shake = true; }));
                configuredLevel.chests.add(new Chest(new Vector2(-42, -82), "", configuredLevel, () -> { configuredLevel.words.get(0).letterPositions[2].set(new Vector2(-90, -50)); configuredLevel.words.get(0).letterPositions[3].set(new Vector2(-70, -50)); }));
                configuredLevel.chests.add(new Chest(new Vector2(100, 3), "", configuredLevel, () -> configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0)))));
                configuredLevel.chests.add(new Chest(new Vector2(-195, -507), "emerald", configuredLevel, () -> { configuredLevel.inventory.deleteCurrentItem(); configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "invisibility")); }));
                configuredLevel.chests.add(new Chest(new Vector2(-95, -252), "", configuredLevel, () -> configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "gold"))));
                configuredLevel.chests.add(new Chest(new Vector2(-50, -252), "gold", configuredLevel, () -> { configuredLevel.inventory.deleteCurrentItem(); configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0))); }));

                configuredLevel.guards = new Array<>();
                configuredLevel.guards.add(new Guard(new Vector2(70, -465), configuredLevel));
                configuredLevel.guards.get(0).guardItem = "phone";
                configuredLevel.guards.add(new Guard(new Vector2(220, -380), configuredLevel));
                configuredLevel.guards.get(1).guardItem = "key";
                configuredLevel.guards.add(new Guard(new Vector2(280, -380), configuredLevel));
                configuredLevel.guards.get(2).guardItem = "doubleKey";

                configuredLevel.levers = new Array<>();
                configuredLevel.levers.add(new Lever(new Vector2(-80, -82), () -> { }, () -> { }, configuredLevel));
                configuredLevel.levers.add(new Lever(new Vector2(-53, -82), () -> { }, () -> { }, configuredLevel));
                configuredLevel.levers.add(new Lever(new Vector2(280, -252), () -> configuredLevel.exitDoor.setButtonPosition(new Vector2(210, -254)), () -> configuredLevel.exitDoor.setButtonPosition(new Vector2(1000, 1000)), configuredLevel));

                configuredLevel.doors = new Array<>();
                configuredLevel.doors.add(new Door(new Vector2(-240, -167), 1, configuredLevel));
                configuredLevel.doors.add(new Door(new Vector2(-240, -507), 0, configuredLevel));
                configuredLevel.doors.add(new Door(new Vector2(350, -252), 3, configuredLevel));
                configuredLevel.doors.add(new Door(new Vector2(510, -167), 2, configuredLevel));

                configuredLevel.signs = new Array<>();
                configuredLevel.signs.add(new Sign(new Vector2(-30, -422)));

                configuredLevel.wires = new Array<>();

                configuredLevel.prisoners = new Array<>();

                configuredLevel.bosses = new Array<>();

                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<>();
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 70, configuredLevel.getPlayer().getPosition().y + 20); configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.currentBubble = 1; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("...", "Player:", 2, "Read Sign", "Don't read Sign", "", "",
                        () -> configuredLevel.currentBubble = 2,
                        () -> configuredLevel.currentBubble = 3,
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Sentry group stationed ahead.", "Sign:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.touchLocked = false; configuredLevel.show = false; configuredLevel.continueBubbles = false; if (configuredLevel.guards.get(0).talkToPlayer) { configuredLevel.currentBubble = 4; } if (configuredLevel.guards.get(1).talkToPlayer) { configuredLevel.currentBubble = 7; } if (configuredLevel.guards.get(2).talkToPlayer) { configuredLevel.currentBubble = 10; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble(".....", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.continueBubbles = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("The escapee is here!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.alarm = true; configuredLevel.touchLocked = false; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.touchLocked = false; configuredLevel.continueBubbles = false; configuredLevel.guards.get(0).guardState = Enums.GuardState.ATTACK;
                            configuredLevel.guards.get(0).alert = false;
                            configuredLevel.guards.get(0).chasePlayer = true;
                            configuredLevel.guards.get(0).talkToPlayer = false;
                            if ((configuredLevel.guards.get(0).guardTouchesPlayer() || configuredLevel.guards.get(1).guardTouchesPlayer() || configuredLevel.guards.get(2).guardTouchesPlayer()) && !configuredLevel.getPlayer().invisibility) { configuredLevel.pressUp = false; configuredLevel.verdict.verdict = false; configuredLevel.defeat = true; configuredLevel.verdict.verdictPhrase = "You were captured by a Guard!"; }
                        }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Huh?", "Guard 2:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.continueBubbles = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I see the prisoner!!", "Guard 2:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.alarm = true; configuredLevel.touchLocked = false; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.touchLocked = false; configuredLevel.continueBubbles = false; configuredLevel.guards.get(1).guardState = Enums.GuardState.ATTACK;
                            configuredLevel.guards.get(1).alert = false;
                            configuredLevel.guards.get(1).chasePlayer = true;
                            configuredLevel.guards.get(1).talkToPlayer = false;
                            if ((configuredLevel.guards.get(0).guardTouchesPlayer() || configuredLevel.guards.get(1).guardTouchesPlayer() || configuredLevel.guards.get(2).guardTouchesPlayer()) && !configuredLevel.getPlayer().invisibility) { configuredLevel.pressUp = false; configuredLevel.verdict.verdict = false; configuredLevel.defeat = true; configuredLevel.verdict.verdictPhrase = "You were captured by a Guard!"; }
                        }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hey!", "Guard 3:", 0, "", "", "", "",
                    () -> {},
                    () -> {},
                    () -> {},
                        () -> { configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.continueBubbles = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Halt, prisoner!!", "Guard 3:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.alarm = true; configuredLevel.touchLocked = false; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.touchLocked = false; configuredLevel.continueBubbles = false; configuredLevel.guards.get(2).guardState = Enums.GuardState.ATTACK;
                            configuredLevel.guards.get(2).alert = false;
                            configuredLevel.guards.get(2).chasePlayer = true;
                            configuredLevel.guards.get(2).talkToPlayer = false;
                            if ((configuredLevel.guards.get(0).guardTouchesPlayer() || configuredLevel.guards.get(1).guardTouchesPlayer() || configuredLevel.guards.get(2).guardTouchesPlayer()) && !configuredLevel.getPlayer().invisibility) { configuredLevel.pressUp = false; configuredLevel.verdict.verdict = false; configuredLevel.defeat = true; configuredLevel.verdict.verdictPhrase = "You were captured by a Guard!"; }
                        }, configuredLevel, -1));
               configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(-220, -20));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(460, -167), new Vector2(1000, 1000), configuredLevel);
                break;
            case 11:
                configuredLevel.show = false;
                configuredLevel.continueBubbles = false;
                configuredLevel.touchLocked = false;
                /*LEVEL CONFIGURATIONS:*/
                //player always starts inside the currentCatacomb
                configuredLevel.catacombs = new Array<>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -100), "DoubleLocked", "DoubleLocked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-300, -185), "Closed", "Closed", "Closed", "DoubleLocked", "DoubleLocked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -270), "Closed", "DoubleLocked", "Closed", "Closed", "Unlocked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -440), "Unlocked", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-300, -525), "Closed", "Closed", "Closed", "Unlocked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -355), "Closed", "Unlocked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -185), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-300, -15), "Closed", "Closed", "Closed", "Locked", "DoubleLocked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, 70), "Locked", "Closed", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(100, 155), "Locked", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, 240), "Locked", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-300, 155), "Closed", "Closed", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 0;

                configuredLevel.items = new DelayedRemovalArray<>();
                configuredLevel.items.add(new Item(new Vector2(1000, 1000), configuredLevel.viewportPosition, "diamond"));
                configuredLevel.items.add(new Item(new Vector2(170, 185), configuredLevel.viewportPosition, "sapphire"));
                configuredLevel.items.add(new Item(new Vector2(70, -325), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(110, -325), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(80, -135), configuredLevel.viewportPosition, "doubleKey"));
                configuredLevel.items.add(new Item(new Vector2(1000, 1000), configuredLevel.viewportPosition, "fire"));

                configuredLevel.puzzles = new Array<>();
                //updated whacker puzzle
                configuredLevel.puzzles.add(new Puzzle(new Vector2(-220, -140), new Vector2(1000, 1000), "whacker3", new Enums.Shape[]{}, () -> configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "doubleKey")), configuredLevel));
                //New puzzle: fire puzzle
                configuredLevel.puzzles.add(new Puzzle(new Vector2(-65, -422), new Vector2(1000, 1000), "fire1", new Enums.Shape[]{}, () -> {
                    configuredLevel.items.get(0).position.set(new Vector2(90, -310));
                    configuredLevel.exitDoor.setButtonPosition(new Vector2(-35, -424));
                    configuredLevel.catacombs.get(3).getLockedDoors().set(3, "Unlocked");
                    configuredLevel.catacombs.get(5).getLockedDoors().set(0, "Unlocked");
                }, configuredLevel));
                configuredLevel.puzzles.add(new Puzzle(new Vector2(-100, 340), new Vector2(1000, 1000), "shapes2", new Enums.Shape[]{Enums.Shape.SQUARE, Enums.Shape.CIRCLE}, () -> {
                    configuredLevel.words.get(0).letterPositions[2].set(new Vector2(-90, 280));
                    configuredLevel.words.get(1).letterPositions[0].set(new Vector2(-90, 320));
                }, configuredLevel));
                configuredLevel.puzzles.add(new Puzzle(new Vector2(-20, 340), new Vector2(1000, 1000), "shapes2", new Enums.Shape[]{Enums.Shape.TRIANGLE, Enums.Shape.CIRCLE}, () -> {
                    configuredLevel.words.get(0).letterPositions[1].set(new Vector2(-10, 280));
                    configuredLevel.words.get(1).letterPositions[2].set(new Vector2(-10, 320));
                }, configuredLevel));
                configuredLevel.puzzles.add(new Puzzle(new Vector2(60, 340), new Vector2(1000, 1000), "shapes2", new Enums.Shape[]{Enums.Shape.CIRCLE, Enums.Shape.CIRCLE}, () -> {
                    configuredLevel.words.get(0).letterPositions[0].set(new Vector2(70, 280));
                    configuredLevel.words.get(1).letterPositions[1].set(new Vector2(70, 320));
                }, configuredLevel));

                configuredLevel.words = new Array<>();
                configuredLevel.words.add(new Word(new String[]{"2", "K", "E", "Y", "S"}, new Vector2[]{new Vector2(-80, -40), new Vector2(-80, 20), new Vector2(0, -40), new Vector2(0, 20), new Vector2(0, -10)}, configuredLevel));
                configuredLevel.words.add(new Word(new String[]{"K", "E", "Y"}, new Vector2[]{new Vector2(1000, 1000), new Vector2(1000, 1000), new Vector2(1000, 1000)}, configuredLevel));
                configuredLevel.words.add(new Word(new String[]{"E", "X", "I", "T"}, new Vector2[]{new Vector2(1000, 1000), new Vector2(1000, 1000), new Vector2(1000, 1000), new Vector2(-230,  255)}, configuredLevel));

                configuredLevel.chests = new Array<>();
                configuredLevel.chests.add(new Chest(new Vector2(-40, -252), "", configuredLevel, () -> { configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "bomb")); configuredLevel.chests.get(0).fade = true; }));
                configuredLevel.chests.add(new Chest(new Vector2(-200, 173), "sapphire", configuredLevel, () -> { configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0))); configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0))); configuredLevel.inventory.deleteCurrentItem(); }));

                configuredLevel.guards = new Array<>();

                configuredLevel.levers = new Array<>();
                configuredLevel.levers.add(new Lever(new Vector2(-80, -252), () -> { if (!configuredLevel.catacombs.get(2).getLockedDoors().get(5).equals("Unlocked")) { configuredLevel.chests.get(0).fade = false; } }, () -> configuredLevel.chests.get(0).fade = true, configuredLevel));
                configuredLevel.levers.add(new Lever(new Vector2(-170, -507), () -> { if (configuredLevel.items.get(configuredLevel.items.size-1).itemType.equals("fire")) {configuredLevel.items.get(configuredLevel.items.size-1).position.set(new Vector2(-90, -410));} }, () -> { if (configuredLevel.items.get(configuredLevel.items.size-1).itemType.equals("fire")) {configuredLevel.items.get(configuredLevel.items.size-1).position.set(new Vector2(1000, 1000));} }, configuredLevel));
                configuredLevel.levers.add(new Lever(new Vector2(80, -167), () -> { configuredLevel.catacombs.get(6).getLockedDoors().set(1, "Unlocked"); configuredLevel.catacombs.get(0).getLockedDoors().set(4, "Unlocked"); /*configuredLevel.catacombs.get(0).getLockedDoors().set(1, "DoubleLocked"); configuredLevel.catacombs.get(7).getLockedDoors().set(4, "DoubleLocked");*/ }, () -> { configuredLevel.catacombs.get(6).getLockedDoors().set(1, "Closed"); configuredLevel.catacombs.get(0).getLockedDoors().set(4, "Closed"); }, configuredLevel));
                configuredLevel.levers.add(new Lever(new Vector2(-170, 3), () -> { configuredLevel.catacombs.get(7).getLockedDoors().set(3, "Unlocked"); configuredLevel.catacombs.get(8).getLockedDoors().set(0, "Unlocked"); }, () -> { configuredLevel.catacombs.get(7).getLockedDoors().set(3, "Locked"); configuredLevel.catacombs.get(8).getLockedDoors().set(0, "Locked"); }, configuredLevel));
                configuredLevel.levers.add(new Lever(new Vector2(-220, 3), () -> { configuredLevel.catacombs.get(7).getLockedDoors().set(4, "DoubleLocked"); configuredLevel.catacombs.get(0).getLockedDoors().set(1, "DoubleLocked"); }, () -> {  }, configuredLevel));
                configuredLevel.levers.add(new Lever(new Vector2(230, 173), () -> configuredLevel.wires.get(11).first = true, () -> configuredLevel.wires.get(11).first = false, configuredLevel));

                configuredLevel.doors = new Array<>();
                configuredLevel.doors.add(new Door(new Vector2(-240, -507), 1, configuredLevel));
                configuredLevel.doors.add(new Door(new Vector2(110, -167), 0, configuredLevel));

                configuredLevel.signs = new Array<>();
                //new puzzle: electricity
                configuredLevel.wires = new Array<>();
                configuredLevel.wires.add(new Electricity(new Vector2(-150, 170), new Vector2(-70, 170), 0, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(-70, 170), new Vector2(-70, 130), 0, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(-70, 130), new Vector2(0, 130), 10, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(0, 130), new Vector2(40, 130), 0, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(40, 130), new Vector2(40, 180), 0, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(40, 180), new Vector2(70, 180), 12, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(70, 180), new Vector2(90, 180), 0, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(90, 180), new Vector2(90, 170), 0, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(90, 170), new Vector2(150, 170), 0, () -> {configuredLevel.catacombs.get(8).getLockedDoors().set(3, "Unlocked"); configuredLevel.catacombs.get(8).getLockedDoors().set(0, "Unlocked"); configuredLevel.items.get(0).position.set(new Vector2(-70, 100)); }));
                configuredLevel.wires.get(0).first = true;
                configuredLevel.wires.add(new Electricity(new Vector2(103, 250), new Vector2(200, 250), 15, () -> {configuredLevel.catacombs.get(8).getLockedDoors().set(3, "Locked"); configuredLevel.catacombs.get(9).getLockedDoors().set(0, "Locked"); configuredLevel.catacombs.get(9).getLockedDoors().set(1, "Unlocked"); configuredLevel.catacombs.get(10).getLockedDoors().set(4, "Unlocked");}));
                configuredLevel.wires.add(new Electricity(new Vector2(200, 250), new Vector2(250, 250), 0, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(250, 250), new Vector2(250, 350), 0, () -> {}));

                configuredLevel.prisoners = new Array<>();

                configuredLevel.bosses = new Array<>();

                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<>();
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 50, configuredLevel.getPlayer().getPosition().y + 20); configuredLevel.touchLocked = true; configuredLevel.continueBubbles = false; configuredLevel.currentBubble = 1; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; if (configuredLevel.puzzles.get(0).solved) { configuredLevel.touchLocked = false; } if (configuredLevel.inventory.selectedItem != -1 && configuredLevel.inventory.inventoryItems.get(configuredLevel.inventory.selectedItem).itemType.equals("bomb")) { configuredLevel.continueBubbles = true; configuredLevel.currentBubble = 2; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Touch and drag the Bomb in your\ninventory to place it in your Catacomb!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.show = true, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.continueBubbles = false; }, configuredLevel, -1));
                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(-50, 65));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(0, 258), new Vector2(1000, 1000), configuredLevel);
                configuredLevel.exitDoor.show = false;
                break;
            case 12:
                configuredLevel.show = false;
                configuredLevel.continueBubbles = false;
                configuredLevel.touchLocked = false;
                /*LEVEL CONFIGURATIONS:*/
                //player always starts inside the currentCatacomb
                configuredLevel.catacombs = new Array<>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-550, 70), "Closed", "Closed", "Closed", "Locked", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-400, 155), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-400, -15), "Closed", "Locked", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-250, -100), "Locked", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-500, -185), "Closed", "Closed", "Closed", "Locked", "DoubleLocked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-250, -270), "Closed", "DoubleLocked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-400, -355), "Locked", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-550, -440), "Closed", "Closed", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-550, -610), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-250, -440), "Closed", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));

                configuredLevel.catacombs.add(new Catacomb(new Vector2(-250, 70), "Closed", "Closed", "Closed", "Unlocked", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-100, 155), "Unlocked", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-100, -15), "Closed", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 0;

                configuredLevel.items = new DelayedRemovalArray<>();
                //new: shock potion
                configuredLevel.items.add(new Item(new Vector2(-320, 15), configuredLevel.viewportPosition, "shock"));
                configuredLevel.items.add(new Item(new Vector2(-170, -70), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(-430, -155), configuredLevel.viewportPosition, "doubleKey"));
                configuredLevel.items.add(new Item(new Vector2(-320, -325), configuredLevel.viewportPosition, "ghost"));
                configuredLevel.items.add(new Item(new Vector2(-280, -325), configuredLevel.viewportPosition, "fire"));
                configuredLevel.items.add(new Item(new Vector2(-450, -410), configuredLevel.viewportPosition, "diamond"));
                configuredLevel.items.add(new Item(new Vector2(-420, -410), configuredLevel.viewportPosition, "diamond"));
                configuredLevel.items.add(new Item(new Vector2(-120, -410), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(100, 185), configuredLevel.viewportPosition, "key"));

                configuredLevel.puzzles = new Array<>();
                configuredLevel.puzzles.add(new Puzzle(new Vector2(-320, 200), new Vector2(1000, 1000), "whacker2", new Enums.Shape[]{}, () -> {
                    configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                    configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0)));
                }, configuredLevel));
                configuredLevel.puzzles.add(new Puzzle(new Vector2(-170, -422), new Vector2(1000, 1000), "fire1", new Enums.Shape[]{}, () -> {
                    configuredLevel.words.get(0).letterPositions[2].set(new Vector2(-120, -310));
                    configuredLevel.words.get(0).letterPositions[3].set(new Vector2(-160, -310));
                }, configuredLevel));

                configuredLevel.words = new Array<>();
                configuredLevel.words.add(new Word(new String[]{"D", "O", "O", "R"}, new Vector2[]{new Vector2(-80, -310), new Vector2(-40, -310), new Vector2(1000, 1000), new Vector2(1000, 1000)}, configuredLevel));

                configuredLevel.chests = new Array<>();
                configuredLevel.chests.add(new Chest(new Vector2(-460, 88), "", configuredLevel, () -> { configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key")); configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key")); }));

                configuredLevel.guards = new Array<>();
                configuredLevel.guards.add(new Guard(new Vector2(-180, -40), configuredLevel));
                configuredLevel.guards.add(new Guard(new Vector2(-330, -125), configuredLevel));
                configuredLevel.guards.get(1).guardItem = "dagger";
                configuredLevel.guards.add(new Guard(new Vector2(-280, -125), configuredLevel));
                configuredLevel.guards.add(new Guard(new Vector2(-150, -82), configuredLevel));
                configuredLevel.guards.add(new Guard(new Vector2(-480, -380), configuredLevel));
                configuredLevel.guards.get(4).guardItem = "phone";

                configuredLevel.levers = new Array<>();

                configuredLevel.doors = new Array<>();
                configuredLevel.doors.add(new Door(new Vector2(-70, -422), 1,  configuredLevel));
                configuredLevel.doors.add(new Door(new Vector2(-180, 88), 0,  configuredLevel));
                configuredLevel.doors.get(0).show = false;

                configuredLevel.signs = new Array<>();
                configuredLevel.signs.add(new Sign(new Vector2(-280, 3)));

                configuredLevel.wires = new Array<>();

                configuredLevel.prisoners = new Array<>();
                configuredLevel.prisoners.add(new Prisoner(new Vector2(-50, -82),  configuredLevel));
                configuredLevel.prisoners.get(0).caged = true;

                configuredLevel.bosses = new Array<>();

                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<>();
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 60, configuredLevel.getPlayer().getPosition().y + 20); configuredLevel.touchLocked = true; configuredLevel.continueBubbles = false; configuredLevel.currentBubble = 1; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; if (configuredLevel.puzzles.get(0).solved) { configuredLevel.touchLocked = false; } if(configuredLevel.signs.get(0).touchesSign(configuredLevel.getPlayer().getPosition())) { configuredLevel.currentBubble = 2; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 80, configuredLevel.getPlayer().getPosition().y + 20); configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.currentBubble = 3; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("...", "Player:", 2, "Read Sign", "Don't read Sign", "", "",
                        () -> configuredLevel.currentBubble = 4,
                        () -> configuredLevel.currentBubble = 5,
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Prisoner Holding Cell ahead.", "Sign:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.touchLocked = false; configuredLevel.show = false; configuredLevel.continueBubbles = false; if (configuredLevel.guards.get(0).talkToPlayer) { configuredLevel.currentBubble = 6; } if (configuredLevel.guards.get(1).talkToPlayer) { configuredLevel.currentBubble = 8; } if (configuredLevel.guards.get(2).talkToPlayer) { configuredLevel.currentBubble = 10; } if (configuredLevel.currentCatacomb == 5) { configuredLevel.currentBubble = 12; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("There's the Escapee!\nCapture him!!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = true; configuredLevel.touchLocked = true; configuredLevel.continueBubbles = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.touchLocked = false; configuredLevel.continueBubbles = false; if (configuredLevel.guards.get(0).guardState != Enums.GuardState.DEFEATED) {configuredLevel.guards.get(0).guardState = Enums.GuardState.ATTACK; } if (configuredLevel.guards.get(1).talkToPlayer) { configuredLevel.currentBubble = 8; } if (configuredLevel.guards.get(2).talkToPlayer) { configuredLevel.currentBubble = 10; } if (configuredLevel.currentCatacomb == 5) { configuredLevel.currentBubble = 12; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Grab that prisoner!!", "Guard 2:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = true; configuredLevel.touchLocked = true; configuredLevel.continueBubbles = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.touchLocked = false; configuredLevel.continueBubbles = false; if (configuredLevel.guards.get(1).guardState != Enums.GuardState.DEFEATED) {configuredLevel.guards.get(1).guardState = Enums.GuardState.ATTACK; } if (configuredLevel.currentCatacomb == 5) { configuredLevel.currentBubble = 12; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Huh? Stop him!!", "Guard 3:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = true; configuredLevel.touchLocked = true; configuredLevel.continueBubbles = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.touchLocked = false; configuredLevel.continueBubbles = false; if (configuredLevel.guards.get(2).guardState != Enums.GuardState.DEFEATED) {configuredLevel.guards.get(2).guardState = Enums.GuardState.ATTACK; } if (configuredLevel.currentCatacomb == 5) { configuredLevel.currentBubble = 12; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble(".....", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.getPlayer().potionTimer = 0; configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 60, configuredLevel.getPlayer().getPosition().y + 20); configuredLevel.touchLocked = true; configuredLevel.continueBubbles = true; configuredLevel.show = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Another Guard!\nWhen will this end?!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Actually...\nThis ends NOW!", "Guard 4:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.guards.get(3).guardItem = "dagger"; configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Ack!!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { for (int i = 0; i < configuredLevel.inventory.inventoryItems.size; i++) { if (configuredLevel.inventory.inventoryItems.get(i).itemType.equals("dagger")) { configuredLevel.inventory.selectedItem = i; } } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Tap on the Guard to fight!!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.continueBubbles = false;
                                configuredLevel.guards.get(3).guardItem = "dagger";
                                if (configuredLevel.guards.get(3).position.x >= configuredLevel.getPlayer().getPosition().x - 30 && configuredLevel.guards.get(3).position.x <= configuredLevel.getPlayer().getPosition().x + 30) {
                                    configuredLevel.guards.get(3).guardState = Enums.GuardState.FIGHT;
                                    configuredLevel.getPlayer().fighting = true;
                                } else {
                                    configuredLevel.guards.get(3).guardState = Enums.GuardState.ATTACK;
                                }
                                if (configuredLevel.guards.get(3).guardEnergy > 20) {
                                    configuredLevel.player.mouthState = Enums.MouthState.OPEN;
                                } else {
                                    configuredLevel.player.mouthState = Enums.MouthState.NORMAL;
                                }
                                if (configuredLevel.guards.get(3).guardEnergy >= 30) {
                                    if (configuredLevel.player.armRotate < 50) {
                                        configuredLevel.player.armRotate += 4f;
                                    } else {
                                        configuredLevel.show = true;
                                        configuredLevel.currentBubble = 17;
                                    }
                                }
                                //player lose
                                if (configuredLevel.guards.get(3).guardEnergy >= 30) {
                                    if (configuredLevel.player.armRotate < 60) {
                                        configuredLevel.player.armRotate += 4f;
                                    } else {
                                        configuredLevel.show = true;
                                        configuredLevel.guards.get(3).mouthState = Enums.MouthState.OPEN;
                                        configuredLevel.currentBubble = 17;
                                    }
                                }
                                //player win
                                if (configuredLevel.guards.get(3).guardEnergy <= 1) {
                                    if (configuredLevel.guards.get(3).armRotate < 60) {
                                        configuredLevel.guards.get(3).armRotate += 4f;
                                    } else {
                                        configuredLevel.show = true;
                                        configuredLevel.continueBubbles = true;
                                        configuredLevel.currentBubble = 18;
                                    }
                                }
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Ha!    \nToo easy!!", "Guard 4:", 1, "No!", "", "", "",
                        () -> {
                                configuredLevel.defeat = true;
                                configuredLevel.verdict.verdict = false;
                                configuredLevel.verdict.verdictPhrase = "You were defeated by a guard!";
                            },
                        () -> {},
                        () -> {},
                        () -> {
                                configuredLevel.getPlayer().heldItem.itemType = "";
                                configuredLevel.inventory.selectedItem = -1;
                            }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("But, how?!", "Guard 4:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.guards.get(3).mouthState = Enums.MouthState.OPEN, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("When will you ever learn?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I am not a criminal!\nI just want to escape!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now,          \nHand over your keys.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("...", "Guard 4:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { if (configuredLevel.inventory.inventoryItems.size < 3) {configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key")); } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("And...", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Fine.", "Guard 4:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { if (configuredLevel.inventory.inventoryItems.size < 4) {configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key")); } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Don't go anywhere.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I wonder what's in\nthat cage over there.", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.touchLocked = false; configuredLevel.continueBubbles = false; if (configuredLevel.prisoners.get(0).cageUnlocked) { configuredLevel.catacombs.get(5).getLockedDoors().set(0, "Locked"); configuredLevel.catacombs.get(6).getLockedDoors().set(3, "Locked"); configuredLevel.currentBubble = 28; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble(".....", "Prisoner 48H:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 160, configuredLevel.getPlayer().getPosition().y + 20); configuredLevel.touchLocked = true; configuredLevel.continueBubbles = true; configuredLevel.show = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Who...\nWho are you?", "Prisoner 48H:", 4, "A friend", "Who are you?", "I'm freeing you!", "Your worst nightmare!",
                        () -> configuredLevel.currentBubble = 53,
                        () -> configuredLevel.currentBubble = 35,
                        () -> configuredLevel.currentBubble = 53,
                        () -> configuredLevel.currentBubble = 30, configuredLevel, -1));
                //#29
                configuredLevel.speechBubbles.add(new SpeechBubble("You're a CHICKEN!!!", "Prisoner 48H:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.prisoners.get(0).mouthState = Enums.MouthState.OPEN, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("What?\nOf course not.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("But...           \nChickens are my worst nightmare.", "Prisoner 48H:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Oh.\nForget that I said that.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("So, who are you really?", "Prisoner 48H:", 3, "A friend", "Who are you?", "I'm freeing you!", "",
                        () -> configuredLevel.currentBubble = 53,
                        () -> configuredLevel.currentBubble = 35,
                        () -> configuredLevel.currentBubble = 53,
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I asked you first.", "Prisoner 48H:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 52));
                configuredLevel.speechBubbles.add(new SpeechBubble("Yeah, but I just freed you.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 52));
                configuredLevel.speechBubbles.add(new SpeechBubble("Okay.\nFine.", "Prisoner 48H:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 52));
                configuredLevel.speechBubbles.add(new SpeechBubble("All the guards refer\nto me as: Prisoner 48H.", "Prisoner 48H:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 52));
                configuredLevel.speechBubbles.add(new SpeechBubble("Other than that,\nI don't remember anything.", "Prisoner 48H:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 52));
                configuredLevel.speechBubbles.add(new SpeechBubble("I have no idea as to\nwhy I'm being imprisoned here.", "Prisoner 48H:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 52));
                configuredLevel.speechBubbles.add(new SpeechBubble("Me too.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 52));
                configuredLevel.speechBubbles.add(new SpeechBubble("Although you don't have\nan earpiece like I do!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 52));
                configuredLevel.speechBubbles.add(new SpeechBubble("c7-x, do you know why?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 52));
                configuredLevel.speechBubbles.add(new SpeechBubble("Sqrry....MalfuNcion....\n...cAnt.talk..nOw...", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 52));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hmm, weird.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 52));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.show = false, configuredLevel, 52));
                configuredLevel.speechBubbles.add(new SpeechBubble("Why did you rescue me?", "Prisoner 48H:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.show = true, configuredLevel, 52));
                configuredLevel.speechBubbles.add(new SpeechBubble("I thought that you could\ngive me some information.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 52));
                configuredLevel.speechBubbles.add(new SpeechBubble("Well, I know how the\nguards get to the surface.", "Prisoner 48H:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 52));
                configuredLevel.speechBubbles.add(new SpeechBubble("They use a huge elevator\nwhich travels up really far!", "Prisoner 48H:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 52));
                configuredLevel.speechBubbles.add(new SpeechBubble("You should try to find that!", "Prisoner 48H:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.touchLocked = false; configuredLevel.continueBubbles = false; if (configuredLevel.currentCatacomb == 7 && !configuredLevel.getPlayer().invisibility) { configuredLevel.currentBubble = 57; } if (configuredLevel.currentCatacomb == 11) { configuredLevel.currentBubble = 61; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("How can I trust you?", "Prisoner 48H:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I am trapped here,\njust like you.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Also, who are you?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.currentBubble = 38, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Huh?     \nThere he is!!", "Guard 5:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = true; configuredLevel.touchLocked = true; configuredLevel.continueBubbles = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Sound the Alarm!", "Guard 5:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.alarm = true, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Oops! I pressed\nthe trapdoor button!", "Guard 5:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN; configuredLevel.show = false; configuredLevel.touchLocked = false; configuredLevel.continueBubbles = false; configuredLevel.shake = true; configuredLevel.catacombs.get(7).getLockedDoors().set(5, "Unlocked"); configuredLevel.catacombs.get(8).getLockedDoors().set(2, "Unlocked"); }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Oh no... Not again!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = true; configuredLevel.touchLocked = true; configuredLevel.continueBubbles = true; configuredLevel.shake = true; configuredLevel.getPlayer().health = 20; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.touchLocked = false; configuredLevel.continueBubbles = false; if (configuredLevel.getPlayer().health <= 0) { configuredLevel.verdict.verdict = false; configuredLevel.defeat = true; configuredLevel.verdict.verdictPhrase = "You were hit by a spike!"; } configuredLevel.catacombs.get(11).drop = true; configuredLevel.getPlayer().danger = true; if (configuredLevel.currentCatacomb != 11 && configuredLevel.exitDoor.unlocked) { configuredLevel.currentBubble = 63; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.shake = false; configuredLevel.catacombs.get(11).drop = false; configuredLevel.getPlayer().danger = false; if (configuredLevel.currentCatacomb != 11 && configuredLevel.exitDoor.unlocked) { configuredLevel.currentBubble = 63; } }, configuredLevel, -1));

                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(-480, 235));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(0, 3), new Vector2(100, 171), configuredLevel);
                break;
            case 13:
                configuredLevel.show = false;
                configuredLevel.continueBubbles = false;
                configuredLevel.touchLocked = false;
                /*LEVEL CONFIGURATIONS:*/
                //player always starts inside the currentCatacomb
                configuredLevel.catacombs = new Array<>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-550, -440), "Closed", "Closed", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-550, -610), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-400, -355), "Locked", "Closed", "Closed", "DoubleLocked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-550, -270), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -440), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -270), "DoubleLocked", "DoubleLocked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-300, -185), "Closed", "Closed", "Closed", "Unlocked", "DoubleLocked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -100), "Unlocked", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-400, -15), "Closed", "Closed", "Closed", "Unlocked", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, 70), "Unlocked", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-400, 155), "Closed", "Locked", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-550, 240), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-550, 70), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 0;

                configuredLevel.items = new DelayedRemovalArray<>();
                configuredLevel.items.add(new Item(new Vector2(-80, -410), configuredLevel.viewportPosition, "gold"));
                configuredLevel.items.add(new Item(new Vector2(-80, -380), configuredLevel.viewportPosition, "doubleKey"));
                configuredLevel.items.add(new Item(new Vector2(-180, -155), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(-430, -240), configuredLevel.viewportPosition, "diamond"));
                configuredLevel.items.add(new Item(new Vector2(-60, 100), configuredLevel.viewportPosition, "bomb"));

                configuredLevel.puzzles = new Array<>();
                configuredLevel.puzzles.add(new Puzzle(new Vector2(0, -150), new Vector2(-450, -150), "shapes3", new Enums.Shape[]{Enums.Shape.CIRCLE, Enums.Shape.SQUARE, Enums.Shape.TRIANGLE}, () -> configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0))), configuredLevel));

                configuredLevel.words = new Array<>();
                configuredLevel.words.add(new Word(new String[]{"2", "K", "E", "Y", "S"}, new Vector2[]{new Vector2(-420, -170), new Vector2(-380, -170), new Vector2(-400, -200), new Vector2(-300, -225), new Vector2(-250, -225)}, configuredLevel));

                configuredLevel.chests = new Array<>();
                configuredLevel.chests.add(new Chest(new Vector2(-450, -592), "", configuredLevel, () -> { configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key")); configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0))); }));
                configuredLevel.chests.add(new Chest(new Vector2(-240, -167), "", configuredLevel, () -> configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "disguise"))));

                configuredLevel.guards = new Array<>();
                configuredLevel.guards.add(new Guard(new Vector2(-20, -40), configuredLevel));
                configuredLevel.guards.get(0).asleep = true;
                configuredLevel.guards.add(new Guard(new Vector2(-280, 45), configuredLevel));
                configuredLevel.guards.add(new Guard(new Vector2(0, 130), configuredLevel));
                configuredLevel.guards.add(new Guard(new Vector2(50, 130), configuredLevel));
                configuredLevel.guards.get(3).guardItem = "key";
                configuredLevel.guards.add(new Guard(new Vector2(-480, 130), configuredLevel));
                configuredLevel.guards.add(new Guard(new Vector2(-420, 130), configuredLevel));
                configuredLevel.guards.get(4).guardItem = "stungun";
                configuredLevel.guards.get(5).guardItem = "stungun";

                configuredLevel.levers = new Array<>();
                configuredLevel.levers.add(new Lever(new Vector2(-430, -422), () -> { configuredLevel.catacombs.get(0).getLockedDoors().set(3, "Unlocked"); configuredLevel.catacombs.get(1).getLockedDoors().set(0, "Unlocked"); }, () -> { configuredLevel.catacombs.get(0).getLockedDoors().set(3, "Locked"); configuredLevel.catacombs.get(1).getLockedDoors().set(0, "Locked"); }, configuredLevel));
                configuredLevel.levers.add(new Lever(new Vector2(-190, 173), () -> configuredLevel.wires.get(15).first = true, () -> configuredLevel.wires.get(15).first = false, configuredLevel));

                configuredLevel.doors = new Array<>();
                configuredLevel.doors.add(new Door(new Vector2(-330, -337), 1,  configuredLevel));
                configuredLevel.doors.add(new Door(new Vector2(-480, -252), 0,  configuredLevel));
                configuredLevel.doors.get(0).unlocked = true;
                configuredLevel.doors.get(1).unlocked = true;
                configuredLevel.doors.add(new Door(new Vector2(-280, -337), 3,  configuredLevel));
                configuredLevel.doors.add(new Door(new Vector2(-500, -592), 2,  configuredLevel));
                configuredLevel.doors.get(2).unlocked = true;
                configuredLevel.doors.add(new Door(new Vector2(-230, -337), 5,  configuredLevel));
                configuredLevel.doors.add(new Door(new Vector2(-50, -422), 4,  configuredLevel));
                configuredLevel.doors.get(4).unlocked = true;
                configuredLevel.doors.get(5).unlocked = true;

                configuredLevel.signs = new Array<>();
                configuredLevel.signs.add(new Sign(new Vector2(-330, 173)));

                configuredLevel.wires = new Array<>();
                configuredLevel.wires.add(new Electricity(new Vector2(-145, -170), new Vector2(-125, -170), 0, () -> {configuredLevel.catacombs.get(2).getLockedDoors().set(3, "Locked"); configuredLevel.catacombs.get(5).getLockedDoors().set(0, "Locked");}));
                configuredLevel.wires.add(new Electricity(new Vector2(-125, -170), new Vector2(-125, -180), 0, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(-125, -180), new Vector2(-115, -180), 0, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(-115, -180), new Vector2(-115, -170), 0, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(-115, -170), new Vector2(-75, -170), 20, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(-75, -170), new Vector2(-15, -170), 0, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(-15, -170), new Vector2(-15, -210), 0, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(-15, -210), new Vector2(15, -210), 10, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(15, -210), new Vector2(35, -210), 0, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(35, -210), new Vector2(55, -255), 0, () -> {}));
                configuredLevel.wires.get(9).first = true;

                configuredLevel.wires.add(new Electricity(new Vector2(-400, 255), new Vector2(-300, 255), 0, () -> {configuredLevel.catacombs.get(11).getLockedDoors().set(4, "Unlocked"); configuredLevel.catacombs.get(10).getLockedDoors().set(1, "Unlocked");}));
                configuredLevel.wires.add(new Electricity(new Vector2(-300, 255), new Vector2(-300, 220), 0, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(-300, 220), new Vector2(-255, 220), 15, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(-255, 220), new Vector2(-200, 220), 0, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(-200, 220), new Vector2(-200, 255), 0, () -> {}));
                configuredLevel.wires.add(new Electricity(new Vector2(-200, 255), new Vector2(-100, 255), 0, () -> {}));

                configuredLevel.prisoners = new Array<>();
                configuredLevel.prisoners.add(new Prisoner(new Vector2(-315, 45),  configuredLevel));
                configuredLevel.prisoners.get(0).mindWipe = true;

                configuredLevel.bosses = new Array<>();

                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<>();
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.touchLocked = true; configuredLevel.currentBubble = 1; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Look, you found some Guard clothes!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.continueBubbles = true; configuredLevel.show = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Maybe the Guards won't\nnotice me if I wear these!", "Player:", 2, "Wear Disguise", "Don't wear Disguise", "", "",
                        () -> { configuredLevel.continueBubbles = false; configuredLevel.show = false; configuredLevel.torchFade = true; },
                        () -> configuredLevel.currentBubble = 3,
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Nah, who needs disguises!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.continueBubbles = false; configuredLevel.touchLocked = false; configuredLevel.show = false; if (configuredLevel.guards.get(0).guardTouchesPlayer()) { configuredLevel.currentBubble = 7; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("This disguise looks convincing!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.continueBubbles = true; configuredLevel.show = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.continueBubbles = false; configuredLevel.touchLocked = false; configuredLevel.show = false; if (configuredLevel.guards.get(0).guardTouchesPlayer()) { configuredLevel.currentBubble = 11; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Zzz...", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 90, configuredLevel.getPlayer().getPosition().y + 20); configuredLevel.touchLocked = true; configuredLevel.continueBubbles = true; configuredLevel.show = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Huh?\nAre you the...", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.guards.get(0).asleep = false, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Escapee!!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.guards.get(0).alert = true; configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.continueBubbles = false; configuredLevel.touchLocked = false; configuredLevel.guards.get(0).guardState = Enums.GuardState.ATTACK; if (configuredLevel.guards.get(0).guardTouchesPlayer()) { configuredLevel.verdict.verdict = false; configuredLevel.defeat = true; configuredLevel.verdict.verdictPhrase = "That did not go well..."; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Zzz...", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 90, configuredLevel.getPlayer().getPosition().y + 20); configuredLevel.touchLocked = true; configuredLevel.continueBubbles = true; configuredLevel.show = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Huh?    \nOh no!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.guards.get(0).asleep = false; configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Please don't tell the Boss\nthat I fell asleep again!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { if (configuredLevel.speechBubbles.get(configuredLevel.currentBubble).textLoaded) {configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN;} }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Uh, okay.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 25));
                configuredLevel.speechBubbles.add(new SpeechBubble("Thank you SO much!\nI thought I was a goner!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 25));
                configuredLevel.speechBubbles.add(new SpeechBubble("What do you mean?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 25));
                configuredLevel.speechBubbles.add(new SpeechBubble("Last time I slept on the job,\nthere were serious repercussions!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 25));
                configuredLevel.speechBubbles.add(new SpeechBubble("Your employer doesn't\nsound very nice!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN, configuredLevel, 25));
                configuredLevel.speechBubbles.add(new SpeechBubble("Shhhh!\nDon't speak that way!!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 25));
                configuredLevel.speechBubbles.add(new SpeechBubble("Don't forget that\nhe's your Boss too!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 25));
                configuredLevel.speechBubbles.add(new SpeechBubble("No, he's not.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 25));
                configuredLevel.speechBubbles.add(new SpeechBubble("What!!!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.guards.get(0).alert = true; configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN; }, configuredLevel, 25));
                configuredLevel.speechBubbles.add(new SpeechBubble("I meant,          \nOf course he's my Boss!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.guards.get(0).alert = false, configuredLevel, 25));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.show = false, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Who are you, anyway?", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.show = true, configuredLevel, -1));
                //26
                configuredLevel.speechBubbles.add(new SpeechBubble("Umm...", "Player:", 4, "I'm new to the Catacombs", "A Guard", "I am escaping!", "(Take Off Disguise)",
                        () -> configuredLevel.currentBubble = 33,
                        () -> configuredLevel.currentBubble = 31,
                        () -> configuredLevel.currentBubble = 27,
                        () -> { configuredLevel.getPlayer().disguise = false; configuredLevel.currentBubble = 8; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Ha ha.\nFunny joke.", "Guard 1:", 2, "I'm not a Guard!", "Yeah, I'm joking.", "", "",
                        () -> configuredLevel.currentBubble = 28,
                        () -> configuredLevel.currentBubble = 30,
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I said:\nI am escaping!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.getPlayer().disguise = false, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.currentBubble = 8, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Who are you really?", "Guard 1:", 3, "I'm new to the Catacombs", "A Guard", "(Take Off Disguise)", "",
                        () -> configuredLevel.currentBubble = 33,
                        () -> configuredLevel.currentBubble = 31,
                        () -> { configuredLevel.getPlayer().disguise = false; configuredLevel.currentBubble = 8; },
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("That's obvious!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("I'm new here.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("When did you arrive here?", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("Uh... Today!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("What's your Sentry Code?", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("The Sentry what?!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("You know, the ID code that\nCentral Security gives you!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("Oh yeah,\nIt was...", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("Guard 62X-J313", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("Let me check\nwith Central Security...", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.guards.get(0).guardItem = "phone", configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("Wait!        \nLet me ask you something!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("Fine.\nAsk away.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.guards.get(0).guardItem = "", configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("Why did you take this job?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("I have always wanted\nto uphold justice!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("This job lets me protect\nour country from criminals!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("Also, It had good pay\nand lots of benefits!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("One more question...", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("Where is the elevator\nthat travels to the surface?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("You must have used\nit to come down here!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("Well, I forgot.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("Just keep traveling up\nand you will find it.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("Okay, thanks!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("Do you happen to\nhave a key with you?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hmm, your story seems untrustworthy.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 55));
                configuredLevel.speechBubbles.add(new SpeechBubble("I don't think I\nshould give you a key.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Will you trade\na key for this?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { for (int i = 0; i < configuredLevel.inventory.inventoryItems.size; i++) { if (configuredLevel.inventory.inventoryItems.get(i).itemType.equals("gold")) { configuredLevel.inventory.selectedItem = i; } } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Where did you...\nOf course I'll trade!!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.guards.get(0).alert = true, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Here is the Gold.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { for (int i = 0; i < configuredLevel.inventory.inventoryItems.size; i++) { if (configuredLevel.inventory.inventoryItems.get(i).itemType.equals("gold")) { configuredLevel.inventory.selectedItem = i; } } configuredLevel.inventory.deleteCurrentItem(); }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Ha ha!\nI'm rich, I'm RICH!!!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.guards.get(0).guardItem = "gold", configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Don't forget the...", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Very well, here's a key.", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.guards.get(0).guardItem = "key", configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("...", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.guards.get(0).guardItem = ""; if (configuredLevel.inventory.inventoryItems.size < 1) { configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0,0), configuredLevel.viewportPosition, "key")); } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Thank you.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.guards.get(0).guardItem = "gold"; configuredLevel.continueBubbles = false; configuredLevel.touchLocked = false; configuredLevel.show = false; if (configuredLevel.currentCatacomb == 8) { configuredLevel.currentBubble = 65; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Help!", "Prisoner 12E:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.continueBubbles = true; configuredLevel.show = true; configuredLevel.touchLocked = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Be quiet, Prisoner!!", "Guard 2:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("What is going\non over here?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 160, configuredLevel.getPlayer().getPosition().y + 20); configuredLevel.show = false; configuredLevel.continueBubbles = false; if (configuredLevel.getPlayer().getPosition().x < configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 165) { configuredLevel.currentBubble = 69; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("...", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.continueBubbles = true; configuredLevel.show = true; configuredLevel.touchLocked = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Stay still Prisoner,\nI'm going to Mind-Wipe you!", "Guard 2:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Mind-Wipe!!!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Who said that?!", "Guard 2:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                            //initialize eyes
                            configuredLevel.guards.get(1).eyeLookLeft = new Vector2(-Constants.EYE_OFFSET + 2, Constants.EYE_OFFSET);
                            configuredLevel.guards.get(1).eyeLookRight = new Vector2(Constants.EYE_OFFSET + 2, Constants.EYE_OFFSET);
                            //initialize mouth
                            configuredLevel.guards.get(1).mouthOffset = new Vector2(0.6f, 0f);
                            configuredLevel.guards.get(1).facing = Enums.Facing.RIGHT;
                            configuredLevel.guards.get(1).alert = true;
                        }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hey! You're not\nsupposed to be here!!", "Guard 2:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 81));
                configuredLevel.speechBubbles.add(new SpeechBubble("The Mind-Wipe room is for\nauthorized personnel only!", "Guard 2:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 81));
                configuredLevel.speechBubbles.add(new SpeechBubble("Who are you, anyway?!", "Guard 2:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 81));
                configuredLevel.speechBubbles.add(new SpeechBubble("Uh, I'm new here.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 81));
                configuredLevel.speechBubbles.add(new SpeechBubble("Only certain Guards are allowed\nto know that we are...", "Guard 2:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 81));
                configuredLevel.speechBubbles.add(new SpeechBubble("Mind-Wiping criminals and\nusing them for evil!", "Guard 2:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 81));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.show = false, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Oops.          \nI said that out loud.", "Guard 2:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.show = true, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I guess I'll have\nto Mind-Wipe you!!", "Guard 2:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("No!!!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 190, configuredLevel.getPlayer().getPosition().y + 20), configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Run to your right!!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.touchLocked = false, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.guards.get(1).guardState = Enums.GuardState.ATTACK; configuredLevel.show = false; configuredLevel.continueBubbles = false; for (int i = 0; i < configuredLevel.inventory.inventoryItems.size; i++) { if (configuredLevel.inventory.inventoryItems.get(i).itemType.equals("bomb")) { configuredLevel.currentBubble = 85; } } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hey!\nWho are you?!", "Guard 3:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.guards.get(2).alert = true; configuredLevel.guards.get(3).alert = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.continueBubbles = false; if (configuredLevel.guards.get(3).guardTouchesPlayer() &&
                                configuredLevel.touchPosition.x > (configuredLevel.guards.get(3).jumpState.equals(Enums.JumpState.GROUNDED) ? (configuredLevel.guards.get(3).position.x - Constants.HEAD_SIZE) - 12 : (configuredLevel.guards.get(3).position.x - Constants.HEAD_SIZE)) &&
                                configuredLevel.touchPosition.y > (configuredLevel.guards.get(3).position.y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f) &&
                                configuredLevel.touchPosition.x < (configuredLevel.guards.get(3).jumpState.equals(Enums.JumpState.GROUNDED) ? (Constants.PLAYER_WIDTH * 2f) + 12 : (Constants.PLAYER_WIDTH * 2f)) + (configuredLevel.guards.get(3).jumpState.equals(Enums.JumpState.GROUNDED) ? (configuredLevel.guards.get(3).position.x - Constants.HEAD_SIZE) - 12 : (configuredLevel.guards.get(3).position.x - Constants.HEAD_SIZE)) &&
                                configuredLevel.touchPosition.y < (Constants.PLAYER_HEIGHT * 2.5f) + (configuredLevel.guards.get(3).position.y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f) && configuredLevel.guards.get(3).guardItem.equals("key")) { configuredLevel.guards.get(3).guardItem = ""; configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key")); }
                            if(configuredLevel.signs.get(0).touchesSign(configuredLevel.getPlayer().getPosition())) { configuredLevel.currentBubble = 89; }
                        }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I have the Escapee!", "Guard 3:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.getPlayer().disguise = false; configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.guards.get(2).alert = false; configuredLevel.guards.get(3).alert = false; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.continueBubbles = false; configuredLevel.verdict.verdict = false; configuredLevel.defeat = true; configuredLevel.verdict.verdictPhrase = "Try using that Bomb."; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 90, configuredLevel.getPlayer().getPosition().y + 20); configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.currentBubble = 90; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("...", "Player:", 2, "Read Sign", "Don't read Sign", "", "",
                        () -> configuredLevel.currentBubble = 91,
                        () -> configuredLevel.currentBubble = 92,
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Transportation Elevator Ahead.", "Sign:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.touchLocked = false; configuredLevel.show = false; configuredLevel.continueBubbles = false; if (configuredLevel.currentCatacomb == 11 && configuredLevel.getPlayer().jumpState == Enums.JumpState.GROUNDED) { configuredLevel.currentBubble = 93; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("There it is!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.getPlayer().resetLegs(); configuredLevel.viewportPosition.set(configuredLevel.getPlayer().getPosition().x, configuredLevel.getPlayer().getPosition().y + 20); configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.continueBubbles = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("The quickest path\nto the surface!!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.continueBubbles = false; configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 100, configuredLevel.getPlayer().getPosition().y + 20); if (configuredLevel.getPlayer().getPosition().x < configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 101) { configuredLevel.currentBubble = 96; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Stop right there, Player!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.getPlayer().resetLegs(); configuredLevel.getPlayer().alert = true; configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.viewportPosition.set(configuredLevel.getPlayer().getPosition().x, configuredLevel.getPlayer().getPosition().y + 20); }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Who said that?!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 106));
                configuredLevel.speechBubbles.add(new SpeechBubble("I, c7-x, said that.", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 106));
                configuredLevel.speechBubbles.add(new SpeechBubble("But, why?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 106));
                configuredLevel.speechBubbles.add(new SpeechBubble("You are so foolish!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 106));
                configuredLevel.speechBubbles.add(new SpeechBubble("I was never helping\nyou, Prisoner!!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 106));
                configuredLevel.speechBubbles.add(new SpeechBubble("I was leading you to Central\nSecurity this whole time!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 106));
                configuredLevel.speechBubbles.add(new SpeechBubble("I don't understand.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 106));
                configuredLevel.speechBubbles.add(new SpeechBubble("I am a robot!\nThis is in my programming!!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 106));
                configuredLevel.speechBubbles.add(new SpeechBubble("Open the Trap-Door!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Opening Trap-Door...", "Security:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN; configuredLevel.show = false; configuredLevel.continueBubbles = false; configuredLevel.shake = true; configuredLevel.catacombs.get(11).getLockedDoors().set(5, "Unlocked"); configuredLevel.catacombs.get(12).getLockedDoors().set(2, "Unlocked"); if (configuredLevel.currentCatacomb == 12 && configuredLevel.getPlayer().jumpState == Enums.JumpState.GROUNDED) { configuredLevel.currentBubble = 108; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Yikes!!!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN; configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.shake = false; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I...       I...     \nI still don't understand!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Very well,", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("I will reveal to you...\nThe truth of the Catacombs!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("The Catacombs is\nactually not a prison.", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("That story is a cover-up\nfor its real purposes!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("We take criminals and\ninstead of imprisoning them...", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("We Mind-Wipe them and use\nMind-Control to perform heists!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("Mind-Control!!!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("Yes, Mind-Control.", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("We have teams of scientists\ndeveloping it for us!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("Does this mean that I\nhad been a criminal?!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("Not exactly...", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("We also Mind-Wipe people who\ninterfere with our business.", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("What do you mean?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("The truth is that:", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("You were sent by your\ngovernment to spy on us.", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("They suspected that\nwe had evil intentions.", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("We found you observing us,\nand so we Mind-Wiped you.", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble(".....", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("That's why I don't\nremember anything!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("We placed you in an\nunderground chamber to test you,", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("To see if you\ncould get this far.", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("I was tracking your\nlocation this whole time!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("But...", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, 134));
                configuredLevel.speechBubbles.add(new SpeechBubble("Why are you telling\nme all of your plans?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Because you are going to\naid us in our plans!!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I will never help you!!!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("You will help us...\nWhen we control your mind!", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.continueBubbles = false;
                            configuredLevel.verdict.verdict = true;
                            configuredLevel.verdict.verdictPhrase = victoryPhrases.get(currentLevel);
                            configuredLevel.victory = true;
                        }, configuredLevel, -1));
                configuredLevel.currentBubble = 0;
                //-230, (235-510) + 255
                configuredLevel.player.position.set(new Vector2(-480, (235-510)));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(-480, 258), new Vector2(1000, 1000), configuredLevel);
                break;
            case 14:
                configuredLevel.show = true;
                configuredLevel.continueBubbles = true;
                configuredLevel.touchLocked = false;
                /*LEVEL CONFIGURATIONS:*/
                //player always starts inside the currentCatacomb
                configuredLevel.catacombs = new Array<>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -100), "Closed", "Closed", "Closed", "Locked", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(100, -15), "Locked", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -270), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(100, -185), "Closed", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 0;

                configuredLevel.items = new DelayedRemovalArray<>();
                configuredLevel.items.add(new Item(new Vector2(1000, 1000), configuredLevel.viewportPosition, "key"));

                configuredLevel.puzzles = new Array<>();

                configuredLevel.words = new Array<>();

                configuredLevel.chests = new Array<>();
                configuredLevel.chests.add(new Chest(new Vector2(190, -167), "", configuredLevel, () -> { configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key")); configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key")); configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0))); configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0))); configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0)));}));

                configuredLevel.guards = new Array<>();
                configuredLevel.guards.add(new Guard(new Vector2(70, 65), configuredLevel));

                configuredLevel.levers = new Array<>();

                configuredLevel.doors = new Array<>();

                configuredLevel.signs = new Array<>();

                configuredLevel.wires = new Array<>();

                configuredLevel.prisoners = new Array<>();

                configuredLevel.bosses = new Array<>(); //x: -80, y: 65
                configuredLevel.bosses.add(new Boss(new Vector2(-80, 65), configuredLevel));
                //configuredLevel.bosses.get(0).bossItem = "spear";
                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<>();
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.touchLocked = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hey Boss!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.show = true, configuredLevel, 6));
                configuredLevel.speechBubbles.add(new SpeechBubble("We've apprehended the Escapee!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 6));
                configuredLevel.speechBubbles.add(new SpeechBubble("Where am I?!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 6));
                configuredLevel.speechBubbles.add(new SpeechBubble("The Boss's chamber,\nof course!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 6));
                configuredLevel.speechBubbles.add(new SpeechBubble("Uh oh...", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Heh heh!", "Guard 1:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.catacombs.get(0).getLockedDoors().set(4, "Unlocked"); configuredLevel.catacombs.get(3).getLockedDoors().set(1, "Unlocked"); }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.continueBubbles = false; configuredLevel.guards.get(0).guardState = Enums.GuardState.ATTACK; configuredLevel.guards.get(0).alert = false; if (configuredLevel.guards.get(0).guardCatacomb == 3 && configuredLevel.guards.get(0).jumpState == Enums.JumpState.GROUNDED) { configuredLevel.currentBubble = 8; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.touchLocked = false; configuredLevel.catacombs.get(0).getLockedDoors().set(4, "Locked"); configuredLevel.catacombs.get(3).getLockedDoors().set(1, "Locked"); if (configuredLevel.getPlayer().getPosition().x < configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 150) { configuredLevel.currentBubble = 9; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Greetings escapee...", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.getPlayer().resetLegs(); configuredLevel.viewportPosition.set(configuredLevel.getPlayer().getPosition().x, configuredLevel.getPlayer().getPosition().y + 20); configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.touchLocked = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("The Catacombs lock-down\nis almost complete.", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 15));
                configuredLevel.speechBubbles.add(new SpeechBubble("Soon, you will be\nunder our control!", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 15));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now, hold still as I prepare the...", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, 15));
                configuredLevel.speechBubbles.add(new SpeechBubble("Mind-Control Machine!!", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.getPlayer().alert = true, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Never!!!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Well then...\nI'll force you to hold still!", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.bosses.get(0).bossItem = "phone", configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.continueBubbles = false; configuredLevel.show = false; configuredLevel.shake = configuredLevel.bosses.get(0).laserShaftY < 55; configuredLevel.bosses.get(0).laserShaftStage = "CONSTRUCTED"; if (configuredLevel.bosses.get(0).duelGunsThird >= 9.5f) { configuredLevel.currentBubble = 17; }}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Yikes!!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.getPlayer().health = 20; configuredLevel.continueBubbles = true; configuredLevel.show = true; configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(0).position.x + 200, configuredLevel.getPlayer().getPosition().y + 20); }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Swipe up to jump\nand swipe down to duck!", "Sign:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { if (configuredLevel.getPlayer().health <= 0) { configuredLevel.defeat = true; configuredLevel.verdict.verdict = false; configuredLevel.verdict.verdictPhrase = "Try to avoid the stun lasers!"; } configuredLevel.getPlayer().alert = false; configuredLevel.getPlayer().danger = true; configuredLevel.continueBubbles = false; configuredLevel.show = false; configuredLevel.bosses.get(0).shootLasers = true; if (configuredLevel.bosses.get(0).lasersShot >= 20 && configuredLevel.bosses.get(0).lasers.size == 0) { configuredLevel.currentBubble = 20; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Arg!\nWhy won't you stop moving!!", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.getPlayer().danger = false; configuredLevel.continueBubbles = true; configuredLevel.show = true; configuredLevel.bosses.get(0).shootLasers = false; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I better switch to Plan B.", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.bosses.get(0).laserShaftStage = "DECONSTRUCTED", configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("So, player...", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.continueBubbles = false; configuredLevel.bosses.get(0).targetPosition.set(new Vector2(configuredLevel.getPlayer().getPosition().x - 50, configuredLevel.getPlayer().getPosition().y)); if (configuredLevel.bosses.get(0).position.x > configuredLevel.getPlayer().getPosition().x - 51) { configuredLevel.currentBubble = 23; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I'll give you\nONE more chance...", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.continueBubbles = true, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Either join me willingly,\nor I will mind-control you.", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("It's your choice.", "Boss:", 3, "I'll join you!", "I want to be controlled!", "Neither!", "",
                        () -> configuredLevel.currentBubble = 26,
                        () -> configuredLevel.currentBubble = 30,
                        () -> configuredLevel.currentBubble = 34,
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("...", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.bosses.get(0).mouthState = Enums.MouthState.OPEN, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("On second thought,\nI don't trust you.", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I was joking anyways.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.currentBubble = 34, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("...", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.bosses.get(0).mouthState = Enums.MouthState.OPEN, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I did not expect\nyou to say that!", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.bosses.get(0).bossItem = "stungun"; configuredLevel.getPlayer().alert = true; configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.verdict.verdict = false; configuredLevel.defeat = true; configuredLevel.verdict.verdictPhrase = "Why did you say that?!"; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Then,       \nI will DESTROY you!!", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Prepare the Spike Pillars!", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN; configuredLevel.continueBubbles = false; configuredLevel.bosses.get(0).targetPosition.set(new Vector2(-80, configuredLevel.getPlayer().getPosition().y)); if (configuredLevel.bosses.get(0).position.x < -79) { configuredLevel.currentBubble = 36; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Avoid the falling Spike Pillars!", "Sign:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.bosses.get(0).bossItem = ""; configuredLevel.bosses.get(0).spawnOpacity = 0f; configuredLevel.continueBubbles = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { if (configuredLevel.bosses.get(0).playerHitBySpikePillar()) {
                            // Vibrate device for 500 milliseconds
                            Gdx.input.vibrate(500);
                            configuredLevel.defeat = true; configuredLevel.verdict.verdict = false; configuredLevel.verdict.verdictPhrase = "A Spike Pillar hit you!"; } configuredLevel.continueBubbles = false; configuredLevel.show = false; configuredLevel.touchLocked = false; configuredLevel.bosses.get(0).useSpikePillars = true; if (configuredLevel.bosses.get(0).spikePillarTimer > 1500) { configuredLevel.currentBubble = 38; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("...", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.shake = false; configuredLevel.continueBubbles = true; configuredLevel.show = true; configuredLevel.bosses.get(0).useSpikePillars = false; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Yes!\nThe pillars stopped!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("But...    \nWhere did the Boss go?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(0).position.x + 60, configuredLevel.getPlayer().getPosition().y + 20); configuredLevel.touchLocked = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I'm right behind you!", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.getPlayer().alert = true; configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN; configuredLevel.bosses.get(0).spawnOpacity = 1f;  configuredLevel.bosses.get(0).targetPosition.set(new Vector2()); configuredLevel.bosses.get(0).position.set(new Vector2(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 200, configuredLevel.bosses.get(0).position.y)); }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.continueBubbles = false; configuredLevel.viewportPosition.set(new Vector2(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 150, configuredLevel.bosses.get(0).position.y + 20)); if (configuredLevel.getPlayer().getPosition().x > configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 145) { configuredLevel.currentBubble = 43; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Wha-!!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I seems that you do\nnot want to cooperate.", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I will have to deal\nwith you personally!", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("...", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.bosses.get(0).bossItem = "spear", configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Yikes!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Luckily, I kept THIS\nfrom earlier!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Ha!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.bosses.get(0).mouthState = Enums.MouthState.OPEN; configuredLevel.getPlayer().alert = false; if (configuredLevel.inventory.inventoryItems.size < 1) { configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(), configuredLevel.viewportPosition, "stungun")); } configuredLevel.inventory.selectedItem = 0; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Move around and\nshoot at the boss!", "Sign:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.bosses.get(0).mouthState = Enums.MouthState.NORMAL; configuredLevel.bosses.get(0).energy = 20; configuredLevel.getPlayer().energy = 20; configuredLevel.getPlayer().health = 20; configuredLevel.bosses.get(0).health = 20; configuredLevel.getPlayer().heldItem.lasers = new DelayedRemovalArray<>(); }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                            if (configuredLevel.show) {
                                configuredLevel.bosses.get(0).bossState = Enums.BossState.HUNT;
                            }
                            configuredLevel.show = false;
                            configuredLevel.continueBubbles = false;
                            configuredLevel.touchLocked = false;
                            configuredLevel.getPlayer().danger = true;
                            configuredLevel.getPlayer().useEnergy = true;
                            configuredLevel.bosses.get(0).danger = true;
                            //player loses
                            if (configuredLevel.getPlayer().health <= 0) {
                                configuredLevel.defeat = true;
                                configuredLevel.verdict.verdict = false;
                                configuredLevel.verdict.verdictPhrase = "The Boss defeated you!";
                            }
                            //boss loses
                            if (configuredLevel.bosses.get(0).health <= 0) {
                                configuredLevel.currentBubble = 52;
                            }
                        }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Noooooo!!!", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {
                            configuredLevel.show = true;
                            configuredLevel.continueBubbles = true;
                            configuredLevel.touchLocked = false;
                            configuredLevel.bosses.get(0).alert = true;
                            configuredLevel.bosses.get(0).mouthState = Enums.MouthState.OPEN;
                            configuredLevel.bosses.get(0).bossItem = "";
                            configuredLevel.bosses.get(0).bossState = Enums.BossState.NORMAL;
                            configuredLevel.getPlayer().danger = false;
                            configuredLevel.getPlayer().useEnergy = false;
                            configuredLevel.bosses.get(0).danger = false;
                            configuredLevel.bosses.get(0).useEnergy = false;
                            configuredLevel.bosses.get(0).health = 20;
                            configuredLevel.getPlayer().health = 20;
                        }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("How could you beat me!\nThis is impossible!!", "Boss:", 3, "I've got skills!", "Uh... Luck?", "It's a secret!", "",
                        () -> configuredLevel.currentBubble = 54,
                        () -> configuredLevel.currentBubble = 54,
                        () -> configuredLevel.currentBubble = 54,
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Arg!!", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Maybe I can still\nget out of this...", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.continueBubbles = false; configuredLevel.bosses.get(0).bossItem = "phone"; configuredLevel.bosses.get(0).moveSpeed = 30; configuredLevel.bosses.get(0).targetPosition.set(new Vector2(-80, configuredLevel.getPlayer().getPosition().y)); if (configuredLevel.bosses.get(0).position.x < -79) { configuredLevel.currentBubble = 56; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Don't you even dare!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.touchLocked = true; configuredLevel.viewportPosition.set(configuredLevel.bosses.get(0).position.x + 20, configuredLevel.bosses.get(0).position.y); configuredLevel.continueBubbles = (configuredLevel.getPlayer().position.x < -49 && configuredLevel.speechBubbles.get(56).textLoaded); }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("!!!", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.continueBubbles = true; configuredLevel.bosses.get(0).mouthState = Enums.MouthState.OPEN; configuredLevel.bosses.get(0).bossItem = ""; configuredLevel.inventory.deleteCurrentItem(); configuredLevel.getPlayer().heldItem.itemType = "phone"; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hmm, where is the\ntrap-door button?", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Found it!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Wait, what?!", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.touchLocked = true; configuredLevel.viewportPosition.set(configuredLevel.bosses.get(0).position.x + 100, configuredLevel.bosses.get(0).position.y); configuredLevel.continueBubbles = (configuredLevel.getPlayer().position.x > -9); }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("NOOOOOO!!!", "Boss:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.items.get(0).position.set(new Vector2(-80, configuredLevel.getPlayer().getPosition().y - 35)); configuredLevel.catacombs.get(0).getLockedDoors().set(5, "Unlocked"); configuredLevel.catacombs.get(2).getLockedDoors().set(2, "Unlocked"); configuredLevel.continueBubbles = (configuredLevel.bosses.get(0).position.y < configuredLevel.catacombs.get(2).position.y); configuredLevel.bosses.get(0).mouthState = Enums.MouthState.OPEN; configuredLevel.bosses.get(0).bossItem = ""; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble(".....", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.bosses.get(0).spawnOpacity = 0f; configuredLevel.bosses.get(0).alert = false; configuredLevel.getPlayer().mouthState = Enums.MouthState.NORMAL; configuredLevel.catacombs.get(0).getLockedDoors().set(5, "Closed"); configuredLevel.catacombs.get(2).getLockedDoors().set(2, "Closed"); configuredLevel.continueBubbles = (configuredLevel.catacombs.get(0).bottomsOffset.x == (0)); }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("The boss is gone...", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.continueBubbles = true, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("YES!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now, I just need to\nfind that elevator again.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.touchLocked = false; configuredLevel.continueBubbles = false; if (configuredLevel.currentCatacomb == 1) { configuredLevel.currentBubble = 67; } }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("There's the elevator!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = true; configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN; configuredLevel.touchLocked = true; configuredLevel.viewportPosition.set(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 60, configuredLevel.getPlayer().getPosition().y); configuredLevel.continueBubbles = true; }, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("After everything, I have\nfinally made it to the end!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("I have collected treasure,\ndefeated Guards, and beat the Boss!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now I can travel to the surface.", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("Goodbye, Catacombs!!!", "Player:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> configuredLevel.exitDoor.unlocked = true, configuredLevel, -1));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> { configuredLevel.show = false; configuredLevel.continueBubbles = false; configuredLevel.touchLocked = false; }, configuredLevel, -1));
                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(30, 65));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(190, 3), new Vector2(1000, 1000), configuredLevel);
                break;
        }
    }

    public void update (Level configuredLevel, float delta) {
        //update individual stuff in each level: The speechBubble "triggers" are boolean values that activate the fourth run function in the speechBubble that the trigger it is set to, when the trigger is true.
        configuredLevel.getPlayer().spawn();
        if (currentLevel == 0) {
            //set some stuff and check if speechBubbles are triggered
            configuredLevel.speechBubbles.get(6).triggered = (configuredLevel.speechBubbles.get(configuredLevel.currentBubble).textLoaded);
            configuredLevel.speechBubbles.get(25).triggered = (configuredLevel.items.size == 0);
            configuredLevel.speechBubbles.get(27).triggered = (configuredLevel.inventory.selectedItem == 0);
            configuredLevel.speechBubbles.get(36).triggered = (configuredLevel.catacombs.get(1).getLockedDoors().get(0).equals("Unlocked"));
            configuredLevel.speechBubbles.get(41).triggered = (configuredLevel.exitDoor.playerHasEscaped);
            configuredLevel.speechBubbles.get(44).triggered = (configuredLevel.exitDoor.unlocked);
            configuredLevel.speechBubbles.get(48).triggered = (configuredLevel.currentBubble == 48);
        }
        if (currentLevel == 1) {
            configuredLevel.speechBubbles.get(13).triggered = (configuredLevel.getPlayer().getPosition().x <= 260);
            configuredLevel.speechBubbles.get(17).triggered = (configuredLevel.currentCatacomb == 1);
            configuredLevel.speechBubbles.get(20).triggered = (configuredLevel.currentCatacomb == 2);
            configuredLevel.speechBubbles.get(24).triggered = (configuredLevel.currentBubble == 24);
            configuredLevel.speechBubbles.get(25).triggered = (configuredLevel.getPlayer().getPosition().x <= -40);
            configuredLevel.speechBubbles.get(26).triggered = (configuredLevel.currentBubble == 26);
            configuredLevel.speechBubbles.get(27).triggered = (configuredLevel.inventory.selectedItem != -1 && configuredLevel.inventory.inventoryItems.size >= 1);
            configuredLevel.speechBubbles.get(29).triggered = (configuredLevel.currentBubble == 29);
            configuredLevel.speechBubbles.get(31).triggered = (configuredLevel.currentBubble == 31);
            configuredLevel.speechBubbles.get(36).triggered = (configuredLevel.currentBubble == 36);
            configuredLevel.speechBubbles.get(37).triggered = (configuredLevel.currentBubble == 37);
        }
        if (currentLevel == 2) {
            configuredLevel.speechBubbles.get(1).triggered = (configuredLevel.exitDoor.playerHasEscaped);
            configuredLevel.speechBubbles.get(2).triggered = (configuredLevel.currentCatacomb == 4);
            configuredLevel.speechBubbles.get(3).triggered = ((configuredLevel.speechBubbles.get(3).textLoaded && configuredLevel.catacombs.get(4).getLockedDoors().get(0).equals("Unlocked")) || configuredLevel.catacombs.get(4).getLockedDoors().get(0).equals("Unlocked"));
            configuredLevel.speechBubbles.get(4).triggered = (configuredLevel.currentCatacomb == 2);
            configuredLevel.speechBubbles.get(5).triggered = (configuredLevel.currentBubble == 5 && configuredLevel.speechBubbles.get(5).textLoaded);
            configuredLevel.speechBubbles.get(6).triggered = (configuredLevel.currentBubble == 6);
            configuredLevel.speechBubbles.get(7).triggered = (configuredLevel.currentCatacomb == 3);
            configuredLevel.speechBubbles.get(12).triggered = (configuredLevel.currentCatacomb == 0 && configuredLevel.currentBubble >= 11);
            configuredLevel.speechBubbles.get(13).triggered = (configuredLevel.currentCatacomb == 0 && configuredLevel.currentBubble >= 11);
            configuredLevel.speechBubbles.get(14).triggered = (configuredLevel.currentCatacomb == 0 && configuredLevel.currentBubble >= 11);
            configuredLevel.speechBubbles.get(17).triggered = (configuredLevel.catacombs.get(0).getLockedDoors().get(4).equals("Locked"));
            configuredLevel.speechBubbles.get(18).triggered = (configuredLevel.currentCatacomb == 4 && configuredLevel.currentBubble == 18);
            configuredLevel.speechBubbles.get(20).triggered = (configuredLevel.currentCatacomb == 7);
        }
        if (currentLevel == 3) {
            configuredLevel.speechBubbles.get(0).triggered = (configuredLevel.getPlayer().spawnTimer > 110);
            configuredLevel.speechBubbles.get(3).triggered = (configuredLevel.currentBubble == 3);
            configuredLevel.speechBubbles.get(4).triggered = (configuredLevel.currentBubble == 4);
            if (configuredLevel.currentCatacomb == 6 && configuredLevel.catacombs.get(6).getLockedDoors().get(0).equals("Unlocked")) { configuredLevel.catacombs.get(6).getLockedDoors().set(0, "Locked"); }
        }
        if (currentLevel == 4) {
            if (configuredLevel.currentCatacomb == 1 && configuredLevel.currentBubble == 0 && configuredLevel.catacombs.get(1).getLockedDoors().get(0).equals("Unlocked")) {
                configuredLevel.catacombs.get(1).getLockedDoors().set(0, "Closed");
                if (configuredLevel.catacombs.get(1).bottomLeftOffset.y < 1) {
                    configuredLevel.catacombs.get(1).getLockedDoors().set(0, "Closed");
                }
            }
            if (configuredLevel.currentCatacomb == 2 && configuredLevel.currentBubble == 22 && configuredLevel.guards.get(0).chasePlayer) {
                configuredLevel.catacombs.get(2).getLockedDoors().set(1, "Locked");
                configuredLevel.currentBubble = 24;
            }
            if (configuredLevel.getPlayer().hasWeapon) {
                configuredLevel.speechBubbles.get(2).Options = 4;
            } else {
                configuredLevel.speechBubbles.get(2).Options = 3;
            }
            configuredLevel.speechBubbles.get(0).triggered = (configuredLevel.guards.get(0).talkToPlayer);
            configuredLevel.speechBubbles.get(1).triggered = (configuredLevel.currentBubble == 1);
            configuredLevel.speechBubbles.get(14).triggered = (configuredLevel.currentBubble == 14);
            configuredLevel.speechBubbles.get(17).triggered = (configuredLevel.currentBubble == 17);
            configuredLevel.speechBubbles.get(18).triggered = (configuredLevel.currentBubble == 18);
            configuredLevel.speechBubbles.get(22).triggered = (configuredLevel.currentBubble == 22);
            configuredLevel.speechBubbles.get(24).triggered = (configuredLevel.currentBubble == 24);
            configuredLevel.speechBubbles.get(25).triggered = (configuredLevel.currentBubble == 25);
            configuredLevel.speechBubbles.get(26).triggered = (configuredLevel.currentBubble == 26);
            configuredLevel.speechBubbles.get(33).triggered = (configuredLevel.currentBubble == 33);
            configuredLevel.speechBubbles.get(46).triggered = (configuredLevel.currentBubble == 46);
            configuredLevel.speechBubbles.get(47).triggered = (configuredLevel.currentBubble == 47);
            configuredLevel.speechBubbles.get(49).triggered = (configuredLevel.currentBubble == 49);
            configuredLevel.speechBubbles.get(55).triggered = (configuredLevel.currentBubble == 55);
            configuredLevel.speechBubbles.get(59).triggered = (configuredLevel.currentBubble == 59);
            configuredLevel.speechBubbles.get(63).triggered = (configuredLevel.currentBubble == 63);
            configuredLevel.speechBubbles.get(72).triggered = (configuredLevel.currentBubble == 72);
            configuredLevel.speechBubbles.get(73).triggered = (configuredLevel.currentBubble == 73);
            configuredLevel.speechBubbles.get(74).triggered = (configuredLevel.currentBubble == 74);
            configuredLevel.speechBubbles.get(75).triggered = (configuredLevel.currentBubble == 75);
            configuredLevel.speechBubbles.get(76).triggered = (configuredLevel.currentBubble == 76);
            configuredLevel.speechBubbles.get(77).triggered = (configuredLevel.currentBubble == 77);
            if (configuredLevel.currentBubble >= 78) {
                configuredLevel.guards.get(0).guardState = Enums.GuardState.DEFEATED;
                configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN;
                configuredLevel.getPlayer().fighting = false;
                configuredLevel.guards.get(0).guardItem = "";
                configuredLevel.guards.get(0).talkToPlayer = false;
            }
            configuredLevel.speechBubbles.get(81).triggered = (configuredLevel.currentBubble == 81);
        }
        if (currentLevel == 5) {
            configuredLevel.catacombs.get(1).longCatacomb = true;
            configuredLevel.catacombs.get(5).longCatacomb = true;
            configuredLevel.catacombs.get(7).longCatacomb = true;
            configuredLevel.catacombs.get(1).stalactites = true;
            configuredLevel.guards.get(0).guardState = Enums.GuardState.DEFEATED;
            configuredLevel.guards.get(0).facing = Enums.Facing.LEFT;
            configuredLevel.guards.get(0).guardCatacomb = 5;
            //initialize eyes
            configuredLevel.guards.get(0).eyeLookLeft = new Vector2(-Constants.EYE_OFFSET - 2,Constants.EYE_OFFSET);
            configuredLevel.guards.get(0).eyeLookRight = new Vector2(Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
            //initialize mouth
            configuredLevel.guards.get(0).mouthOffset = new Vector2(-0.6f, 0f);
            configuredLevel.speechBubbles.get(0).triggered = (configuredLevel.currentCatacomb == 1);
            configuredLevel.speechBubbles.get(2).triggered = (configuredLevel.currentBubble == 2);
            configuredLevel.speechBubbles.get(3).triggered = (configuredLevel.currentBubble == 3);
            configuredLevel.speechBubbles.get(4).triggered = (configuredLevel.currentBubble == 4);
            configuredLevel.speechBubbles.get(5).triggered = (configuredLevel.currentBubble == 5);
            configuredLevel.speechBubbles.get(6).triggered = (configuredLevel.currentBubble == 6);
            configuredLevel.speechBubbles.get(7).triggered = (configuredLevel.currentBubble == 7);
            configuredLevel.speechBubbles.get(8).triggered = (configuredLevel.currentBubble == 8);
            configuredLevel.speechBubbles.get(19).triggered = (configuredLevel.currentBubble == 19);
            configuredLevel.speechBubbles.get(20).triggered = (configuredLevel.currentBubble == 20);
            configuredLevel.speechBubbles.get(25).triggered = (configuredLevel.currentBubble == 25);
            configuredLevel.speechBubbles.get(26).triggered = (configuredLevel.currentBubble == 26);
            configuredLevel.speechBubbles.get(27).triggered = (configuredLevel.currentBubble == 27);
            configuredLevel.speechBubbles.get(28).triggered = (configuredLevel.currentBubble == 28);
            configuredLevel.speechBubbles.get(34).triggered = (configuredLevel.currentBubble == 34);
            configuredLevel.speechBubbles.get(35).triggered = (configuredLevel.currentBubble == 35 && configuredLevel.speechBubbles.get(configuredLevel.currentBubble).textLoaded);
            configuredLevel.speechBubbles.get(36).triggered = (configuredLevel.currentBubble == 36);
            configuredLevel.speechBubbles.get(39).triggered = (configuredLevel.currentBubble == 39);
            configuredLevel.speechBubbles.get(40).triggered = (configuredLevel.currentBubble == 40 && ((configuredLevel.getPlayer().getPosition().x > configuredLevel.guards.get(0).position.x && configuredLevel.currentCatacomb == 5) || configuredLevel.guards.get(0).alert));
        }
        if (currentLevel == 6) {
            configuredLevel.catacombs.get(3).longCatacomb = true;
            configuredLevel.catacombs.get(5).longCatacomb = true;
            configuredLevel.catacombs.get(7).longCatacomb = true;
            configuredLevel.speechBubbles.get(0).triggered = (configuredLevel.currentCatacomb == 2);
            configuredLevel.speechBubbles.get(3).triggered = (configuredLevel.puzzles.get(2).solved);
            configuredLevel.speechBubbles.get(5).triggered = (configuredLevel.currentBubble == 5);
        }
        if (currentLevel == 7) {
            configuredLevel.catacombs.get(1).longCatacomb = true;
            configuredLevel.catacombs.get(8).longCatacomb = true;
            configuredLevel.catacombs.get(8).stalactites = true;
            configuredLevel.catacombs.get(9).stalactites = true;
            //move guards
            if (configuredLevel.currentBubble < 2) {
                configuredLevel.guards.get(1).position.set(new Vector2(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 600, -619));
            } else if (configuredLevel.currentBubble >= 2 && configuredLevel.currentBubble < 18) {
                configuredLevel.guards.get(1).position.set(new Vector2(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 60, -119));
            }
            if (configuredLevel.currentBubble >= 4 && configuredLevel.currentBubble < 10 && configuredLevel.guards.get(0).position.x > configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 160) {
                configuredLevel.guards.get(0).moveGuard(delta, 20, new Vector2(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 140, configuredLevel.getPlayer().getPosition().y + 20));
            } else {
                configuredLevel.guards.get(0).resetLegs();
            }
            if (configuredLevel.guards.get(1).guardState != Enums.GuardState.PATROLLING) {
                //initialize eyes
                configuredLevel.guards.get(1).eyeLookLeft = new Vector2(-Constants.EYE_OFFSET + 2, Constants.EYE_OFFSET);
                configuredLevel.guards.get(1).eyeLookRight = new Vector2(Constants.EYE_OFFSET + 2, Constants.EYE_OFFSET);
                //initialize mouth
                configuredLevel.guards.get(1).mouthOffset = new Vector2(0.6f, 0f);
                configuredLevel.guards.get(1).facing = Enums.Facing.RIGHT;
            }
            //make Guard 1's eyes and mouth be in the same spot if he is not confused
            if (!configuredLevel.guards.get(0).suspicious) {
                //initialize eyes
                configuredLevel.guards.get(0).eyeLookLeft = new Vector2(-Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
                configuredLevel.guards.get(0).eyeLookRight = new Vector2(Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
                //initialize mouth
                configuredLevel.guards.get(0).mouthOffset = new Vector2(-0.6f, 0f);
                configuredLevel.guards.get(0).facing = Enums.Facing.LEFT;
            }
            configuredLevel.guards.get(0).guardState = Enums.GuardState.DEFEATED;
            if (configuredLevel.currentBubble < 18) {
                configuredLevel.guards.get(1).guardState = Enums.GuardState.DEFEATED;
            }
            //defeat if guard 1 sees player
            if (configuredLevel.guards.get(1).talkToPlayer && configuredLevel.guards.get(1).position.dst(configuredLevel.getPlayer().getPosition()) < 50) {
                configuredLevel.continueBubbles = false; configuredLevel.defeat = true; configuredLevel.verdict.verdict = false; configuredLevel.verdict.verdictPhrase = "You were captured by a Guard!";
            }
            configuredLevel.speechBubbles.get(0).triggered = (configuredLevel.currentCatacomb == 1 && configuredLevel.getPlayer().getPosition().x > configuredLevel.guards.get(0).position.x - 50);
            configuredLevel.speechBubbles.get(2).triggered = (configuredLevel.currentBubble == 2);
            configuredLevel.speechBubbles.get(3).triggered = (configuredLevel.currentBubble == 3);
            configuredLevel.speechBubbles.get(4).triggered = (configuredLevel.currentBubble == 4);
            configuredLevel.speechBubbles.get(12).triggered = (configuredLevel.currentBubble == 12);
            configuredLevel.speechBubbles.get(14).triggered = (configuredLevel.currentBubble == 14);
            configuredLevel.speechBubbles.get(16).triggered = (configuredLevel.currentBubble == 16);
            configuredLevel.speechBubbles.get(17).triggered = (configuredLevel.currentBubble == 17);
            configuredLevel.speechBubbles.get(18).triggered = (configuredLevel.currentBubble == 18);
            configuredLevel.speechBubbles.get(19).triggered = (configuredLevel.currentBubble == 19);
            configuredLevel.speechBubbles.get(21).triggered = (configuredLevel.currentBubble == 21);
            configuredLevel.speechBubbles.get(22).triggered = (configuredLevel.currentBubble == 22);
            configuredLevel.speechBubbles.get(23).triggered = (configuredLevel.currentBubble == 23);
            configuredLevel.speechBubbles.get(24).triggered = (configuredLevel.currentBubble == 24);
            configuredLevel.speechBubbles.get(25).triggered = (configuredLevel.currentBubble == 25);
            configuredLevel.speechBubbles.get(26).triggered = (configuredLevel.currentBubble == 26);
        }
        if (currentLevel == 8) {
            configuredLevel.catacombs.get(0).longCatacomb = true;
            configuredLevel.catacombs.get(4).longCatacomb = true;
            configuredLevel.catacombs.get(6).longCatacomb = true;
            configuredLevel.catacombs.get(8).longCatacomb = true;
            //lever puzzle
            if (configuredLevel.levers.get(0).triggered && !configuredLevel.levers.get(1).triggered && configuredLevel.levers.get(2).triggered && configuredLevel.currentCatacomb != 5) {
                configuredLevel.catacombs.get(5).getLockedDoors().set(4, "Unlocked"); configuredLevel.catacombs.get(4).getLockedDoors().set(1, "Unlocked");
            } else {
                configuredLevel.catacombs.get(5).getLockedDoors().set(4, "Closed"); configuredLevel.catacombs.get(4).getLockedDoors().set(1, "Closed");
            }
            //initialize eyes
            configuredLevel.guards.get(0).eyeLookLeft = new Vector2(-Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
            configuredLevel.guards.get(0).eyeLookRight = new Vector2(Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
            //initialize mouth
            configuredLevel.guards.get(0).mouthOffset = new Vector2(-0.6f, 0f);
            configuredLevel.guards.get(0).facing = Enums.Facing.LEFT;
            //guard
            configuredLevel.guards.get(0).guardState = Enums.GuardState.DEFEATED;
            //speechBubble choice logic
            if (configuredLevel.puzzles.get(2).solution[0] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[1] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[2] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[3] == Enums.Shape.TRIANGLE && (configuredLevel.currentBubble == 16 || configuredLevel.currentBubble == 20 || configuredLevel.currentBubble == 26 || configuredLevel.currentBubble == 33)) {
                configuredLevel.currentBubble = 41;
            }
            //reinitialize some speechBubble options
            configuredLevel.speechBubbles.get(40).Options = (configuredLevel.puzzles.get(2).solution[0] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[1] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[2] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[3] == Enums.Shape.TRIANGLE ? 0 : 4);
            configuredLevel.speechBubbles.get(19).Options = (configuredLevel.puzzles.get(2).solution[0] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[1] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[2] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[3] == Enums.Shape.TRIANGLE ? 0 : 4);
            configuredLevel.speechBubbles.get(25).Options = (configuredLevel.puzzles.get(2).solution[0] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[1] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[2] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[3] == Enums.Shape.TRIANGLE ? 0 : 4);
            configuredLevel.speechBubbles.get(32).Options = (configuredLevel.puzzles.get(2).solution[0] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[1] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[2] == Enums.Shape.TRIANGLE && configuredLevel.puzzles.get(2).solution[3] == Enums.Shape.TRIANGLE ? 0 : 4);
            configuredLevel.speechBubbles.get(40).option1 = (configuredLevel.puzzles.get(2).solution[0] == Enums.Shape.TRIANGLE ? "" : "Who are you?");
            configuredLevel.speechBubbles.get(19).option1 = (configuredLevel.puzzles.get(2).solution[0] == Enums.Shape.TRIANGLE ? "" : "Who are you?");
            configuredLevel.speechBubbles.get(25).option1 = (configuredLevel.puzzles.get(2).solution[0] == Enums.Shape.TRIANGLE ? "" : "Who are you?");
            configuredLevel.speechBubbles.get(32).option1 = (configuredLevel.puzzles.get(2).solution[0] == Enums.Shape.TRIANGLE ? "" : "Who are you?");
            configuredLevel.speechBubbles.get(40).option2 = (configuredLevel.puzzles.get(2).solution[1] == Enums.Shape.TRIANGLE ? "" : "Who do you work for?");
            configuredLevel.speechBubbles.get(19).option2 = (configuredLevel.puzzles.get(2).solution[1] == Enums.Shape.TRIANGLE ? "" : "Who do you work for?");
            configuredLevel.speechBubbles.get(25).option2 = (configuredLevel.puzzles.get(2).solution[1] == Enums.Shape.TRIANGLE ? "" : "Who do you work for?");
            configuredLevel.speechBubbles.get(32).option2 = (configuredLevel.puzzles.get(2).solution[1] == Enums.Shape.TRIANGLE ? "" : "Who do you work for?");
            configuredLevel.speechBubbles.get(40).option3 = (configuredLevel.puzzles.get(2).solution[2] == Enums.Shape.TRIANGLE ? "" : "Why am I here?");
            configuredLevel.speechBubbles.get(19).option3 = (configuredLevel.puzzles.get(2).solution[2] == Enums.Shape.TRIANGLE ? "" : "Why am I here?");
            configuredLevel.speechBubbles.get(25).option3 = (configuredLevel.puzzles.get(2).solution[2] == Enums.Shape.TRIANGLE ? "" : "Why am I here?");
            configuredLevel.speechBubbles.get(32).option3 = (configuredLevel.puzzles.get(2).solution[2] == Enums.Shape.TRIANGLE ? "" : "Why am I here?");
            configuredLevel.speechBubbles.get(40).option4 = (configuredLevel.puzzles.get(2).solution[3] == Enums.Shape.TRIANGLE ? "" : "How do I escape?");
            configuredLevel.speechBubbles.get(19).option4 = (configuredLevel.puzzles.get(2).solution[3] == Enums.Shape.TRIANGLE ? "" : "How do I escape?");
            configuredLevel.speechBubbles.get(25).option4 = (configuredLevel.puzzles.get(2).solution[3] == Enums.Shape.TRIANGLE ? "" : "How do I escape?");
            configuredLevel.speechBubbles.get(32).option4 = (configuredLevel.puzzles.get(2).solution[3] == Enums.Shape.TRIANGLE ? "" : "How do I escape?");
            //triggers
            configuredLevel.speechBubbles.get(0).triggered = (configuredLevel.inventory.inventoryItems.size > 0 && configuredLevel.inventory.inventoryItems.get(0).itemType.equals("ghost"));
            configuredLevel.speechBubbles.get(3).triggered = (configuredLevel.getPlayer().ghost);
            configuredLevel.speechBubbles.get(4).triggered = (configuredLevel.currentBubble == 4);
            configuredLevel.speechBubbles.get(6).triggered = (configuredLevel.currentBubble == 6);
            configuredLevel.speechBubbles.get(7).triggered = (configuredLevel.currentBubble == 7);
            configuredLevel.speechBubbles.get(8).triggered = (configuredLevel.currentBubble == 8);
            configuredLevel.speechBubbles.get(9).triggered = (configuredLevel.currentBubble == 9 && configuredLevel.speechBubbles.get(configuredLevel.currentBubble).textLoaded);
            configuredLevel.speechBubbles.get(10).triggered = (configuredLevel.currentBubble == 10);
            configuredLevel.speechBubbles.get(11).triggered = (configuredLevel.currentBubble == 11);
            configuredLevel.speechBubbles.get(13).triggered = (configuredLevel.currentBubble == 13);
            configuredLevel.speechBubbles.get(14).triggered = (configuredLevel.currentBubble == 14);
            configuredLevel.speechBubbles.get(17).triggered = (configuredLevel.currentBubble == 17);
            configuredLevel.speechBubbles.get(23).triggered = (configuredLevel.currentBubble == 23);
            configuredLevel.speechBubbles.get(21).triggered = (configuredLevel.currentBubble == 21);
            configuredLevel.speechBubbles.get(27).triggered = (configuredLevel.currentBubble == 27);
            configuredLevel.speechBubbles.get(28).triggered = (configuredLevel.currentBubble == 28);
            configuredLevel.speechBubbles.get(34).triggered = (configuredLevel.currentBubble == 34);
            configuredLevel.speechBubbles.get(46).triggered = (configuredLevel.currentBubble == 46);
            configuredLevel.speechBubbles.get(47).triggered = (configuredLevel.currentBubble == 47);
            configuredLevel.speechBubbles.get(48).triggered = (configuredLevel.currentBubble == 48);
            configuredLevel.speechBubbles.get(49).triggered = (configuredLevel.currentBubble == 49);
            configuredLevel.speechBubbles.get(50).triggered = (configuredLevel.currentBubble == 50);
            configuredLevel.speechBubbles.get(52).triggered = (configuredLevel.currentBubble == 52);
            configuredLevel.speechBubbles.get(53).triggered = (configuredLevel.currentBubble == 53);
            configuredLevel.speechBubbles.get(54).triggered = (configuredLevel.currentBubble == 54);
            configuredLevel.speechBubbles.get(55).triggered = (configuredLevel.currentBubble == 55);
            configuredLevel.speechBubbles.get(56).triggered = (configuredLevel.currentBubble == 56);
        }
        if (currentLevel == 9) {
            //player hits floor spikes
            if (configuredLevel.catacombs.get(11).touchingFloorSpikes(new Vector2(configuredLevel.getPlayer().getPosition().x, (configuredLevel.getPlayer().getPosition().y - Constants.PLAYER_HEIGHT) - Constants.HEAD_SIZE * 2f))) {
                configuredLevel.defeat = true;
                configuredLevel.verdict.verdict = false;
                configuredLevel.verdict.verdictPhrase = "Ouch! Try to avoid those spikes!";
            }
            //open secret diamond vault
            if (configuredLevel.catacombs.get(6).getLockedDoors().get(0).equals("Unlocked")) {
                configuredLevel.catacombs.get(6).getLockedDoors().set(4, "Unlocked");
                configuredLevel.catacombs.get(7).getLockedDoors().set(1, "Unlocked");
            }
            if (configuredLevel.currentBubble < 7) {
                //initialize eyes
                configuredLevel.guards.get(0).eyeLookLeft = new Vector2(-Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
                configuredLevel.guards.get(0).eyeLookRight = new Vector2(Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
                //initialize mouth
                configuredLevel.guards.get(0).mouthOffset = new Vector2(-0.6f, 0f);
                configuredLevel.guards.get(0).facing = Enums.Facing.LEFT;
            }
            //guard
            if (configuredLevel.currentBubble < 8) {
                configuredLevel.guards.get(0).guardState = Enums.GuardState.DEFEATED;
            }
            //catacomb initialize
            configuredLevel.catacombs.get(6).longCatacomb = true;
            configuredLevel.catacombs.get(11).longCatacomb = true;
            configuredLevel.catacombs.get(11).floorSpikes = true;
            configuredLevel.speechBubbles.get(0).triggered = (configuredLevel.signs.get(0).touchesSign(configuredLevel.getPlayer().getPosition()));
            configuredLevel.speechBubbles.get(5).triggered = (configuredLevel.currentBubble == 5);
            configuredLevel.speechBubbles.get(6).triggered = (configuredLevel.currentBubble == 6);
            configuredLevel.speechBubbles.get(7).triggered = (configuredLevel.currentBubble == 7);
            configuredLevel.speechBubbles.get(8).triggered = (configuredLevel.currentBubble == 8);
            configuredLevel.speechBubbles.get(9).triggered = (configuredLevel.currentBubble == 9);
        }
        if (currentLevel == 10) {
            //lever puzzle
            if (!configuredLevel.levers.get(0).triggered && configuredLevel.levers.get(1).triggered) {
                configuredLevel.catacombs.get(1).getLockedDoors().set(3, "Unlocked"); configuredLevel.catacombs.get(2).getLockedDoors().set(0, "Unlocked");
            } else {
                configuredLevel.catacombs.get(1).getLockedDoors().set(3, "Closed"); configuredLevel.catacombs.get(2).getLockedDoors().set(0, "Closed");
            }
            if (configuredLevel.currentCatacomb == 3) {
                configuredLevel.shake = false;
            }
            //steal keys from guards
            if (configuredLevel.guards.get(2).guardTouchesPlayer() &&
                    configuredLevel.touchPosition.x > (configuredLevel.guards.get(2).jumpState.equals(Enums.JumpState.GROUNDED) ? (configuredLevel.guards.get(2).position.x - Constants.HEAD_SIZE) - 12 : (configuredLevel.guards.get(2).position.x - Constants.HEAD_SIZE)) &&
                    configuredLevel.touchPosition.y > (configuredLevel.guards.get(2).position.y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f) &&
                    configuredLevel.touchPosition.x < (configuredLevel.guards.get(2).jumpState.equals(Enums.JumpState.GROUNDED) ? (Constants.PLAYER_WIDTH * 2f) + 12 : (Constants.PLAYER_WIDTH * 2f)) + (configuredLevel.guards.get(2).jumpState.equals(Enums.JumpState.GROUNDED) ? (configuredLevel.guards.get(2).position.x - Constants.HEAD_SIZE) - 12 : (configuredLevel.guards.get(2).position.x - Constants.HEAD_SIZE)) &&
                    configuredLevel.touchPosition.y < (Constants.PLAYER_HEIGHT * 2.5f) + (configuredLevel.guards.get(2).position.y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f) && configuredLevel.guards.get(2).guardItem.equals("doubleKey")) { configuredLevel.guards.get(2).guardItem = ""; configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "doubleKey"));
            }
            if (configuredLevel.guards.get(1).guardTouchesPlayer() &&
                    configuredLevel.touchPosition.x > (configuredLevel.guards.get(1).jumpState.equals(Enums.JumpState.GROUNDED) ? (configuredLevel.guards.get(1).position.x - Constants.HEAD_SIZE) - 12 : (configuredLevel.guards.get(1).position.x - Constants.HEAD_SIZE)) &&
                    configuredLevel.touchPosition.y > (configuredLevel.guards.get(1).position.y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f) &&
                    configuredLevel.touchPosition.x < (configuredLevel.guards.get(1).jumpState.equals(Enums.JumpState.GROUNDED) ? (Constants.PLAYER_WIDTH * 2f) + 12 : (Constants.PLAYER_WIDTH * 2f)) + (configuredLevel.guards.get(1).jumpState.equals(Enums.JumpState.GROUNDED) ? (configuredLevel.guards.get(1).position.x - Constants.HEAD_SIZE) - 12 : (configuredLevel.guards.get(1).position.x - Constants.HEAD_SIZE)) &&
                    configuredLevel.touchPosition.y < (Constants.PLAYER_HEIGHT * 2.5f) + (configuredLevel.guards.get(1).position.y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f) && configuredLevel.guards.get(1).guardItem.equals("key")) { configuredLevel.guards.get(1).guardItem = ""; configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
            }
            //guard logic
            if ((configuredLevel.currentBubble == 6 || configuredLevel.currentBubble == 9 || configuredLevel.currentBubble == 12) && !configuredLevel.getPlayer().invisibility && configuredLevel.getPlayer().getPosition().dst(configuredLevel.guards.get(0).position) < 100) {
                configuredLevel.guards.get(0).guardState = Enums.GuardState.ATTACK;
                configuredLevel.guards.get(0).alert = false;
                configuredLevel.guards.get(0).chasePlayer = true;
                configuredLevel.guards.get(0).talkToPlayer = false;
            }
            if ((configuredLevel.currentBubble == 6 || configuredLevel.currentBubble == 9 || configuredLevel.currentBubble == 12) && !configuredLevel.getPlayer().invisibility && configuredLevel.getPlayer().getPosition().dst(configuredLevel.guards.get(1).position) < 100) {
                configuredLevel.guards.get(1).guardState = Enums.GuardState.ATTACK;
                configuredLevel.guards.get(1).alert = false;
                configuredLevel.guards.get(1).chasePlayer = true;
                configuredLevel.guards.get(1).talkToPlayer = false;
            }
            if ((configuredLevel.currentBubble == 6 || configuredLevel.currentBubble == 9 || configuredLevel.currentBubble == 12) && !configuredLevel.getPlayer().invisibility && configuredLevel.getPlayer().getPosition().dst(configuredLevel.guards.get(2).position) < 100) {
                configuredLevel.guards.get(2).guardState = Enums.GuardState.ATTACK;
                configuredLevel.guards.get(2).alert = false;
                configuredLevel.guards.get(2).chasePlayer = true;
                configuredLevel.guards.get(2).talkToPlayer = false;
            }
            //close door behind player
            if (configuredLevel.currentCatacomb == 8) {
                configuredLevel.catacombs.get(8).getLockedDoors().set(4, "Closed");
                configuredLevel.alarm = false;
            }
            configuredLevel.catacombs.get(7).longCatacomb = true;
            configuredLevel.catacombs.get(10).longCatacomb = true;
            configuredLevel.speechBubbles.get(0).triggered = (configuredLevel.signs.get(0).touchesSign(configuredLevel.getPlayer().getPosition()));
            configuredLevel.speechBubbles.get(3).triggered = (configuredLevel.currentBubble == 3);
            configuredLevel.speechBubbles.get(4).triggered = (configuredLevel.currentBubble == 4);
            configuredLevel.speechBubbles.get(5).triggered = (configuredLevel.currentBubble == 5);
            configuredLevel.speechBubbles.get(6).triggered = (configuredLevel.currentBubble == 6);
            configuredLevel.speechBubbles.get(7).triggered = (configuredLevel.currentBubble == 7);
            configuredLevel.speechBubbles.get(8).triggered = (configuredLevel.currentBubble == 8);
            configuredLevel.speechBubbles.get(9).triggered = (configuredLevel.currentBubble == 9);
            configuredLevel.speechBubbles.get(10).triggered = (configuredLevel.currentBubble == 10);
            configuredLevel.speechBubbles.get(11).triggered = (configuredLevel.currentBubble == 11);
            configuredLevel.speechBubbles.get(12).triggered = (configuredLevel.currentBubble == 12);
        }
        if (currentLevel == 11) {
            if (configuredLevel.chests.get(0).opened) {
                configuredLevel.chests.get(0).fade = true;
            }
            //bomb blows open doors
            if (configuredLevel.items.size > 0 && configuredLevel.items.get(configuredLevel.items.size - 1).bomb.exploding) {
                if (configuredLevel.items.get(configuredLevel.items.size - 1).bomb.currentCatacomb == 2) {
                    configuredLevel.catacombs.get(2).getLockedDoors().set(5, "Unlocked");
                    configuredLevel.catacombs.get(3).getLockedDoors().set(2, "Unlocked");
                } else if (configuredLevel.items.get(configuredLevel.items.size - 1).bomb.currentCatacomb == 5) {
                    configuredLevel.catacombs.get(5).getLockedDoors().set(0, "Unlocked");
                    configuredLevel.catacombs.get(3).getLockedDoors().set(3, "Unlocked");
                }
            }
            configuredLevel.catacombs.get(8).longCatacomb = true;
            configuredLevel.catacombs.get(10).longCatacomb = true;
            configuredLevel.speechBubbles.get(0).triggered = (configuredLevel.currentCatacomb == 1);
            configuredLevel.speechBubbles.get(1).triggered = (configuredLevel.currentBubble == 1);
            configuredLevel.speechBubbles.get(2).triggered = (configuredLevel.currentBubble == 2);
            configuredLevel.speechBubbles.get(3).triggered = (configuredLevel.currentBubble == 3);
        }
        if (currentLevel == 12) {
            configuredLevel.catacombs.get(4).longCatacomb = true;
            configuredLevel.catacombs.get(5).longCatacomb = true;
            configuredLevel.catacombs.get(9).longCatacomb = true;
            configuredLevel.catacombs.get(11).longCatacomb = true;
            configuredLevel.catacombs.get(11).stalactites = true;
            configuredLevel.catacombs.get(8).floorSpikes = true;
            //player hits floor spikes
            if (configuredLevel.catacombs.get(8).touchingFloorSpikes(new Vector2(configuredLevel.getPlayer().getPosition().x, (configuredLevel.getPlayer().getPosition().y - Constants.PLAYER_HEIGHT) - Constants.HEAD_SIZE * 2f))) {
                configuredLevel.defeat = true;
                configuredLevel.verdict.verdict = false;
                configuredLevel.verdict.verdictPhrase = "That is going to hurt!!";
            }
            //guards capture player
            if (((configuredLevel.guards.get(0).guardState == Enums.GuardState.ATTACK && configuredLevel.guards.get(0).guardTouchesPlayer()) || (configuredLevel.guards.get(1).guardState == Enums.GuardState.ATTACK && configuredLevel.guards.get(1).guardTouchesPlayer()) || (configuredLevel.guards.get(2).guardState == Enums.GuardState.ATTACK && configuredLevel.guards.get(2).guardTouchesPlayer())) && !configuredLevel.getPlayer().shock) {
                configuredLevel.defeat = true; configuredLevel.verdict.verdict = false; configuredLevel.verdict.verdictPhrase = "Better luck next time!";
            }
            //initialize eyes
            configuredLevel.guards.get(0).eyeLookLeft = new Vector2(-Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
            configuredLevel.guards.get(0).eyeLookRight = new Vector2(Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
            //initialize mouth
            configuredLevel.guards.get(0).mouthOffset = new Vector2(-0.6f, 0f);
            configuredLevel.guards.get(0).facing = Enums.Facing.LEFT;
            //state
            if (configuredLevel.currentBubble < 16) {
                configuredLevel.guards.get(3).guardState = Enums.GuardState.DEFEATED;
            }
            //close door behind player
            if (configuredLevel.currentCatacomb == 5) {
                configuredLevel.catacombs.get(5).getLockedDoors().set(1, "Closed");
                configuredLevel.catacombs.get(4).getLockedDoors().set(4, "Closed");
            }
            //player beat dagger guard
            if (configuredLevel.currentBubble >= 18) {
                configuredLevel.guards.get(3).guardState = Enums.GuardState.DEFEATED;
                configuredLevel.getPlayer().fighting = false;
                configuredLevel.guards.get(3).guardItem = "";
                configuredLevel.guards.get(3).talkToPlayer = false;
            }
            configuredLevel.guards.get(4).guardState = Enums.GuardState.DEFEATED;
            //initialize eyes
            configuredLevel.guards.get(4).eyeLookLeft = new Vector2(-Constants.EYE_OFFSET + 2, Constants.EYE_OFFSET);
            configuredLevel.guards.get(4).eyeLookRight = new Vector2(Constants.EYE_OFFSET + 2, Constants.EYE_OFFSET);
            //initialize mouth
            configuredLevel.guards.get(4).mouthOffset = new Vector2(0.6f, 0f);
            configuredLevel.guards.get(4).facing = Enums.Facing.RIGHT;
            //sign
            if(configuredLevel.signs.get(0).touchesSign(configuredLevel.getPlayer().getPosition()) && configuredLevel.currentBubble == 0) { configuredLevel.currentBubble = 2; }
            configuredLevel.speechBubbles.get(0).triggered = (configuredLevel.currentCatacomb == 1);
            configuredLevel.speechBubbles.get(1).triggered = (configuredLevel.currentBubble == 1);
            configuredLevel.speechBubbles.get(2).triggered = (configuredLevel.currentBubble == 2);
            configuredLevel.speechBubbles.get(5).triggered = (configuredLevel.currentBubble == 5);
            configuredLevel.speechBubbles.get(6).triggered = (configuredLevel.currentBubble == 6);
            configuredLevel.speechBubbles.get(7).triggered = (configuredLevel.currentBubble == 7);
            configuredLevel.speechBubbles.get(8).triggered = (configuredLevel.currentBubble == 8);
            configuredLevel.speechBubbles.get(9).triggered = (configuredLevel.currentBubble == 9);
            configuredLevel.speechBubbles.get(10).triggered = (configuredLevel.currentBubble == 10);
            configuredLevel.speechBubbles.get(11).triggered = (configuredLevel.currentBubble == 11);
            configuredLevel.speechBubbles.get(12).triggered = (configuredLevel.currentBubble == 12);
            configuredLevel.speechBubbles.get(14).triggered = (configuredLevel.currentBubble == 14);
            configuredLevel.speechBubbles.get(15).triggered = (configuredLevel.currentBubble == 15);
            configuredLevel.speechBubbles.get(16).triggered = (configuredLevel.currentBubble == 16);
            configuredLevel.speechBubbles.get(17).triggered = (configuredLevel.currentBubble == 17);
            configuredLevel.speechBubbles.get(18).triggered = (configuredLevel.currentBubble == 18);
            configuredLevel.speechBubbles.get(22).triggered = (configuredLevel.currentBubble == 22);
            configuredLevel.speechBubbles.get(24).triggered = (configuredLevel.currentBubble == 24);
            configuredLevel.speechBubbles.get(27).triggered = (configuredLevel.currentBubble == 27);
            configuredLevel.speechBubbles.get(27).triggered = (configuredLevel.currentBubble == 27);
            configuredLevel.speechBubbles.get(28).triggered = (configuredLevel.currentBubble == 28);
            configuredLevel.speechBubbles.get(30).triggered = (configuredLevel.currentBubble == 30);
            configuredLevel.speechBubbles.get(46).triggered = (configuredLevel.currentBubble == 46);
            configuredLevel.speechBubbles.get(47).triggered = (configuredLevel.currentBubble == 47);
            configuredLevel.speechBubbles.get(52).triggered = (configuredLevel.currentBubble == 52);
            configuredLevel.speechBubbles.get(56).triggered = (configuredLevel.currentBubble == 56);
            configuredLevel.speechBubbles.get(57).triggered = (configuredLevel.currentBubble == 57);
            configuredLevel.speechBubbles.get(58).triggered = (configuredLevel.currentBubble == 58);
            configuredLevel.speechBubbles.get(60).triggered = (configuredLevel.currentBubble == 60);
            configuredLevel.speechBubbles.get(61).triggered = (configuredLevel.currentBubble == 61);
            configuredLevel.speechBubbles.get(62).triggered = (configuredLevel.currentBubble == 62);
            configuredLevel.speechBubbles.get(63).triggered = (configuredLevel.currentBubble == 63);
        }
        if (currentLevel == 13) {
            configuredLevel.catacombs.get(1).getLockedDoors().set(0, "Closed");
            configuredLevel.catacombs.get(6).getLockedDoors().set(0, "Closed");
            configuredLevel.catacombs.get(2).longCatacomb = true;
            configuredLevel.catacombs.get(5).longCatacomb = true;
            configuredLevel.catacombs.get(8).longCatacomb = true;
            configuredLevel.catacombs.get(9).longCatacomb = true;
            configuredLevel.catacombs.get(10).longCatacomb = true;
            configuredLevel.speechBubbles.get(0).triggered = (configuredLevel.getPlayer().heldItem.itemType.equals("disguise"));
            configuredLevel.speechBubbles.get(1).triggered = (configuredLevel.currentBubble == 1);
            configuredLevel.speechBubbles.get(4).triggered = (configuredLevel.currentBubble == 4);
            configuredLevel.speechBubbles.get(5).triggered = (configuredLevel.currentBubble == 5);
            configuredLevel.speechBubbles.get(6).triggered = (configuredLevel.currentBubble == 6);
            configuredLevel.speechBubbles.get(7).triggered = (configuredLevel.currentBubble == 7);
            configuredLevel.speechBubbles.get(8).triggered = (configuredLevel.currentBubble == 8);
            configuredLevel.speechBubbles.get(9).triggered = (configuredLevel.currentBubble == 9);
            configuredLevel.speechBubbles.get(10).triggered = (configuredLevel.currentBubble == 10);
            configuredLevel.speechBubbles.get(11).triggered = (configuredLevel.currentBubble == 11);
            configuredLevel.speechBubbles.get(12).triggered = (configuredLevel.currentBubble == 12);
            configuredLevel.speechBubbles.get(13).triggered = (configuredLevel.currentBubble == 13);
            configuredLevel.speechBubbles.get(18).triggered = (configuredLevel.currentBubble == 18);
            configuredLevel.speechBubbles.get(22).triggered = (configuredLevel.currentBubble == 22);
            configuredLevel.speechBubbles.get(23).triggered = (configuredLevel.currentBubble == 23);
            configuredLevel.speechBubbles.get(24).triggered = (configuredLevel.currentBubble == 24);
            configuredLevel.speechBubbles.get(25).triggered = (configuredLevel.currentBubble == 25);
            configuredLevel.speechBubbles.get(28).triggered = (configuredLevel.currentBubble == 28);
            configuredLevel.speechBubbles.get(29).triggered = (configuredLevel.currentBubble == 29);
            configuredLevel.speechBubbles.get(40).triggered = (configuredLevel.currentBubble == 40);
            configuredLevel.speechBubbles.get(42).triggered = (configuredLevel.currentBubble == 42);
            configuredLevel.speechBubbles.get(56).triggered = (configuredLevel.currentBubble == 56);
            configuredLevel.speechBubbles.get(57).triggered = (configuredLevel.currentBubble == 57);
            configuredLevel.speechBubbles.get(58).triggered = (configuredLevel.currentBubble == 58);
            configuredLevel.speechBubbles.get(59).triggered = (configuredLevel.currentBubble == 59);
            configuredLevel.speechBubbles.get(61).triggered = (configuredLevel.currentBubble == 61);
            configuredLevel.speechBubbles.get(62).triggered = (configuredLevel.currentBubble == 62);
            configuredLevel.speechBubbles.get(64).triggered = (configuredLevel.currentBubble == 64);
            configuredLevel.speechBubbles.get(65).triggered = (configuredLevel.currentBubble == 65);
            configuredLevel.speechBubbles.get(68).triggered = (configuredLevel.currentBubble == 68);
            configuredLevel.speechBubbles.get(69).triggered = (configuredLevel.currentBubble == 69);
            configuredLevel.speechBubbles.get(71).triggered = (configuredLevel.currentBubble == 71);
            configuredLevel.speechBubbles.get(72).triggered = (configuredLevel.currentBubble == 72);
            configuredLevel.speechBubbles.get(79).triggered = (configuredLevel.currentBubble == 79);
            configuredLevel.speechBubbles.get(80).triggered = (configuredLevel.currentBubble == 80);
            configuredLevel.speechBubbles.get(82).triggered = (configuredLevel.currentBubble == 82);
            configuredLevel.speechBubbles.get(83).triggered = (configuredLevel.currentBubble == 83);
            configuredLevel.speechBubbles.get(84).triggered = (configuredLevel.currentBubble == 84);
            configuredLevel.speechBubbles.get(85).triggered = (configuredLevel.currentBubble == 85);
            configuredLevel.speechBubbles.get(86).triggered = (configuredLevel.currentBubble == 86);
            configuredLevel.speechBubbles.get(87).triggered = (configuredLevel.currentBubble == 87);
            configuredLevel.speechBubbles.get(88).triggered = (configuredLevel.currentBubble == 88);
            configuredLevel.speechBubbles.get(89).triggered = (configuredLevel.currentBubble == 89);
            configuredLevel.speechBubbles.get(92).triggered = (configuredLevel.currentBubble == 92);
            configuredLevel.speechBubbles.get(93).triggered = (configuredLevel.currentBubble == 93);
            configuredLevel.speechBubbles.get(95).triggered = (configuredLevel.currentBubble == 95);
            configuredLevel.speechBubbles.get(96).triggered = (configuredLevel.currentBubble == 96);
            configuredLevel.speechBubbles.get(107).triggered = (configuredLevel.currentBubble == 107);
            configuredLevel.speechBubbles.get(108).triggered = (configuredLevel.currentBubble == 108);
            configuredLevel.speechBubbles.get(116).triggered = (configuredLevel.currentBubble == 116);
            configuredLevel.speechBubbles.get(127).triggered = (configuredLevel.currentBubble == 127);
            configuredLevel.speechBubbles.get(135).triggered = (configuredLevel.currentBubble == 135);
            configuredLevel.speechBubbles.get(137).triggered = (configuredLevel.currentBubble == 137);

            //set currentBubble to 4 if you don't even get the guard clothes
            if (configuredLevel.currentCatacomb == 7 && configuredLevel.currentBubble < 4) { configuredLevel.currentBubble = 4; }

            //initialize eyes
            configuredLevel.prisoners.get(0).eyeLookLeft = new Vector2(-Constants.EYE_OFFSET + 2, Constants.EYE_OFFSET);
            configuredLevel.prisoners.get(0).eyeLookRight = new Vector2(Constants.EYE_OFFSET + 2, Constants.EYE_OFFSET);
            //initialize mouth
            configuredLevel.prisoners.get(0).mouthOffset = new Vector2(0.6f, 0f);
            configuredLevel.prisoners.get(0).facing = Enums.Facing.RIGHT;
            //initialize eyes
            configuredLevel.guards.get(0).eyeLookLeft = new Vector2(-Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
            configuredLevel.guards.get(0).eyeLookRight = new Vector2(Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
            //initialize mouth
            configuredLevel.guards.get(0).mouthOffset = new Vector2(-0.6f, 0f);
            configuredLevel.guards.get(0).facing = Enums.Facing.LEFT;
            if (configuredLevel.currentBubble < 72) {
                //initialize eyes
                configuredLevel.guards.get(1).eyeLookLeft = new Vector2(-Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
                configuredLevel.guards.get(1).eyeLookRight = new Vector2(Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
                //initialize mouth
                configuredLevel.guards.get(1).mouthOffset = new Vector2(-0.6f, 0f);
                configuredLevel.guards.get(1).facing = Enums.Facing.LEFT;
            }
            //initialize guardState
            if (configuredLevel.currentBubble != 10) {
                configuredLevel.guards.get(0).guardState = Enums.GuardState.DEFEATED;
            }
            if (configuredLevel.currentBubble < 84) {
                configuredLevel.guards.get(1).guardState = Enums.GuardState.DEFEATED;
            }
            configuredLevel.guards.get(2).guardState = Enums.GuardState.DEFEATED;
            configuredLevel.guards.get(3).guardState = Enums.GuardState.DEFEATED;
            //initialize eyes
            configuredLevel.guards.get(2).eyeLookLeft = new Vector2(-Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
            configuredLevel.guards.get(2).eyeLookRight = new Vector2(Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
            //initialize mouth
            configuredLevel.guards.get(2).mouthOffset = new Vector2(-0.6f, 0f);
            configuredLevel.guards.get(2).facing = Enums.Facing.LEFT;

            //guards with stun guns
            configuredLevel.guards.get(4).guardState = Enums.GuardState.DEFEATED;
            configuredLevel.guards.get(5).guardState = Enums.GuardState.DEFEATED;
            //initialize eyes
            configuredLevel.guards.get(4).eyeLookLeft = new Vector2(-Constants.EYE_OFFSET + 2, Constants.EYE_OFFSET);
            configuredLevel.guards.get(4).eyeLookRight = new Vector2(Constants.EYE_OFFSET + 2, Constants.EYE_OFFSET);
            //initialize mouth
            configuredLevel.guards.get(4).mouthOffset = new Vector2(0.6f, 0f);
            configuredLevel.guards.get(4).facing = Enums.Facing.RIGHT;
            //initialize eyes
            configuredLevel.guards.get(5).eyeLookLeft = new Vector2(-Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
            configuredLevel.guards.get(5).eyeLookRight = new Vector2(Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
            //initialize mouth
            configuredLevel.guards.get(5).mouthOffset = new Vector2(-0.6f, 0f);
            configuredLevel.guards.get(5).facing = Enums.Facing.LEFT;

            //bomb knocks out guards
            if (configuredLevel.items.size > 0 && configuredLevel.items.get(configuredLevel.items.size - 1).bomb.exploding) {
                if ((configuredLevel.items.get(configuredLevel.items.size - 1).bomb.position.dst(configuredLevel.guards.get(2).position.x, configuredLevel.guards.get(2).position.y - Constants.PLAYER_HEIGHT) < 60) || (configuredLevel.items.get(configuredLevel.items.size - 1).bomb.position.dst(configuredLevel.guards.get(3).position.x, configuredLevel.guards.get(3).position.y - Constants.PLAYER_HEIGHT) < 60)) {
                    configuredLevel.guards.get(2).alert = false;
                    configuredLevel.guards.get(3).alert = false;
                    configuredLevel.guards.get(2).asleep = true;
                    configuredLevel.guards.get(3).asleep = true;
                }
            }
            //guards capture player
            if (((!configuredLevel.guards.get(2).asleep && configuredLevel.guards.get(2).guardTouchesPlayer()) || (!configuredLevel.guards.get(3).asleep && configuredLevel.guards.get(3).guardTouchesPlayer())) && configuredLevel.currentBubble == 86) {
                configuredLevel.currentBubble = 87;
            }
            if (configuredLevel.guards.get(1).talkToPlayer && configuredLevel.currentBubble > 84) {
                configuredLevel.guards.get(1).guardState = Enums.GuardState.ATTACK;
            }
            if (configuredLevel.guards.get(1).guardState == Enums.GuardState.ATTACK && configuredLevel.guards.get(1).guardTouchesPlayer()) {
                configuredLevel.defeat = true; configuredLevel.verdict.verdict = false; configuredLevel.verdict.verdictPhrase = "You were captured by a Guard!";
            }

            //prisoner
            configuredLevel.prisoners.get(0).mouthState = Enums.MouthState.OPEN;
            //close doors behind player
            if (configuredLevel.currentCatacomb == 7) {
                configuredLevel.catacombs.get(6).getLockedDoors().set(3, "Locked");
                configuredLevel.catacombs.get(7).getLockedDoors().set(0, "Locked");
            }
            if (configuredLevel.currentCatacomb == 8) {
                configuredLevel.catacombs.get(7).getLockedDoors().set(1, "Locked");
                configuredLevel.catacombs.get(8).getLockedDoors().set(4, "Locked");
            }
            if (configuredLevel.currentCatacomb == 9 && configuredLevel.guards.get(3).guardItem.equals("key")) {
                configuredLevel.catacombs.get(8).getLockedDoors().set(3, "Locked");
                configuredLevel.catacombs.get(9).getLockedDoors().set(0, "Locked");
            }
            //change into guard disguise
            if (configuredLevel.torchUp && configuredLevel.currentBubble == 2) { configuredLevel.getPlayer().disguise = true; configuredLevel.inventory.deleteCurrentItem(); }

            if (configuredLevel.currentBubble == 2 && configuredLevel.getPlayer().disguise && !configuredLevel.torchFade) {
                configuredLevel.currentBubble = 5;
            }
        }
        if (currentLevel == 14) {
            configuredLevel.catacombs.get(0).longCatacomb = true;
            configuredLevel.catacombs.get(2).longCatacomb = true;
            if (configuredLevel.currentBubble < 7) {
            //initialize eyes
            configuredLevel.guards.get(0).eyeLookLeft = new Vector2(-Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
            configuredLevel.guards.get(0).eyeLookRight = new Vector2(Constants.EYE_OFFSET - 2, Constants.EYE_OFFSET);
            //initialize mouth
            configuredLevel.guards.get(0).mouthOffset = new Vector2(-0.6f, 0f);
            configuredLevel.guards.get(0).facing = Enums.Facing.LEFT;
                configuredLevel.guards.get(0).guardState = Enums.GuardState.DEFEATED;
            }
            //set player's direction
            if (configuredLevel.currentBubble >= 48) {
                if (configuredLevel.getPlayer().getPosition().x > configuredLevel.bosses.get(0).position.x) {
                    configuredLevel.getPlayer().facing = Enums.Facing.LEFT;
                } else if (configuredLevel.getPlayer().getPosition().x <= configuredLevel.bosses.get(0).position.x) {
                    configuredLevel.getPlayer().facing = Enums.Facing.RIGHT;
                }
            }

            //set player's item in hand
            if (configuredLevel.currentBubble >= 57 && configuredLevel.currentBubble < 66) {
                configuredLevel.getPlayer().heldItem.itemType = "phone";
            }

            //initialize boss mouth
            configuredLevel.bosses.get(0).mouthOffset.y = 0f;

            configuredLevel.speechBubbles.get(0).triggered = (configuredLevel.currentBubble == 0);
            configuredLevel.speechBubbles.get(1).triggered = (configuredLevel.currentBubble == 1);
            configuredLevel.speechBubbles.get(6).triggered = (configuredLevel.currentBubble == 6);
            configuredLevel.speechBubbles.get(7).triggered = (configuredLevel.currentBubble == 7);
            configuredLevel.speechBubbles.get(8).triggered = (configuredLevel.currentBubble == 8);
            configuredLevel.speechBubbles.get(9).triggered = (configuredLevel.currentBubble == 9);
            configuredLevel.speechBubbles.get(13).triggered = (configuredLevel.currentBubble == 13);
            configuredLevel.speechBubbles.get(14).triggered = (configuredLevel.currentBubble == 14);
            configuredLevel.speechBubbles.get(15).triggered = (configuredLevel.currentBubble == 15);
            configuredLevel.speechBubbles.get(16).triggered = (configuredLevel.currentBubble == 16);
            configuredLevel.speechBubbles.get(17).triggered = (configuredLevel.currentBubble == 17);
            configuredLevel.speechBubbles.get(19).triggered = (configuredLevel.currentBubble == 19);
            configuredLevel.speechBubbles.get(20).triggered = (configuredLevel.currentBubble == 20);
            configuredLevel.speechBubbles.get(21).triggered = (configuredLevel.currentBubble == 21);
            configuredLevel.speechBubbles.get(22).triggered = (configuredLevel.currentBubble == 22);
            configuredLevel.speechBubbles.get(23).triggered = (configuredLevel.currentBubble == 23);
            configuredLevel.speechBubbles.get(26).triggered = (configuredLevel.currentBubble == 26);
            configuredLevel.speechBubbles.get(28).triggered = (configuredLevel.currentBubble == 28);
            configuredLevel.speechBubbles.get(29).triggered = (configuredLevel.currentBubble == 29);
            configuredLevel.speechBubbles.get(30).triggered = (configuredLevel.currentBubble == 30);
            configuredLevel.speechBubbles.get(32).triggered = (configuredLevel.currentBubble == 32);
            configuredLevel.speechBubbles.get(33).triggered = (configuredLevel.currentBubble == 33);
            configuredLevel.speechBubbles.get(34).triggered = (configuredLevel.currentBubble == 34);
            configuredLevel.speechBubbles.get(35).triggered = (configuredLevel.currentBubble == 35);
            configuredLevel.speechBubbles.get(36).triggered = (configuredLevel.currentBubble == 36);
            configuredLevel.speechBubbles.get(37).triggered = (configuredLevel.currentBubble == 37);
            configuredLevel.speechBubbles.get(38).triggered = (configuredLevel.currentBubble == 38);
            configuredLevel.speechBubbles.get(39).triggered = (configuredLevel.currentBubble == 39);
            configuredLevel.speechBubbles.get(40).triggered = (configuredLevel.currentBubble == 40);
            configuredLevel.speechBubbles.get(41).triggered = (configuredLevel.currentBubble == 41);
            configuredLevel.speechBubbles.get(42).triggered = (configuredLevel.currentBubble == 42);
            configuredLevel.speechBubbles.get(43).triggered = (configuredLevel.currentBubble == 43);
            configuredLevel.speechBubbles.get(46).triggered = (configuredLevel.currentBubble == 46);
            configuredLevel.speechBubbles.get(47).triggered = (configuredLevel.currentBubble == 47);
            configuredLevel.speechBubbles.get(49).triggered = (configuredLevel.currentBubble == 49);
            configuredLevel.speechBubbles.get(50).triggered = (configuredLevel.currentBubble == 50);
            configuredLevel.speechBubbles.get(51).triggered = (configuredLevel.currentBubble == 51);
            configuredLevel.speechBubbles.get(52).triggered = (configuredLevel.currentBubble == 52);
            configuredLevel.speechBubbles.get(55).triggered = (configuredLevel.currentBubble == 55);
            configuredLevel.speechBubbles.get(56).triggered = (configuredLevel.currentBubble == 56);
            configuredLevel.speechBubbles.get(57).triggered = (configuredLevel.currentBubble == 57);
            configuredLevel.speechBubbles.get(60).triggered = (configuredLevel.currentBubble == 60);
            configuredLevel.speechBubbles.get(61).triggered = (configuredLevel.currentBubble == 61);
            configuredLevel.speechBubbles.get(62).triggered = (configuredLevel.currentBubble == 62);
            configuredLevel.speechBubbles.get(63).triggered = (configuredLevel.currentBubble == 63);
            configuredLevel.speechBubbles.get(66).triggered = (configuredLevel.currentBubble == 66);
            configuredLevel.speechBubbles.get(67).triggered = (configuredLevel.currentBubble == 67);
            configuredLevel.speechBubbles.get(71).triggered = (configuredLevel.currentBubble == 71);
            configuredLevel.speechBubbles.get(72).triggered = (configuredLevel.currentBubble == 72);
        }
        //escape
        if (configuredLevel.exitDoor.playerHasEscaped) {
            configuredLevel.continueBubbles = false;
            configuredLevel.verdict.verdict = true;
            configuredLevel.verdict.verdictPhrase = victoryPhrases.get(currentLevel);
            configuredLevel.victory = true;
        }
    }

}
