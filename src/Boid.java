import geometry.Point;
import geometry.Vector;

import java.util.Set;

public class Boid
{
    static final double RADIUS = 10;
    static final double MAX_VELOCITY = 4;

    private double x;
    private double y;

    private Set<Boid> boids;
    private Point target;
    private Vector velocity;

    Boid(double x, double y, Set<Boid> boids)
    {
        this.x = x;
        this.y = y;
        this.boids = boids;
    }

    public Point getTarget()
    {
        return target;
    }

    public void setTarget(Point target)
    {
        this.target = target;
    }

    public double distance(Boid boid)
    {
        double dx = getX() - boid.getX();
        double dy = getY() - boid.getY();

        return Math.sqrt(dx * dx + dy * dy);
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public void move()
    {
        for (Boid boid : boids)
        {
            if (boid == this)
            {
                continue;
            }
            System.out.println(distance(boid));
        }
    }
}
