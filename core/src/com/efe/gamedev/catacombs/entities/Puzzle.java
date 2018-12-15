package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.items.Fire;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 1/16/2018.
 * Puzzles for player to solve
 * //The "shapes" puzzles are made out of several Shape classes which can change shape when they are pressed. If the puzzle's shapes are the same as the solution's shapes, it will run the solveFunction (give player a key or something) and all shapes will turn golden.
 */

public class Puzzle {
    private Vector2 position;
    public Vector2 hintPosition;
    private String type;
    public Enums.Shape[] solution;
    private Runnable solveFunction;
    public boolean solved;
    //shapes variables
    private Shape[] shapes;
    private Array<Shape> hintShapes;
    //holes variables: "whacker" puzzles
    private Hole[] holes;
    private float whackTimer;
    private float difficulty;
    private float successTimer;
    private int successNumber;
    //fire variables
    private boolean torch1;
    private boolean torch2;
    private boolean torch3;
    //level
    private Level level;

    public Puzzle (Vector2 position, Vector2 hintPosition, String type, Enums.Shape[] solution, final Runnable solveFunction, Level level) {
        this.position = position;
        this.hintPosition = hintPosition;
        this.type = type;
        this.solveFunction = solveFunction;
        this.solution = solution;
        solved = false;
        shapes = new Shape[]{new Shape(new Vector2(position.x + 2.5f, position.y + 2.5f), level, 15),new Shape(new Vector2(position.x + 22.5f, position.y + 2.5f), level, 15),new Shape(new Vector2(position.x + 42.5f, position.y + 2.5f), level, 15),new Shape(new Vector2(position.x + 62.5f, position.y + 2.5f), level, 15),new Shape(new Vector2(position.x + 82.5f, position.y + 2.5f), level, 15)};
        hintShapes = new Array<Shape>();
        //add more holes if difficulty is higher
        if ((Integer.parseInt(type.charAt(type.length()-1)+"")) == 3) {
            holes = new Hole[]{new Hole(new Vector2(position.x, position.y)),new Hole(new Vector2(position.x + 20, position.y)),new Hole(new Vector2(position.x + 40, position.y)),new Hole(new Vector2(position.x, position.y + 30)),new Hole(new Vector2(position.x + 20, position.y + 30)),new Hole(new Vector2(position.x + 40, position.y + 30)),new Hole(new Vector2(position.x, position.y + 60)),new Hole(new Vector2(position.x + 20, position.y + 60)),new Hole(new Vector2(position.x + 40, position.y + 60))};
        } else {
          //otherwise, have 6 holes
            holes = new Hole[]{new Hole(new Vector2(position.x, position.y)),new Hole(new Vector2(position.x + 20, position.y)),new Hole(new Vector2(position.x + 40, position.y)),new Hole(new Vector2(position.x, position.y + 30)),new Hole(new Vector2(position.x + 20, position.y + 30)),new Hole(new Vector2(position.x + 40, position.y + 30))};
        }
        this.level = level;
        whackTimer = 0;
        difficulty = 25;
        successTimer = 0;
        successNumber = 0;
        torch1 = false;
        torch2 = false;
        torch3 = false;
    }

