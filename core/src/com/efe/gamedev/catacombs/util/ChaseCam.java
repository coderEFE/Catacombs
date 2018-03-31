package com.efe.gamedev.catacombs.util;

import com.badlogic.gdx.graphics.Camera;
import com.efe.gamedev.catacombs.entities.Player;

/**
 * Created by coder on 11/20/2017.
 * The ChaseCam makes it look as though the Player is always in the center because the Player is moving and the ChaseCam sets the viewport camera to follow the player.
 * A very simple custom Camera using Orthographic view.
 */

public class ChaseCam {

    public static final String TAG = ChaseCam.class.getName();

    private Camera camera;
    private Player target;
    //get Camera and Player
    public ChaseCam (Camera camera, Player target) {
        this.camera = camera;
        this.target = target;
    }
    //update ChaseCam so that it aligns to Player's position, thus making it look like the the camera is moving and not the Player because the Player is always in the center.
    public void update () {
        camera.position.x = target.getPosition().x;
        camera.position.y = target.getPosition().y;
    }

}
