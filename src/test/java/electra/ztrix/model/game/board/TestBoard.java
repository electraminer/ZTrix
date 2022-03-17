package electra.ztrix.model.game.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import electra.ztrix.model.game.action.Revertable;
import electra.ztrix.model.game.common.Coordinate;
import electra.ztrix.model.game.region.Rectangle;
import electra.ztrix.model.game.region.Region;

/**
 * Tests the Board class.
 *
 * @author Electra
 */
class TestBoard {
    /** The Board used for testing. */
    private Board              board;

    /** The Mino used for testing. */
    public static Mino         MINO              = new Mino();

    /** An Array of invalid Board sizes. */
    public static Coordinate[] INVALID_SIZES     = {
            new Coordinate( 0, 0 ),
            new Coordinate( 0, 1 ),
            new Coordinate( 1, 0 ),
            new Coordinate( -1, 1 ),
            new Coordinate( 1, -1 ),
            new Coordinate( -10, 10 ),
            new Coordinate( 10, -10 ),
    };

    /** An Array of valid Board sizes. */
    public static Coordinate[] VALID_SIZES       = {
            new Coordinate( 1, 1 ),
            new Coordinate( 1, 10 ),
            new Coordinate( 10, 1 ),
            new Coordinate( 10, 10 ),
            new Coordinate( 10, 20 ),
            new Coordinate( 20, 10 ),
    };

    /** An Array of positions not inside the Board. */
    public static Coordinate[] INVALID_POSITIONS = {
            new Coordinate( -1, 0 ),
            new Coordinate( 0, -1 ),
            new Coordinate( -1, 19 ),
            new Coordinate( 0, 20 ),
            new Coordinate( 9, -1 ),
            new Coordinate( 10, 0 ),
            new Coordinate( 10, 19 ),
            new Coordinate( 9, 20 ),
            new Coordinate( -1, -1 ),
            new Coordinate( 10, 20 ),
            new Coordinate( 40, 50 ),
    };

    /** An Array of positions inside the Board. */
    public static Coordinate[] VALID_POSITIONS   = {
            new Coordinate( 0, 0 ),
            new Coordinate( 0, 19 ),
            new Coordinate( 9, 0 ),
            new Coordinate( 9, 19 ),
            new Coordinate( 4, 8 ),
            new Coordinate( 8, 16 ),
    };

    /** A position not overlapping any other tested position or Region. */
    public static Coordinate   OTHER_POSITION    = new Coordinate( 1, 1 );

    /** An Array of Regions not inside the Board. */
    public static Region[]     INVALID_REGIONS   = {
            new Rectangle( -1, -1, 0, 0 ),
            new Rectangle( -1, -1, 1, 1 ),
            new Rectangle( -1, 5, 1, 6 ),
            new Rectangle( 5, -1, 6, 1 ),
            new Rectangle( 5, 19, 6, 21 ),
            new Rectangle( 9, 5, 11, 6 ),
            new Rectangle( -1, -1, 11, 21 ),
    };

    /** An Array of Regions inside the Board. */
    public static Region[]     VALID_REGIONS     = {
            new Rectangle( 0, 0, 1, 5 ),
            new Rectangle( 0, 0, 5, 1 ),
            new Rectangle( 0, 15, 5, 20 ),
            new Rectangle( 5, 0, 10, 5 ),
            new Rectangle( 5, 15, 10, 20 ),
            new Rectangle( 0, 10, 10, 20 ),
            new Rectangle( 2, 2, 9, 9 ),
            new Rectangle( 7, 7, 10, 18 ),
    };

    /**
     * Initializes the Board before each test.
     */
    @BeforeEach
    public void initializeBoard () {
        Coordinate size = new Coordinate( 10, 20 );
        board = new Board( size );
    }

    /**
     * Tests that a Board cannot be created with an invalid size.
     */
    @Test
    public void testBoardConstructorInvalid () {
        // Check the NullPointerException.
        assertThrows( NullPointerException.class,
                () -> new Board( null ),
                "Board(null) did not throw an Exception." );
        // Check IllegalArgumentExceptions for various invalid sizes.
        for ( Coordinate size : INVALID_SIZES ) {
            assertThrows( IllegalArgumentException.class,
                    () -> new Board( size ),
                    "Board(" + size + ") did not throw an Exception." );
        }
    }

    /**
     * Tests that getBounds() works proplery.
     */
    @Test
    public void testBoardGetBounds () {
        // Check getBounds() for Boards with various sizes.
        for ( Coordinate size : VALID_SIZES ) {
            Board board = new Board( size );
            Rectangle bounds = new Rectangle( Coordinate.ORIGIN, size );
            assertEquals( bounds, board.getBounds(),
                    "getBounds() was wrong." );
        }
    }

    /**
     * Tests that the Board's Minos are initialized to null.
     */
    @Test
    public void testBoardInitialization () {
        // Check that various positions on the Board are initialized to null.
        for ( Coordinate position : VALID_POSITIONS ) {
            Mino mino = board.getMinoAt( position );
            assertNull( mino,
                    "getMinoAt(" + position + ") did not initialize to null." );
        }
    }

