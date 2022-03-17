package electra.ztrix.model.game.region;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import electra.ztrix.model.game.common.Coordinate;

/**
 * A generic Region containing an arbitrary Set of Coordinates.
 *
 * @author Electra
 */
public class SetRegion implements Region {
    /** The Set of contained Coordinates. */
    private final Set<Coordinate> set;
    /** The Rectangle bounding box of the Region. */
    private final Rectangle bounds;

    /**
     * Generates the Rectangle bounding box of the Region from its positions.
     *
     * @param positions
     *            The positions contained in the Region.
     * @return The Rectangle bounding box of the Region.
     */
    private Rectangle generateBounds ( Iterable<Coordinate> positions ) {
        // Keep track of the bounds seen so far.
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        // Update the bounds for each position in the Iterable.
        for ( Coordinate position : positions ) {
            if ( position.getX() < minX ) {
                minX = position.getX();
            }
            if ( position.getY() < minY ) {
                minY = position.getY();
            }
            if ( position.getX() > maxX ) {
                maxX = position.getX();
            }
            if ( position.getY() > maxY ) {
                maxY = position.getY();
            }
        }
        return new Rectangle( minX, minY, maxX + 1, maxY + 1 );
    }

    /**
     * Creates a new SetRegion from an Iterable of positions.
     *
     * @param positions
     *            The positions contained in the Region, non-null and non-empty.
     */
    public SetRegion ( Iterable<Coordinate> positions ) {
        if ( positions == null ) {
            throw new NullPointerException( "SetRegion(positions) must be non-null." );
        }
        // Add the positions to the Set.
        set = new HashSet<>();
        for ( Coordinate position : positions ) {
            set.add( position );
        }
        // Cannot have an empty Region.
        if ( set.isEmpty() ) {
            throw new IllegalArgumentException( "SetRegion(positions) must be non-empty." );
        }
        bounds = generateBounds( positions );
    }

    @Override
    public Iterator<Coordinate> iterator () {
        return set.iterator();
    }

    @Override
    public int count () {
        return set.size();
    }

    @Override
    public boolean contains ( Coordinate position ) {
        if ( position == null ) {
            throw new NullPointerException( "contains(position) must be non-null." );
        }
        return set.contains( position );
    }

    @Override
    public Rectangle getBounds () {
        return bounds;
    }
}
