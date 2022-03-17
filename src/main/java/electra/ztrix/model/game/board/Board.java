package electra.ztrix.model.game.board;

import java.util.LinkedList;
import java.util.Queue;

import electra.ztrix.model.game.action.Revertable;
import electra.ztrix.model.game.common.Coordinate;
import electra.ztrix.model.game.region.Rectangle;
import electra.ztrix.model.game.region.Region;

/**
 * A grid of Minos that can be checked and modified.
 *
 * @author Electra
 */
public class Board {
    /** The Rectangle bounding box of the Board. */
    private final Rectangle bounds;
    /** The matrix storing the grid of Minos. */
    private final Mino[][] matrix;

    /**
     * Creates a new Board with a given size.
     *
     * @param size
     *            The size as a Coordinate, non-null with X and Y positive.
     */
    public Board ( Coordinate size ) {
        this.bounds = new Rectangle( Coordinate.ORIGIN, size );
        this.matrix = new Mino[size.getY()][size.getX()];
    }

    /**
     * Gets the rectangle bounding box of the Board.
     *
     * @return the bounds.
     */
    public Rectangle getBounds () {
        return bounds;
    }

    /**
     * Gets the Mino at a particular position.
     *
     * @param position
     *            The position, non-null and within the Board's bounds.
     * @return The Mino at the position.
     */
    public Mino getMinoAt ( Coordinate position ) {
        if ( position == null ) {
            throw new NullPointerException( "getMinoAt(position) must be non-null." );
        }
        if ( !bounds.contains( position ) ) {
            throw new IndexOutOfBoundsException( "getMinoAt(position) must be within the Board's bounds." );
        }
        // Return the Mino at the position.
        return matrix[position.getY()][position.getX()];
    }

    /**
     * Gets whether a region is entirely empty and contained within the Board's
     * bounds. This can be used to check collision with the Board.
     *
     * @param region
     *            The Region to check, non-null.
     * @return True if the region is empty and within the Board's bounds.
     */
    public boolean isRegionEmpty ( Region region ) {
        if ( region == null ) {
            throw new NullPointerException( "isRegionEmpty(region) must be non-null." );
        }
        if ( !bounds.containsRegion( region ) ) {
            return false;
        }
        // Check for a Mino at each position.
        for ( Coordinate position : region ) {
            Mino mino = matrix[position.getY()][position.getX()];
            if ( mino != null ) {
                return false;
            }
        }
        // If none exist, return True.
        return true;
    }

    /**
     * Sets the Mino at a particular position, revertably.
     *
     * @param position
     *            The position, non-null and within the Board's bounds.
     * @param mino
     *            The Mino to set, non-null.
     * @return A Revertable to revert this method.
     */
    public Revertable setMinoAt ( Coordinate position, Mino mino ) {
        if ( mino == null ) {
            throw new NullPointerException( "setMinoAt(mino) must be non-null." );
        }
        // Save the previous Mino before writing.
        Mino prev = getMinoAt( position );
        matrix[position.getY()][position.getX()] = mino;
        // Create a Revertable using the previous Mino.
        return new Revertable() {
            @Override
            public void revert () {
                matrix[position.getY()][position.getX()] = prev;
            }
        };
    }

    /**
     * Sets an entire Region of the Board to a particular Mino, revertably.
     *
     * @param region
     *            The Region to check, non-null and within the Board's bounds.
     * @param mino
     *            The Mino to set, non-null.
     * @return A Revertable to revert this method.
     */
    public Revertable setRegion ( Region region, Mino mino ) {
        if ( region == null ) {
            throw new NullPointerException( "setRegion(region) must be non-null." );
        }
        if ( !bounds.containsRegion( region ) ) {
            throw new IndexOutOfBoundsException( "setRegion(region) must be within the Board's bounds." );
        }
        if ( mino == null ) {
            throw new NullPointerException( "setRegion(mino) must be non-null." );
        }
        // While writing, add the previous Mino at each position to a Queue.
        Queue<Mino> prevs = new LinkedList<Mino>();
        for ( Coordinate pos : region ) {
            Mino prev = matrix[pos.getY()][pos.getX()];
            prevs.add( prev );
            matrix[pos.getY()][pos.getX()] = mino;
        }
        // Create a Revertable that empties this Queue to restore the Minos.
        return new Revertable() {
            @Override
            public void revert () {
                for ( Coordinate pos : region ) {
                    Mino prev = prevs.remove();
                    matrix[pos.getY()][pos.getX()] = prev;
                }
            }
        };
    }
}
