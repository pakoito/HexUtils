package hexUtils;

import hexUtils.math.IntVector;

public class AxialCoordinate {

	public final IntVector vector;

	public AxialCoordinate(IntVector v) {
		if (v.size() != 2)
			throw new IllegalArgumentException();
		this.vector = v;
	}

	public AxialCoordinate(int q, int r) {
		this(new IntVector(q, r));
	}

	public AxialCoordinate(AxialCoordinate c) {
		this(new IntVector(c.vector));
	}

	/**
	 * @return the equivalent CubeCoordinate of this AxialCoordinate
	 */
	public CubeCoordinate toCube() {
		int x = this.vector.values[0];
		int z = this.vector.values[1];
		int y = -x - z;
		return new CubeCoordinate(x, y, z);
	}

}
