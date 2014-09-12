package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TileImages {
	public static TileImages instance;
	
	// Tile images
	private BufferedImage grass;
	private BufferedImage dirt;
	private BufferedImage lava;
	private BufferedImage rough;
	private BufferedImage sand;
	private BufferedImage snow;
	private BufferedImage subterrenean;
	private BufferedImage swamp;
	private BufferedImage water;
	
	// Path to tile image location
	private final String root_path = "res/tiles/";
	private final String grass_path = root_path + "grass.png";
	private final String dirt_path = root_path + "dirt.png";
	private final String lava_path = root_path + "lava.png";
	private final String rough_path = root_path + "rough.png";
	private final String sand_path = root_path + "sand.png";
	private final String snow_path = root_path + "snow.png";
	private final String subterrenean_path = root_path + "subterrenean.png";
	private final String swamp_path = root_path + "swamp.png";
	private final String water_path = root_path + "water.png";
	
	private TileImages() {
		loadImages();
	}
	
	private void loadImages() {
		this.grass = loadImage( grass_path );
		this.dirt = loadImage( dirt_path );
		this.lava = loadImage( lava_path );
		this.rough = loadImage( rough_path );
		this.sand = loadImage( sand_path );
		this.snow = loadImage( snow_path );
		this.subterrenean = loadImage( subterrenean_path );
		this.swamp = loadImage( swamp_path );
		this.water = loadImage( water_path );
	}
	
	private BufferedImage loadImage( String path ) {
		BufferedImage retval = null;
		try {
			retval = ImageIO.read( new File( path ) );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		return retval;
	}

	
	public static TileImages getInstance() {
		if( instance == null ) instance = new TileImages();
		return instance;
	}
	

	public BufferedImage getGrass() {
		if( grass == null ) grass = loadImage( grass_path );
		return grass;
	}
	

	public BufferedImage getDirt() {
		if( dirt == null ) dirt = loadImage( dirt_path );
		return dirt;
	}
	

	public BufferedImage getLava() {
		if( lava == null ) lava = loadImage( lava_path );
		return lava;
	}
	

	public BufferedImage getRough() {
		if( rough == null ) rough = loadImage( rough_path );
		return rough;
	}
	

	public BufferedImage getSand() {
		if( sand == null ) sand = loadImage( sand_path );
		return sand;
	}
	

	public BufferedImage getSnow() {
		if( snow == null ) snow = loadImage( snow_path );
		return snow;
	}
	

	public BufferedImage getSubterrenean() {
		if( subterrenean == null ) subterrenean = loadImage( subterrenean_path );
		return subterrenean;
	}
	

	public BufferedImage getSwamp() {
		if( swamp == null ) swamp = loadImage( swamp_path );
		return swamp;
	}
	

	public BufferedImage getWater() {
		if( water == null ) water = loadImage( water_path );
		return water;
	}
}
