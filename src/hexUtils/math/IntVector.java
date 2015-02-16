package hexUtils.math;

public class IntVector {

	public final int[] values;

	public IntVector(int size) {
		if (size < 0)
			throw new IllegalArgumentException();
		this.values = new int[size];
	}

	public IntVector(int... values) {
		this.values = values;
	}

	public IntVector(IntVector intVector) {
		this.values = new int[intVector.size()];
		for (int i = 0; i < this.size(); i++)
			this.values[i] = intVector.values[i];
	}

	public IntVector scale(int factor) {
		int[] result = new int[this.size()];

		for (int i = 0; i < this.size(); i++)
			result[i] = this.values[i] * factor;

		return new IntVector(result);
	}

	public IntVector add(IntVector v) {
		int[] result = new int[this.size()];

		for (int i = 0; i < this.size(); i++)
			result[i] = this.values[i] + v.values[i];

		return new IntVector(result);
	}

	public IntVector subtract(IntVector v) {
		int[] result = new int[this.size()];

		for (int i = 0; i < this.size(); i++)
			result[i] = this.values[i] - v.values[i];

		return new IntVector(result);
	}

	public int maximumNorm() {
		int length = 0;
		for (int value : this.values) {
			if (Math.abs(value) > length) {
				length = Math.abs(value);
			}
		}
		return length;
	}

	public int size() {
		return this.values.length;
	}

	public boolean sameSize(IntVector v) {
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
		if (o == null || !(o instanceof IntVector))
			return false;

		IntVector other = (IntVector) o;

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

		for (int value : this.values)
			hashCode = hashCode * mult + value;

		return hashCode;
	}

}
