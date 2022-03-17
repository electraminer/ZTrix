package electra.ztrix.model.game.region;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import electra.ztrix.model.game.common.Coordinate;
import electra.ztrix.model.game.common.TestCoordinate;

/**
 * Tests the Rectangle class.
 *
 * @author Electra
 */
public class TestRectangle implements TestRegion {
    /** The Rectangle used for testing. */
    public static Rectangle RECTANGLE = new Rectangle( -1, -1, 2, 2 );

    /** An Array of positions not inside the Rectangle. */
    public static Coordinate[] INVALID_POSITIONS = {
            new Coordinate( 2, -1 ),
            new Coordinate( 2, 0 ),
            new Coordinate( 2, 1 ),
            new Coordinate( -1, 2 ),
            new Coordinate( 0, 2 ),
            new Coordinate( 1, 2 ),
            new Coordinate( 2, 2 ),
            new Coordinate( -2, -2 ),
            new Coordinate( 4, 5 ),
    };

    /** An Array of positions inside the Rectangle. */
    public static Coordinate[] VALID_POSITIONS = {
            new Coordinate( -1, -1 ),
            new Coordinate( 0, -1 ),
            new Coordinate( 1, -1 ),
            new Coordinate( -1, 0 ),
            new Coordinate( 0, 0 ),
            new Coordinate( 1, 0 ),
            new Coordinate( -1, 1 ),
            new Coordinate( 0, 1 ),
            new Coordinate( 1, 1 ),
    };

    /** An Array of Regions not inside the Rectangle. */
    public static Region[] INVALID_REGIONS = {
            new Rectangle( -2, -2, 1, 1 ),
            new Rectangle( -2, 0, 0, 1 ),
            new Rectangle( 0, -2, 1, 0 ),
            new Rectangle( 0, 1, 1, 3 ),
            new Rectangle( 1, 0, 3, 1 ),
            new Rectangle( -4, -4, 5, 5 ),
    };

    /**
     * Gets a series of Rectangles to test.
     *
     * @return The Rectangles to test.
     */
    public static Iterable<Rectangle> getRectangles () {
        List<Rectangle> list = new ArrayList<>();
        // Create Region with various X and Y components.
        for ( int minX = -2; minX < 2; minX++ ) {
            for ( int minY = -2; minY < 2; minY++ ) {
                Coordinate minimum = new Coordinate( minX, minY );
                for ( int maxX = minX + 1; maxX < 3; maxX++ ) {
                    for ( int maxY = minY + 1; maxY < 3; maxY++ ) {
                        Coordinate maximum = new Coordinate( maxX, maxY );
                        Rectangle rect = new Rectangle( minimum, maximum );
                        assertEquals( minimum, rect.getMinimum(),
                                rect + ".getMinimum() is wrong." );
                        assertEquals( maximum, rect.getMaximum(),
                                rect + ".getMaximum() is wrong." );
                        Set<Coordinate> set = new HashSet<>();
                        for ( int x = minX; x < maxX; x++ ) {
                            for ( int y = minY; y < maxY; y++ ) {
                                Coordinate position = new Coordinate( x, y );
                                set.add( position );
                            }
                        }
                        for ( Coordinate position : TestCoordinate.getCoordinates() ) {
                            assertEquals( set.contains( position ), rect.contains( position ),
                                    rect + ".contains(" + position + ") is wrong." );
                        }
                        list.add( rect );
                    }
                }
            }
        }
        return list;
    }

    @Override
    public Iterable<Region> getRegions () {
        List<Region> list = new ArrayList<>();
        for ( Rectangle region : getRectangles() ) {
            list.add( region );
        }
        return list;
    }

    /**
     * Tests that a Rectangle cannot be created with an invalid minimum or
     * maximum.
     */
    @Test
    public void testRectangleConstructorInvalid () {
        // Test NullPointerExceptions.
        assertThrows( NullPointerException.class,
                () -> new Rectangle( null, Coordinate.ORIGIN ),
                "Rectangle(null, ORIGIN) did not throw an Exception." );
        assertThrows( NullPointerException.class,
                () -> new Rectangle( Coordinate.ORIGIN, null ),
                "Rectangle(ORIGIN, null) did not throw an Exception." );
        // Test IllegalArgumentExceptions for various invalid sizes.
        for ( int i = -10; i <= 0; i++ ) {
            Coordinate coordX = new Coordinate( i, 1 );
            assertThrows( IllegalArgumentException.class,
                    () -> new Rectangle( Coordinate.ORIGIN, coordX ),
                    "Rectangle(ORIGIN, " + coordX + ") did not throw an Exception." );
            Coordinate coordY = new Coordinate( 1, i );
            assertThrows( IllegalArgumentException.class,
                    () -> new Rectangle( Coordinate.ORIGIN, coordY ),
                    "Rectangle(ORIGIN, " + coordY + ") did not throw an Exception." );

        }
    }

