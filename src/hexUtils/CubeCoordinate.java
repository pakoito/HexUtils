package hexUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import hexUtils.math.IntVector;

public class CubeCoordinate {

	/*
	 * The six possible directions in CubeCoordinates. Add one of those to a
	 * CubeCoordinate and you get one of its neighbors.
	 */
	public static final CubeCoordinate[] DIRECTIONS = new CubeCoordinate[] {
			new CubeCoordinate(1, -1, 0), new CubeCoordinate(1, 0, -1),
			new CubeCoordinate(0, 1, -1), new CubeCoordinate(-1, 1, 0),
			new CubeCoordinate(-1, 0, 1), new CubeCoordinate(0, -1, 1) };

	/*
	 * The six possible diagonals in CubeCoordinates.
	 */
	public static final CubeCoordinate[] DIAGONALS = new CubeCoordinate[] {
			new CubeCoordinate(2, -1, -1), new CubeCoordinate(1, 1, -2),
			new CubeCoordinate(-1, 2, -1), new CubeCoordinate(-2, 1, 1),
			new CubeCoordinate(-1, -1, 2), new CubeCoordinate(1, -2, 1) };

	public static final CubeCoordinate CENTER = new CubeCoordinate(0, 0, 0);

	public final IntVector vector;

	public CubeCoordinate(IntVector v) {
		if (v.size() != 3 || (v.values[0] + v.values[1] + v.values[2] != 0))
			throw new IllegalArgumentException();
		this.vector = v;
	}

	public CubeCoordinate(int x, int y, int z) {
		this(new IntVector(x, y, z));
	}

	public CubeCoordinate(CubeCoordinate c) {
		this(new IntVector(c.vector));
	}

	public CubeCoordinate(CubeCoordinateFloats ccf) {
		this(new IntVector(ccf.round().vector));
	}

	/**
	 * @return A new CubeCoordinate with each coordinate multiplied by the given
	 *         factor
	 */
	public CubeCoordinate scale(int factor) {
		return new CubeCoordinate(this.vector.scale(factor));
	}

	/**
	 * @return A new CubeCoordinate gotten by adding the given CubeCoordinate
	 *         component by component
	 */
	public CubeCoordinate add(CubeCoordinate c) {
		return new CubeCoordinate(this.vector.add(c.vector));
	}

	/**
	 * @return A new CubeCoordinate gotten by subtracting the given
	 *         CubeCoordinate component by component
	 */
	public CubeCoordinate subtract(CubeCoordinate c) {
		return new CubeCoordinate(this.vector.subtract(c.vector));
	}

	/**
	 * @return A new CubeCoordinate gotten by rotating left by 60 degrees around
	 *         the center (0,0,0)
	 */
	public CubeCoordinate rotateLeft() {
		return new CubeCoordinate(-this.vector.values[1],
				-this.vector.values[2], -this.vector.values[0]);
	}

	/**
	 * @return A new CubeCoordinate gotten by rotating right by 60 degrees
	 *         around the center (0,0,0)
	 */
	public CubeCoordinate rotateRight() {
		return new CubeCoordinate(-this.vector.values[2],
				-this.vector.values[0], -this.vector.values[1]);
	}

	/**
	 * @return the distance from this coordinate to the center (0,0,0)
	 */
	public float length() {
		return this.vector.maximumNorm();
	}

	/**
	 * @return the equivalent AxialCoordinate of this CubeCoordinate
	 */
	public AxialCoordinate toAxial() {
		return new AxialCoordinate(this.vector.values[0], this.vector.values[2]);
	}

	/**
	 * @return CubeCoordinates for the six neighbors of this CubeCoordinate
	 */
	public CubeCoordinate[] getNeighbors() {
		CubeCoordinate[] neighbors = new CubeCoordinate[6];

		for (int i = 0; i < 6; i++)
			neighbors[i] = this.add(DIRECTIONS[i]);

		return neighbors;
	}

