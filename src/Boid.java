import geometry.Point;
import geometry.Vector;

import java.util.Set;

public class Boid
{
    static final double RADIUS = 20;
    static final double MAX_VELOCITY = 4;

    static final double MULTIPLIER_JIGGLE = 0.8;

    private double x, y;

    private Set<Boid> boids;
    private Point target;

    private Vector velocity;
    private Vector previousVelocity;

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

    public double distance(Point point)
    {
        double dx = getX() - point.getX();
        double dy = getY() - point.getY();

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

    private void applyJiggle()
    {
        // -0.5 to also get negative numbers
        velocity.addX((Math.random() - 0.5) * MULTIPLIER_JIGGLE);
        velocity.addY((Math.random() - 0.5) * MULTIPLIER_JIGGLE);
    }

    public void move()
    {
        previousVelocity = velocity;
        velocity = new Vector();

        applyJiggle();


        for (Boid boid : boids)
        {
            if (boid == this || distance(boid) > 100)
            {
                continue;
            }
            //System.out.println(distance(boid));
        }


        velocity.limit(MAX_VELOCITY);

        x += velocity.getX();
        y += velocity.getY();
    }
}