    /**
     * Tests that containsRegion() works properly.
     */
    @Test
    public void testRectangleContainsRegion () {
        // Test that the Rectangle contains itself.
        assertTrue( RECTANGLE.containsRegion( RECTANGLE ),
                "contains(" + RECTANGLE + ") did not return True." );
        // Test various 1x1 Rectangles at positions inside the Rectangle.
        for ( Coordinate minimum : VALID_POSITIONS ) {
            Coordinate size = new Coordinate( 1, 1 );
            Coordinate maximum = minimum.plus( size );
            Region region = new Rectangle( minimum, maximum );
            assertTrue( RECTANGLE.containsRegion( region ),
                    "containsRegion(" + region + ") did not return True." );
        }
        // Test various Regions not inside the Rectangle.
        for ( Region region : INVALID_REGIONS ) {
            assertFalse( RECTANGLE.containsRegion( region ),
                    "containsRegion(" + region + ") did not return False." );
        }
    }

    /**
     * Tests that containsRegion() cannot be called with a null Region.
     */
    @Test
    public void testRectangleContainsRegionNull () {
        // Test containsRegion() for each Rectangle.
        for ( Rectangle rect : getRectangles() ) {
            // Test the NullPointerException.
            assertThrows( NullPointerException.class,
                    () -> rect.containsRegion( null ),
                    "containsRegion(null) did not throw an Exception." );
        }
    }

    /**
     * Tests that equals() works properly.
     */
    @Test
    public void testRectangleEquals () {
        // Test equals() for each Rectangle.
        for ( Rectangle rect : getRectangles() ) {
            int minX = rect.getMinimum().getX();
            int minY = rect.getMinimum().getY();
            int maxX = rect.getMaximum().getX();
            int maxY = rect.getMaximum().getY();
            // Test the equality of the Rectangle against various objects.
            assertTrue( rect.equals( rect ),
                    rect + ".equals(" + rect + ") did not return True." );
            assertFalse( rect.equals( null ),
                    rect + ".equals(null) did not return False." );
            assertFalse( rect.equals( new Object() ),
                    rect + ".equals(Object) did not return False." );
            // Test the equality of the Rectangle against other Rectangles.
            Rectangle rectEq = new Rectangle( minX, minY, maxX, maxY );
            assertTrue( rect.equals( rectEq ),
                    rect + ".equals(" + rectEq + ") did not return True." );
            Rectangle rectMin = new Rectangle( minX - 1, minY - 1, maxX, maxY );
            assertFalse( rect.equals( rectMin ),
                    rect + ".equals(" + rectMin + ") did not return False." );
            Rectangle rectMax = new Rectangle( minX, minY, maxX + 1, maxY + 1 );
            assertFalse( rect.equals( rectMax ),
                    rect + ".equals(" + rectMax + ") did not return False." );
        }

    }

    /**
     * Tests that hashCode() works properly.
     */
    @Test
    public void testCoordinateHashCode () {
        // Test hashCode() for each Rectangle.
        for ( Rectangle rect : getRectangles() ) {
            int minX = rect.getMinimum().getX();
            int minY = rect.getMinimum().getY();
            int maxX = rect.getMaximum().getX();
            int maxY = rect.getMaximum().getY();
            assertEquals( rect.hashCode(), rect.hashCode(),
                    rect + ".hashCode() did not match itself." );
            Rectangle rectEq = new Rectangle( minX, minY, maxX, maxY );
            assertEquals( rect.hashCode(), rectEq.hashCode(),
                    rectEq + ".hashCode() did not match " + rect + ".hashCode()." );
        }
    }

    /**
     * Tests that toString() works properly.
     */
    @Test
    public void testRectangleToString () {
        // Test the toString() of various Rectangles.
        Rectangle rect1 = new Rectangle( 0, 0, 1, 1 );
        assertEquals( "Rect[(0, 0) - (1, 1)]", rect1.toString(),
                rect1 + ".toString() was wrong." );
        Rectangle rect2 = new Rectangle( -2, -3, 4, 5 );
        assertEquals( "Rect[(-2, -3) - (4, 5)]", rect2.toString(),
                rect2 + ".toString() was wrong." );
        Rectangle rect3 = new Rectangle( 5, -14, 7, 8 );
        assertEquals( "Rect[(5, -14) - (7, 8)]", rect3.toString(),
                rect3 + ".toString() was wrong." );
        Rectangle rect4 = new Rectangle( -69, -4, 20, -3 );
        assertEquals( "Rect[(-69, -4) - (20, -3)]", rect4.toString(),
                rect4 + ".toString() was wrong." );
    }
}
