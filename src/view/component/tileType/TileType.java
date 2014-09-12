package view.component.tileType;

import java.awt.Color;
import java.awt.image.BufferedImage;

import model.enumeration.TileName;
import model.enumeration.TileState;

public class TileType {
	private Color color;
	private TileName name;
	private TileState state;
	private BufferedImage backgroundImage;
	
	public TileType( Color c, TileName name, TileState state, BufferedImage bgImg ) {
		this.color = c;
		this.name = name;
		this.state = state;
		this.backgroundImage = bgImg;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public TileName getName() {
		return name;
	}

	public void setName(TileName name) {
		this.name = name;
	}

	public TileState getState() {
		return state;
	}

	public void setState(TileState state) {
		this.state = state;
	}

	public BufferedImage getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(BufferedImage backgroundImage) {
		this.backgroundImage = backgroundImage;
	}
}
