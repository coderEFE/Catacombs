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
import com.efe.gamedev.catacombs.util.Constants;
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
        currentLevel = 6;
        victoryPhrases = new Array<String>();
        victoryPhrases.add("Beginner's Luck!");
        victoryPhrases.add("Nice Jumps!");
        victoryPhrases.add("Amazing Job!");
        victoryPhrases.add("You are almost ready!");
        victoryPhrases.add("Difficulty Increasing...");
        victoryPhrases.add("24 hours until Lock-Down!");
        victoryPhrases.add("23 hours until Lock-Down!");
    }

    //setup stuff for each level
    public void configureLevel(final Level configuredLevel) {
        //stuff that is the same for each level
        configuredLevel.viewportPosition = new Vector2();
        configuredLevel.touchPosition = new Vector2();
        configuredLevel.lookPosition = new Vector2();
        configuredLevel.alarm = false;
        configuredLevel.shake = false;

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
        configuredLevel.getPlayer().alert = false;
        configuredLevel.getPlayer().duck = false;

        switch (currentLevel) {
            case 0:
                configuredLevel.touchLocked = true;
                /*LEVEL CONFIGURATIONS:*/
                //player always starts inside the currentCatacomb
                configuredLevel.catacombs = new Array<Catacomb>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -100), "Closed", "Closed", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -15), "Locked", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
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
                configuredLevel.catacombs.add(new Catacomb(new Vector2(200, -385), "Closed", "Unlocked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(50, -300), "Closed", "Unlocked", "Closed", "Closed", "Unlocked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-100, -215), "Closed", "Locked", "Closed", "Closed", "Unlocked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-250, -130), "Closed", "Locked", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-400, -45), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 0;

                configuredLevel.items = new DelayedRemovalArray<Item>();
                configuredLevel.items.add(new Item(new Vector2(-190, -105), configuredLevel.viewportPosition, "key"));

                configuredLevel.puzzles = new Array<Puzzle>();

                configuredLevel.words = new Array<Word>();

                configuredLevel.chests = new Array<Chest>();
                configuredLevel.chests.add(new Chest(new Vector2(-40, -197), "", configuredLevel, new Runnable() {
                    public void run() {
                        configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                    }
                }));

                configuredLevel.guards = new Array<Guard>();

                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<SpeechBubble>();
                configuredLevel.speechBubbles.add(new SpeechBubble("You can pause the game by tapping the\n pause button in the bottom left corner", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("If you ever get stuck, you\ncan reset the level there!", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Well, I still don't know my location!", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Don't worry,\nI've searched my databases and...", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("I have found that each of these hexagons\nare called a catacomb!", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("We are in the Catacombs! An underground\ncemetery full of tombs and tunnels!", "c7-x:", 4, "Lovely", "How are you so optimistic?", "Get me out of here!", "Great",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 6;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 7;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 13;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 6;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("I wouldn't go that far, but\nit is pretty nice down here!", "c7-x:", 2, "Interrogate c7-x", "Ask for help", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 8;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 13;
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
                configuredLevel.speechBubbles.add(new SpeechBubble("It's in my programming!", "c7-x:", 2, "Tell me more...", "Help me escape!", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 8;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 13;
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
                configuredLevel.speechBubbles.add(new SpeechBubble("How am I the only human down here?", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("I'm sorry, but I cannot answer that.", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("So you do know!?", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("I know how you can escape", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Fine.\nTell me.", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("First, come up to the\nleft side of this catacomb", "c7-x:", 1, "Okay", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                                configuredLevel.touchLocked = false;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.isArrow = true;
                                configuredLevel.targetBox.left = true;
                                configuredLevel.targetBox.position.set(new Vector2(250, -350));
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
                                configuredLevel.currentBubble = 14;
                                configuredLevel.show = true;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.targetBox.target = false;
                                configuredLevel.touchLocked = true;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("That's the easy part...", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Now you need to jump\nto the next catacomb!", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("To jump, you need to tap twice in the very\ntop corner that you want to jump to", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Right now, you need to jump\nto the top left corner!", "c7-x:", 1, "Jump time!", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                                configuredLevel.touchLocked = false;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.isArrow = false;
                                configuredLevel.targetBox.itemWidth = 5;
                                configuredLevel.targetBox.position.set(new Vector2(170, -260));
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
                                configuredLevel.currentBubble = 18;
                                configuredLevel.touchLocked = true;
                                configuredLevel.show = true;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.targetBox.target = false;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Pro skills!", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("I would jump for you,\nbut I don't have legs!", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("It looks like there is another\ncatacomb on your left to be jumped", "c7-x:", 1, "On it!", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                                configuredLevel.touchLocked = false;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.isArrow = true;
                                configuredLevel.targetBox.left = true;
                                configuredLevel.targetBox.position.set(new Vector2(50, -170));
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
                                if (configuredLevel.viewportPosition.x < 40) {
                                    configuredLevel.viewportPosition.x = 40;
                                }
                                configuredLevel.touchLocked = true;
                                configuredLevel.show = true;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.targetBox.target = false;
                                configuredLevel.currentBubble = 21;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Wow, is that a treasure chest!", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("We are in a tomb, so it might be!", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Some chests will need items\nto be unlocked...", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("But this chest already is! Tap on it!", "c7-x:", 0, "", "", "", "",
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
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("It gave me a key!", "Player:", configuredLevel.targetBox.target ? 0 : 1, "Proceed", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.viewportPosition.set(-41, 0);
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.isArrow = true;
                                configuredLevel.show = false;
                                configuredLevel.targetBox.position.set(new Vector2(-60, -160));
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
                                configuredLevel.show = true;
                                configuredLevel.targetBox.target = false;
                                configuredLevel.currentBubble = 26;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Some doors are located higher up!", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.getPlayer().mouthState = Enums.MouthState.NORMAL;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.isArrow = false;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.targetBox.position.set(new Vector2(-97.5f, -104.5f));
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("To unlock them, all you need is a key", "c7-x:", configuredLevel.inventory.selectedItem == -1 ? 1 : 0, "Grab key", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.touchLocked = false;
                                configuredLevel.show = false;
                                if (configuredLevel.inventory.selectedItem == -1 && configuredLevel.inventory.inventoryItems.size >= 1) {
                                    configuredLevel.inventory.inventoryItems.get(0).targeted = true;
                                }
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
                                configuredLevel.show = true;
                                configuredLevel.touchLocked = true;
                                configuredLevel.inventory.inventoryItems.get(0).targeted = false;
                                configuredLevel.currentBubble = 28;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Try unlocking the door\n in the upper left corner!", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
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
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Not bad, now jump up to the\ncatacomb in the top left corner!", "c7-x:", 1, "Okay", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 31;
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
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
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
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Exhilarating!", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Do you see that red\npad to the left of you?", "c7-x:", 2, "Yes", "Nope", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 37;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 34;
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
                configuredLevel.speechBubbles.add(new SpeechBubble("It is on the floor of this catacomb", "c7-x:", 2, "I see it now!", "Huh?", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 37;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 35;
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
                configuredLevel.speechBubbles.add(new SpeechBubble("This is not funny!", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Now do you see it?", "c7-x:", 1, "Yes, I do", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 37;
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
                                configuredLevel.exitDoor.padColor = new Color(MathUtils.random(0.2f, 0.6f), MathUtils.random(0.2f, 0.6f), MathUtils.random(0.2f, 0.6f), 1);
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Good!", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.exitDoor.padColor = Color.RED;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("The pad is called the Exit Pad\nand unlocks the Exit Door!", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("To activate the Exit Pad,\nyou must step on it", "c7-x:", 1, "Step on pad", "", "", "",
                        new Runnable() {
                            public void run() {
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
                                configuredLevel.viewportPosition.set(new Vector2(configuredLevel.getPlayer().getPosition()));
                                configuredLevel.show = true;
                                configuredLevel.touchLocked = true;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.currentBubble = 40;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Alright!", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.getPlayer().resetLegs();
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Remember, to escape the level...", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("You must first step on the Exit Pad\nthen go to the Exit Door!", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Hey, I see a key to my left!", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Walk over to the key to grab it", "c7-x:", 0, "", "", "", "",
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
                                if (configuredLevel.items.size >= 1) {
                                    configuredLevel.items.get(0).targeted = true;
                                    configuredLevel.touchLocked = false;
                                    configuredLevel.continueBubbles = false;
                                } else {
                                    configuredLevel.touchLocked = true;
                                    configuredLevel.currentBubble = 45;
                                }
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now use it to unlock the door\nin the top left corner!", "c7-x:", 1, "Unlock door", "", "", "",
                        new Runnable() {
                            public void run() {
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
                                if (configuredLevel.catacombs.get(3).getLockedDoors().get(1).equals("Unlocked")) {
                                    configuredLevel.targetBox.isArrow = true;
                                    configuredLevel.targetBox.position.set(new Vector2(-100 - 150, -100 + 85));
                                }
                                if (configuredLevel.currentCatacomb == 4) {
                                    configuredLevel.targetBox.target = false;
                                    configuredLevel.show = true;
                                    configuredLevel.currentBubble = 46;
                                }
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now, escape the level through\nthat Exit Door!", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(300, -250));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(-350, -27), new Vector2(-170, -114), configuredLevel);
                break;
            case 2:
                configuredLevel.touchLocked = true;

                configuredLevel.catacombs = new Array<Catacomb>();
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
                configuredLevel.speechBubbles.add(new SpeechBubble("One of them lets you see a\nmap of the level you are in", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Using what you have learned,\ngo and navigate the catacombs!", "c7-x:", 1, "Try it out!", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.touchLocked = false;
                                configuredLevel.continueBubbles = false;
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
                                configuredLevel.currentBubble = 3;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.position.set(new Vector2(191.5f, -74.5f));
                                configuredLevel.show = true;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("There are diamonds in some catacombs,\nwhich are very valuable!", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.touchLocked = false;
                                configuredLevel.show = false;
                                configuredLevel.targetBox.target = false;
                                configuredLevel.currentBubble = 4;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Lets test your jumping skills!", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.show = true;
                                configuredLevel.touchLocked = true;
                                configuredLevel.inventory.selectedItem = 0;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.position.set(new Vector2(341.5f, -159.5f));
                                configuredLevel.viewportPosition.set(new Vector2(285, 0));
                                configuredLevel.continueBubbles = true;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Try unlocking the door\n in the upper right corner!", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.touchLocked = false;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                                if (configuredLevel.catacombs.get(2).getLockedDoors().get(3).equals("Unlocked")) {
                                    configuredLevel.targetBox.isArrow = true;
                                    configuredLevel.targetBox.left = false;
                                    configuredLevel.targetBox.position.set(new Vector2(340, -150));
                                    configuredLevel.currentBubble = 7;
                                    configuredLevel.show = true;
                                }
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Superb! Now tap twice in the very\ntop right corner to jump up there!", "c7-x:", 1, "Got it", "", "", "",
                        new Runnable() {
                            public void run() {
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
                                configuredLevel.show = true;
                                configuredLevel.targetBox.target = false;
                                configuredLevel.currentBubble = 8;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.touchLocked = true;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Genius!", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Not as genius\nas I am, of course!", "c7-x:", 3, "You are a robot!", "Oh, really?", "No way!", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 14;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 10;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 10;
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Well then, what is.....\n9 cubed!", "c7-x:", 4, "28", "The cube of 9", "821", "729",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 13;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 11;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 13;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 12;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble(".........................................", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Correct!", "c7-x:", 1, "Continue the catacombs", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.show = false;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.left = true;
                                configuredLevel.targetBox.position.set(new Vector2(170, -150));
                                configuredLevel.continueBubbles = false;
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
                                configuredLevel.show = true;
                                configuredLevel.targetBox.target = false;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.currentBubble = 15;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Incorrect!", "c7-x:", 1, "Continue the catacombs", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.show = false;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.left = true;
                                configuredLevel.targetBox.position.set(new Vector2(170, -150));
                                configuredLevel.continueBubbles = false;
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
                                configuredLevel.show = true;
                                configuredLevel.targetBox.target = false;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.currentBubble = 15;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("I am a robot, a small one!", "c7-x:", 1, "Continue the catacombs", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.show = false;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.left = true;
                                configuredLevel.targetBox.position.set(new Vector2(170, -150));
                                configuredLevel.continueBubbles = false;
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
                                configuredLevel.show = true;
                                configuredLevel.targetBox.target = false;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.currentBubble = 15;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("You can lock doors with\nkeys as well as unlock them", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("In order to jump to the next\ncatacomb...", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("You must lock the door underneath\nthe catacomb you want to jump to", "c7-x:", 1, "Lock door and jump out", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.touchLocked = false;
                                configuredLevel.show = false;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.left = false;
                                configuredLevel.targetBox.isArrow = false;
                                configuredLevel.targetBox.position.set(new Vector2(151.5f + (60 * 0.6f), -99.5f));
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
                                if (!configuredLevel.touchLocked) {
                                    configuredLevel.targetBox.isArrow = true;
                                    configuredLevel.targetBox.position.set(new Vector2(190, -70));
                                    configuredLevel.currentBubble = 18;
                                }
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Great job!", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.show = true;
                                configuredLevel.targetBox.target = false;
                                configuredLevel.continueBubbles = true;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Now, find the exit and\nget out of this place!", "c7-x:", 3, "Yes sir!", "Okay", "I know!", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.show = false;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.isArrow = true;
                                configuredLevel.targetBox.position.set(new Vector2(340, 20));
                                configuredLevel.continueBubbles = false;
                                configuredLevel.currentBubble = 20;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.show = false;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.isArrow = true;
                                configuredLevel.targetBox.position.set(new Vector2(340, 20));
                                configuredLevel.continueBubbles = false;
                                configuredLevel.currentBubble = 20;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.show = false;
                                configuredLevel.targetBox.target = true;
                                configuredLevel.targetBox.isArrow = true;
                                configuredLevel.targetBox.position.set(new Vector2(340, 20));
                                configuredLevel.continueBubbles = false;
                                configuredLevel.currentBubble = 20;
                            }
                        },
                        new Runnable() {
                            public void run() {
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("After you step on the exit pad\nyou still need to get to the door!", "c7-x:", 1, "Got it!", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                                configuredLevel.currentBubble = 1;
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
                                configuredLevel.show = true;
                                configuredLevel.targetBox.position.set(new Vector2(750, -65));
                            }
                        }, configuredLevel));
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

                configuredLevel.items = new DelayedRemovalArray<Item>();
                configuredLevel.items.add(new Item(new Vector2(130, 10), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(530, -70), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(390, 10), configuredLevel.viewportPosition, "key"));

                configuredLevel.puzzles = new Array<Puzzle>();
                configuredLevel.puzzles.add(new Puzzle(new Vector2(660, 50), new Vector2(500, 130), "shapes3", new Enums.Shape[]{Enums.Shape.SQUARE, Enums.Shape.CIRCLE, Enums.Shape.TRIANGLE}, new Runnable() {
                    public void run() {
                        configuredLevel.catacombs.get(6).getLockedDoors().set(5, "Unlocked");
                        configuredLevel.catacombs.get(7).getLockedDoors().set(2, "Unlocked");
                    }
                }, configuredLevel));

                configuredLevel.words = new Array<Word>();
                configuredLevel.words.add(new Word(new String[]{"K", "E", "Y"}, new Vector2[]{new Vector2(370, 50), new Vector2(395, 50), new Vector2(420, 50)}, configuredLevel));
                configuredLevel.words.add(new Word(new String[]{"K", "E", "Y"}, new Vector2[]{new Vector2(550, -35), new Vector2(420, -120), new Vector2(370, -120)}, configuredLevel));

                configuredLevel.chests = new Array<Chest>();
                configuredLevel.chests.add(new Chest(new Vector2(250, -82), "", configuredLevel, new Runnable() {
                    public void run() {
                        configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                    }
                }));
                configuredLevel.chests.add(new Chest(new Vector2(550, -252), "", configuredLevel, new Runnable() {
                    public void run() {
                        configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                        configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                        configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                    }
                }));

                configuredLevel.guards = new Array<Guard>();

                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<SpeechBubble>();
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.show = true;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.currentBubble = 1;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("c7x, How long until I escape this place?", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("By my calculations.............\nI have no clue.", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("But, you are a robot!", "Player:", 0, "", "", "", "",
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
                                configuredLevel.continueBubbles = false;
                                if (configuredLevel.currentCatacomb == 2) {
                                    configuredLevel.currentBubble = 4;
                                }
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("It looks like there will\nbe some puzzles along the way!", "c7-x:", 0, "", "", "", "",
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
                                if (configuredLevel.currentCatacomb == 4) {
                                    configuredLevel.show = false;
                                }
                                if (configuredLevel.currentCatacomb == 6 && configuredLevel.catacombs.get(6).getLockedDoors().get(0).equals("Unlocked")) {
                                    configuredLevel.catacombs.get(6).getLockedDoors().set(0, "Locked");
                                }
                            }
                        }, configuredLevel));
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
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-400, -300), "Closed", "Closed", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-250, -215), "Locked", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-100, -300), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(50, -385), "Locked", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-100, -470), "Closed", "Closed", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 1;

                configuredLevel.items = new DelayedRemovalArray<Item>();
                configuredLevel.items.add(new Item(new Vector2(-285, -270), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(-330, -269), configuredLevel.viewportPosition, "dagger"));
                configuredLevel.items.add(new Item(new Vector2(-265, -270), configuredLevel.viewportPosition, "gold"));
                configuredLevel.items.add(new Item(new Vector2(-135, -185), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(-30, -270), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(10, -440), configuredLevel.viewportPosition, "diamond"));

                configuredLevel.puzzles = new Array<Puzzle>();

                configuredLevel.words = new Array<Word>();
                configuredLevel.words.add(new Word(new String[]{"G", "O", "L", "D"}, new Vector2[]{new Vector2(15, -200), new Vector2(-15, -230), new Vector2(10, -250), new Vector2(-45, -230)}, configuredLevel));

                configuredLevel.chests = new Array<Chest>();
                configuredLevel.chests.add(new Chest(new Vector2(140, -367), "gold", configuredLevel, new Runnable() {
                    public void run() {
                        configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                        configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0)));
                        configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0)));
                        configuredLevel.inventory.inventoryItems.removeIndex(configuredLevel.inventory.selectedItem);
                    }
                }));

                configuredLevel.guards = new Array<Guard>();
                configuredLevel.guards.add(new Guard(new Vector2(-150, -149), configuredLevel));

                configuredLevel.speechBubbles = new Array<SpeechBubble>();
                configuredLevel.speechBubbles.add(new SpeechBubble("Halt stranger!", "Guard 1:", 0, "", "", "", "",
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
                                configuredLevel.show = true;
                                configuredLevel.catacombs.get(1).getLockedDoors().set(0, "Locked");
                                configuredLevel.catacombs.get(1).getLockedDoors().set(4, "Locked");
                                configuredLevel.continueBubbles = true;
                                configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("You are not authorized to\nenter this area!", "Guard 1:", 0, "", "", "", "",
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
                                configuredLevel.getPlayer().mouthState = Enums.MouthState.NORMAL;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Well...", "Player:", 4, "Talk with Guard", "Run Away", "Bribe Guard", "Attack Guard",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 3;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 56;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 50;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 64;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("What are you doing here?", "Guard 1:", 4, "None of your business", "Escaping!", "I am lost", "What are YOU doing here?",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 27;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 34;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 4;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 28;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Really!?", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Yep, I am not sure how\nI came here.", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("...                  \nYou don't know where you are?", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Well... Not really.", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("How could you have traveled\nhere without knowing where you are?!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("This is the Catacombs, the most secure\nunderground prison in the world!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Prison!!      \nDo they keep criminals here?", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("All the worst criminals in the\nworld are kept down here!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("It is strange that you don't\nknow how you traveled here.", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("It is nearly impossible to venture\ndown here without being a...", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Prisoner!!!", "Guard 1:", 3, "I am not a criminal!", "Who, me?", "What!!!", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 15;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 15;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 15;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Nice try, but you\ncan't fool me anymore!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("I am alerting security!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.show = false;
                                configuredLevel.player.mouthState = Enums.MouthState.OPEN;
                                configuredLevel.guards.get(0).guardItem = "phone";
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("CODE RED! We have a\nprisoner on the loose!", "Security:", 0, "", "", "", "",
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
                                configuredLevel.show = true;
                                configuredLevel.alarm = true;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Run, player, RUN!!!", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Where?", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Unlock the door on your right!", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
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
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Got you!", "Guard 1:", 1, "Oh no!", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.defeat = true;
                                configuredLevel.verdict.verdict = false;
                                configuredLevel.verdict.verdictPhrase = "You were captured by a guard!";
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Hey! Where are you going!!!", "Guard 1:", 0, "", "", "", "",
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
                                configuredLevel.continueBubbles = true;
                                configuredLevel.alarm = false;
                                configuredLevel.alarm = false;
                                configuredLevel.show = true;
                                configuredLevel.touchLocked = false;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("That was a close one!", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.catacombs.get(2).getLockedDoors().set(1, "Closed");
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("None of my business!?", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("I guard this prison for a living!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Prison!!      \nDo they keep criminals here?", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("This is the Catacombs, the most secure\nunderground prison in the world!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("All the worst criminals in the\nworld are kept down here!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("It is nearly impossible to venture\ndown here without being a...", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Prisoner!!!", "Guard 1:", 3, "Huh?", "I am not a criminal!", "But...", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 15;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 15;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 15;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("...            \nYou're escaping?", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Uh... yeah.", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("I really want to\nget out of this place!", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("You have the audacity\n to leave this prison?!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Wait, this is a prison?", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("This is the Catacombs, the most secure\nunderground prison in the world!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("All the worst criminals in the\nworld are kept down here.", "Guard 1:", 2, "But I haven't done anything wrong!", "I'm not a criminal!", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 41;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 41;
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
                configuredLevel.speechBubbles.add(new SpeechBubble("All the prisoners say that!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Enough talking,\nI am alerting security!", "Guard 1:", 1, "Wait...", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 43;
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Will you let me go\nif I give you my gold?", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Hmm, let me see it.", "Guard 1:", 2, "Give guard gold", "Don't give guard gold", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 47;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 45;
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
                configuredLevel.speechBubbles.add(new SpeechBubble("How dare you!?", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.currentBubble = 17;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("...", "Guard 1:", 0, "", "", "", "",
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
                                for (int i = 0; i < configuredLevel.inventory.inventoryItems.size; i++) {
                                    if (configuredLevel.inventory.inventoryItems.get(i).itemType.equals("gold")) {
                                        configuredLevel.inventory.inventoryItems.removeIndex(i);
                                    }
                                }
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Actually, I've changed my mind.", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.currentBubble = 17;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("What are you doing down here?!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Uh... Leaving?", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("...            \nYou're escaping?", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("No, I just really want\nto get out of this place!", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("That's the same thing...", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.currentBubble = 37;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Sorry, can't talk now!", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("huh?            \nWho are you?", "Guard 1:", 4, "None of your business", "Nobody", "Who are YOU?", "I don't know",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 27;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 60;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 58;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 60;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Well, I am a security guard...", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.currentBubble = 28;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("You must be somebody!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("I would talk,\nbut I have to leave...", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("How could you have traveled\nhere without knowing who you are?!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.currentBubble = 9;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("What are you doing down here!?", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("I am escaping and there is\nnothing you can do to stop me.", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("You have the audacity\n to leave this prison?!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Wait, this is a prison?", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("This is the Catacombs, the most secure\nunderground prison in the world!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("All the worst criminals in the\nworld are kept down here.", "Guard 1:", 2, "But I haven't done anything wrong!", "I'm not a criminal!", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 70;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 70;
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
                configuredLevel.speechBubbles.add(new SpeechBubble("All the prisoners say that!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Enough talking,\nI am alerting security!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.show = false;
                                configuredLevel.player.mouthState = Enums.MouthState.OPEN;
                                configuredLevel.guards.get(0).guardItem = "phone";
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("CODE RED! We have a\nprisoner on the loose!", "Security:", 0, "", "", "", "",
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
                                configuredLevel.show = true;
                                configuredLevel.alarm = true;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("I won't let myself be captured!", "Player:", 0, "", "", "", "",
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
                                for (int i = 0; i < configuredLevel.inventory.inventoryItems.size; i++) {
                                    if (configuredLevel.inventory.inventoryItems.get(i).itemType.equals("dagger")) {
                                        configuredLevel.inventory.selectedItem = i;
                                    }
                                }
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Oh, really?", "Guard 1:", 0, "", "", "", "",
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
                                configuredLevel.guards.get(0).guardItem = "dagger";
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Tap on the guard to attack him!", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.continueBubbles = false;
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
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Got you!", "Guard 1:", 1, "Oh no!", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.defeat = true;
                                configuredLevel.verdict.verdict = false;
                                configuredLevel.verdict.verdictPhrase = "You were captured by a guard!";
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
                                configuredLevel.getPlayer().heldItem.itemType = "";
                                configuredLevel.inventory.selectedItem = -1;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("No!!!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Don't mess with me again.", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("That was a close one!", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.show = false;
                                configuredLevel.touchLocked = false;
                            }
                        }, configuredLevel));

                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(-300, -135));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(-50, -452), new Vector2(0, -284), configuredLevel);
                break;
            case 5:
                configuredLevel.touchLocked = false;
                configuredLevel.show = false;
                configuredLevel.continueBubbles = false;

                configuredLevel.catacombs = new Array<Catacomb>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-600, 100), "Closed", "Closed", "Closed", "Closed", "Unlocked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-450, 15), "Closed", "Unlocked", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-200, -70), "Locked", "Locked", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-50, 15), "Locked", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-350, -155), "Unlocked", "Closed", "Closed", "Locked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-600, -240), "Locked", "Closed", "Closed", "Unlocked", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-750, -325), "Closed", "Closed", "Closed", "Locked", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-600, -410), "Closed", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 0;

                configuredLevel.items = new DelayedRemovalArray<Item>();
                configuredLevel.items.add(new Item(new Vector2(-535, 130), configuredLevel.viewportPosition, "key"));
                configuredLevel.items.add(new Item(new Vector2(-135, -40), configuredLevel.viewportPosition, "key"));

                configuredLevel.puzzles = new Array<Puzzle>();
                configuredLevel.puzzles.add(new Puzzle(new Vector2(50, 100), new Vector2(1000, 1000), "shapes2", new Enums.Shape[]{Enums.Shape.TRIANGLE, Enums.Shape.CIRCLE}, new Runnable() {
                    public void run() {
                        configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "gold"));
                        configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                    }
                }, configuredLevel));

                configuredLevel.words = new Array<Word>();
                configuredLevel.words.add(new Word(new String[]{"E", "X", "I", "T"}, new Vector2[]{new Vector2(-490, -300), new Vector2(-370, -280), new Vector2(-400, -370), new Vector2(-360, -360)}, configuredLevel));

                configuredLevel.chests = new Array<Chest>();
                configuredLevel.chests.add(new Chest(new Vector2(-110, -52), "key", configuredLevel, new Runnable() {
                    public void run() {
                        configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                        configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0)));
                        configuredLevel.inventory.scoreDiamonds.add(new Diamond(new Vector2(0, 0)));
                        configuredLevel.inventory.inventoryItems.removeIndex(configuredLevel.inventory.selectedItem);
                    }
                }));
                configuredLevel.chests.add(new Chest(new Vector2(-280, -137), "gold", configuredLevel, new Runnable() {
                    public void run() {
                        configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                        configuredLevel.inventory.inventoryItems.add(new Item(new Vector2(0, 0), configuredLevel.viewportPosition, "key"));
                        configuredLevel.inventory.inventoryItems.removeIndex(configuredLevel.inventory.selectedItem);
                    }
                }));

                configuredLevel.guards = new Array<Guard>(); //-449
                configuredLevel.guards.add(new Guard(new Vector2(-429, -184), configuredLevel));

                configuredLevel.speechBubbles = new Array<SpeechBubble>();
                configuredLevel.speechBubbles.add(new SpeechBubble("There are spikes on the ceiling!", "Player:", 0, "", "", "", "",
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
                                configuredLevel.show = true;
                                configuredLevel.getPlayer().viewportPosition.set(new Vector2(configuredLevel.catacombs.get(1).position.x + (200 / 3.5f), configuredLevel.getPlayer().getPosition().y));
                                configuredLevel.touchLocked = true;
                                if (configuredLevel.inventory.inventoryItems.size != 0) {
                                    configuredLevel.catacombs.get(1).getLockedDoors().set(1, "Locked");
                                }
                                configuredLevel.continueBubbles = true;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Oh no! Those are Stalactites!!!", "c7-x:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("They are going to fall!\nGet out of the way!!!", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN;
                                configuredLevel.shake = true;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
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
                                    configuredLevel.catacombs.get(2).getLockedDoors().set(1, "Locked");
                                    configuredLevel.currentBubble = 4;
                                }
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("We made it!", "Player:", 0, "", "", "", "",
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
                                configuredLevel.show = true;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.touchLocked = true;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.show = false;
                                configuredLevel.continueBubbles = false;
                                configuredLevel.touchLocked = false;
                                if (configuredLevel.currentCatacomb == 5 && configuredLevel.getPlayer().jumpState == Enums.JumpState.GROUNDED) {
                                    configuredLevel.viewportPosition.set(new Vector2(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 220, configuredLevel.getPlayer().getPosition().y));
                                    if (configuredLevel.getPlayer().getPosition().x < configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 222) {
                                        configuredLevel.currentBubble = 6;
                                    }
                                }
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("I see a Guard!", "PLayer:", 0, "", "", "", "",
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
                                configuredLevel.show = true;
                                configuredLevel.getPlayer().alert = true;
                                configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.touchLocked = true;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.show = false;
                                configuredLevel.viewportPosition.set(new Vector2(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 240, configuredLevel.getPlayer().getPosition().y));
                                configuredLevel.touchPosition.set(new Vector2(configuredLevel.catacombs.get(configuredLevel.currentCatacomb).position.x + 220, configuredLevel.getPlayer().getPosition().y));
                                configuredLevel.getPlayer().mouthState = Enums.MouthState.NORMAL;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("......", "Guard 1:", 0, "", "", "", "",
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
                                configuredLevel.show = true;
                                configuredLevel.guards.get(0).guardItem = "phone";
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Guard 24A-G678, do you read me?", "Security:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Affirmative, sir.", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("An escaped prisoner has been\nrecently spotted in your vicinity.", "Security:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("We are initiating the\nCatacombs lock-down sequence.", "Security:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("...                   \nAre you sure that's necessary, sir?", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Not typically, but this prisoner is...", "Security:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("...Special.", "Security:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("We cannot let him escape.", "Security:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Very well, sir.", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Countdown has been initiated.\nLock-down occurs in 24 hours.", "Security:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("", "Security:", 0, "", "", "", "",
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
                                configuredLevel.show = false;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("One more thing,\nIf you see the prisoner...", "Security:", 0, "", "", "", "",
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
                                configuredLevel.show = true;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Yes...", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Don't hesitate to use\nyour Stun Gun.", "Security:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Yes, sir.", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Central Security out.", "Security:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("", "Security:", 0, "", "", "", "",
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
                                configuredLevel.show = false;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Be careful, player.", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.show = true;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "Security:", 0, "", "", "", "",
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
                                configuredLevel.show = false;
                                configuredLevel.touchLocked = false;
                                configuredLevel.continueBubbles = false;
                                if (configuredLevel.getPlayer().getPosition().x < configuredLevel.guards.get(0).position.x) {
                                    configuredLevel.currentBubble = 28;
                                }
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Huh?", "Guard 1:", 0, "", "", "", "",
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
                                configuredLevel.show = true;
                                configuredLevel.guards.get(0).talkToPlayer = true;
                                configuredLevel.guards.get(0).alert = true;
                                configuredLevel.continueBubbles = true;
                                configuredLevel.viewportPosition.x = configuredLevel.guards.get(0).position.x - 50;
                                configuredLevel.touchLocked = true;
                                configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Halt prisoner!\nDo not take another step!", "Guard 1:", 4, "Make me", "Whatever", "I don't think so", "Yes, sir!",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 33;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 30;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 33;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 31;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("You will regret the day\nthat you came across me...", "Guard 1:", 1, "Okay?", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 34;
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Finally!\nA person who recognizes my authority!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Actually, I was mocking you...", "Player:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("How dare you!!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Prepare to be stunned!\nLiterally.", "Guard 1:", 0, "", "", "", "",
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
                                configuredLevel.guards.get(0).guardItem = "stungun";
                                configuredLevel.viewportPosition.x = configuredLevel.guards.get(0).position.x - 80;
                                configuredLevel.getPlayer().health = 20;
                                configuredLevel.guards.get(0).alert = false;
                                configuredLevel.getPlayer().alert = true;
                                configuredLevel.getPlayer().mouthState = Enums.MouthState.OPEN;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("Tap on the player to\nduck the Stun Gun lasers!", "c7-x:", 0, "", "", "", "",
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
                                    if (configuredLevel.guards.get(0).heldItem.stungun.lasers.size > 0) {
                                        configuredLevel.getPlayer().duck = true;
                                        configuredLevel.guards.get(0).heldItem.stungun.fire = false;
                                    } else {
                                        configuredLevel.currentBubble = 36;
                                    }
                                }
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble(".....", "Guard 1:", 0, "", "", "", "",
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
                                configuredLevel.guards.get(0).heldItem.stungun.fire = false;
                                configuredLevel.guards.get(0).mouthState = Enums.MouthState.OPEN;
                                configuredLevel.getPlayer().duck = false;
                                configuredLevel.getPlayer().danger = false;
                                configuredLevel.getPlayer().useEnergy = false;
                                configuredLevel.continueBubbles = true;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("What!! I'm out of Ammo!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("Why does Central Security always\ngive us the cheap weapons?!", "Guard 1:", 0, "", "", "", "",
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
                configuredLevel.speechBubbles.add(new SpeechBubble("This is your chance!\nUnlock the door to your left!", "c7-x:", 1, "Got it!", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.currentBubble = 40;
                                configuredLevel.continueBubbles = false;
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
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
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
                                configuredLevel.currentBubble = 41;
                                configuredLevel.touchLocked = true;
                                configuredLevel.show = true;
                            }
                        }, configuredLevel));
                configuredLevel.speechBubbles.add(new SpeechBubble("You aren't going\nanywhere, prisoner!", "Guard 1:", 1, "Oh no!", "", "", "",
                        new Runnable() {
                            public void run() {
                                configuredLevel.defeat = true;
                                configuredLevel.verdict.verdict = false;
                                configuredLevel.verdict.verdictPhrase = "You went the wrong way!";
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

                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(-500, 235));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(-490, -392), new Vector2(-650, -309), configuredLevel);
                break;
            case 6:
                configuredLevel.show = false;
                configuredLevel.continueBubbles = false;
                configuredLevel.touchLocked = false;
                /*LEVEL CONFIGURATIONS:*/
                //player always starts inside the currentCatacomb
                configuredLevel.catacombs = new Array<Catacomb>();
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -100), "Locked", "Locked", "Closed", "Locked", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -15), "Locked", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(150, 70), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(300, -15), "Closed", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-300, -15), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-550, 70), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-300, -185), "Closed", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-550, -270), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -185), "Closed", "Locked", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(150, -270), "Closed", "Closed", "Closed", "Closed", "Locked", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(0, -355), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(300, -355), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.catacombs.add(new Catacomb(new Vector2(-150, -440), "Closed", "Closed", "Closed", "Closed", "Closed", "Closed", configuredLevel));
                configuredLevel.currentCatacomb = 0;

                configuredLevel.items = new DelayedRemovalArray<Item>();
                configuredLevel.items.add(new Item(new Vector2(60, 10), configuredLevel.viewportPosition, "key"));

                configuredLevel.puzzles = new Array<Puzzle>();

                configuredLevel.words = new Array<Word>();

                configuredLevel.chests = new Array<Chest>();

                configuredLevel.guards = new Array<Guard>();

                //add SpeechBubbles
                configuredLevel.speechBubbles = new Array<SpeechBubble>();
                configuredLevel.speechBubbles.add(new SpeechBubble("", "c7-x:", 0, "", "", "", "",
                        new Runnable() {public void run() {}},
                        new Runnable() {public void run() {}},
                        new Runnable() {public void run() {}},
                        new Runnable() {public void run() {}}, configuredLevel));
                configuredLevel.currentBubble = 0;

                configuredLevel.player.position.set(new Vector2(-50, 65));
                //exit door
                configuredLevel.exitDoor = new Exit(new Vector2(-50, -82), new Vector2(-100, -84), configuredLevel);
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
            if (!configuredLevel.words.get(0).solved) {
                configuredLevel.exitDoor.show = false;
            }
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
