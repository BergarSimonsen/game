package model.resource;

import model.enumeration.ResourceType;

public class Resource extends Obtainable {
	ResourceType type;
	int value;
	
	public Resource( ResourceType type, int value ) {
		this.type = type;
		this.value = value;
	}

	public ResourceType getType() {
		return type;
	}

	public void setType(ResourceType type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
