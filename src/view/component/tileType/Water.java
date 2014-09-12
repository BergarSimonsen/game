package view.component.tileType;

import java.awt.Color;

import model.TileImages;
import model.enumeration.TileName;
import model.enumeration.TileState;

public class Water extends TileType {
	public Water() {
		super(Color.BLUE, TileName.WATER, TileState.NAUTICAL_OPEN, TileImages.getInstance().getWater());
	}
}