    /**
     * Tests that getMinoAt() cannot be called with an invalid position.
     */
    @Test
    public void testBoardGetMinoAtInvalid () {
        // Check the NullPointerException.
        assertThrows( NullPointerException.class,
                () -> board.getMinoAt( null ),
                "getMinoAt(null) did not throw an Exception." );
        // Check IndexOutOfBoundsExceptions for various invalid positions.
        for ( Coordinate position : INVALID_POSITIONS ) {
            assertThrows( IndexOutOfBoundsException.class,
                    () -> board.getMinoAt( position ),
                    "getMinoAt(" + position + ") did not throw an Exception." );
        }
    }

    /**
     * Tests that isRegionEmpty() works properly.
     */
    @Test
    public void testBoardIsRegionEmpty () {
        // Check isRegionEmpty() for various Regions outside the Board.
        for ( Region region : INVALID_REGIONS ) {
            assertFalse( board.isRegionEmpty( region ),
                    "isRegionEmpty(" + region + ") did not return False." );
        }
        // Set a Mino at a position not contained within the tested Regions.
        board.setMinoAt( OTHER_POSITION, MINO );
        // Check isRegionEmpty() for various Regions inside the Board.
        for ( Region region : VALID_REGIONS ) {
            assertTrue( board.isRegionEmpty( region ),
                    "isRegionEmpty(" + region + ") did not return True." );
        }
        // Set Minos at positions contained within the tested Regions.
        for ( Coordinate position : VALID_POSITIONS ) {
            board.setMinoAt( position, MINO );
        }
        // Check isRegionEmpty() for various Regions inside the Board.
        for ( Region region : VALID_REGIONS ) {
            assertFalse( board.isRegionEmpty( region ),
                    "isRegionEmpty(" + region + ") did not return False." );
        }
    }

    /**
     * Tests that isRegionEmpty() cannot be called with a null region.
     */
    @Test
    public void testIsRegionEmptyNull () {
        // Check the NullPointerException.
        assertThrows( NullPointerException.class,
                () -> board.isRegionEmpty( null ),
                "isRegionEmpty(null) did not throw an Exception." );
    }

    /**
     * Tests that setMinoAt() works properly.
     */
    @Test
    public void testBoardSetMinoAt () {
        // Check setMinoAt() for various positions inside the Board.
        for ( Coordinate position : VALID_POSITIONS ) {
            Revertable revertable = board.setMinoAt( position, MINO );
            Mino mino = board.getMinoAt( position );
            assertEquals( MINO, mino,
                    "setMinoAt(" + position + ", MINO) did not set the position." );
            Mino other = board.getMinoAt( OTHER_POSITION );
            assertNull( other,
                    "setMinoAt(" + position + ", MINO) affected another position." );
            // Check that the method is Revertable.
            revertable.revert();
            Mino reverted = board.getMinoAt( position );
            assertNull( reverted,
                    "setMinoAt().revert() did not revert the position." );
        }
    }

    /**
     * Tests that setMinoAt() cannot be called with invalid arguments.
     */
    @Test
    public void testBoardSetMinoAtInvalid () {
        // Check NullPointerExceptions.
        assertThrows( NullPointerException.class,
                () -> board.setMinoAt( null, MINO ),
                "setMinoAt(null, MINO) did not throw an Exception." );
        assertThrows( NullPointerException.class,
                () -> board.setMinoAt( OTHER_POSITION, null ),
                "setMinoAt(" + OTHER_POSITION + ", null) did not throw an Exception." );
        // Check IndexOutOfBoundsExceptions for various invalid Positions.
        for ( Coordinate position : INVALID_POSITIONS ) {
            assertThrows( IndexOutOfBoundsException.class,
                    () -> board.setMinoAt( position, MINO ),
                    "setMinoAt(" + position + ", MINO) did not throw an Exception." );
        }
    }

    /**
     * Tests that setRegion() works properly.
     */
    @Test
    public void testBoardSetRegion () {
        // Check setRegion() for various Regions inside the Board.
        for ( Region region : VALID_REGIONS ) {
            Revertable revertable = board.setRegion( region, MINO );
            for ( Coordinate position : region ) {
                Mino mino = board.getMinoAt( position );
                assertEquals( MINO, mino,
                        "setRegion(" + region + ", MINO) did not set the position " + position + "." );
            }
            Mino other = board.getMinoAt( OTHER_POSITION );
            assertNull( other,
                    "setRegion(" + region + ", MINO) affected another position." );
            // Check that the method is Revertable.
            revertable.revert();
            for ( Coordinate position : region ) {
                Mino reverted = board.getMinoAt( position );
                assertNull( reverted,
                        "setMinoAt().revert() did not revert the position " + position + "." );
            }
        }
    }

    /**
     * Tests that setRegion() cannot be called with invalid arguments.
     */
    @Test
    public void testBoardSetRegionInvalid () {
        // Check NullPointerExceptions.
        assertThrows( NullPointerException.class,
                () -> board.setRegion( null, MINO ),
                "setMinoAt(null, MINO) did not throw an Exception." );
        assertThrows( NullPointerException.class,
                () -> board.setRegion( board.getBounds(), null ),
                "setMinoAt(" + board.getBounds() + ", null) did not throw an Exception." );
        // Check IndexOutOfBoundsExceptions for various invalid Regions.
        for ( Region region : INVALID_REGIONS ) {
            assertThrows( IndexOutOfBoundsException.class,
                    () -> board.setRegion( region, MINO ),
                    "setMinoAt(" + region + ", MINO) did not throw an Exception." );
        }
    }

}
