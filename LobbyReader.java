package main;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

/* take screenshot
check all champ placeholders
-> check if a champ is selected there
-> if so, find out what that champ is
return the name of the champ? Or return the array of all the champs selected (+ banned?)

I think it should return an array of all in play champs.
Integer[][] // rows = team 1, team 2
			// cols = players 1-5, per team

I thought about making Portrait objects,
but the simplest route would be to use a 2d integer array containing the rgb integer
and using bitwise to find the ARGB

Integer[2][0] // get player 3 from team 1

--------------------------------------------------------*/
public class LobbyReader{
	private static File bankFolder;
	private static String[] selection;
	private static BufferedImage ss;
	private int t1x, t2x, y1, playerSpacing;
	private Dimension screenSize, defaultClientSize, topLeftCoords;
	
	private JFrame imageViewer;
	
	public LobbyReader(){
		imageViewer = new JFrame("LR-Vision");
		
	
		final int PICKS = 10;
		final int BANS = 6;
		selection = new String[PICKS+BANS]; // first 10 for picks, last 6 for bans
		ss = screenshot(); // screenshot
	
		// default client location, adjust by screen resolution
		screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		defaultClientSize = new Dimension(1024, 768);
		topLeftCoords = new Dimension(((screenSize.width/4)-(defaultClientSize.width/4)),
												(screenSize.height/4)-(defaultClientSize.height/4));
		t1x = topLeftCoords.width+34; // team 1 x
		t2x = topLeftCoords.width+1010; // team 2 x
		y1 = topLeftCoords.height+113; // y-position of the first player on each team
		playerSpacing = 66; // spacing per player

		openPortraitFolder();
		processPortraits();
	}
	
	private void processPortraits() {
		// TODO: MULTITHREAD: put this in its own thread, for team 1. Then do a second for team 2
		for(int i = 0; i < 5; i++){ // for every player
			String playerName = OCR(new Dimension(t1x, y1+playerSpacing*i));
			String champName = identifySelection(new Dimension(t1x, y1+playerSpacing*i));
			selection[i] += "1" + playerName + ":" + champName;
		}
	
		for(int i = 0; i < 5; i++){ // for every player
			String selection = identifySelection(new Dimension(t2x, y1+playerSpacing*i));
		}
	}

	// TODO: implement tesseract-ocr api
	private String OCR(Dimension dimension) {
		
		
		return null;
	}

	public BufferedImage screenshot(){
		// TODO: replace with auto screenshot taker, or constant screen checker (for updating screenshots)
		// ideas: opencv/+ocr, find the portraits, and save them. that's the name of the game :^)
		try {
			return new java.awt.Robot().createScreenCapture(new java.awt.Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize()));
		} catch (java.awt.HeadlessException | java.awt.AWTException e1) {
			e1.printStackTrace();
		}
	}
	
	public void openPortraitFolder(){
		bankFolder = new File("/portraits");
	}
	
	/*
	 * the champ portrait bank will need to be stocked before processing is possible
	 * check portrait: found? Return name. Not? save it, and ask for its label
	 */
	public String identifySelection(Dimension playerCoords) throws IOException{
		// check portrait
		final int SIDE_LENGTH = 5;
		for(int x = 0; x < SIDE_LENGTH; x++){
			for(int y = 0; y < SIDE_LENGTH; y++){
				int clr = checkPixel(new Dimension(playerCoords.width+x, playerCoords.height+y));
				// if found
	
				// if not found{
					System.out.println("What is the name of this champ?");
					BufferedImage portrait = ss.getSubimage(playerCoords.width, playerCoords.height, SIDE_LENGTH, SIDE_LENGTH);
					displayImage(portrait);
					File outputfile = new File("image.jpg");
					javax.imageio.ImageIO.write(portrait, "jpg", outputfile);
				//}
			}
		}
		
		return null;
	}


	private void displayImage(BufferedImage portrait) {
		
	}
	
	/*
	 * returns the color of the pixel
	 * this is really more of just a personal reference,
	 * as I plan to come back and use the bitwise operations shown here
	 */
	public int checkPixel(Dimension coords) throws IOException{
		// Getting pixel color by position x and y... neat stuff
		int clr =  ss.getRGB(coords.width,coords.height); 
		/*int  a  = (clr & 0xff000000) >> 24; // alpha \o/ (?)
		int  r  = (clr & 0x00ff0000) >> 16; // good ol' roy g. biv
		int  g  = (clr & 0x0000ff00) >> 8;
		int  b  =  clr & 0x000000ff;
		System.out.print("R"+ r);
		System.out.print(",G"+ g);
		System.out.print(",B"+ b);
		System.out.println(",A"+ a);*/
		
		return clr;
	}
	
	public String[] getSelection(){
		return selection;
	}
	
	/*
	 * I was thinking that update should return the new selection string
	 * but nah
	 */
	public void update(){
		ss = screenshot();
	}
}
/*------------------------------
supposedly, [GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
int width = gd.getDisplayMode().getWidth();
int height = gd.getDisplayMode().getHeight();]
is supposed to be better for grabbing the monitor resolution in a multi-monitor config.
i.e. use this as a back up if program goes kaputt due to resolution finding problems*/