package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 1/16/2018.
 * Puzzles for player to solve
 */

public class Puzzle {
    private Vector2 position;
    private Vector2 hintPosition;
    private String type;
    private Enums.Shape[] solution;
    private Runnable solveFunction;
    private boolean solved;
    private Shape[] shapes;
    private Array<Shape> hintShapes;
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
        this.level = level;
    }

    public void render (ShapeRenderer renderer) {
        for (Shape shape : shapes) {
            shape.solved = solved;
        }
        //trigger function if puzzle is solved
        if (type.equals("shapes1") && solution[0].equals(shapes[0].getShapeType()) && !solved) {
            solveFunction.run();
            solved = true;
        }
        if (type.equals("shapes2") && solution[0].equals(shapes[0].getShapeType()) && solution[1].equals(shapes[1].getShapeType()) && !solved) {
            solveFunction.run();
            solved = true;
        }
        if (type.equals("shapes3") && solution[0].equals(shapes[0].getShapeType()) && solution[1].equals(shapes[1].getShapeType()) && solution[2].equals(shapes[2].getShapeType()) && !solved) {
            solveFunction.run();
            solved = true;
        }
        if (type.equals("shapes4") && solution[0].equals(shapes[0].getShapeType()) && solution[1].equals(shapes[1].getShapeType()) && solution[2].equals(shapes[2].getShapeType()) && solution[3].equals(shapes[3].getShapeType()) && !solved) {
            solveFunction.run();
            solved = true;
        }
        if (type.equals("shapes5") && solution[0].equals(shapes[0].getShapeType()) && solution[1].equals(shapes[1].getShapeType()) && solution[2].equals(shapes[2].getShapeType()) && solution[23].equals(shapes[3].getShapeType()) && solution[4].equals(shapes[4].getShapeType()) && !solved) {
            solveFunction.run();
            solved = true;
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
        }
    }

    public void renderHint (ShapeRenderer renderer) {
        renderer.setColor(Color.LIGHT_GRAY);
        renderer.rect(hintPosition.x, hintPosition.y, solution.length * 15, 15);
        if (type.equals("shapes1") || type.equals("shapes2") || type.equals("shapes3") || type.equals("shapes4") || type.equals("shapes5")) {
            for (int i = 0; i < solution.length; i++) {
                hintShapes.add(new Shape(new Vector2(hintPosition.x + (2.5f * 0.75f) + (i * 15f), hintPosition.y + (2.5f * 0.75f)), level, (15 * 0.75f)));
            }
            for (int i = 0; i < solution.length; i++) {
                hintShapes.get(i).setShapeType(solution[i]);
                hintShapes.get(i).changable = false;
                hintShapes.get(i).render(renderer);
            }
        }
    }
}
