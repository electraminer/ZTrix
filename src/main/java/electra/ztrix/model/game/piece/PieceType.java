package electra.ztrix.model.game.piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import electra.ztrix.model.game.board.Board;
import electra.ztrix.model.game.board.Mino;
import electra.ztrix.model.game.common.Coordinate;
import electra.ztrix.model.game.common.Rotation;
import electra.ztrix.model.game.region.Region;
import electra.ztrix.model.game.region.SetRegion;

/**
 * An immutable Object defining a piece's Mino, shape and rotation behavior,
 * from which ActivePieces can be created.
 *
 * @author Electra
 */
public class PieceType {
    /** The base offset table used by the pieces JLSTZ. */
    private static final Map<Rotation, Coordinate> BASE_OFFSET_TABLE_JLSTZ = Map.of(
            Rotation.R0, Coordinate.ORIGIN,
            Rotation.CW, Coordinate.ORIGIN,
            Rotation.R180, Coordinate.ORIGIN,
            Rotation.CCW, Coordinate.ORIGIN );

    /** The base offset table used by the pieces IO. */
    private static final Map<Rotation, Coordinate> BASE_OFFSET_TABLE_IO = Map.of(
            Rotation.R0, new Coordinate( 0, 0 ),
            Rotation.CW, new Coordinate( -1, 0 ),
            Rotation.R180, new Coordinate( -1, 1 ),
            Rotation.CCW, new Coordinate( 0, 1 ) );

    /** The kick offset table used by the pieces JLSTZ. */
    private static final Iterable<Map<Rotation, Coordinate>> KICK_OFFSET_TABLE_JLSTZ = List.of(
            Map.of( // Kick 1 to the side.
                    Rotation.R0, new Coordinate( 0, 0 ),
                    Rotation.CW, new Coordinate( 1, 0 ),
                    Rotation.R180, new Coordinate( 0, 0 ),
                    Rotation.CCW, new Coordinate( -1, 0 ) ),
            Map.of( // Kick 1 up/down and 1 to the side.
                    Rotation.R0, new Coordinate( 0, 0 ),
                    Rotation.CW, new Coordinate( 1, -1 ),
                    Rotation.R180, new Coordinate( 0, 0 ),
                    Rotation.CCW, new Coordinate( -1, -1 ) ),
            Map.of( // Kick 2 up/down.
                    Rotation.R0, new Coordinate( 0, 0 ),
                    Rotation.CW, new Coordinate( 0, 2 ),
                    Rotation.R180, new Coordinate( 0, 0 ),
                    Rotation.CCW, new Coordinate( 0, 2 ) ),
            Map.of( // Kick 2 up/down and 1 to the side.
                    Rotation.R0, new Coordinate( 0, 0 ),
                    Rotation.CW, new Coordinate( 1, 2 ),
                    Rotation.R180, new Coordinate( 0, 0 ),
                    Rotation.CCW, new Coordinate( -1, 2 ) ) );

    /** The kick offset table used by the pieces IO. */
    private static final Iterable<Map<Rotation, Coordinate>> KICK_OFFSET_TABLE_IO = List.of(
            Map.of( // Kick to the left.
                    Rotation.R0, new Coordinate( -1, 0 ),
                    Rotation.CW, new Coordinate( 1, 0 ),
                    Rotation.R180, new Coordinate( 2, 0 ),
                    Rotation.CCW, new Coordinate( 0, 0 ) ),
            Map.of( // Kick to the right.
                    Rotation.R0, new Coordinate( 2, 0 ),
                    Rotation.CW, new Coordinate( 1, 0 ),
                    Rotation.R180, new Coordinate( -1, 0 ),
                    Rotation.CCW, new Coordinate( 0, 0 ) ),
            Map.of( // Kick to the left and down.
                    Rotation.R0, new Coordinate( -1, 0 ),
                    Rotation.CW, new Coordinate( 1, 1 ),
                    Rotation.R180, new Coordinate( 2, -1 ),
                    Rotation.CCW, new Coordinate( 0, -2 ) ),
            Map.of( // Kick to the right and down.
                    Rotation.R0, new Coordinate( 2, 0 ),
                    Rotation.CW, new Coordinate( 1, -2 ),
                    Rotation.R180, new Coordinate( -1, 1 ),
                    Rotation.CCW, new Coordinate( 0, 1 ) ) );

