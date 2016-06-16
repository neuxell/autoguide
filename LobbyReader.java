package main;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
	
	public LobbyReader(){
		openPortraitFolder();
		bankFolder = new File("/portraits");
	
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
	
		// TODO: MULTITHREAD: put this in its own thread, for team 1. Then do a second for team 2
		for(int i = 0; i < 5; i++){ // for every player
			String selection = identifySelection(new Dimension(t1x, y1+playerSpacing*i));
		}
	
		for(int i = 0; i < 5; i++){ // for every player
			String selection = identifySelection(new Dimension(t2x, y1+playerSpacing*i));
		}
	}
	
	public void openPortraitFolder(){
	
	}
	
	/*
	 * the champ portrait bank will need to be stocked before processing is possible
	 * check portrait: found? Return name. Not? save it, and ask for its label
	 */
	public String identifySelection(Dimension playerCoords){
	
		// check portrait
		final int SIDE_LENGTH = 5;
		for(int x = 0; x < SIDE_LENGTH; x++){
			for(int y = 0; y < SIDE_LENGTH; y++){
				int clr = checkPixel(new Dimension(playerCoords.width+x, playerCoords.height+y);
				// if found
	
				// if not found{
					System.out.println("What is the name of this champ?");
					displayImage(champ);
				}
			}
		}
	}
	
	/*
	 * returns the color of the pixel
	 * this is really more of just a personal reference,
	 * as I plan to come back and use the bitwise operations shown here
	 */
	public int checkPixel(Dimension coords) throws IOException{
		// Getting pixel color by position x and y... neat stuff
		int clr =  img.getRGB(coords.width,coords.height); 
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
	
	}
}
/*------------------------------
supposedly, [GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
int width = gd.getDisplayMode().getWidth();
int height = gd.getDisplayMode().getHeight();]
is supposed to be better for grabbing the monitor resolution in a multi-monitor config.
i.e. use this as a back up if program goes kaputt due to resolution finding problems*/