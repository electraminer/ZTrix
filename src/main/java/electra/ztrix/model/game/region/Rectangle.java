package electra.ztrix.model.game.region;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import electra.ztrix.model.game.common.Coordinate;
import electra.ztrix.model.game.common.Rotation;

/**
 * A rectangular Region with efficient containment checks. Can be used as a
 * bounding box to see if two Regions are close to intersecting.
 *
 * @author Electra
 */
public class Rectangle implements Region {
    /** The contianed Coordinate with the minimum X and Y, inclusive. */
    private final Coordinate minimum;
    /** The contianed Coordinate with the maximum X and Y, exclusive. */
    private final Coordinate maximum;

    /**
     * Creates a Rectangle with given minimum and maximum Coordinates.
     *
     * @param minimum
     *            The minimum Coordinate, inclusive, non-null.
     * @param maximum
     *            The maximum Coordinate, exclusive, non-null and greater than
     *            the minimum in both X and Y.
     */
    public Rectangle ( Coordinate minimum, Coordinate maximum ) {
        if ( minimum == null ) {
            throw new NullPointerException( "Rectangle(minimum) must be non-null." );
        }
        if ( maximum == null ) {
            throw new NullPointerException( "Rectangle(maximum) must be non-null." );
        }
        if ( maximum.getX() <= minimum.getX() ) {
            throw new IllegalArgumentException( "Rectangle(maximum) must be greater than the minimum in X." );
        }
        if ( maximum.getY() <= minimum.getY() ) {
            throw new IllegalArgumentException( "Rectangle(maximum) must be greater than the minimum in Y." );
        }
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * Creates a Rectangle with given minimum and maximum X and Y components.
     *
     * @param minX
     *            The minimum X value, inclusive.
     * @param minY
     *            The minimum Y value, inclusive.
     * @param maxX
     *            The maximum X value, exclusive and greater than the minimum.
     * @param maxY
     *            The maximum Y value, exclusive and greater than the minimum.
     */
    public Rectangle ( int minX, int minY, int maxX, int maxY ) {
        this( new Coordinate( minX, minY ), new Coordinate( maxX, maxY ) );
    }

    /**
     * Gets the contianed Coordinate with the minimum X and Y, inclusive.
     *
     * @return the minimum Coordinate.
     */
    public Coordinate getMinimum () {
        return minimum;
    }

    /**
     * Gets the contianed Coordinate with the maximum X and Y, exclusive.
     *
     * @return the maximum Coordinate.
     */
    public Coordinate getMaximum () {
        return maximum;
    }

    @Override
    public int count () {
        int width = maximum.getX() - minimum.getX();
        int height = maximum.getY() - minimum.getY();
        return width * height;
    }

    @Override
    public boolean contains ( Coordinate position ) {
        if ( position == null ) {
            throw new NullPointerException( "contains(position) must be non-null." );
        }
        // Check against each edge of the Rectangle.
        if ( position.getX() < minimum.getX() ) {
            return false;
        }
        if ( position.getY() < minimum.getY() ) {
            return false;
        }
        if ( position.getX() >= maximum.getX() ) {
            return false;
        }
        if ( position.getY() >= maximum.getY() ) {
            return false;
        }
        return true;
    }

    @Override
    public Rectangle getBounds () {
        return this;
    }

    @Override
    public Rectangle translate ( Coordinate offset ) {
        if ( offset == null ) {
            throw new NullPointerException( "translate(offset) must be non-null." );
        }
        // Translate the corners of the Rectangle.
        Coordinate newMinimum = minimum.plus( offset );
        Coordinate newMaximum = maximum.plus( offset );
        return new Rectangle( newMinimum, newMaximum );
    }

    @Override
    public Rectangle rotate ( Rotation direction, Coordinate center ) {
        if ( direction == null ) {
            throw new NullPointerException( "rotate(direction) must be non-null." );
        }
        if ( center == null ) {
            throw new NullPointerException( "rotate(center) must be non-null." );
        }
        // Rotate the corners of the Rectangle.
        Coordinate rotatedMinimum = minimum.rotate( direction, center );
        Coordinate inclusiveMaximum = maximum.minus( new Coordinate( 1, 1 ) );
        Coordinate rotatedMaximum = inclusiveMaximum.rotate( direction, center );
        // Rotation does not preserve corners, so fix with min() and max().
        int minX = Math.min( rotatedMinimum.getX(), rotatedMaximum.getX() );
        int minY = Math.min( rotatedMinimum.getY(), rotatedMaximum.getY() );
        int maxX = Math.max( rotatedMinimum.getX(), rotatedMaximum.getX() );
        int maxY = Math.max( rotatedMinimum.getY(), rotatedMaximum.getY() );
        return new Rectangle( minX, minY, maxX + 1, maxY + 1 );
    }

    /**
     * Checks whether the Rectangle contains a Region.
     *
     * @param region
     *            The Region to check, non-null.
     * @return True if the Rectangle contains the Region.
     */
    public boolean containsRegion ( Region region ) {
        if ( region == null ) {
            throw new NullPointerException( "containsRegion(region) must be non-null." );
        }
        // Check the Region's bounds against each edge of the Rectangle.
        Rectangle bounds = region.getBounds();
        if ( bounds.minimum.getX() < minimum.getX() ) {
            return false;
        }
        if ( bounds.minimum.getY() < minimum.getY() ) {
            return false;
        }
        if ( bounds.maximum.getX() > maximum.getX() ) {
            return false;
        }
        if ( bounds.maximum.getY() > maximum.getY() ) {
            return false;
        }
        return true;
    }

    @Override
    public Iterator<Coordinate> iterator () {
        return new Iterator<Coordinate>() {
            /** The current X coordinate of the Iterator. */
            private int x = minimum.getX();
            /** The current Y coordinate of the Iterator. */
            private int y = minimum.getY();

            @Override
            public boolean hasNext () {
                return y < maximum.getY();
            }

            @Override
            public Coordinate next () {
                if ( y >= maximum.getY() ) {
                    throw new NoSuchElementException();
                }
                // Create a Coordinate for the position.
                Coordinate pos = new Coordinate( x, y );
                // Update the X and Y values for the next position.
                x++;
                if ( x >= maximum.getX() ) {
                    y++;
                    x = minimum.getX();
                }
                // Return the position.
                return pos;
            }
        };
    }

    @Override
    public boolean equals ( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        Rectangle rect = (Rectangle) obj;
        // Check equality by comparing the minimum and maximum Coordinates.
        return maximum.equals( rect.maximum ) && minimum.equals( rect.minimum );
    }

    @Override
    public int hashCode () {
        return Objects.hash( maximum, minimum );
    }

    @Override
    public String toString () {
        return "Rect[" + minimum + " - " + maximum + "]";
    }
}
