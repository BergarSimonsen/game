package view.component.tileType;

import java.awt.Color;

import model.TileImages;
import model.enumeration.TileName;
import model.enumeration.TileState;

public class Dirt extends TileType {
	public Dirt() {
		super(Color.GRAY, TileName.DIRT, TileState.OPEN, TileImages.getInstance().getDirt());
	}
}