    /** The J PieceType. */
    public static final PieceType J_PIECE = new PieceType(
            new Mino(),
            new SetRegion( List.of(
                    new Coordinate( 0, -1 ),
                    new Coordinate( 0, 0 ),
                    new Coordinate( 0, 1 ),
                    new Coordinate( 1, -1 ) ) ),
            BASE_OFFSET_TABLE_JLSTZ,
            KICK_OFFSET_TABLE_JLSTZ );

    /** The L PieceType. */
    public static final PieceType L_PIECE = new PieceType(
            new Mino(),
            new SetRegion( List.of(
                    new Coordinate( 0, -1 ),
                    new Coordinate( 0, 0 ),
                    new Coordinate( 0, 1 ),
                    new Coordinate( 1, 1 ) ) ),
            BASE_OFFSET_TABLE_JLSTZ,
            KICK_OFFSET_TABLE_JLSTZ );

    /** The S PieceType. */
    public static final PieceType S_PIECE = new PieceType(
            new Mino(),
            new SetRegion( List.of(
                    new Coordinate( -1, 0 ),
                    new Coordinate( 0, 0 ),
                    new Coordinate( 1, 0 ),
                    new Coordinate( 1, 1 ) ) ),
            BASE_OFFSET_TABLE_JLSTZ,
            KICK_OFFSET_TABLE_JLSTZ );

    /** The T PieceType. */
    public static final PieceType T_PIECE = new PieceType(
            new Mino(),
            new SetRegion( List.of(
                    new Coordinate( 0, -1 ),
                    new Coordinate( 0, 0 ),
                    new Coordinate( 0, 1 ),
                    new Coordinate( 1, 0 ) ) ),
            BASE_OFFSET_TABLE_JLSTZ,
            KICK_OFFSET_TABLE_JLSTZ );

    /** The Z PieceType. */
    public static final PieceType Z_PIECE = new PieceType(
            new Mino(),
            new SetRegion( List.of(
                    new Coordinate( 0, 0 ),
                    new Coordinate( 0, 1 ),
                    new Coordinate( 1, -1 ),
                    new Coordinate( 1, 0 ) ) ),
            BASE_OFFSET_TABLE_JLSTZ,
            KICK_OFFSET_TABLE_JLSTZ );

    /** The I PieceType. */
    public static final PieceType I_PIECE = new PieceType(
            new Mino(),
            new SetRegion( List.of(
                    new Coordinate( 0, -1 ),
                    new Coordinate( 0, 0 ),
                    new Coordinate( 0, 1 ),
                    new Coordinate( 0, 2 ) ) ),
            BASE_OFFSET_TABLE_IO,
            KICK_OFFSET_TABLE_IO );

    /** The O PieceType. */
    public static final PieceType O_PIECE = new PieceType(
            new Mino(),
            new SetRegion( List.of(
                    new Coordinate( -1, 0 ),
                    new Coordinate( -1, 1 ),
                    new Coordinate( 0, 0 ),
                    new Coordinate( 0, 1 ) ) ),
            BASE_OFFSET_TABLE_IO,
            KICK_OFFSET_TABLE_IO );

    /** The Mino the piece is made of. */
    private final Mino mino;
    /** The shapes of each Rotation state of the piece. */
    private final Map<Rotation, Region> shapes;
    /** The kicks for each pair of start and end Rotation states. */
    private final Map<Rotation, Map<Rotation, Iterable<Coordinate>>> kickTable;

    /**
     * Generates the Map of shapes from an initial shape and the base offsets.
     *
     * @param shape
     *            The shape of the R0 Rotation state.
     * @param baseOffsetTable
     *            A Map of the position of each Rotation state's center.
     * @return The Map of shapes.
     */
    private Map<Rotation, Region> generateShapes ( Region shape,
            Map<Rotation, Coordinate> baseOffsetTable ) {
        Map<Rotation, Region> shapes = new HashMap<>();
        for ( Rotation rotation : Rotation.values() ) {
            Region rotatedShape = shape.rotate( rotation, Coordinate.ORIGIN );
            // Calculate the translation that moves the center to the origin.
            Coordinate baseOffset = baseOffsetTable.get( rotation );
            if ( baseOffset == null ) {
                throw new IllegalArgumentException(
                        "PieceType(baseOffsetTable) must have an entry for each Rotation state." );
            }
            Coordinate negatedOffset = baseOffset.negate();
            Region offsetShape = rotatedShape.translate( negatedOffset );
            shapes.put( rotation, offsetShape );
        }
        return shapes;
    }

