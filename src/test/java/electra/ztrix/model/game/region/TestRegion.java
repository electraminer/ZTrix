package electra.ztrix.model.game.region;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.Test;

import electra.ztrix.model.game.common.Coordinate;
import electra.ztrix.model.game.common.Rotation;
import electra.ztrix.model.game.common.TestCoordinate;

/**
 * Tests the Region class.
 *
 * @author Electra
 */
public interface TestRegion {

    /**
     * Gets a series of Regions to test.
     *
     * @return The Regions to test.
     */
    public Iterable<Region> getRegions ();

    /**
     * Tests that contains() cannot be called with a null position.
     */
    @Test
    public default void testRegionContainsNull () {
        // Test containsRegion() for each Region.
        for ( Region region : getRegions() ) {
            // Test the NullPointerException.
            assertThrows( NullPointerException.class,
                    () -> region.contains( null ),
                    "contains(null) did not throw an Exception." );
        }
    }

    /**
     * Tests that iterator() returns an Iterator with the correct positions.
     */
    @Test
    public default void testRegionIterator () {
        // Test iterator() for each Region.
        for ( Region region : getRegions() ) {
            // Get the set of positions according to iterator().
            Set<Coordinate> set = new HashSet<>();
            for ( Coordinate position : region ) {
                assertNotNull( position,
                        region + ".iterator() containd null." );
                set.add( position );
            }
            // Test that this set is the same as that defined by contains().
            for ( Coordinate position : TestCoordinate.getCoordinates() ) {
                assertEquals( region.contains( position ), set.contains( position ),
                        region + " iterator() and contains() did not match for position " + position + "." );
            }
        }
    }

    /**
     * Tests that count() matches the length of iterator().
     */
    @Test
    public default void testRegionIteratorCount () {
        // Test iterator() and count() match for each Region.
        for ( Region region : getRegions() ) {
            // Create an Iterator and iterate through it.
            Iterator<Coordinate> iter = region.iterator();
            for ( int i = 0; i < region.count(); i++ ) {
                assertTrue( iter.hasNext(),
                        region + ".iterator().hasNext() had fewer elements than count()." );
                iter.next();
            }
            // Test that the Iterator is out of positions.
            assertFalse( iter.hasNext(),
                    region + ".iterator() had more elements than count()." );
            assertThrows( NoSuchElementException.class,
                    () -> iter.next(),
                    region + ".iterator().next() did not throw an Exception." );
        }
    }

    /**
     * Tests that getBounds() works properly.
     */
    @Test
    public default void testRegionGetBounds () {
        // Test getBounds() for each Region.
        for ( Region region : getRegions() ) {
            Rectangle bounds = region.getBounds();
            // Test that getBounds() contains the Region.
            for ( Coordinate position : region ) {
                assertTrue( bounds.contains( position ),
                        region + ".getBounds() did not contain position " + position + "." );
            }
            // Test that getBounds() does not contain the Region when moved.
            for ( Coordinate offset : Coordinate.DIRECTIONS ) {
                Rectangle newBounds = bounds.translate( offset );
                boolean containsAll = true;
                for ( Coordinate position : region ) {
                    if ( !newBounds.contains( position ) ) {
                        containsAll = false;
                        break;
                    }
                }
                assertFalse( containsAll,
                        region + ".getBounds() contained the Region when shifted by " + offset + "." );
            }
        }
    }

    /**
     * Tests that translate() works properly.
     */
    @Test
    public default void testRegionTranslate () {
        // Test translate() for each Region.
        for ( Region region : getRegions() ) {
            for ( Coordinate offset : TestCoordinate.getCoordinates() ) {
                Region newRegion = region.translate( offset );
                // Test that each position is translated in the new Region.
                assertEquals( region.count(), newRegion.count(),
                        region + ".translate(" + offset + ") ) has the wrong count()." );
                for ( Coordinate position : region ) {
                    Coordinate newPos = position.plus( offset );
                    assertTrue( newRegion.contains( newPos ),
                            region + ".translate(" + offset + ") is missing " + newPos + "." );
                }
            }
        }
    }

    /**
     * Tests that translate() cannot be called with a null offset.
     */
    @Test
    public default void testRegionTranslateNull () {
        // Test translate() for each Region.
        for ( Region region : getRegions() ) {
            // Check the NullPointerException.
            assertThrows( NullPointerException.class,
                    () -> region.translate( null ),
                    region + ".translate(null) did not throw an Exception." );
        }
    }

    /**
     * Tests that rotate() works properly.
     */
    @Test
    public default void testRegionRotate () {
        // Test rotate() for each Region.
        for ( Region region : getRegions() ) {
            for ( Rotation rotation : Rotation.values() ) {
                for ( Coordinate center : TestCoordinate.getCoordinates() ) {
                    Region newRegion = region.rotate( rotation, center );
                    // Test that each position is rotated in the new Region.
                    assertEquals( region.count(), newRegion.count(),
                            region + ".rotate(" + rotation + ", " + center + ") has the wrong count()." );
                    for ( Coordinate position : region ) {
                        Coordinate newPos = position.rotate( rotation, center );
                        assertTrue( newRegion.contains( newPos ),
                                region + ".rotate(" + rotation + ", " + center + ") is missing " + newPos + "." );
                    }
                }
            }
        }
    }

    /**
     * Tests that rotate() cannot be called with null parameters.
     */
    @Test
    public default void testRegionRotateNull () {
        // Test rotate() for each Region.
        for ( Region region : getRegions() ) {
            // Check NullPointerExceptions.
            assertThrows( NullPointerException.class,
                    () -> region.rotate( null, Coordinate.ORIGIN ),
                    region + ".translate(null, ORIGIN) did not throw an Exception." );
            assertThrows( NullPointerException.class,
                    () -> region.rotate( Rotation.R0, null ),
                    region + ".translate(R0, null) did not throw an Exception." );
        }
    }

}
