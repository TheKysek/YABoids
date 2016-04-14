package net.YABoids;

import net.YABoids.geometry.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class Board
{
    private static final double MARGIN = 20;
    private static final double REPEL_FORCE = 2;

    private ArrayList<ArrayList<Set<Boid>>> grid;

    private Set<Boid> boids = new HashSet<>();

    private Vector scare = new Vector();

    private double width;
    private double height;

    private int cellsVertically;
    private int cellsHorizontally;

    Board(double width, double height, int numOfBoids)
    {
        this.width = width;
        this.height = height;

        this.cellsVertically = (int) Math.ceil(width / Boid.VIEW_DISTANCE);
        this.cellsHorizontally = (int) Math.ceil(height / Boid.VIEW_DISTANCE);

        initGrid();

        //System.out.println(ce);

        for (int i = 0; i < numOfBoids; i++)
        {
            addBoid(new Boid(getWidth() * Math.random(), getHeight() * Math.random(), getScare()));
        }
    }

    void initGrid()
    {
        this.grid = new ArrayList<>();

        for (int i = 0; i < cellsVertically; i++)
        {
            grid.add(new ArrayList<>());

            for (int j = 0; j < cellsHorizontally; j++)
            {
                grid.get(i).add(new HashSet<>());
            }
        }
    }

    Vector getScare()
    {
        return scare;
    }

    double getWidth()
    {
        return width;
    }

    double getHeight()
    {
        return height;
    }

    Set<Boid> getBoids()
    {
        return boids;
    }

    void moveBoids()
    {
        updateGrid();


        for (Boid boid : boids)
        {
            Set<Boid> boidsNearby = new HashSet<>();

            int x = (int) (boid.getX() / Boid.VIEW_DISTANCE);
            int y = (int) (boid.getY() / Boid.VIEW_DISTANCE);

            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    boidsNearby.addAll(getFromCell(x + i, y + j));
                }
            }

            boid.move(boidsNearby);

            if (boid.getX() < MARGIN)
            {
                boid.getVelocity().addX(REPEL_FORCE);
            }

            if (boid.getY() < MARGIN)
            {
                boid.getVelocity().addY(REPEL_FORCE);
            }

            if (boid.getX() > width - MARGIN)
            {
                boid.getVelocity().addX(-REPEL_FORCE);
            }

            if (boid.getY() > height - MARGIN)
            {
                boid.getVelocity().addY(-REPEL_FORCE);
            }
        }
    }

    Set<Boid> getFromCell(int x, int y)
    {
        if (cellInBounds(x, y))
        {
            return grid.get(x).get(y);
        } else
        {
            return new HashSet<>();
        }
    }

    boolean cellInBounds(int x, int y)
    {
        return !(x < 0 || y < 0 || x >= cellsVertically || y >= cellsHorizontally);
    }

    void clearGrid()
    {
        for (ArrayList<Set<Boid>> column : grid)
        {
            column.forEach(Set<Boid>::clear);
        }
    }

    void updateGrid()
    {
        clearGrid();

        for (Boid boid : boids)
        {
            int x = (int) (boid.getX() / Boid.VIEW_DISTANCE);
            int y = (int) (boid.getY() / Boid.VIEW_DISTANCE);

            if (cellInBounds(x, y))
            {
                grid.get(x).get(y).add(boid);
            }

        }
    }

    void addBoid(Boid boid)
    {
        boids.add(boid);
    }

}