    /**
     * Generates the kick table from the kick offset table.
     *
     * @param kickOffsetTable
     *            For each kick, a Map of the position of each Rotation state's
     *            center, relative to the center defined by the base offsets.
     * @return The kick table.
     */
    private Map<Rotation, Map<Rotation, Iterable<Coordinate>>> generateKickTable (
            Iterable<Map<Rotation, Coordinate>> kickOffsetTable ) {
        Map<Rotation, Map<Rotation, Iterable<Coordinate>>> kickTable = new HashMap<>();
        for ( Rotation oldRotation : Rotation.values() ) {
            // Generate the kick table for a particular initial Rotation state.
            Map<Rotation, Iterable<Coordinate>> rotationKicks = new HashMap<>();
            for ( Rotation newRotation : Rotation.values() ) {
                // Generate a singular List of kicks.
                List<Coordinate> kicks = new ArrayList<>();
                for ( Map<Rotation, Coordinate> offsetTable : kickOffsetTable ) {
                    // Subtract the offsets of the Rotations to find the kick.
                    Coordinate startOffset = offsetTable.get( oldRotation );
                    Coordinate endOffset = offsetTable.get( newRotation );
                    if ( startOffset == null || endOffset == null ) {
                        throw new IllegalArgumentException(
                                "PieceType(kickOffsetTable) must have an entry for each Rotation state." );
                    }
                    Coordinate kick = endOffset.minus( startOffset );
                    kicks.add( kick );
                }
                rotationKicks.put( newRotation, kicks );
            }
            kickTable.put( oldRotation, rotationKicks );
        }
        return kickTable;
    }

    /**
     * Creates a new PieceType from it's Mino, shape, and offsets.
     *
     * @param mino
     *            The Mino the piece is made of, non-null.
     * @param shape
     *            The shape of the R0 Rotation state, non-null.
     * @param baseOffsetTable
     *            A Map of the position of each Rotation state's center,
     *            non-null with an entry for each Rotation state.
     * @param kickOffsetTable
     *            For each kick, a Map of the position of each Rotation state's
     *            center, relative to the center defined by the base offsets,
     *            non-null with an entry for each Rotation state.
     */
    public PieceType ( Mino mino, Region shape,
            Map<Rotation, Coordinate> baseOffsetTable,
            Iterable<Map<Rotation, Coordinate>> kickOffsetTable ) {
        if ( mino == null ) {
            throw new NullPointerException( "PieceType(mino) must be non-null." );
        }
        if ( shape == null ) {
            throw new NullPointerException( "PieceType(shape) must be non-null." );
        }
        if ( baseOffsetTable == null ) {
            throw new NullPointerException( "PieceType(baseOffsetTable) must be non-null." );
        }
        if ( kickOffsetTable == null ) {
            throw new NullPointerException( "PieceType(kickOffsetTable) must be non-null." );
        }
        this.mino = mino;
        shapes = generateShapes( shape, baseOffsetTable );
        kickTable = generateKickTable( kickOffsetTable );
    }

    /**
     * Gets the Mino the piece is made of.
     *
     * @return the Mino the piece is made of.
     */
    public Mino getMino () {
        return mino;
    }

    /**
     * Gets the shape of a particular Rotation state of the PieceType.
     *
     * @param rotation
     *            The Rotation state to check.
     * @return the shape of that Rotation state.
     */
    public Region getShape ( Rotation rotation ) {
        return shapes.get( rotation );
    }

    /**
     * Gets the kicks on rotating from Rotation state to another.
     *
     * @param oldRotation
     *            The Rotation state you are rotating from.
     * @param newRotation
     *            The Rotation state you are rotating to.
     * @return the kicks for that Rotation, not including the null kick.
     */
    public Iterable<Coordinate> getKicks ( Rotation oldRotation, Rotation newRotation ) {
        Map<Rotation, Iterable<Coordinate>> rotationKicks = kickTable.get( oldRotation );
        return rotationKicks.get( newRotation );
    }

    /**
     * Creates a new ActivePiece of this PieceType on a particular Board.
     *
     * @param board
     *            The Board the ActivePiece is on, non-null.
     * @param position
     *            The initial position of the ActivePiece, non-null.
     * @param rotation
     *            The initial Rotation of the ActivePiece, non-null.
     * @return The newly created ActivePiece.
     */
    public ActivePiece createActivePiece ( Board board, Coordinate position, Rotation rotation ) {
        return new ActivePiece( this, board, position, rotation );
    }
}
