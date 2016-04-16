package net.YABoids;

import net.YABoids.geometry.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class Boid
{
    static final double VIEW_DISTANCE = 70;

    private static final double MAX_VELOCITY = 4.8;

    private static final double PRESERVE_PREVIOUS_VELOCITY_MULTIPLIER = 1;

    private static final double COLLISION_AVOIDANCE_RADIUS = 35;

    private static final double COLLISION_AVOIDANCE_MULTIPLIER = 0.15;

    private static final double ALIGN_MULTIPLIER = 0.6;

    private static final double SCARE_MULTIPLIER = 0.8;
    //private static final double TARGET_MULTIPLIER = 0;

    private static final double CENTER_OF_MASS_MULTIPLIER = 0.45;

    private static final double JIGGLE_MULTIPLIER = 0.5;
    private static final double JIGGLE_PROBABILITY = 0.5;

    private double x, y;

    private Set<Boid> boidsInViewDistance;

    private Vector scare;

    private Vector velocity;

    Boid(double x, double y, Vector scare)
    {
        this.x = x;
        this.y = y;
        this.scare = scare;
        this.velocity = new Vector();
    }

    private double distance(Boid boid)
    {
        double dx = x - boid.getX();
        double dy = y - boid.getY();

        return Math.sqrt(dx * dx + dy * dy);
    }

    private double distance(Vector point)
    {
        double dx = x - point.getX();
        double dy = y - point.getY();

        return Math.sqrt(dx * dx + dy * dy);
    }

    double getX()
    {
        return x;
    }

    double getY()
    {
        return y;
    }

    private void applyJiggle()
    {
        if (Math.random() < JIGGLE_PROBABILITY)
        {
            // Subtracting 0.5 to also get negative numbers
            velocity.add((Math.random() - 0.5) * JIGGLE_MULTIPLIER, (Math.random() - 0.5) * JIGGLE_MULTIPLIER);
        }
    }

    private void applyAlign()
    {
        if (boidsInViewDistance.size() > 0)
        {
            Vector align = new Vector();

            for (Boid boid : boidsInViewDistance)
            {
                align.add(boid.getVelocity());
            }

            align.normalize();

            align.scale(ALIGN_MULTIPLIER);

            velocity.add(align);
        }
    }

    private void applyCenterOfMass()
    {
        if (boidsInViewDistance.size() > 0)
        {

            double cx = 0;
            double cy = 0;

            for (Boid boid : boidsInViewDistance)
            {
                cx += boid.getX();
                cy += boid.getY();
            }

            cx = cx / boidsInViewDistance.size();
            cy = cy / boidsInViewDistance.size();

            Vector toCenter = new Vector(cx - x, cy - y);

            toCenter.normalize();
            toCenter.scale(CENTER_OF_MASS_MULTIPLIER);

            velocity.add(toCenter);
        }
    }

    private void applyCollisionAvoidance()
    {
        if (boidsInViewDistance.size() > 0)
        {
            Vector avoid = new Vector();

            boidsInViewDistance.stream().filter(boid -> distance(boid) < COLLISION_AVOIDANCE_RADIUS).forEach(boid -> {
                Vector avoidBoid = new Vector(x - boid.getX(), y - boid.getY());
                avoidBoid.scale((COLLISION_AVOIDANCE_RADIUS - distance(boid)) / COLLISION_AVOIDANCE_RADIUS);
                avoid.add(avoidBoid);
            });

            avoid.scale(COLLISION_AVOIDANCE_MULTIPLIER);

            velocity.add(avoid);

        }
    }

    private void applyScare()
    {
        if (scare != null && distance(scare) < VIEW_DISTANCE)
        {
            double dx = x - scare.getX();
            double dy = y - scare.getY();

            Vector away = new Vector(dx, dy);
            away.normalize();
            away.scale(VIEW_DISTANCE / distance(scare));
            away.scale(SCARE_MULTIPLIER);

            velocity.add(away);
        }
    }

    /**
     * Moves the boid
     *
     * @param boidsNearby Set of boids to move according to
     */
    void move(Set<Boid> boidsNearby)
    {
        boidsInViewDistance = new HashSet<>();

        boidsInViewDistance.addAll(boidsNearby.stream().filter(boid -> boid != this && distance(boid) < VIEW_DISTANCE).collect(Collectors.toSet()));

        velocity.scale(PRESERVE_PREVIOUS_VELOCITY_MULTIPLIER);

        applyScare();
        applyAlign();
        applyCenterOfMass();
        applyJiggle();
        applyCollisionAvoidance();

        velocity.limit(MAX_VELOCITY);

        x += velocity.getX();
        y += velocity.getY();
    }

    Vector getVelocity()
    {
        return velocity;
    }
}
