package electra.ztrix.model.game.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests the Coordinate class.
 *
 * @author Electra
 */
public class TestCoordinate {

    /**
     * Gets a series of Coordinates to test.
     *
     * @return The Coordinates to test.
     */
    public static Iterable<Coordinate> getCoordinates () {
        List<Coordinate> list = new ArrayList<>();
        // Create Coordinates with various X and Y components.
        for ( int x = -5; x <= 5; x++ ) {
            for ( int y = -5; y <= 5; y++ ) {
                // Test that the Coordinate's defining getters work properly.
                Coordinate coord = new Coordinate( x, y );
                assertEquals( x, coord.getX(),
                        "getX() was wrong." );
                assertEquals( y, coord.getY(),
                        "getY() was wrong." );
                list.add( coord );
            }
        }
        return list;
    }

    /**
     * Tests that plus() works properly.
     */
    @Test
    public void testCoordinatePlus () {
        // Test each Coordinate.
        for ( Coordinate coord : getCoordinates() ) {
            int x = coord.getX();
            int y = coord.getY();
            // Test plus() with various other Coordinates.
            Coordinate offset = new Coordinate( 1, 1 );
            Coordinate sum = new Coordinate( x + 1, y + 1 );
            Coordinate negated = new Coordinate( -x, -y );
            assertEquals( coord, coord.plus( Coordinate.ORIGIN ),
                    coord + ".plus(ORIGIN) was wrong." );
            assertEquals( coord, Coordinate.ORIGIN.plus( coord ),
                    "ORIGIN.plus(" + coord + ") was wrong." );
            assertEquals( sum, coord.plus( offset ),
                    coord + ".plus(" + offset + ") was wrong." );
            assertEquals( Coordinate.ORIGIN, coord.plus( negated ),
                    coord + ".plus(" + negated + ") was wrong." );
        }
    }

    /**
     * Tests that plus() cannot be called with a null offset.
     */
    @Test
    public void testCoordinatePlusNull () {
        // Test the NullPointerException.
        assertThrows( NullPointerException.class,
                () -> Coordinate.ORIGIN.plus( null ),
                "plus(null) did not throw an Exception." );
    }

    /**
     * Tests that minus() works properly.
     */
    @Test
    public void testCoordinateMinus () {
        // Test each Coordinate.
        for ( Coordinate coord : getCoordinates() ) {
            int x = coord.getX();
            int y = coord.getY();
            // Test minus() with various other Coordinates.
            Coordinate offset = new Coordinate( 1, 1 );
            Coordinate diff = new Coordinate( x - 1, y - 1 );
            Coordinate negated = new Coordinate( -x, -y );
            assertEquals( coord, coord.minus( Coordinate.ORIGIN ),
                    coord + ".minus(ORIGIN) was wrong." );
            assertEquals( negated, Coordinate.ORIGIN.minus( coord ),
                    "ORIGIN.minus(" + coord + ") was wrong." );
            assertEquals( diff, coord.minus( offset ),
                    coord + ".minus(" + offset + ") was wrong." );
            assertEquals( Coordinate.ORIGIN, coord.minus( coord ),
                    coord + ".minus(" + coord + ") was wrong." );
        }
    }

    /**
     * Tests that minus() cannot be called with a null offset.
     */
    @Test
    public void testCoordinateMinusNull () {
        // Test the NullPointerException.
        assertThrows( NullPointerException.class,
                () -> Coordinate.ORIGIN.minus( null ),
                "minus(null) did not throw an Exception." );
    }

    /**
     * Tests that negate() works properly.
     */
    @Test
    public void testCoordinateNegate () {
        // Test each Coordinate.
        for ( Coordinate coord : getCoordinates() ) {
            int x = coord.getX();
            int y = coord.getY();
            Coordinate negated = new Coordinate( -x, -y );
            assertEquals( negated, coord.negate(),
                    coord + ".negated() was wrong." );
        }
    }

