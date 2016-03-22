package net.YABoids.geometry;

public class Point
{
    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    private double x;
    private double y;

    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getY()
    {
        return y;
    }

    public double getX()
    {
        return x;
    }
}