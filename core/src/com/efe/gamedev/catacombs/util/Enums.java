package com.efe.gamedev.catacombs.util;

/**
 * Created by coder on 11/20/2017.
 * This class takes care of enums used by other classes and objects
 */

public class Enums {
    //mouth states
    public enum MouthState {
        NORMAL,
        OPEN,
        TALKING
    }
    //jump states
    public enum JumpState {
        GROUNDED,
        JUMPING,
        FALLING
    }
    //Player directions
    public enum Facing {
        LEFT,
        RIGHT
    }
    //Shapes
    public enum Shape {
        SQUARE,
        TRIANGLE,
        CIRCLE
    }
    //Guard states
    public enum GuardState {
        PATROLLING,
        ALERT,
        ATTACK,
        UNSURE,
        FIGHT,
        DEFEATED
    }
    //Boss states
    public enum BossState {
        NORMAL,
        HUNT,
        ATTACK,
        SMASH
    }
}