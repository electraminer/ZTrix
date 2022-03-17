package electra.ztrix.model.game.region;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import electra.ztrix.model.game.common.Coordinate;

/**
 * Tests the SetRegion class.
 *
 * @author Electra
 */
public class TestSetRegion implements TestRegion {
    /**
     * Gets a series of SetRegions to test.
     *
     * @return The SetRegions to test.
     */
    public static Iterable<SetRegion> getSetRegions () {
        List<SetRegion> list = new ArrayList<>();
        Rectangle rect = new Rectangle( -2, -2, 3, 3 );
        Rectangle rect1 = new Rectangle( -2, -2, 0, 0 );
        Rectangle rect2 = new Rectangle( -2, 0, 0, 3 );
        Rectangle rect3 = new Rectangle( 0, -2, 3, 3 );
        for ( Coordinate pos1 : rect1 ) {
            for ( Coordinate pos2 : rect2 ) {
                for ( Coordinate pos3 : rect3 ) {
                    List<Coordinate> positions = List.of( pos1, pos2, pos3 );
                    SetRegion setRegion = new SetRegion( positions );
                    for ( Coordinate position : rect ) {
                        boolean listContains = positions.contains( position );
                        boolean setContains = setRegion.contains( position );
                        assertEquals( listContains, setContains,
                                setRegion + " iterator() and contains() did not match for position " + position + "." );
                    }
                    list.add( setRegion );
                }
            }
        }
        return list;
    }

    @Override
    public Iterable<Region> getRegions () {
        List<Region> list = new ArrayList<>();
        for ( SetRegion region : getSetRegions() ) {
            list.add( region );
        }
        return list;
    }

    /**
     * Tests that a SetRegion cannot be created with invalid parameters.
     */
    @Test
    public void testSetRegionConstructorInvalid () {
        // Check the NullPointerException.
        assertThrows( NullPointerException.class,
                () -> new SetRegion( null ),
                "SetRegion(null) did not throw an Exception." );
        // Check the IllegalArgumentExceptions for an empty SetRegion.
        List<Coordinate> empty = List.of();
        assertThrows( IllegalArgumentException.class,
                () -> new SetRegion( empty ),
                "SetRegion(empty) did not throw an Exception." );
    }
}