    public void render (ShapeRenderer renderer) {
        for (Shape shape : shapes) {
            shape.solved = solved;
        }
        //trigger function if puzzle is solved
        if (type.equals("shapes1") && solution[0].equals(shapes[0].getShapeType()) && !solved) {
            solveFunction.run();
            solved = true;
            level.gameplayScreen.sound3.play();
        }
        if (type.equals("shapes2") && solution[0].equals(shapes[0].getShapeType()) && solution[1].equals(shapes[1].getShapeType()) && !solved) {
            solveFunction.run();
            solved = true;
            level.gameplayScreen.sound3.play();
        }
        if (type.equals("shapes3") && solution[0].equals(shapes[0].getShapeType()) && solution[1].equals(shapes[1].getShapeType()) && solution[2].equals(shapes[2].getShapeType()) && !solved) {
            solveFunction.run();
            solved = true;
            level.gameplayScreen.sound3.play();
        }
        if (type.equals("shapes4") && solution[0].equals(shapes[0].getShapeType()) && solution[1].equals(shapes[1].getShapeType()) && solution[2].equals(shapes[2].getShapeType()) && solution[3].equals(shapes[3].getShapeType()) && !solved) {
            solveFunction.run();
            solved = true;
            level.gameplayScreen.sound3.play();
        }
        if (type.equals("shapes5") && solution[0].equals(shapes[0].getShapeType()) && solution[1].equals(shapes[1].getShapeType()) && solution[2].equals(shapes[2].getShapeType()) && solution[3].equals(shapes[3].getShapeType()) && solution[4].equals(shapes[4].getShapeType()) && !solved) {
            solveFunction.run();
            solved = true;
            level.gameplayScreen.sound3.play();
        }
        //Shape puzzles
        if (type.equals("shapes1")) {
            renderer.setColor(Color.LIGHT_GRAY);
            renderer.rect(position.x, position.y, 20, 20);
            shapes[0].render(renderer);
        } else if (type.equals("shapes2")) {
            renderer.setColor(Color.LIGHT_GRAY);
            renderer.rect(position.x, position.y, 40, 20);
            shapes[0].render(renderer);
            shapes[1].render(renderer);
        } else if (type.equals("shapes3")) {
            renderer.setColor(Color.LIGHT_GRAY);
            renderer.rect(position.x, position.y, 60, 20);
            shapes[0].render(renderer);
            shapes[1].render(renderer);
            shapes[2].render(renderer);
        } else if (type.equals("shapes4")) {
            renderer.setColor(Color.LIGHT_GRAY);
            renderer.rect(position.x, position.y, 80, 20);
            shapes[0].render(renderer);
            shapes[1].render(renderer);
            shapes[2].render(renderer);
            shapes[3].render(renderer);
        } else if (type.equals("shapes5")) {
            renderer.setColor(Color.LIGHT_GRAY);
            renderer.rect(position.x, position.y, 100, 20);
            shapes[0].render(renderer);
            shapes[1].render(renderer);
            shapes[2].render(renderer);
            shapes[3].render(renderer);
            shapes[4].render(renderer);
        } else if (type.equals("whacker1") || type.equals("whacker2") || type.equals("whacker3")) {
            //whacker puzzles
            //set difficulty: whacker1 = 25, whacker2 = 15, whacker3 = 20, but has 9 different holes.
            switch (Integer.parseInt(type.charAt(type.length()-1)+"")) {
                case 1:
                    difficulty = 25;
                    break;
                case 2:
                    difficulty = 15;
                    break;
                case 3:
                    difficulty = 20;
                    break;
            }
            //render and update holes
            renderHoles(renderer);
        } else if (type.equals("fire1") || type.equals("fire2") || type.equals("fire3")) {
            //render torch
            renderTorch(renderer);
        }
    }
    //render the hint for shape puzzles, telling which shapes go where
    public void renderHint (ShapeRenderer renderer) {
        //draw gray rectangle
        renderer.setColor(Color.LIGHT_GRAY);
        renderer.rect(hintPosition.x, hintPosition.y, solution.length * 15, 15);
        if (type.equals("shapes1") || type.equals("shapes2") || type.equals("shapes3") || type.equals("shapes4") || type.equals("shapes5")) {
            //add shapes to the hintShapes array depending on size of puzzle
            for (int i = 0; i < solution.length; i++) {
                hintShapes.add(new Shape(new Vector2(hintPosition.x + (2.5f * 0.75f) + (i * 15f), hintPosition.y + (2.5f * 0.75f)), level, (15 * 0.75f)));
            }
            //render shapes
            for (int i = 0; i < solution.length; i++) {
                hintShapes.get(i).setShapeType(solution[i]);
                hintShapes.get(i).changeable = false;
                hintShapes.get(i).render(renderer);
                //set hint shapes position to always be at the hintPosition
                hintShapes.get(i).setShapePosition(new Vector2(hintPosition.x + (2.5f * 0.75f) + (i * 15f), hintPosition.y + (2.5f * 0.75f)));
            }
        }
    }
    //render the holes that the ball pops up from at random intervals. Used in the "whacker" puzzles.
    private void renderHoles (ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //add six holes in a 2 by 3 grid
        //render holes
        for (int i = 0; i < holes.length; i++) {
            holes[i].render(renderer);
            //two holes cannot both have a ball, only one can
            for (int j = 0; j < holes.length; j++) {
                if (holes[i].hasBall && holes[j].hasBall && i != j) {
                    holes[i].hasBall = false;
                    holes[j].hasBall = false;
                }
            }
        }
        //randomly make a hole have a ball for a random interval
        if (MathUtils.random() < 0.1f && whackTimer == 0 && !solved) {
            holes[MathUtils.random(holes.length - 1)].hasBall = true;
        }
        //set timer for how long a hole can have ball
        for (int i = 0; i < holes.length; i++) {
            //If a hole has a ball, let it keep the ball for a count of 20 (very short time period), before randomly giving the ball to another hole and restarting the timer.
            if (holes[i].hasBall && !holes[i].ballTouched) {
                whackTimer++;
            }
            if (whackTimer > difficulty) {
                holes[i].hasBall = false;
                whackTimer = 0;
            }
            //If you tap on ball when is has not been touched, freeze the puzzle and turn ball green
            if (holes[i].hasBall && !holes[i].ballTouched && holes[i].touchHole(level.touchPosition) && !level.touchPosition.equals(new Vector2())) {
                level.touchPosition.set(new Vector2());
                holes[i].ballTouched = true;
                whackTimer = 1;
            }
            //if ball is touched, set a short timer
            if (holes[i].ballTouched && successTimer < 126) {
                successTimer++;
                //color is golden on the last time
                if (successNumber == 2 && holes[i].hasBall && !solved) {
                    holes[i].golden = true;
                    solved = true;
                    solveFunction.run();
                    level.gameplayScreen.sound3.play();
                }
                //make ball flash its opacity
                if (successTimer >= 25 && successTimer < 50) {
                    holes[i].hasBall = false;
                } else if (successTimer >= 50 && successTimer < 75) {
                    holes[i].hasBall = true;
                } else if (successTimer >= 75 && successTimer < 100) {
                    holes[i].hasBall = false;
                } else if (successTimer >= 100 && successTimer < 125) {
                    holes[i].hasBall = true;
                } else if (successTimer >= 125) {
                    successNumber++;
                    //prevent puzzle from resetting on the third time
                    if (successNumber != 3) {
                        successTimer = 0;
                        whackTimer = 0;
                        holes[i].hasBall = false;
                        holes[i].ballTouched = false;
                    }

                }
            }
        }
        //reset touchPosition after pressing a hole. No cheating!
        for (int i = 0; i < holes.length; i++) {
            if (holes[i].touchHole(level.touchPosition)) {
                level.touchPosition.set(new Vector2());
            }
        }
    }
    //draw torch for fire puzzles
    private void renderTorch (ShapeRenderer renderer) {
        Fire[] fires = new Fire[]{new Fire(new Vector2(position.x + 5, position.y + 38))};
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(new Color(0.9f, 0.9f, 0.6f, 1f));
        //base of torch
        renderer.triangle(position.x, position.y, position.x + 4, position.y + 5, position.x + 20, position.y);
        renderer.triangle(position.x + 4, position.y + 5, position.x + 16, position.y + 5, position.x + 20, position.y);
        //neck of torch
        renderer.rect(position.x + 8, position.y + 5, 4, 30);
        //torch bowl 1
        renderer.arc(position.x + 10, position.y + 38, 10, 180, 180, 20);
        //render fire
        //place fire item on torch to solve puzzle
        if (level.getPlayer().heldItem.itemType.equals("fire") && level.inventory.inventoryItems.size > 0 && level.touchPosition.dst(new Vector2(position.x + 10, position.y + 38)) < 12 && level.getPlayer().position.x > position.x && level.getPlayer().getPosition().x < position.x + 20) {
            torch1 = true;
            level.inventory.deleteCurrentItem();
        }
        if (torch1) {
            fires[0].render(renderer);
        }
        //render fire light
        if (!solved) {
            renderer.setColor(Color.GRAY);
            renderer.circle(position.x + 10, position.y + 34, 3, 6);
        } else {
            renderer.setColor(new Color((1f / 255f) * 254, (1f / 255f) * 99, (1f / 255f), 1f));
            renderer.circle(position.x + 10, position.y + 34, 3, 6);
        }
        //solve puzzle
        //easy
        if (type.equals("fire1") && torch1 && !solved) {
            solved = true;
            solveFunction.run();
            level.gameplayScreen.sound3.play();
        }
        //medium
        if (type.equals("fire2") && torch1 && torch2 && !solved) {
            solved = true;
            solveFunction.run();
            level.gameplayScreen.sound3.play();
        }
        //hard
        if (type.equals("fire3") && torch1 && torch2 && torch3 && !solved) {
            solved = true;
            solveFunction.run();
            level.gameplayScreen.sound3.play();
        }
    }

}
