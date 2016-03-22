package net.YABoids;

import net.YABoids.geometry.Vector;

import java.util.HashSet;
import java.util.Set;

class Board
{
    private static final double MARGIN = 20;
    private static final double REPEL_FORCE = 2;

    private Set<Boid> boids = new HashSet<>();
    private Vector scare = new Vector();
    private double width;
    private double height;

    Board(double width, double height, int numOfBoids)
    {
        this.width = width;
        this.height = height;

        for (int i = 0; i < numOfBoids; i++)
        {
            addBoid(new Boid(getWidth() * Math.random(), getHeight() * Math.random(), getBoids(), getScare()));
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
        boids.forEach(Boid::move);

        for (Boid boid : boids)
        {
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

    void addBoid(Boid boid)
    {
        boids.add(boid);
    }

}
