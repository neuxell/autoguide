package main;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial") // because serial versions are for tryhards :P nah, i just dont think I'll be needing it here.
public class LobbyReader extends JFrame{
	private BufferedImage image;
	private Map<String, Integer> picks;

	public static void main(String[] args){
		new LobbyReader();
	}
	
	public LobbyReader(){
		//standard jframe setup
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// TODO: replace with auto screenshot taker, or constant screen checker (for updating screenshots)
		// ideas: opencv/+ocr, find the portraits, and save them. that's the name of the game :^)
		try {
			image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			getContentPane().add(new JLabel(new ImageIcon(image)), BorderLayout.CENTER);
			pack();
			setVisible(true);
		} catch (HeadlessException | AWTException e1) {
			e1.printStackTrace();
		}
		picks = new HashMap<String, Integer>();

		// TODO: this is hardcoded for 1920x1080, so uh.. yeah. You can see why that's bad.
		int[] X = new int[]{370, 1342}; // team 1, team 2
		int[] Y = new int[]{317, 382, 446, 514, 579}; // players 1-5
		int size = 25; //note: aoe
		
		/*
		 * basically, in the initial phase of this portion, it was to extract from 10 points (xy's)
		 * then i expanded on that, adding size adjustment of the aoe
		 */
		for(int x:X){ // loop through teams (x-positions)
			for(int y:Y){ // loop through the players (y-positions)
				int clr = 0;
				try {
					clr = checkPixel(x, y);
					List<Integer> sample = new ArrayList<Integer>();
					// records the portrait sample of 'size' area |for visual purposes atm, later can be rev.eng'd
					for(int i = 0; i<size; i++){
						for(int j = 0; j<size; j++){ // TODO: identify/update champions based on pixel selection
							sample.add(image.getRGB(x+i, y+j));
							image.setRGB(x+i, y+j, Color.red.getRGB());
							/*
							 * alternative approach to the aoe... but I didn't care to learn how to use it.
							 * besides, my approach is probably simpler... which I guess is better in this case
							 */
							//image.setRGB(x, y, 30, 30, new int[]{Color.blue.getRGB()}, 0, 2);
						}
					}
					repaint();

					String champ = identify(sample);
					if(champ != "")
						System.out.println(champ + " detected.");
					else{
						System.out.println("Unidentified champion.");
						// TODO: 
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				picks.put("("+x+","+y+")", clr);
				Dimension d = new Dimension(1024, 728);
			}
		}
		
		System.out.println("\n\n"+picks.toString());
	}
	
	
	
	/*
	 * match a small set of pixels to the champion portraits archive.
	 * if it's found, return the champ's name
	 * otherwise, save it, and ask for its name
	 */
	public String identify(List<Integer> sample) throws FileNotFoundException{
		String champname = "";
		/*
		 * find all files in the directory
		 */
		Scanner colorScan = new Scanner(System.in);
		File[] portraitList = new File("/portraits").listFiles();
		if(portraitList != null)
			for(File f : portraitList){
				System.out.println(f.getName() + "'s portrait found.");
				colorScan = new Scanner(f);
				for(Integer clr : sample)
					if(!colorScan.hasNext()){
						System.out.print("Matched: ");
						break;
					}
					else if(Integer.valueOf(colorScan.nextLine()) != clr){
						System.out.print("Match failed for ");
						break;
					}
			}
		else{
			System.out.println("No portraits found.");
			return null;
		}
		/*
		 * for every portrait, do this
		 */
		// first, find all traces of the first pixel
		
		// followup with traces of the second | for now, this is the farthest it will be taken
		// (won't be matching like 99 points. just 1 initially)
		// finally, squentially match the rest of the image
		
		return champname;
	}

	/*
	 *	returns the color of the pixel
	 */
	public int checkPixel(int x, int y) throws IOException{
		// Getting pixel color by position x and y... neat stuff
		int clr =  image.getRGB(x,y); 
		int  a  = (clr & 0xff000000) >> 24; // alpha \o/
		int  r  = (clr & 0x00ff0000) >> 16; // good ol' roy g. biv
		int  g  = (clr & 0x0000ff00) >> 8;
		int  b  =  clr & 0x000000ff;
		System.out.print("R"+ r);
		System.out.print(",G"+ g);
		System.out.print(",B"+ b);
		System.out.println(",A"+ a);
		
		return clr;
	}
	
	public Map<String, Integer> getPicks(){
		return picks;
	}
}

