import geometry.Point;
import geometry.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class Boid
{
    private static final double VIEW_DISTANCE = 80;

    private static final double MAX_VELOCITY = 5.5;

    private static final double PRESERVE_PREVIOUS_VELOCITY_MULTIPLIER = 1;

    private static final double COLLISION_AVOIDANCE_RADIUS = 35;

    private static final double COLLISION_AVOIDANCE_MULTIPLIER = 0.1;

    private static final double ALIGN_MULTIPLIER = 0.06;

    //private static final double TARGET_MULTIPLIER = 0.0003;
    private static final double TARGET_MULTIPLIER = 0;

    private static final double CENTER_OF_MASS_MULTIPLIER = 0.01;

    private static final double JIGGLE_MULTIPLIER = 1.4;
    private static final double JIGGLE_PROBABILITY = 0.3;

    private double x, y;

    private Set<Boid> boids;
    private Set<Boid> boidsNearby;

    private Point target = new Point(900, 480);

    private Vector velocity;

    Boid(double x, double y, Set<Boid> boids)
    {
        this.x = x;
        this.y = y;
        this.boids = boids;

        velocity = new Vector();
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
        if (Math.random() < JIGGLE_PROBABILITY)
        {
            // -0.5 to also get negative numbers
            velocity.addX((Math.random() - 0.5) * JIGGLE_MULTIPLIER);
            velocity.addY((Math.random() - 0.5) * JIGGLE_MULTIPLIER);
        }
    }

    private void applyAlign()
    {
        if (boidsNearby.size() > 0)
        {
            Vector align = new Vector();

            for (Boid boid : boidsNearby)
            {
                align.add(boid.getVelocity());
            }

            align.scale(1.0 / boidsNearby.size());

            align.scale(ALIGN_MULTIPLIER);

            velocity.add(align);
        }
    }

    private void applyCenterOfMass()
    {
        if (boidsNearby.size() > 0)
        {

            double cx = 0;
            double cy = 0;

            for (Boid boid : boidsNearby)
            {
                cx += boid.getX();
                cy += boid.getY();
            }


            cx = cx / boidsNearby.size();
            cy = cy / boidsNearby.size();

            Vector toCenter = new Vector(cx - x, cy - y);

            toCenter.scale(CENTER_OF_MASS_MULTIPLIER);

            velocity.add(toCenter);
        }
    }

    private void applyCollisionAvoidance()
    {
        if (boidsNearby.size() > 0)
        {
            Vector avoid = new Vector();

            for (Boid boid : boidsNearby)
            {
                if (distance(boid) < COLLISION_AVOIDANCE_RADIUS)
                {
                    Vector avoidBoid = new Vector(x - boid.getX(), y - boid.getY());
                    avoidBoid.scale((COLLISION_AVOIDANCE_RADIUS - distance(boid))/COLLISION_AVOIDANCE_RADIUS);
                    avoid.add(avoidBoid);
                }
            }

            avoid.scale(COLLISION_AVOIDANCE_MULTIPLIER);

            velocity.add(avoid);


        }
    }

    private void applyToTarget()
    {
        if (target != null)
        {
            double dx = target.getX() - x;
            double dy = target.getY() - y;

            Vector toTarget = new Vector(dx, dy);
            toTarget.scale(TARGET_MULTIPLIER);

            velocity.add(toTarget);
        }
    }

    void move()
    {
        boidsNearby = new HashSet<>();

        //System.out.println(boids.size());

        boidsNearby.addAll(boids.stream().filter(boid -> boid != this && distance(boid) < VIEW_DISTANCE).collect(Collectors.toList()));

        velocity.scale(PRESERVE_PREVIOUS_VELOCITY_MULTIPLIER);

        applyToTarget();
        applyAlign();
        applyCenterOfMass();
        applyJiggle();
        applyCollisionAvoidance();

        velocity.limit(MAX_VELOCITY);

        x += velocity.getX();
        y += velocity.getY();

        //System.out.println("(" + x + ", " + y + ")");
    }

    Vector getVelocity()
    {
        return velocity;
    }
}
