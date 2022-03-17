package electra.ztrix.model.game.piece;

import electra.ztrix.model.game.action.Revertable;
import electra.ztrix.model.game.board.Board;
import electra.ztrix.model.game.common.Coordinate;
import electra.ztrix.model.game.common.Rotation;
import electra.ztrix.model.game.region.Region;

/**
 * A mutable class that represents a moving ActivePiece on a Board, with a
 * particular position and Rotation. Can be moved directly, and also implements
 * movement behavior based on the properties of it's PieceType.
 *
 * @author Electra
 */
public class ActivePiece {
    /** The type of the ActivePiece. */
    private final PieceType type;
    /** The Board the ActivePiece is on. */
    private final Board board;
    /** The position of the ActivePiece. */
    private Coordinate position;
    /** The Rotation of the ActivePiece. */
    private Rotation rotation;

    /**
     * Creates a new ActivePiece of a given type on a particular Board.
     *
     * @param type
     *            The type of the ActivePiece, non-null.
     * @param board
     *            The Board the ActivePiece is on, non-null.
     * @param position
     *            The initial position of the ActivePiece, non-null.
     * @param rotation
     *            The initial Rotation of the ActivePiece, non-null.
     */
    public ActivePiece ( PieceType type, Board board,
            Coordinate position, Rotation rotation ) {
        if ( type == null ) {
            throw new NullPointerException( "ActivePiece(type) must be non-null." );
        }
        if ( board == null ) {
            throw new NullPointerException( "ActivePiece(board) must be non-null." );
        }
        if ( position == null ) {
            throw new NullPointerException( "ActivePiece(position) must be non-null." );
        }
        if ( rotation == null ) {
            throw new NullPointerException( "ActivePiece(rotation) must be non-null." );
        }
        this.type = type;
        this.board = board;
        this.position = position;
        this.rotation = rotation;
    }

    /**
     * Gets the type of the ActivePiece.
     *
     * @return the type.
     */
    public PieceType getPieceType () {
        return type;
    }

    /**
     * Gets the Board the ActivePiece is on.
     *
     * @return the Board.
     */
    public Board getBoard () {
        return board;
    }

    /**
     * Gets the position of the ActivePiece.
     *
     * @return the position.
     */
    public Coordinate getPosition () {
        return position;
    }

    /**
     * Gets the Rotation of the ActivePiece.
     *
     * @return the Rotation.
     */
    public Rotation getRotation () {
        return rotation;
    }

    /**
     * Gets the Region of the Board the ActivePiece currently takes up.
     *
     * @return the Region.
     */
    public Region getRegion () {
        Region shape = type.getShape( rotation );
        return shape.translate( position );
    }

    /**
     * Sets the position of the ActivePiece, with no collision checks.
     *
     * @param newPos
     *            The new Position, non-null.
     * @return A Revertable to revert this method.
     */
    public Revertable setPosition ( Coordinate newPos ) {
        if ( newPos == null ) {
            throw new NullPointerException( "setPosition(newPos) must be non-null." );
        }
        // Save the previous position before writing.
        Coordinate oldPos = position;
        position = newPos;
        // Create a Revertable using the previous position.
        return new Revertable() {
            @Override
            public void revert () {
                position = oldPos;
            }
        };
    }

    /**
     * Sets the Rotation of the ActivePiece, with no collision checks.
     *
     * @param newRot
     *            The new Rotation, non-null.
     * @return A Revertable to revert this method.
     */
    public Revertable setRotation ( Rotation newRot ) {
        if ( newRot == null ) {
            throw new NullPointerException( "setRotation(newRot) must be non-null." );
        }
        // Save the previous Rotation before writing.
        Rotation oldRot = rotation;
        rotation = newRot;
        // Create a Revertable using the previous Rotation.
        return new Revertable() {
            @Override
            public void revert () {
                rotation = oldRot;
            }
        };
    }

    /**
     * Checks the collision of the ActivePiece with the Board's Minos.
     *
     * @return True if the ActivePiece is not colliding with the Board.
     */
    public boolean isNotColliding () {
        Region region = getRegion();
        return board.isRegionEmpty( region );
    }

    /**
     * Moves the ActivePiece by an offset, checking collision.
     *
     * @param offset
     *            The offset to move by, non-null.
     * @return A Revertable to revert the move, or null if the move failed.
     */
    public Revertable move ( Coordinate offset ) {
        if ( offset == null ) {
            throw new NullPointerException( "move(offset) must be non-null." );
        }
        // Move the piece, saving a Revertable to revert the move.
        Coordinate newPos = position.plus( offset );
        Revertable revertable = setPosition( newPos );
        // Return a Revertable if the move succeeded.
        if ( isNotColliding() ) {
            return revertable;
        }
        // Revert the move if it failed.
        revertable.revert();
        return null;
    }

    /**
     * Rotates the ActivePiece, checking collision.
     *
     * @param direction
     *            The direction to rotate, non-null.
     * @return A Revertable to revert the rotate, or null if the rotate failed.
     */
    public Revertable rotate ( Rotation direction ) {
        if ( direction == null ) {
            throw new NullPointerException( "rotate(direction) must be non-null." );
        }
        // Create a Revertable using the previous position and Rotation.
        Coordinate prevPos = position;
        Rotation prevRot = rotation;
        Revertable revertable = new Revertable() {
            @Override
            public void revert () {
                position = prevPos;
                rotation = prevRot;
            }
        };
        // Try the default Rotaiton.
        rotation = prevRot.plus( direction );
        if ( isNotColliding() ) {
            return revertable;
        }
        // Try each kick for the Rotation.
        Iterable<Coordinate> kicks = type.getKicks( prevRot, rotation );
        for ( Coordinate kick : kicks ) {
            position = prevPos.plus( kick );
            if ( isNotColliding() ) {
                return revertable;
            }
        }
        // Revert the Rotation if it failed.
        revertable.revert();
        return null;
    }

    /**
     * Places the ActivePiece on the Board, moving it to the position it landed.
     *
     * @return A Revertable to revert the placement.
     */
    public Revertable place () {
        Coordinate prevPos = position;
        while ( move( Coordinate.MINUS_Y ) != null ) {
            // Keep moving down til you hit the floor.
        }
        // Place the piece, saving a Revertable to revert the placement.
        Region region = getRegion();
        Revertable revertable = board.setRegion( region, type.getMino() );
        // Create a new Revertable that reverts both the placement and movement.
        return new Revertable() {
            @Override
            public void revert () {
                revertable.revert();
                position = prevPos;
            }
        };
    }
}
