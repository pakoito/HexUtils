package hexUtils.math;

public class FloatVector {

	public final float[] values;

	public FloatVector(int size) {
		if (size < 0)
			throw new IllegalArgumentException();
		this.values = new float[size];
	}

	public FloatVector(float... values) {
		this.values = values;
	}

	public FloatVector(FloatVector floatVector) {
		this(floatVector.values);
	}

	public FloatVector(IntVector intVector) {
		this(intVector.values[0], intVector.values[1], intVector.values[2]);
	}

	public FloatVector scale(float factor) {
		float[] result = new float[this.size()];

		for (int i = 0; i < this.size(); i++)
			result[i] = this.values[i] * factor;

		return new FloatVector(result);
	}

	public FloatVector add(FloatVector v) {
		float[] result = new float[this.size()];

		for (int i = 0; i < this.size(); i++)
			result[i] = this.values[i] + v.values[i];

		return new FloatVector(result);
	}

	public FloatVector subtract(FloatVector v) {
		float[] result = new float[this.size()];

		for (int i = 0; i < this.size(); i++)
			result[i] = this.values[i] - v.values[i];

		return new FloatVector(result);
	}

	public float maximumNorm() {
		float length = 0;
		for (float value : this.values) {
			if (Math.abs(value) > length) {
				length = Math.abs(value);
			}
		}
		return length;
	}

	public int size() {
		return this.values.length;
	}

	public boolean sameSize(FloatVector v) {
		return this.size() == v.size();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < values.length; i++) {
			sb.append(values[i]);
			sb.append(',');
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof FloatVector))
			return false;

		FloatVector other = (FloatVector) o;

		if (!this.sameSize(other))
			return false;

		for (int i = 0; i < this.size(); i++)
			if (this.values[i] != other.values[i])
				return false;

		return true;
	}

	@Override
	public int hashCode() {
		int hashCode = 57;
		int mult = 29;

		for (float value : this.values)
			hashCode = hashCode * mult
					+ ((value == 0.0F) ? 0 : Float.floatToIntBits(value));

		return hashCode;
	}

}
