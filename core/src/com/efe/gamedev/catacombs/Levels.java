package com.efe.gamedev.catacombs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.efe.gamedev.catacombs.entities.Catacomb;
import com.efe.gamedev.catacombs.entities.Chest;
import com.efe.gamedev.catacombs.entities.Exit;
import com.efe.gamedev.catacombs.entities.Guard;
import com.efe.gamedev.catacombs.entities.Item;
import com.efe.gamedev.catacombs.entities.Puzzle;
import com.efe.gamedev.catacombs.entities.SpeechBubble;
import com.efe.gamedev.catacombs.entities.Word;
import com.efe.gamedev.catacombs.entities.levelVerdict;
import com.efe.gamedev.catacombs.items.Diamond;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 12/28/2017.
 * This class keep track of all the levels in the game.
 * It may look complicated but it isn't really
 */

public class Levels {

    public int currentLevel;
    private Array<String> victoryPhrases;

    public Levels () {
        //the levels are stored in an array, so the first level is level 0.
        currentLevel = 0;
        victoryPhrases = new Array<String>();
        victoryPhrases.add("Beginners Luck!");
        victoryPhrases.add("Nice Jumps!");
        victoryPhrases.add("Amazing Job!");
        victoryPhrases.add("You are almost ready!");
        victoryPhrases.add("Difficulty Increasing...");
    }

