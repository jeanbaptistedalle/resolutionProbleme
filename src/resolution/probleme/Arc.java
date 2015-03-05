package resolution.probleme;

public class Arc {

	int x;
	int y;

	public Arc(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arc other = (Arc) obj;
		if (x != other.x || x != other.y)
			return false;
		if (y != other.x || y != other.y)
			return false;
		return true;
	}
	
	public String toString(){
		return x+">"+y;
	}

}
