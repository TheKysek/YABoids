import java.util.HashSet;
import java.util.Set;

public class Board
{
    private Set<Boid> boids = new HashSet<>();

    private double width;
    private double height;

    public Board(double width, double height)
    {
        this.width = width;
        this.height = height;
    }

    public double getWidth()
    {
        return width;
    }

    public double getHeight()
    {
        return height;
    }

    public Set<Boid> getBoids()
    {
        return boids;
    }

    public void moveBoids()
    {
        boids.forEach(Boid::move);
    }

    public void addBoid(Boid boid)
    {
        boids.add(boid);
    }
}