	/**
	 * @return whether the passed CubeCoordinate is a neighbor of this one
	 */
	public boolean isNeighbor(CubeCoordinate c) {
		return Math.abs(c.vector.values[0] - this.vector.values[0]) <= 1
				&& Math.abs(c.vector.values[1] - this.vector.values[1]) <= 1
				&& Math.abs(c.vector.values[2] - this.vector.values[2]) <= 1
				&& !this.equals(c);
	}

	/**
	 * @return the distance from the passed CubeCoordinate to this one
	 */
	public int distance(CubeCoordinate c) {
		return (Math.abs(this.vector.values[0] - c.vector.values[0])
				+ Math.abs(this.vector.values[1] - c.vector.values[1]) + Math
					.abs(this.vector.values[2] - c.vector.values[2])) / 2;
	}

	/**
	 * @return the distance from this CubeCordinate to the center (0, 0, 0)
	 */
	public int distance() {
		return this.distance(CENTER);
	}

	/**
	 * @return an array containing the CubeCoordinates forming the direct line
	 *         between this and the given CubeCoordinate.
	 */
	public CubeCoordinate[] linedraw(CubeCoordinate c) {
		ArrayList<CubeCoordinate> coords = new ArrayList<CubeCoordinate>();
		for (CubeCoordinateFloats ccf : new CubeCoordinateFloats(this)
				.linedraw(new CubeCoordinateFloats(c)))
			coords.add(ccf.round());
		return (CubeCoordinate[]) coords.toArray();
	}

	/**
	 * This does the linedrawing twice, with different, minimal offsets to the
	 * target location. When an interpolation point would be on the edge of two
	 * hexes, this will return an array for each possible choice to which hex
	 * the edge should belong.
	 * 
	 * @param c
	 *            the CubeCoordinate to draw the line to
	 * @return the outer array contains two arrays as returned by linedraw(c)
	 */
	public CubeCoordinate[][] linedrawWithOffsets(CubeCoordinate c) {
		CubeCoordinate[][] coords = new CubeCoordinate[2][];

		ArrayList<CubeCoordinate> coordsPositiveOffset = new ArrayList<CubeCoordinate>();
		for (CubeCoordinateFloats ccf : new CubeCoordinateFloats(this)
				.linedraw(new CubeCoordinateFloats(c)
						.add(new CubeCoordinateFloats(0.01f, 0, 0))))
			coordsPositiveOffset.add(ccf.round());

		ArrayList<CubeCoordinate> coordsNegativeOffset = new ArrayList<CubeCoordinate>();
		for (CubeCoordinateFloats ccf : new CubeCoordinateFloats(this)
				.linedraw(new CubeCoordinateFloats(c)
						.add(new CubeCoordinateFloats(-0.01f, 0, 0))))
			coordsPositiveOffset.add(ccf.round());

		coords[0] = (CubeCoordinate[]) coordsPositiveOffset.toArray();
		coords[1] = (CubeCoordinate[]) coordsNegativeOffset.toArray();

		return coords;
	}

	/**
	 * @return all CubeCoordinates with a distance to this equal to or less than
	 *         radius
	 */
	public CubeCoordinate[] range(int radius) {
		ArrayList<CubeCoordinate> coords = new ArrayList<CubeCoordinate>();

		for (int dx = -radius; dx <= radius; dx++)
			for (int dy = Math.max(-radius, -dx - radius); dy >= Math.min(
					radius, -dx + radius); dy++)
				coords.add(this.add(new CubeCoordinate(dx, dy, -dx - dy)));

		return (CubeCoordinate[]) coords.toArray();
	}

	/**
	 * 
	 * @param map
	 *            holds the centerpoints for the circles and the corresponding
	 *            radii
	 * @return the CubeCoordinates in the ranges for all the CubeCoordinates in
	 *         the passed map
	 */
	public CubeCoordinate[] intersectingRange(Map<CubeCoordinate, Integer> map) {
		ArrayList<CubeCoordinate> coords = new ArrayList<CubeCoordinate>();

		int xmin, xmax, zmin, zmax;

		xmin = maximum(lowerValues(map, 0));
		xmax = minimum(higherValues(map, 0));
		zmin = maximum(lowerValues(map, 2));
		zmax = minimum(higherValues(map, 2));

		for (int x = xmin; x <= xmax; x++)
			for (int z = zmin; z <= zmax; z++)
				coords.add(new CubeCoordinate(x, -x - z, z));

		return (CubeCoordinate[]) coords.toArray();
	}

