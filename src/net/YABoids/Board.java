package net.YABoids;

import net.YABoids.geometry.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class Board
{
    private static final double MARGIN = 20;
    private static final double REPEL_FORCE = 2;
    double width;
    double height;
    int cellsHorizontally;
    int cellsVertically;
    private ArrayList<ArrayList<Set<Boid>>> grid;
    private Set<Boid> boids = new HashSet<>();
    private Vector scare = new Vector();

    Board(double width, double height)
    {
        this.width = width;
        this.height = height;

        this.cellsHorizontally = (int) Math.ceil(width / Boid.VIEW_DISTANCE);
        this.cellsVertically = (int) Math.ceil(height / Boid.VIEW_DISTANCE);

        initGrid();
    }

    void addRandomBoids(int numOfBoids)
    {
        for (int i = 0; i < numOfBoids; i++)
        {
            addBoid(new Boid(getWidth() * Math.random(), getHeight() * Math.random(), getScare()));
        }
    }

    private void initGrid()
    {
        this.grid = new ArrayList<>();

        for (int i = 0; i < cellsHorizontally; i++)
        {
            grid.add(new ArrayList<>());

            for (int j = 0; j < cellsVertically; j++)
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

            for (int i = -1; i < 2; i++)
            {
                for (int j = -1; j < 2; j++)
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

    private Set<Boid> getFromCell(int x, int y)
    {
        if (cellInBounds(x, y))
        {
            return grid.get(x).get(y);
        } else
        {
            return new HashSet<>();
        }
    }

    private boolean cellInBounds(int x, int y)
    {
        return x >= 0 && y >= 0 && x < cellsHorizontally && y < cellsVertically;
    }

    private void clearGrid()
    {
        for (ArrayList<Set<Boid>> column : grid)
        {
            column.forEach(Set<Boid>::clear);
        }
    }

    private void updateGrid()
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

    private void addBoid(Boid boid)
    {
        boids.add(boid);
    }

}
