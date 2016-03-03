package cn.hlj.ems.util;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * 创建一个类，和\commons\tree_data1.json 里的数据的结构相一致
 * 
 * @author EHJ
 *
 */
public class Navigation {
	private Integer id;
	private String text;

	private String state = "open";
	private String url;

	private Collection<Navigation> children = new LinkedHashSet<>();

	public Navigation() {
		super();
	}

	public Navigation(Integer id, String text, String url) {
		super();
		this.id = id;
		this.text = text;
		this.url = url;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Collection<Navigation> getChildren() {
		return children;
	}

	public void setChildren(Collection<Navigation> children) {
		this.children = children;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Navigation other = (Navigation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
