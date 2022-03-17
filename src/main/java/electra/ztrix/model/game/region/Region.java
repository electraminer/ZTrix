package electra.ztrix.model.game.region;

import java.util.ArrayList;
import java.util.List;

import electra.ztrix.model.game.common.Coordinate;
import electra.ztrix.model.game.common.Rotation;

/**
 * An immutable interface containing a set of Coordinate positions.
 *
 * @author Electra
 */
public interface Region extends Iterable<Coordinate> {

    /**
     * Gets the number of positions in the Region.
     *
     * @return The number of positions in the Region.
     */
    public int count ();

    /**
     * Checks whether the Region contains a position.
     *
     * @param position
     *            The position to check, non-null.
     * @return True if the Region contains the position.
     */
    public boolean contains ( Coordinate position );

    /**
     * Gets the Rectangle bounding box of the Region.
     *
     * @return the bounds.
     */
    public Rectangle getBounds ();

    /**
     * Creates a new Region by translating this one.
     *
     * @param offset
     *            The offset to translate by, non-null.
     * @return The new, translated Region.
     */
    public default Region translate ( Coordinate offset ) {
        if ( offset == null ) {
            throw new NullPointerException( "translate(offset) must be non-null." );
        }
        // Translate each position individually, adding to a SetRegion.
        List<Coordinate> positions = new ArrayList<>();
        for ( Coordinate position : this ) {
            Coordinate newPos = position.plus( offset );
            positions.add( newPos );
        }
        return new SetRegion( positions );
    }

    /**
     * Creates a new Region by rotating this one.
     *
     * @param direction
     *            The direction to rotate, non-null,
     * @param center
     *            The position to rotate around, non-null.
     *
     * @return The new, rotated Region.
     */
    public default Region rotate ( Rotation direction, Coordinate center ) {
        if ( direction == null ) {
            throw new NullPointerException( "rotate(direction) must be non-null." );
        }
        if ( center == null ) {
            throw new NullPointerException( "rotate(center) must be non-null." );
        }
        // Rotate each position individually, adding to a SetRegion.
        List<Coordinate> positions = new ArrayList<>();
        for ( Coordinate position : this ) {
            Coordinate newPos = position.rotate( direction, center );
            positions.add( newPos );
        }
        return new SetRegion( positions );
    }
}
