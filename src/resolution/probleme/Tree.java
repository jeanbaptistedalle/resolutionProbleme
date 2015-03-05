package resolution.probleme;

import java.util.ArrayList;
import java.util.List;

public class Tree {

	private Integer value;
	private List<Integer> avalaible;
	private List<Tree> childs;

	public Tree() {
		value = null;
		childs = new ArrayList<Tree>();
		avalaible = new ArrayList<Integer>();
	}

	public Tree(final Integer value) {
		this.value = value;
	}

	public void addChild(final Tree tree) {
		childs.add(tree);
	}

	public boolean isRoot() {
		return value == null;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public List<Tree> getChilds() {
		return childs;
	}

	public void setChilds(List<Tree> childs) {
		this.childs = childs;
	}

	public String toString() {
		return value + "" + childs;
	}

	public List<Integer> getAvalaible() {
		return avalaible;
	}

	public void setAvalaible(List<Integer> avalaible) {
		this.avalaible = avalaible;
	}
}