    //setup stuff for each level
    public void configureLevel(final Level configuredLevel) {
        //stuff that is the same for each level
        configuredLevel.viewportPosition = new Vector2();
        configuredLevel.touchPosition = new Vector2();
        configuredLevel.lookPosition = new Vector2();

        configuredLevel.victory = false;
        configuredLevel.defeat = false;
        configuredLevel.verdict = new levelVerdict(configuredLevel);

        configuredLevel.targetBox.target = false;
        configuredLevel.continueBubbles = true;
        configuredLevel.show = true;
        configuredLevel.targetBox.isArrow = false;
        configuredLevel.levelStarted = false;
        configuredLevel.inventory.paused = false;
        configuredLevel.inventory.inventoryItems = new DelayedRemovalArray<Item>();
        configuredLevel.inventory.selectedItem = -1;
        configuredLevel.inventory.scoreDiamonds = new DelayedRemovalArray<Diamond>();
        configuredLevel.inventory.speechButton.speech = true;
        //initialize torch timer
        configuredLevel.torchLight = 10f;
        configuredLevel.startTime = TimeUtils.nanoTime();
        configuredLevel.getPlayer().getVelocity().set(new Vector2());
        configuredLevel.getPlayer().legRotate = 170;
        configuredLevel.getPlayer().armRotate = 0;
        configuredLevel.getPlayer().startTime = TimeUtils.nanoTime();
        configuredLevel.getPlayer().viewportPosition = new Vector2();

        switch (currentLevel) {
            case 0:
                configuredLevel.touchLocked = true;
                /*LEVEL CONFIGURATIONS:*/
                //player always starts inside the currentCatacomb
                configuredLevel.catacombs = new Array<Catacomb>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -100), "Closed", "Closed", "Closed", "Locked", "Closed", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -15), "Locked", "Closed", "Closed", "Closed", "Closed", "Closed"));
                configuredLevel.currentCatacomb = 1;

                configuredLevel.items = new DelayedRemovalArray<Item>();
                configuredLevel.items.add(new Item(new Vector2(60, 10), configuredLevel.viewportPosition, "key"));

                configuredLevel.puzzles = new Array<Puzzle>();

                configuredLevel.words = new Array<Word>();

                configuredLevel.chests = new Array<Chest>();

                configuredLevel.guards = new Array<Guard>();

                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<SpeechBubble>();
                //A SpeechBubble consists of these parameters: (mainText, targetText, numberOfOptions, option1, option2, option3, option4, function1, function2, function3, function4, level);
                configuredLevel.speechBubbles.add(new SpeechBubble("Where am I?", "Player:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Some kind of tomb?", "Player:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hey, there is something by my ear!", "Player:", 1, "Turn earpiece on", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 3;
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("BEEP! Greetings, my name is c7-x", "c7-x:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("My mission is to help people in need", "c7-x:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Are you in need?", "c7-x:", 3, "Nope", "Well, yes", "Can you say that again?", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 6;
                                configuredLevel.continueBubbles = false;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 7;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 5;
                                configuredLevel.speechBubbles.get(configuredLevel.currentBubble).speechBubbleAlpha = 0;
                                configuredLevel.speechBubbles.get(configuredLevel.currentBubble).option1Alpha = 0;
                                configuredLevel.speechBubbles.get(configuredLevel.currentBubble).option2Alpha = 0;
                                configuredLevel.speechBubbles.get(configuredLevel.currentBubble).option3Alpha = 0;
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("OK, shutting down", "c7-x:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.defeat = true;
                                configuredLevel.verdict.verdict = false;
                                configuredLevel.verdict.verdictPhrase = "You needed c7-x to help you!";
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Alright, what is your condition?", "c7-x:", 4, "I am Alive", "Good, but confused", "Not so good", "Horrible",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 8;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 9;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 9;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 13;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("I can see that", "c7-x:", 1, "Try again", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 7;
                                configuredLevel.speechBubbles.get(configuredLevel.currentBubble).speechBubbleAlpha = 0;
                                configuredLevel.speechBubbles.get(configuredLevel.currentBubble).option1Alpha = 0;
                                configuredLevel.speechBubbles.get(configuredLevel.currentBubble).option2Alpha = 0;
                                configuredLevel.speechBubbles.get(configuredLevel.currentBubble).option3Alpha = 0;
                                configuredLevel.speechBubbles.get(configuredLevel.currentBubble).option4Alpha = 0;
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("BEEP! Scanning your surroundings...", "c7-x:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hmm, It appears you are\n45.3 miles underground", "c7-x:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("WHAT! How did I get here?", "Player:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("I don't know the answer to that question", "c7-x:", 4, "Ask for advice", "Troubleshoot", "Turn c7-x off", "Ask a question",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 22;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 15;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 6;
                                configuredLevel.continueBubbles = false;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 19;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("I sense that you are not in any\nphysical pain", "c7-x:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("How can the status you give\nme be 'horrible'?", "c7-x:", 3, "It's an expression", "I was kidding", "Well, how else would you put it?", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 42;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 42;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 43;
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Can you teleport me out of here\nor something?", "Player:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("No", "c7-x:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("How about clearing me\na path to the surface?", "Player:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("That assignment is beyond my capabilities", "c7-x:", 3, "Ask for advice", "Look around", "Turn c7-x off", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 22;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 21;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 6;
                                configuredLevel.continueBubbles = false;
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Is your name really c7-x?", "Player:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("My full name is c7-x345Qy78G224hb-5Y,\n But you can call me c7-x", "c7-x:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("This place is really creepy!", "Player:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("What should I do, c7-x?", "Player:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hmm, let me look at your surroundings...", "c7-x:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("................", "c7-x:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("It appears that there is a key\nto your left. It may help you", "c7-x:", 1, "Pick up key", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.touchLocked = false;
                                if (configuredLevel.items.size != 0) {
                                    configuredLevel.items.get(0).targeted = true;
                                }
                                configuredLevel.show = false;
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.touchLocked = true;
                                configuredLevel.currentBubble = 26;
                                configuredLevel.show = true;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Great Job!", "c7-x:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now, to use the key you need\nto tap on it", "c7-x:", 1, "Tap on key", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.touchLocked = false;
                                if (configuredLevel.inventory.inventoryItems.size != 0) {
                                    configuredLevel.inventory.inventoryItems.get(0).targeted = true;
                                }
                                configuredLevel.show = false;
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.touchLocked = true;
                                if (configuredLevel.inventory.inventoryItems.size != 0) {
                                    configuredLevel.inventory.inventoryItems.get(0).targeted = false;
                                }
                                configuredLevel.currentBubble = 28;
                                configuredLevel.show = true;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("At this pace you might actually survive!", "c7-x:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Thanks?", "Player:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("By the way, how did YOU\nget this far underground?", "Player:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("BEEP! That information is classified", "c7-x:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("What! By whom!?", "Player:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Do you want me to\nhelp you or not?", "c7-x:", 2, "Yes", "No", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 34;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 6;
                                configuredLevel.continueBubbles = false;
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Next, you will need to unlock\nthat door in front of you", "c7-x:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("With what?", "Player:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("The key!", "c7-x:", 1, "Unlock Door", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.position.set(new Vector2(41.5f, 10.5f));
                                configuredLevel.show = false;
                                configuredLevel.touchLocked = false;
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.touchLocked = true;
                                if (configuredLevel.player.jumpState == Enums.JumpState.GROUNDED) {
                                    configuredLevel.show = true;
                                    configuredLevel.currentBubble = 37;
                                }
                                configuredLevel.targetBox.target = false;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Good work!", "c7-x:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now, what do you want me\nto do?", "Player:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("I think you can handle this\non your own!", "c7-x:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("You can move around by\ntapping the screen", "c7-x:", 0, "", "", "", "",
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("All you need to do is to\nescape through this exit door!", "c7-x:", 1, "Escape the Level", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.touchLocked = false;
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("In that case, we should\nstop wasting our time", "c7-x:", 3, "Ask for advice", "Look around", "Turn c7-x off", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 22;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 21;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 6;
                                configuredLevel.continueBubbles = false;
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("I would call it: 'a struggle'", "c7-x:", 3, "Ask for advice", "Look around", "Turn c7-x off", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 22;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 21;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 6;
                                configuredLevel.continueBubbles = false;
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(100, 150));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(-50, -82), new Vector2(-100, -84), configuredLevel);

                break;
            case 1:
                configuredLevel.touchLocked = true;

                configuredLevel.catacombs = new Array<Catacomb>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(200, -385), "Closed", "Unlocked", "Closed", "Closed", "Closed", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(50, -300), "Closed", "Unlocked", "Closed", "Closed", "Unlocked", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-100, -215), "Closed", "Locked", "Closed", "Closed", "Unlocked", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-250, -130), "Closed", "Locked", "Closed", "Closed", "Locked", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-400, -45), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed"));
                configuredLevel.currentCatacomb = 0;

                configuredLevel.items = new DelayedRemovalArray<Item>();
                configuredLevel.items.add(new Item(new Vector2(-190, -105), configuredLevel.viewportPosition, "key"));

                configuredLevel.puzzles = new Array<Puzzle>();

                configuredLevel.words = new Array<Word>();

                configuredLevel.chests = new Array<Chest>();
                configuredLevel.chests.add(new Chest(new Vector2(-40, -197), configuredLevel, new Runnable() { public void run() { configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0,0), configuredLevel.viewportPosition, "key")); } }));

                configuredLevel.guards = new Array<Guard>();

                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<SpeechBubble>();
                configuredLevel.speechBubbles.add(new SpeechBubble("You can pause the game by tapping the\n pause button in the bottom left corner", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("If you ever get stuck, you\ncan reset the level there!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Well, I still don't know my location!", "Player:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Don't worry,\nI've searched my databases and...", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("I have found that each of these hexagons\nare called a catacomb!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("We are in the Catacombs! An underground\ncemetery full of tombs and tunnels!", "c7-x:", 4, "Lovely", "How are you so optimistic?", "Get me out of here!", "Great",
                        new Runnable() { public void run() { configuredLevel.currentBubble = 6; } },
                        new Runnable() { public void run() { configuredLevel.currentBubble = 7; } },
                        new Runnable() { public void run() { configuredLevel.currentBubble = 13; } },
                        new Runnable() { public void run() { configuredLevel.currentBubble = 6; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("I wouldn't go that far, but\nit is pretty nice down here!", "c7-x:", 2, "Interrogate c7-x", "Ask for help", "", "",
                        new Runnable() { public void run() { configuredLevel.currentBubble = 8; } },
                        new Runnable() { public void run() { configuredLevel.currentBubble = 13; } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("It's in my programming!", "c7-x:", 2, "Tell me more...", "Help me escape!", "", "",
                        new Runnable() { public void run() { configuredLevel.currentBubble = 8; } },
                        new Runnable() { public void run() { configuredLevel.currentBubble = 13; } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("How am I the only human down here?", "Player:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() {  } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("I'm sorry, but I cannot answer that.", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("So you do know!?", "Player:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("I know how you can escape", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Fine.\nTell me.", "Player:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("First, come up to the\nleft side of this catacomb", "c7-x:", 1, "Okay", "", "", "",
                        new Runnable() { public void run() { configuredLevel.show = false; configuredLevel.continueBubbles = false; configuredLevel.touchLocked = false; configuredLevel.targetBox.target = true; configuredLevel.targetBox.isArrow = true; configuredLevel.targetBox.left = true; configuredLevel.targetBox.position.set(new Vector2(250, -350)); } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.currentBubble = 14; configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.targetBox.target = false; configuredLevel.touchLocked = true; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("That's the easy part...", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now you need to jump\nto the next catacomb!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("To jump, you need to tap twice in the very\ntop corner that you want to jump to", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Right now, you need to jump\nto the top left corner!", "c7-x:", 1, "Jump time!", "", "", "",
                        new Runnable() { public void run() { configuredLevel.show = false; configuredLevel.continueBubbles = false; configuredLevel.touchLocked = false; configuredLevel.targetBox.target = true; configuredLevel.targetBox.isArrow = false; configuredLevel.targetBox.itemWidth = 5; configuredLevel.targetBox.position.set(new Vector2(170, -260)); } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.currentBubble = 18; configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.targetBox.target = false; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Pro skills!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("I would jump for you,\nbut I don't have legs!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("It looks like there is another\ncatacomb on your left to be jumped", "c7-x:", 1, "On it!", "", "", "",
                        new Runnable() { public void run() { configuredLevel.show = false; configuredLevel.continueBubbles = false; configuredLevel.touchLocked = false; configuredLevel.targetBox.target = true; configuredLevel.targetBox.isArrow = true; configuredLevel.targetBox.left = true; configuredLevel.targetBox.position.set(new Vector2(50, -170)); } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { if (configuredLevel.viewportPosition.x < 40) { configuredLevel.viewportPosition.x = 40; } configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.targetBox.target = false; configuredLevel.currentBubble = 21; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Wow, is that a treasure chest!", "Player:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("We are in a tomb, so it might be!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Some chests will need items\nto be unlocked...", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("But this chest already is! Tap on it!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { if (!configuredLevel.speechBubbles.get(configuredLevel.currentBubble).textLoaded) { configuredLevel.touchPosition.set(new Vector2()); } configuredLevel.continueBubbles = false; if (!configuredLevel.chests.get(0).opened) { configuredLevel.touchLocked = false; } if (configuredLevel.touchPosition.x > configuredLevel.chests.get(0).position.x && configuredLevel.touchPosition.x < configuredLevel.chests.get(0).position.x + 40 && configuredLevel.touchPosition.y > configuredLevel.chests.get(0).position.y && configuredLevel.touchPosition.y < configuredLevel.chests.get(0).position.y + 26 && configuredLevel.speechBubbles.get(configuredLevel.currentBubble).textLoaded) { configuredLevel.show = false; configuredLevel.touchPosition.set(new Vector2(configuredLevel.chests.get(0).position.x + 2, configuredLevel.chests.get(0).position.y + 2)); } if (configuredLevel.chests.get(0).opened) { configuredLevel.show = true; configuredLevel.touchLocked = true; configuredLevel.currentBubble = 25; } } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("It gave me a key!", "Player:", configuredLevel.targetBox.target ? 0 : 1, "Proceed", "", "", "",
                        new Runnable() { public void run() { configuredLevel.viewportPosition.set(-41, 0); configuredLevel.targetBox.target = true; configuredLevel.targetBox.isArrow = true; configuredLevel.show = false; configuredLevel.targetBox.position.set(new Vector2(-60, -160)); } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.targetBox.target = false; configuredLevel.currentBubble = 26; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Some doors are located higher up!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.getPlayer().mouthState = Enums.MouthState.NORMAL; configuredLevel.targetBox.target = true; configuredLevel.targetBox.isArrow = false; configuredLevel.continueBubbles = true; configuredLevel.targetBox.position.set(new Vector2(-97.5f, -104.5f)); } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("To unlock them, all you need is a key", "c7-x:", configuredLevel.inventory.selectedItem == -1 ? 1 : 0, "Grab key", "", "", "",
                        new Runnable() { public void run() { configuredLevel.touchLocked = false; configuredLevel.show = false; if (configuredLevel.inventory.selectedItem == -1 && configuredLevel.inventory.inventoryItems.size >= 1) { configuredLevel.inventory.inventoryItems.get(0).targeted = true; } } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.show = true; configuredLevel.touchLocked = true; configuredLevel.inventory.inventoryItems.get(0).targeted = false; configuredLevel.currentBubble = 28; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Try unlocking the door\n in the upper left corner!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.show = false; configuredLevel.continueBubbles = false; if (configuredLevel.catacombs.get(2).getLockedDoors().get(1).equals("Unlocked")) {configuredLevel.targetBox.isArrow = true; configuredLevel.touchLocked = true; configuredLevel.targetBox.position.set(new Vector2(-100, -100)); configuredLevel.currentBubble = 30; configuredLevel.show = true;} else { configuredLevel.touchLocked = false; } } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Not bad, now jump up to the\ncatacomb in the top left corner!", "c7-x:", 1, "Okay", "", "", "",
                        new Runnable() { public void run() { configuredLevel.currentBubble = 31; } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.show = false; if (configuredLevel.viewportPosition.x < -110) { configuredLevel.viewportPosition.x = -110; } if (configuredLevel.speechBubbles.get(configuredLevel.currentBubble).textLoaded) { configuredLevel.touchLocked = false; } else { configuredLevel.touchLocked = true; } if (configuredLevel.currentCatacomb == 3) { configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.targetBox.target = false; configuredLevel.continueBubbles = true; configuredLevel.currentBubble = 32; } } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Exhilarating!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Do you see that red\npad to the left of you?", "c7-x:", 2, "Yes", "Nope", "", "",
                        new Runnable() { public void run() { configuredLevel.currentBubble = 37; } },
                        new Runnable() { public void run() { configuredLevel.currentBubble = 34; } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("It is on the floor of this catacomb", "c7-x:", 2, "I see it now!", "Huh?", "", "",
                        new Runnable() { public void run() { configuredLevel.currentBubble = 37; } },
                        new Runnable() { public void run() { configuredLevel.currentBubble = 35; } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("This is not funny!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now do you see it?", "c7-x:", 1, "Yes, I do", "", "", "",
                        new Runnable() { public void run() { configuredLevel.currentBubble = 37; } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.exitDoor.padColor = new Color(MathUtils.random(0.2f, 0.6f), MathUtils.random(0.2f, 0.6f), MathUtils.random(0.2f, 0.6f), 1); } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Good!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.exitDoor.padColor = Color.RED; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("The pad is called the Exit Pad\nand unlocks the Exit Door!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("To activate the Exit Pad,\nyou must step on it", "c7-x:", 1, "Step on pad", "", "", "",
                        new Runnable() { public void run() { configuredLevel.show = false; configuredLevel.touchLocked = false; } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.viewportPosition.set(new Vector2(configuredLevel.getPlayer().getPosition())); configuredLevel.show = true; configuredLevel.touchLocked = true; configuredLevel.continueBubbles = true; configuredLevel.currentBubble = 40; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Alright!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.getPlayer().resetLegs(); } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Remember, to escape the level...", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("You must first step on the Exit Pad\nthen go to the Exit Door!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Hey, I see a key to my left!", "Player:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Walk over to the key to grab it", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { if (configuredLevel.items.size >= 1) { configuredLevel.items.get(0).targeted = true; configuredLevel.touchLocked = false; configuredLevel.continueBubbles = false; } else { configuredLevel.touchLocked = true; configuredLevel.currentBubble = 45; } } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now use it to unlock the door\nin the top left corner!", "c7-x:", 1, "Unlock door", "", "", "",
                        new Runnable() { public void run() { configuredLevel.touchLocked = false; configuredLevel.show = false; if (configuredLevel.inventory.selectedItem != -1) { configuredLevel.inventory.inventoryItems.get(0).targeted = false; } if (configuredLevel.inventory.selectedItem == -1 && configuredLevel.inventory.inventoryItems.size >= 1) { configuredLevel.inventory.inventoryItems.get(0).targeted = true; } if (configuredLevel.catacombs.get(3).getLockedDoors().get(1).equals("Locked")) { configuredLevel.targetBox.target = true; configuredLevel.targetBox.isArrow = false; configuredLevel.continueBubbles = false; configuredLevel.targetBox.position.set(new Vector2(-97.5f - 150, -104.5f + 85)); } } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { if (configuredLevel.catacombs.get(3).getLockedDoors().get(1).equals("Unlocked")) { configuredLevel.targetBox.isArrow = true; configuredLevel.targetBox.position.set(new Vector2(-100 - 150, -100 + 85)); } if (configuredLevel.currentCatacomb == 4) { configuredLevel.targetBox.target = false; configuredLevel.show = true; configuredLevel.currentBubble = 46; } } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now, escape the level through\nthat Exit Door!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(300, -250));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(-350, -27), new Vector2(-170, -114), configuredLevel);
                break;
            case 2:
                configuredLevel.touchLocked = true;

                configuredLevel.catacombs = new Array<Catacomb>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -185), "Closed", "Closed", "Closed", "Locked", "Locked", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -15), "Closed", "Closed", "Closed", "Closed", "Unlocked", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(150, -270), "Closed", "Locked", "Closed", "Locked", "Closed", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(300, -185), "Locked", "Closed", "Closed", "Closed", "Closed", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(150, -100), "Locked", "Unlocked", "Closed", "Unlocked", "Closed", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(300, -15), "Unlocked", "Closed", "Closed", "Closed", "Locked", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(450, -100), "Closed", "Locked", "Closed", "Closed", "DoubleLocked", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(600, -185), "Closed", "DoubleLocked", "Closed", "Locked", "Closed", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(750, -100), "Locked", "Closed", "Closed", "Closed", "Closed", "Closed"));
                configuredLevel.currentCatacomb = 1;

                configuredLevel.items = new DelayedRemovalArray<Item>();
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

                configuredLevel.puzzles = new Array<Puzzle>();

                configuredLevel.words = new Array<Word>();

                configuredLevel.chests = new Array<Chest>();

                configuredLevel.guards = new Array<Guard>();

                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<SpeechBubble>();
                configuredLevel.speechBubbles.add(new SpeechBubble("In the pause menu there\nare several useful buttons!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("One of them lets you see a\nmap of the level you are in", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Using what you have learned,\ngo and navigate the catacombs!", "c7-x:", 1, "Try it out!", "", "", "",
                        new Runnable() { public void run() { configuredLevel.touchLocked = false; configuredLevel.continueBubbles = false; configuredLevel.show = false; } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.currentBubble = 3; configuredLevel.targetBox.target = true; configuredLevel.targetBox.position.set(new Vector2(191.5f, -74.5f)); configuredLevel.show = true; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("There are diamonds in some catacombs,\nwhich are very valuable!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.touchLocked = false; configuredLevel.show = false; configuredLevel.targetBox.target = false; configuredLevel.currentBubble = 4; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Lets test your jumping skills!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.show = true; configuredLevel.touchLocked = true; configuredLevel.inventory.selectedItem = 0; configuredLevel.targetBox.target = true; configuredLevel.targetBox.position.set(new Vector2(341.5f, -159.5f)); configuredLevel.viewportPosition.set(new Vector2(285, 0)); configuredLevel.continueBubbles = true; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Try unlocking the door\n in the upper right corner!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.touchLocked = false; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.show = false; configuredLevel.continueBubbles = false; if (configuredLevel.catacombs.get(2).getLockedDoors().get(3).equals("Unlocked")) {configuredLevel.targetBox.isArrow = true; configuredLevel.targetBox.left = false; configuredLevel.targetBox.position.set(new Vector2(340, -150)); configuredLevel.currentBubble = 7; configuredLevel.show = true;} } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Superb! Now tap twice in the very\ntop right corner to jump up there!", "c7-x:", 1, "Got it", "", "", "",
                        new Runnable() { public void run() { configuredLevel.show = false; } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.show = true; configuredLevel.targetBox.target = false; configuredLevel.currentBubble = 8; configuredLevel.continueBubbles = true; configuredLevel.touchLocked = true; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Genius!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Not as genius\nas I am, of course!", "c7-x:", 3, "You are a robot!", "Oh, really?", "No way!", "",
                        new Runnable() { public void run() { configuredLevel.currentBubble = 14; } },
                        new Runnable() { public void run() { configuredLevel.currentBubble = 10; } },
                        new Runnable() { public void run() { configuredLevel.currentBubble = 10; } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Well then, what is.....\n9 cubed!", "c7-x:", 4, "28", "The cube of 9", "821", "729",
                        new Runnable() { public void run() { configuredLevel.currentBubble = 13; } },
                        new Runnable() { public void run() { configuredLevel.currentBubble = 11; } },
                        new Runnable() { public void run() { configuredLevel.currentBubble = 13; } },
                        new Runnable() { public void run() { configuredLevel.currentBubble = 12; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble(".........................................", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Correct!", "c7-x:", 1, "Continue the catacombs", "", "", "",
                        new Runnable() { public void run() { configuredLevel.show = false; configuredLevel.targetBox.target = true; configuredLevel.targetBox.left = true; configuredLevel.targetBox.position.set(new Vector2(170, -150)); configuredLevel.continueBubbles = false; configuredLevel.touchLocked = false; } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.targetBox.target = false; configuredLevel.continueBubbles = true; configuredLevel.currentBubble = 15; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Incorrect!", "c7-x:", 1, "Continue the catacombs", "", "", "",
                        new Runnable() { public void run() { configuredLevel.show = false; configuredLevel.targetBox.target = true; configuredLevel.targetBox.left = true; configuredLevel.targetBox.position.set(new Vector2(170, -150)); configuredLevel.continueBubbles = false; configuredLevel.touchLocked = false; } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.targetBox.target = false; configuredLevel.continueBubbles = true; configuredLevel.currentBubble = 15; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("I am a robot, a small one!", "c7-x:", 1, "Continue the catacombs", "", "", "",
                        new Runnable() { public void run() { configuredLevel.show = false; configuredLevel.targetBox.target = true; configuredLevel.targetBox.left = true; configuredLevel.targetBox.position.set(new Vector2(170, -150)); configuredLevel.continueBubbles = false; configuredLevel.touchLocked = false; } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.touchLocked = true; configuredLevel.show = true; configuredLevel.targetBox.target = false; configuredLevel.continueBubbles = true; configuredLevel.currentBubble = 15; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("You can lock doors with\nkeys as well as unlock them", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("In order to jump to the next\ncatacomb...", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("You must lock the door underneath\nthe catacomb you want to jump to", "c7-x:", 1, "Lock door and jump out", "", "", "",
                        new Runnable() { public void run() { configuredLevel.touchLocked = false; configuredLevel.show = false; configuredLevel.targetBox.target = true; configuredLevel.targetBox.left = false; configuredLevel.targetBox.isArrow = false; configuredLevel.targetBox.position.set(new Vector2(151.5f + (60 * 0.6f), -99.5f)); configuredLevel.continueBubbles = false; } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { if (!configuredLevel.touchLocked) {configuredLevel.targetBox.isArrow = true; configuredLevel.targetBox.position.set(new Vector2(190, -70)); configuredLevel.currentBubble = 18;} } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Great job!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.show = true; configuredLevel.targetBox.target = false; configuredLevel.continueBubbles = true; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now, find the exit and\nget out of this place!", "c7-x:", 3, "Yes sir!", "Okay", "I know!", "",
                        new Runnable() { public void run() { configuredLevel.show = false; configuredLevel.targetBox.target = true; configuredLevel.targetBox.isArrow = true; configuredLevel.targetBox.position.set(new Vector2(340, 20)); configuredLevel.continueBubbles = false; configuredLevel.currentBubble = 20; } },
                        new Runnable() { public void run() { configuredLevel.show = false; configuredLevel.targetBox.target = true; configuredLevel.targetBox.isArrow = true; configuredLevel.targetBox.position.set(new Vector2(340, 20)); configuredLevel.continueBubbles = false; configuredLevel.currentBubble = 20; } },
                        new Runnable() { public void run() { configuredLevel.show = false; configuredLevel.targetBox.target = true; configuredLevel.targetBox.isArrow = true; configuredLevel.targetBox.position.set(new Vector2(340, 20)); configuredLevel.continueBubbles = false; configuredLevel.currentBubble = 20; } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("After you step on the exit pad\nyou still need to get to the door!", "c7-x:", 1, "Got it!", "", "", "",
                        new Runnable() { public void run() { configuredLevel.show = false; configuredLevel.continueBubbles = false; configuredLevel.currentBubble = 1; } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.show = true; configuredLevel.targetBox.position.set(new Vector2(750, -65)); } }, configuredLevel));
                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(100, 150));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(850, -82), new Vector2(680, -169), configuredLevel);
                break;
            case 3:
                configuredLevel.touchLocked = false;
                configuredLevel.show = false;
                configuredLevel.continueBubbles = false;

                configuredLevel.catacombs = new Array<Catacomb>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(150, -100), "Closed", "Locked", "Closed", "Locked", "Closed", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -15), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(300, -15), "Locked", "Closed", "Closed", "Closed", "DoubleLocked", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(300, -185), "Closed", "Locked", "Closed", "Locked", "Locked", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(450, -100), "Locked", "DoubleLocked", "Closed", "DoubleLocked", "Closed", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(450, -270), "Closed", "Locked", "Closed", "Closed", "Closed", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(600, -15), "DoubleLocked", "Unlocked", "Closed", "Closed", "Closed", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(600, -185), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(450, 70), "Closed", "Closed", "Closed", "Closed", "Unlocked", "Closed"));
                configuredLevel.currentCatacomb = 1;

                configuredLevel.items = new DelayedRemovalArray<Item>();
                configuredLevel.items.add(new Item(new Vector2(130, 10), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(530, -70), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(390, 10), configuredLevel.viewportPosition, "key"));

                configuredLevel.puzzles = new Array<Puzzle>();
                configuredLevel.puzzles.add(new Puzzle(new Vector2(660, 50), new Vector2(500, 130), "shapes3", new Enums.Shape[]{Enums.Shape.SQUARE, Enums.Shape.CIRCLE, Enums.Shape.TRIANGLE}, new Runnable() { public void run() { configuredLevel.catacombs.get(6).getLockedDoors().set(5, "Unlocked"); configuredLevel.catacombs.get(7).getLockedDoors().set(2, "Unlocked"); } }, configuredLevel));

                configuredLevel.words = new Array<Word>();
                configuredLevel.words.add(new Word(new String[]{"K","E","Y"}, new Vector2[]{new Vector2(370, 50),new Vector2(395, 50),new Vector2(420, 50)}, configuredLevel));
                configuredLevel.words.add(new Word(new String[]{"K","E","Y"}, new Vector2[]{new Vector2(550, -35),new Vector2(420, -120),new Vector2(370, -120)}, configuredLevel));

                configuredLevel.chests = new Array<Chest>();
                configuredLevel.chests.add(new Chest(new Vector2(250, -82), configuredLevel, new Runnable() { public void run() { configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0,0), configuredLevel.viewportPosition, "key")); } }));
                configuredLevel.chests.add(new Chest(new Vector2(550, -252), configuredLevel, new Runnable() { public void run() { configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0,0), configuredLevel.viewportPosition, "key")); configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0,0), configuredLevel.viewportPosition, "key")); configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0,0), configuredLevel.viewportPosition, "key")); } }));

                configuredLevel.guards = new Array<Guard>();

                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<SpeechBubble>();
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.show = true; configuredLevel.continueBubbles = true; configuredLevel.currentBubble = 1; } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("c7x, How long until I escape this place?", "Player:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("By my calculations.............\nI have no clue.", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("I thought you were a robot!", "Player:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { configuredLevel.continueBubbles = false; if (configuredLevel.currentCatacomb == 2) { configuredLevel.currentBubble = 4; } } }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("It looks like there will\nbe some puzzles along the way!", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { if (configuredLevel.currentCatacomb == 4) { configuredLevel.show = false; } if (configuredLevel.currentCatacomb == 6 && configuredLevel.catacombs.get(6).getLockedDoors().get(0).equals("Unlocked")) { configuredLevel.catacombs.get(6).getLockedDoors().set(0, "Locked"); } } }, configuredLevel));
                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(100, 150));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(710, -167), new Vector2(550, -84), configuredLevel);
                break;
            case 4:
                configuredLevel.touchLocked = false;
                configuredLevel.show = false;
                configuredLevel.continueBubbles = false;

                configuredLevel.catacombs = new Array<Catacomb>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-400, -300), "Closed", "Closed", "Closed", "Locked", "Closed", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-250, -215), "Locked", "Closed", "Closed", "Closed", "Locked", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-100, -300), "Closed", "Locked", "Closed", "Closed", "Locked", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(50, -385), "Locked", "Locked", "Closed", "Closed", "Closed", "Closed"));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-100, -470), "Closed", "Closed", "Closed", "Locked", "Closed", "Closed"));
                configuredLevel.currentCatacomb = 1;

                configuredLevel.items = new DelayedRemovalArray<Item>();
                configuredLevel.items.add(new Item(new Vector2(-285, -270), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(-330, -265), configuredLevel.viewportPosition, "dagger"));
                configuredLevel.items.add(new Item(new Vector2(-265, -270), configuredLevel.viewportPosition, "gold"));
                configuredLevel.items.add(new Item(new Vector2(-135, -185), configuredLevel.viewportPosition, "key"));

                configuredLevel.puzzles = new Array<Puzzle>();

                configuredLevel.words = new Array<Word>();
                configuredLevel.words.add(new Word(new String[]{"G","O","L","D"}, new Vector2[]{new Vector2(15, -200),new Vector2(-15, -230),new Vector2(10, -250),new Vector2(-45, -230)}, configuredLevel));

                configuredLevel.chests = new Array<Chest>();

                configuredLevel.guards = new Array<Guard>();
                configuredLevel.guards.add(new Guard(new Vector2(-150, -149), configuredLevel));

                configuredLevel.speechBubbles = new Array<SpeechBubble>();
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() { } },
                        new Runnable() { public void run() {  } }, configuredLevel));
                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(-300, -135));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(710, -167), new Vector2(550, -84), configuredLevel);
                break;
        }
    }

    public void update (Level configuredLevel) {
        configuredLevel.getPlayer().spawn();
        if (currentLevel == 0) {
            //set some stuff and check if speechBubbles are triggered
            configuredLevel.speechBubbles.get(6).triggered = (configuredLevel.speechBubbles.get(configuredLevel.currentBubble).textLoaded);
            configuredLevel.speechBubbles.get(25).triggered = (configuredLevel.items.size == 0);
            configuredLevel.speechBubbles.get(27).triggered = (configuredLevel.inventory.selectedItem == 0);
            configuredLevel.speechBubbles.get(36).triggered = (configuredLevel.catacombs.get(1).getLockedDoors().get(0).equals("Unlocked"));
            configuredLevel.speechBubbles.get(41).triggered = (configuredLevel.exitDoor.playerHasEscaped);
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
            configuredLevel.speechBubbles.get(39).triggered = (configuredLevel.exitDoor.unlocked);
            configuredLevel.speechBubbles.get(40).triggered = (configuredLevel.currentBubble == 40);
            configuredLevel.speechBubbles.get(44).triggered = (configuredLevel.currentBubble == 44);
            configuredLevel.speechBubbles.get(45).triggered = (configuredLevel.currentBubble == 45);
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
        //escape
        if (configuredLevel.exitDoor.playerHasEscaped) {
            configuredLevel.continueBubbles = false;
            configuredLevel.verdict.verdict = true;
            configuredLevel.verdict.verdictPhrase = victoryPhrases.get(currentLevel);
            configuredLevel.victory = true;
        }
    }

}
