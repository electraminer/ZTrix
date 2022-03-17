package electra.ztrix.model.game.common;

import java.util.Objects;

/**
 * An immutable object that can represent 2D positions, vectors, or sizes.
 *
 * @author Electra
 */
public class Coordinate {
    /** The 0, 0 Coordinate. */
    public static Coordinate ORIGIN = new Coordinate( 0, 0 );
    /** The Coordinate representing an increase to X. */
    public static Coordinate PLUS_X = new Coordinate( 1, 0 );
    /** The Coordinate representing an increase to Y. */
    public static Coordinate PLUS_Y = new Coordinate( 0, 1 );
    /** The Coordinate representing a decrease to X. */
    public static Coordinate MINUS_X = new Coordinate( -1, 0 );
    /** The Coordinate representing a decrease to Y. */
    public static Coordinate MINUS_Y = new Coordinate( 0, -1 );
    /** A list of Coordinates representing the four cardinal directions. */
    public static Coordinate[] DIRECTIONS = { PLUS_X, PLUS_Y, MINUS_X, MINUS_Y };

    /** The X component. */
    private final int x;
    /** The Y component. */
    private final int y;

    /**
     * Creates a new Coordinate from its X and Y components.
     *
     * @param x
     *            The X component.
     * @param y
     *            The Y component.
     */
    public Coordinate ( int x, int y ) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the X component.
     *
     * @return the X component.
     */
    public int getX () {
        return x;
    }

    /**
     * Gets the Y component.
     *
     * @return the Y component.
     */
    public int getY () {
        return y;
    }

    /**
     * Creates a new Coordinate by adding an offset to this one.
     *
     * @param offset
     *            The Coordinate to add, non-null.
     * @return The new Coordinate.
     */
    public Coordinate plus ( Coordinate offset ) {
        if ( offset == null ) {
            throw new NullPointerException( "plus(offset) must be non-null." );
        }
        // Calculate the new Coordinate.
        int newX = x + offset.x;
        int newY = y + offset.y;
        return new Coordinate( newX, newY );
    }

    /**
     * Creates a new Coordinate by subtracting an offset from this one.
     *
     * @param offset
     *            The Coordinate to subtract, non-null.
     * @return The new Coordinate.
     */
    public Coordinate minus ( Coordinate offset ) {
        if ( offset == null ) {
            throw new NullPointerException( "minus(offset) must be non-null." );
        }
        // Calculate the new Coordinate.
        int newX = x - offset.x;
        int newY = y - offset.y;
        return new Coordinate( newX, newY );
    }

    /**
     * Creates a new Coordinate by negating this one.
     *
     * @return The new, negated Coordinate.
     */
    public Coordinate negate () {
        return ORIGIN.minus( this );
    }

    /**
     * Creates a new Coordinate by rotating this one.
     *
     * @param direction
     *            The direction to rotate, non-null.
     * @param center
     *            The position to rotate around, non-null.
     *
     * @return The new, rotated Coordinate.
     */
    public Coordinate rotate ( Rotation direction, Coordinate center ) {
        if ( direction == null ) {
            throw new NullPointerException( "rotate(direction) must be non-null." );
        }
        if ( center == null ) {
            throw new NullPointerException( "rotate(center) must be non-null." );
        }
        // Calculate the relative offset from the center.
        int relX = x - center.x;
        int relY = y - center.y;
        // Rotate the relative offset for each 90 degree rotation.
        for ( int i = 0; i < direction.ordinal(); i++ ) {
            int temp = relX;
            relX = relY;
            relY = -temp;
        }
        // Calculate the new absolute Coordinate from the relative offset.
        int newX = relX + center.x;
        int newY = relY + center.y;
        return new Coordinate( newX, newY );
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
        Coordinate coord = (Coordinate) obj;
        // Check equality by comparing the X and Y components.
        return ( x == coord.x ) && ( y == coord.y );
    }

    @Override
    public int hashCode () {
        return Objects.hash( x, y );
    }

    @Override
    public String toString () {
        return "(" + x + ", " + y + ")";
    }
}
