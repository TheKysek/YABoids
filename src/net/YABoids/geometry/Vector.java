package net.YABoids.geometry;

public class Vector
{
    private double x;
    private double y;

    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector()
    {
        x = 0;
        y = 0;
    }

    public void add(Vector vector)
    {
        x += vector.getX();
        y += vector.getY();
    }

    public void add(double x, double y)
    {
        this.x += x;
        this.y += y;
    }

    public void addX(double d)
    {
        x += d;
    }

    public void addY(double d)
    {
        y += d;
    }

    public void subtract(Vector vector)
    {
        x -= vector.getX();
        y -= vector.getY();
    }

    public void set(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public void scale(double scale)
    {
        x *= scale;
        y *= scale;
    }

    public void limit(double maxLength)
    {
        if (getLength() > maxLength)
        {
            scale(maxLength / getLength());
        }
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public double getRotationRad()
    {
        return Math.atan2(y, x);
    }

    public double getLength()
    {
        return Math.sqrt(x * x + y * y);
    }

}
