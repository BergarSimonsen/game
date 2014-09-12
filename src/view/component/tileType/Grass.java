package view.component.tileType;

import java.awt.Color;

import model.TileImages;
import model.enumeration.TileName;
import model.enumeration.TileState;

public class Grass extends TileType {
	public Grass() {
		super(Color.GREEN, TileName.GRASS, TileState.OPEN, TileImages.getInstance().getGrass());
	}
}
