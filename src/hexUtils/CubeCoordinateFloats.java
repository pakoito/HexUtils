package hexUtils;

import java.util.ArrayList;

import hexUtils.math.FloatVector;

public class CubeCoordinateFloats {

	public final FloatVector vector;

	public CubeCoordinateFloats(FloatVector v) {
		if (v.size() != 3)
			throw new IllegalArgumentException();
		this.vector = v;
	}

	public CubeCoordinateFloats(float x, float y, float z) {
		this(new FloatVector(x, y, z));
	}

	public CubeCoordinateFloats(CubeCoordinateFloats c) {
		this(new FloatVector(c.vector));
	}

	public CubeCoordinateFloats(CubeCoordinate c) {
		this(new FloatVector(c.vector));
	}

	/**
	 * @return A new CubeCoordinateFloats with each coordinate multiplied by the
	 *         given factor
	 */
	public CubeCoordinateFloats scale(float factor) {
		return new CubeCoordinateFloats(this.vector.scale(factor));
	}

	/**
	 * @return A new CubeCoordinateFloats gotten by adding the given
	 *         CubeCoordinateFloats component by component
	 */
	public CubeCoordinateFloats add(CubeCoordinateFloats ccf) {
		return new CubeCoordinateFloats(this.vector.add(ccf.vector));
	}

	/**
	 * @return A new CubeCoordinateFloats gotten by subtracting the given
	 *         CubeCoordinateFloats component by component
	 */
	public CubeCoordinateFloats subtract(CubeCoordinateFloats c) {
		return new CubeCoordinateFloats(this.vector.subtract(c.vector));
	}

	/**
	 * @return an array containing the CubeCoordinateFloats forming the direct
	 *         line between this and the given CubeCoordinateFloats.
	 */
	public CubeCoordinateFloats[] linedraw(CubeCoordinateFloats ccf) {
		ArrayList<CubeCoordinateFloats> coords = new ArrayList<CubeCoordinateFloats>();

		int distance = this.round().distance(ccf.round());
		for (int i = 0; i < distance + 1; i++)
			coords.add(new CubeCoordinateFloats(ccfLerp(ccf, i / distance)));
		return (CubeCoordinateFloats[]) coords.toArray();
	}

	/**
	 * Linear interpolation between this and the given CubeCoordinateFloats
	 */
	public CubeCoordinateFloats ccfLerp(CubeCoordinateFloats ccf, float f) {
		return new CubeCoordinateFloats(this.add(ccf.subtract(this).scale(f)));
	}

	/**
	 * Rounds to the nearest CubeCoordinate
	 * 
	 * @return
	 */
	public CubeCoordinate round() {
		int[] roundedCoordinates = new int[3];
		int sum = 0;

		for (int i = 0; i < 3; i++) {
			roundedCoordinates[i] = Math.round(this.vector.values[i]);
			sum += roundedCoordinates[i];
		}

		if (sum != 0) {
			float[] deltas = new float[3];
			int highestDeltaIndex = 0;

			for (int i = 0; i < 3; i++) {
				deltas[i] = Math.abs(roundedCoordinates[i]
						- this.vector.values[i]);
				if (deltas[i] > deltas[highestDeltaIndex]) {
					highestDeltaIndex = i;
				}
			}

			roundedCoordinates[highestDeltaIndex] = -sum
					+ roundedCoordinates[highestDeltaIndex];
		}

		return new CubeCoordinate(roundedCoordinates[0], roundedCoordinates[1],
				roundedCoordinates[2]);
	}

	@Override
	public String toString() {
		return this.vector.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof CubeCoordinateFloats))
			return false;
		CubeCoordinateFloats c = (CubeCoordinateFloats) o;
		return (this.vector.equals(c.vector));
	}

	@Override
	public int hashCode() {
		return this.vector.hashCode();
	}
}
