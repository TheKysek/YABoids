import geometry.Point;
import geometry.Vector;

import java.util.Set;

class Boid
{
    static final double VIEW_DISTANCE = 150;

    static final double MAX_VELOCITY = 4;

    static final double PRESERVE_PREVIOUS_VELOCITY_MULTIPLIER = 1.2;

    static final double COLLISION_AVOIDANCE_RADIUS = 35;
    static final double COLLISION_AVOIDANCE_MULTIPLIER = 0.11;

    static final double ALIGN_MULTIPLIER = 0.02;
    static final double ALIGN_PROBABILITY = 1;

    static final double TARGET_MULTIPLIER = 0.04;

    static final double JIGGLE_MULTIPLIER = 0.9;
    static final double JIGGLE_PROBABILITY = 0.3;
    private double x, y;
    private Set<Boid> boids;
    private Point target = new Point(900, 480);
    private Vector velocity;
    private Vector nextVelocity;

    Boid(double x, double y, Set<Boid> boids)
    {
        this.x = x;
        this.y = y;
        this.boids = boids;

        velocity = new Vector();
        nextVelocity = new Vector();
    }

    public Point getTarget()
    {
        return target;
    }

    public void setTarget(Point target)
    {
        this.target = target;
    }

    private double distance(Boid boid)
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

    double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    private void applyJiggle()
    {
        // -0.5 to also get negative numbers
        nextVelocity.addX((Math.random() - 0.5) * JIGGLE_MULTIPLIER);
        nextVelocity.addY((Math.random() - 0.5) * JIGGLE_MULTIPLIER);
    }

    void move()
    {
        updateNextVelocity();
        applyNextVelocity();
    }

    void updateNextVelocity()
    {
        nextVelocity = new Vector(velocity);

        nextVelocity.scale(PRESERVE_PREVIOUS_VELOCITY_MULTIPLIER);

        if (Math.random() < JIGGLE_PROBABILITY)
        {
            applyJiggle();
        }

        if (target != null)
        {
            Vector tmp = new Vector(target.getX() - x, target.getY() - y);
            tmp.forceLength(MAX_VELOCITY);
            tmp.scale(TARGET_MULTIPLIER);
            nextVelocity.add(tmp);
        }

        for (Boid boid : boids)
        {
            if (boid == this)
            {
                continue;
            }

            double distance = distance(boid);

            if (distance > VIEW_DISTANCE)
            {
                continue;
            }

            if (distance < COLLISION_AVOIDANCE_RADIUS)
            {
                Vector tmp = new Vector(target.getX() - x, target.getY() - y);
                tmp.forceLength(MAX_VELOCITY);
                tmp.scale(-COLLISION_AVOIDANCE_MULTIPLIER);
                nextVelocity.add(tmp);
            }

            if (Math.random() < ALIGN_PROBABILITY)
            {
                Vector tmp = new Vector(boid.getVelocity());
                tmp.scale(ALIGN_MULTIPLIER);
                nextVelocity.add(tmp);
            }

            //System.out.println(distance(boid));
        }


        nextVelocity.limit(MAX_VELOCITY);

    }

    private void applyNextVelocity()
    {
        x += nextVelocity.getX();
        y += nextVelocity.getY();

        velocity = nextVelocity;
    }

    private Vector getVelocity()
    {
        return velocity;
    }
}