    /**
     * Tests that rotate() works properly.
     */
    @Test
    public void testCoordinateRotate () {
        // Test each Coordinate.
        for ( Coordinate coord : getCoordinates() ) {
            int x = coord.getX();
            int y = coord.getY();
            // Test rotations around the origin.
            Coordinate flipped = coord.negate();
            Coordinate clockwise = new Coordinate( y, -x );
            Coordinate counter = clockwise.negate();
            assertEquals( coord, coord.rotate( Rotation.R0, Coordinate.ORIGIN ),
                    coord + ".rotate(R0, ORIGIN) was wrong." );
            assertEquals( flipped, coord.rotate( Rotation.R180, Coordinate.ORIGIN ),
                    coord + ".rotate(R180, ORIGIN) was wrong." );
            assertEquals( clockwise, coord.rotate( Rotation.CW, Coordinate.ORIGIN ),
                    coord + ".rotate(CW, ORIGIN) was wrong." );
            assertEquals( counter, coord.rotate( Rotation.CCW, Coordinate.ORIGIN ),
                    coord + ".rotate(CCW, ORIGIN) was wrong." );
            // Test rotations around a center.
            Coordinate center = new Coordinate( 5, 5 );
            Coordinate rotated = center.plus( center ).minus( coord );
            assertEquals( rotated, coord.rotate( Rotation.R180, center ),
                    coord + ".rotate(R180, " + center + ",) was wrong." );
        }
    }

    /**
     * Tests that rotate() cannot be called with null parameters
     */
    @Test
    public void testCoordinateRotateNull () {
        // Test NullPointerExceptions.
        assertThrows( NullPointerException.class,
                () -> Coordinate.ORIGIN.rotate( null, Coordinate.ORIGIN ),
                "rotate(null, ORIGIN) did not throw an Exception." );
        assertThrows( NullPointerException.class,
                () -> Coordinate.ORIGIN.rotate( Rotation.R0, null ),
                "rotate(R0, null) did not throw an Exception." );
    }

    /**
     * Tests that equals() works properly.
     */
    @Test
    public void testCoordinateEquals () {
        // Test equality of the origin against various objects.
        Coordinate origin = new Coordinate( 0, 0 );
        assertTrue( origin.equals( origin ),
                origin + ".equals(" + origin + ") did not return True." );
        assertFalse( origin.equals( null ),
                origin + ".equals(null) did not return False." );
        assertFalse( origin.equals( new Object() ),
                origin + ".equals(Object) did not return False." );
        assertTrue( origin.equals( Coordinate.ORIGIN ),
                origin + ".equals(ORIGIN) did not return True." );
        // Test each Coordinate.
        for ( Coordinate coord : getCoordinates() ) {
            int x = coord.getX();
            int y = coord.getY();
            // Test equality of the Coordinate against other Coordinates.
            Coordinate coordEq = new Coordinate( x, y );
            assertTrue( coord.equals( coordEq ),
                    coord + ".equals(" + coordEq + ") did not return True." );
            Coordinate coordX = new Coordinate( x + 1, y );
            assertFalse( coord.equals( coordX ),
                    coord + ".equals(" + coordX + ") did not return False." );
            Coordinate coordY = new Coordinate( x, y + 1 );
            assertFalse( coord.equals( coordY ),
                    coord + ".equals(" + coordY + ") did not return False." );

        }
    }

    /**
     * Tests that hashCode() works properly.
     */
    @Test
    public void testCoordinateHashCode () {
        // Test the hashCode() of the origin.
        Coordinate origin = new Coordinate( 0, 0 );
        assertEquals( origin.hashCode(), origin.hashCode(),
                origin + ".hashCode() did not match itself" );
        assertEquals( origin.hashCode(), Coordinate.ORIGIN.hashCode(),
                Coordinate.ORIGIN + ".hashCode() did not match " + origin + ".hashCode()." );

        // Test each Coordinate.
        for ( Coordinate coord : getCoordinates() ) {
            int x = coord.getX();
            int y = coord.getY();
            Coordinate coordEq = new Coordinate( x, y );
            assertEquals( coord.hashCode(), coordEq.hashCode(),
                    coordEq + ".hashCode() did not match " + coord + ".hashCode()." );
        }

    }

    /**
     * Tests that toString() works properly.
     */
    @Test
    public void testCoordinateToString () {
        // Test the toString() of various Coordinates.
        assertEquals( "(0, 0)", Coordinate.ORIGIN.toString(),
                Coordinate.ORIGIN + ".toString() was not correct." );
        Coordinate coord1 = new Coordinate( 1, 4 );
        assertEquals( "(1, 4)", coord1.toString(),
                coord1 + ".toString() was wrong." );
        Coordinate coord2 = new Coordinate( -3, 5 );
        assertEquals( "(-3, 5)", coord2.toString(),
                coord2 + ".toString() was wrong." );
        Coordinate coord3 = new Coordinate( 4, -20 );
        assertEquals( "(4, -20)", coord3.toString(),
                coord3 + ".toString() was wrong." );
        Coordinate coord4 = new Coordinate( -70, -3 );
        assertEquals( "(-70, -3)", coord4.toString(),
                coord4 + ".toString() was wrong." );
    }
}