	/**
	 * 
	 * @param steps
	 *            limit for the flood fill
	 * @param obstacles
	 *            the CubeCoordinates that act as obstacles for the flood fill,
	 *            they can not get "flooded"
	 * @return All CubeCoordinates that can be reached from this through steps
	 *         steps, without being able to enter the obstacles.
	 */
	public CubeCoordinate[] distanceLimitedFloodFill(int steps,
			CubeCoordinate... obstacles) {
		Set<CubeCoordinate> coords = new HashSet<CubeCoordinate>();
		coords.add(this);

		ArrayList<ArrayList<CubeCoordinate>> fringes = new ArrayList<ArrayList<CubeCoordinate>>();
		fringes.add(new ArrayList<CubeCoordinate>());
		fringes.get(0).add(this);

		for (int i = 2; i <= steps; i++) {
			fringes.add(new ArrayList<CubeCoordinate>());

			for (CubeCoordinate c : fringes.get(i - 1))
				for (CubeCoordinate direction : DIRECTIONS) {
					CubeCoordinate neighbor = c.add(direction);

					boolean isObstacle = false;
					for (CubeCoordinate obstacle : obstacles)
						if (obstacle.equals(c)) {
							isObstacle = true;
							break;
						}

					if (!isObstacle) {
						coords.add(neighbor);
						fringes.get(i).add(neighbor);
					}
				}
		}

		return (CubeCoordinate[]) coords.toArray();
	}

	/**
	 * @return all CubeCoordinates with an exact distance of radius to this
	 */
	public CubeCoordinate[] ring(int radius) {
		ArrayList<CubeCoordinate> coords = new ArrayList<CubeCoordinate>();
		CubeCoordinate currentCoord = this.add(DIRECTIONS[4].scale(radius));

		for (CubeCoordinate direction : DIRECTIONS)
			for (int i = 0; i < radius; i++) {
				coords.add(currentCoord);
				currentCoord = currentCoord.add(direction);
			}

		return (CubeCoordinate[]) coords.toArray();
	}

	/**
	 * @return whether the given CubeCoordinate is on a ring of the given radius
	 *         around this
	 */
	public boolean onRing(CubeCoordinate c, int radius) {
		return this.distance(c) == radius;
	}

	@Override
	public String toString() {
		return this.vector.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof CubeCoordinate))
			return false;
		CubeCoordinate c = (CubeCoordinate) o;
		return (this.vector.equals(c.vector));
	}

	@Override
	public int hashCode() {
		return this.vector.hashCode();
	}

	private int maximum(int[] integers) {
		int max = integers[0];

		for (int i : integers)
			if (integers[i] > max)
				max = integers[i];

		return max;
	}

	private int minimum(int[] integers) {
		int min = integers[0];

		for (int i : integers)
			if (integers[i] < min)
				min = integers[i];

		return min;
	}

	private int[] higherValues(Map<CubeCoordinate, Integer> map, int coord) {
		int counter = 0;
		int[] higherValues = new int[map.size()];
		for (Entry<CubeCoordinate, Integer> entry : map.entrySet()) {
			higherValues[counter] = entry.getKey().vector.values[coord]
					+ entry.getValue();
			counter++;
		}
		return higherValues;
	}

	private int[] lowerValues(Map<CubeCoordinate, Integer> map, int coord) {
		int counter = 0;
		int[] lowerValues = new int[map.size()];
		for (Entry<CubeCoordinate, Integer> entry : map.entrySet()) {
			lowerValues[counter] = entry.getKey().vector.values[coord]
					- entry.getValue();
			counter++;
		}
		return lowerValues;
	}
}
