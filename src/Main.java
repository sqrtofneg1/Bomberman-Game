import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class Main extends JFrame{
	
    int windowWidth = 544;
    int windowHeight = 564;
    int fps = 60;
    
    Image imgp1up, imgp1down, imgp1right, imgp1left;
    Image imgp2up, imgp2down, imgp2right, imgp2left;
    Image imgbomb, imgbomb2, imgbomb3;
    Image imgbombup, imgpowerup;
    
    int p1lastface = 0;
    int p2lastface = 0;
    
    Rectangle[][] tilerect = new Rectangle[35][35];
    int[][] tilestatus = new int[35][35];
    int player1xpos = 1, player1ypos = 1;
    int player2xpos = 13, player2ypos = 13;
    Rectangle player1 = new Rectangle (player1xpos*32+32, player1ypos*32+52, 32, 32);
    Rectangle player2 = new Rectangle (player2xpos*32+32, player2ypos*32+52, 32, 32);
    
    boolean p1left = false, p1right = true, p1up = false, p1down = true;
    boolean p2left = false, p2right = false, p2up = false, p2down = false;
    
    int p1bombmod = 1, p2bombmod = 1;
    int p1powermod = 1, p2powermod = 1;
    
    int p1life = 1, p2life = 1;
    boolean p1lifecooldown = false, p2lifecooldown = false;
    
    int bombs;
    
    boolean firsttime = true;
    
    public Main(){
    	super("Bomberman Game");
    	setTitle("Bomberman Game");
        setSize(windowWidth, windowHeight);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(new GridLayout(windowWidth, windowHeight));
        initialize();
        try{
			imgp1down = ImageIO.read(new File("resources/img/p1down.png"));
		}
		catch(Exception e){		}
        try{
			imgp1left = ImageIO.read(new File("resources/img/p1left.png"));
		}
		catch(Exception e){		}
        try{
			imgp1up = ImageIO.read(new File("resources/img/p1up.png"));
		}
		catch(Exception e){		}
        try{
			imgp1right = ImageIO.read(new File("resources/img/p1right.png"));
		}
		catch(Exception e){		}
        try{
			imgp2down = ImageIO.read(new File("resources/img/p2down.png"));
		}
		catch(Exception e){		}
        try{
			imgp2left = ImageIO.read(new File("resources/img/p2left.png"));
		}
		catch(Exception e){		}
        try{
			imgp2up = ImageIO.read(new File("resources/img/p2up.png"));
		}
		catch(Exception e){		}
        try{
			imgp2right = ImageIO.read(new File("resources/img/p2right.png"));
		}
		catch(Exception e){		}
        try{
			imgbomb = ImageIO.read(new File("resources/img/bomb.png"));
		}
		catch(Exception e){		}
        try{
			imgbomb2 = ImageIO.read(new File("resources/img/bomb2.png"));
		}
		catch(Exception e){		}
        try{
			imgbomb3 = ImageIO.read(new File("resources/img/bomb3.png"));
		}
		catch(Exception e){		}
        try{
			imgbombup = ImageIO.read(new File("resources/img/bombup.png"));
		}
		catch(Exception e){		}
        try{
			imgpowerup = ImageIO.read(new File("resources/img/powerup.png"));
		}
		catch(Exception e){		}
        p1Move p1m = new p1Move();
        p1m.start();
        p2Move p2m = new p2Move();
        p2m.start();
    }
    
    void initialize(){
    	gridtilestatus();
        gridtilerect();
    }
    
    public void paint(Graphics g){ // This method will draw everything
    	Image buffer;
    	super.paint(g);
    	buffer = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB);
    	Graphics gb = buffer.getGraphics();
    	for (int row = 0; row < 15; row++){
    		for (int column = 0; column < 15; column++){
    			if (tilestatus[row][column]==0){
    				gb.setColor(Color.black);
    				gb.fillRect(tilerect[row][column].x, tilerect[row][column].y, tilerect[row][column].width, tilerect[row][column].height);
    			}
    			else if (tilestatus[row][column]==1){
    				gb.setColor(Color.DARK_GRAY);
    				gb.fillRect(tilerect[row][column].x, tilerect[row][column].y, tilerect[row][column].width, tilerect[row][column].height);
    			}
    			else if (tilestatus[row][column]==3){
    				gb.setColor(Color.LIGHT_GRAY);
    				gb.fillRect(tilerect[row][column].x, tilerect[row][column].y, tilerect[row][column].width, tilerect[row][column].height);
    			}
    			else if (tilestatus[row][column]==4){
    				gb.setColor(Color.LIGHT_GRAY);
    				gb.fillRect(tilerect[row][column].x, tilerect[row][column].y, tilerect[row][column].width, tilerect[row][column].height);
    				gb.drawImage(imgbomb, tilerect[row][column].x, tilerect[row][column].y, null);
    			}
    			else if (tilestatus[row][column]==5){
    				gb.setColor(Color.LIGHT_GRAY);
    				gb.fillRect(tilerect[row][column].x, tilerect[row][column].y, tilerect[row][column].width, tilerect[row][column].height);
    				gb.drawImage(imgbomb2, tilerect[row][column].x, tilerect[row][column].y, null);
    			}
    			else if (tilestatus[row][column]==6){
    				gb.setColor(Color.LIGHT_GRAY);
    				gb.fillRect(tilerect[row][column].x, tilerect[row][column].y, tilerect[row][column].width, tilerect[row][column].height);
    				gb.drawImage(imgbomb3, tilerect[row][column].x, tilerect[row][column].y, null);
    			}
    			else if (tilestatus[row][column]==7){//fire
    				gb.setColor(Color.RED);
    				gb.fillRect(tilerect[row][column].x, tilerect[row][column].y, tilerect[row][column].width, tilerect[row][column].height);
    			}
    			else if (tilestatus[row][column]==8){//fire, but bomb powerup
    				gb.setColor(Color.RED);
    				gb.fillRect(tilerect[row][column].x, tilerect[row][column].y, tilerect[row][column].width, tilerect[row][column].height);
    			}
    			else if (tilestatus[row][column]==9){//fire, but power powerup
    				gb.setColor(Color.RED);
    				gb.fillRect(tilerect[row][column].x, tilerect[row][column].y, tilerect[row][column].width, tilerect[row][column].height);
    			}
    			else if (tilestatus[row][column]==10){
    				gb.setColor(Color.LIGHT_GRAY);
    				gb.fillRect(tilerect[row][column].x, tilerect[row][column].y, tilerect[row][column].width, tilerect[row][column].height);
    				gb.drawImage(imgbombup, tilerect[row][column].x, tilerect[row][column].y, null);
    			}
    			else if (tilestatus[row][column]==11){// bomb powerup
    				gb.setColor(Color.LIGHT_GRAY);
    				gb.fillRect(tilerect[row][column].x, tilerect[row][column].y, tilerect[row][column].width, tilerect[row][column].height);
    				gb.drawImage(imgpowerup, tilerect[row][column].x, tilerect[row][column].y, null);
    			}
    			else if(tilestatus[row][column]==12){// power powerup
    				gb.setColor(Color.RED);
    				gb.fillRect(tilerect[row][column].x, tilerect[row][column].y, tilerect[row][column].width, tilerect[row][column].height);
    			}
    			else{
    				gb.setColor(Color.GRAY);
    				gb.fillRect(tilerect[row][column].x, tilerect[row][column].y, tilerect[row][column].width, tilerect[row][column].height);
    			}
    		}
    	}
    	if(p1lastface==0){
    		gb.drawImage(imgp1down, player1.x, player1.y, null);
    	}
    	else if(p1lastface==1){
    		gb.drawImage(imgp1left, player1.x, player1.y, null);
    	}
    	else if(p1lastface==2){
    		gb.drawImage(imgp1up, player1.x, player1.y, null);
    	}
    	else if(p1lastface==3){
    		gb.drawImage(imgp1right, player1.x, player1.y, null);
    	}
    	if(p2lastface==0){
    		gb.drawImage(imgp2down, player2.x, player2.y, null);
    	}
    	else if(p2lastface==1){
    		gb.drawImage(imgp2left, player2.x, player2.y, null);
    	}
    	else if(p2lastface==2){
    		gb.drawImage(imgp2up, player2.x, player2.y, null);
    	}
    	else if(p2lastface==3){
    		gb.drawImage(imgp2right, player2.x, player2.y, null);
    	}
    	if(firsttime == true){ //repaints once because lag
    		firsttime = false;
    		repaint();
    	}
    	gb.dispose();
    	g.drawImage(buffer, 0, 0, null);
    }
    
    void gridtilerect(){
    	for (int row = 0; row < 15; row++){
    		for (int column = 0; column < 15; column++){
    			Rectangle temprect = new Rectangle (row*32 + 32, column*32 + 52, 32, 32);
    			tilerect[row][column] = temprect;
    		}
    	}
    }
    
    void gridtilestatus(){
    	for (int row = 0; row < 15; row++){
    		for (int column = 0; column < 15; column++){
    			if (row == 0 || row == 14 || column == 0 || column == 14){ // if in first or last row or column
    				tilestatus[row][column] = 0;
    			}
    			else if ((row == 1 && column == 1) || (row == 2 && column == 1) || (row == 1 && column == 2)) { // if top left hand corner
    				tilestatus[row][column] = 3;
    			}
    			else if ((row == 1 && column == 13) || (row == 2 && column == 13) || (row == 1 && column == 12)) { // if top right hand corner
    				tilestatus[row][column] = 3;
    			}
    			else if ((row == 13 && column == 1) || (row == 12 && column == 1) || (row == 13 && column == 2)) { // if bottom left hand corner
    				tilestatus[row][column] = 3;
    			}
    			else if ((row == 13 && column == 13) || (row == 12 && column == 13) || (row == 13 && column == 12)) { // if bottom right hand corner
    				tilestatus[row][column] = 3;
    			}
    			else if (row % 2 == 0 && column % 2 == 0){ // if in every second row and column
    				tilestatus[row][column] = 1;
    			}
    			else { // everything else
    				tilestatus[row][column] = 2;
    			}
    		}
    	}
    }
    
    private class p1Move extends Thread implements KeyListener{
    	public void run(){
    		addKeyListener(this);
    			while(true){
    				try{//see if you can move in the various directions
    					if(tilestatus[player1xpos-1][player1ypos]==3 || tilestatus[player1xpos-1][player1ypos]==7 || tilestatus[player1xpos-1][player1ypos]==10 || tilestatus[player1xpos-1][player1ypos]==11){
    						p1left = true;
    					}
    					else{
    						p1left = false;
    					}
    					if(tilestatus[player1xpos][player1ypos+1]==3 || tilestatus[player1xpos][player1ypos+1]==7 || tilestatus[player1xpos][player1ypos+1]==10 || tilestatus[player1xpos][player1ypos+1]==11){
    						p1down = true;
    					}
    					else{
    						p1down = false;
    					}
    					if(tilestatus[player1xpos+1][player1ypos]==3 || tilestatus[player1xpos+1][player1ypos]==7 || tilestatus[player1xpos+1][player1ypos]==10 || tilestatus[player1xpos+1][player1ypos]==11){
    						p1right = true;
    					}
    					else{
    						p1right = false;
    					}
    					if(tilestatus[player1xpos][player1ypos-1]==3 || tilestatus[player1xpos][player1ypos-1]==7 || tilestatus[player1xpos][player1ypos-1]==10 || tilestatus[player1xpos][player1ypos-1]==11){
    						p1up = true;
    					}
    					else{
    						p1up = false;
    					}
    					if(tilestatus[player1xpos][player1ypos]==7 && p1lifecooldown==false){
    						p1life = p1life - 1;
    						p1lifecooldown = true;
    						Timer timerp1life = new Timer(1100, new ActionListener(){
    		    				@Override
    		    				public void actionPerformed(ActionEvent ae){
    		    					p1lifecooldown = false;
    		    				}
    		    			});
    		    			timerp1life.setRepeats(false);
    		    			timerp1life.start();
    					}
    					if(tilestatus[player1xpos][player1ypos]==10){
    						p1bombmod = p1bombmod + 1;
    						tilestatus[player1xpos][player1ypos] = 3;
    					}
    					if(tilestatus[player1xpos][player1ypos]==11){
    						p1powermod = p1powermod + 1;
    						tilestatus[player1xpos][player1ypos] = 3;
    					}
    					if(p1life == 0){
    						JOptionPane.showMessageDialog(null, "Player 1 has died! Player 2 wins!");
    						System.exit(0);
    					}
    					Thread.sleep(1000/fps);
    				}
    				catch(Exception e){
    					break;
    				}
    			}
    	}
    	public void keyPressed(KeyEvent event){
    		if(event.getKeyCode() == KeyEvent.VK_W && p1up == true){
    			p1left = false;//these 3 falses are so that you can't glitch the system by inputting two moves before they can be processed
    			p1down = false;
    			p1right = false;
    			player1ypos = player1ypos - 1;
    			player1.y = player1ypos*32+52;
    			p1lastface = 2;
    			repaint();
    		}
    		if(event.getKeyCode() == KeyEvent.VK_A && p1left == true){
    			p1up = false;
    			p1down = false;
    			p1right = false;
    			player1xpos = player1xpos - 1;
    			player1.x = player1xpos*32+32;
    			p1lastface = 1;
    			repaint();
    		}
    		if(event.getKeyCode() == KeyEvent.VK_S && p1down == true){
    			p1left = false;
    			p1up = false;
    			p1right = false;
   				player1ypos = player1ypos + 1;
   				player1.y = player1ypos*32+52;
   				p1lastface = 0;
   				repaint();
    		}
    		if(event.getKeyCode() == KeyEvent.VK_D && p1right == true){
    			p1left = false;
    			p1down = false;
    			p1up = false;
   				player1xpos = player1xpos + 1;
   				player1.x = player1xpos*32+32;
   				p1lastface = 3;
   				repaint();
    		}
    		if(event.getKeyCode() == KeyEvent.VK_W){//direction the dude is facing
    			p1lastface = 2;
   				repaint();
    		}
    		if(event.getKeyCode() == KeyEvent.VK_A){
    			p1lastface = 1;
   				repaint();
    		}
    		if(event.getKeyCode() == KeyEvent.VK_S){
    			p1lastface = 0;
   				repaint();
    		}
    		if(event.getKeyCode() == KeyEvent.VK_D){
    			p1lastface = 3;
   				repaint();
    		}
    		if(event.getKeyCode() == KeyEvent.VK_Q && tilestatus[player1xpos][player1ypos]==3){
    			bomb(player1xpos, player1ypos, 0, p1powermod);
    			repaint();
    		}
    	}
    	public void keyReleased(KeyEvent event){//these are just here because they have to be
    		
    	}
    	public void keyTyped(KeyEvent event){
    		
    	}
    }
    
    private class p2Move extends Thread implements KeyListener{
    	public void run(){
    		addKeyListener(this);
    			while(true){
    				try{
    					if(tilestatus[player2xpos-1][player2ypos]==3 || tilestatus[player2xpos-1][player2ypos]==7 || tilestatus[player2xpos-1][player2ypos]==10 || tilestatus[player2xpos-1][player2ypos]==11){
    						p2left = true;
    					}
    					else{
    						p2left = false;
    					}
    					if(tilestatus[player2xpos][player2ypos+1]==3 || tilestatus[player2xpos][player2ypos+1]==7 || tilestatus[player2xpos][player2ypos+1]==10 || tilestatus[player2xpos][player2ypos+1]==11){
    						p2down = true;
    					}
    					else{
    						p2down = false;
    					}
    					if(tilestatus[player2xpos+1][player2ypos]==3 || tilestatus[player2xpos+1][player2ypos]==7 || tilestatus[player2xpos+1][player2ypos]==10 || tilestatus[player2xpos+1][player2ypos]==11){
    						p2right = true;
    					}
    					else{
    						p2right = false;
    					}
    					if(tilestatus[player2xpos][player2ypos-1]==3 || tilestatus[player2xpos][player2ypos-1]==7 || tilestatus[player2xpos][player2ypos-1]==10 || tilestatus[player2xpos][player2ypos-1]==11){
    						p2up = true;
    					}
    					else{
    						p2up = false;
    					}
    					if(tilestatus[player2xpos][player2ypos]==7 && p2lifecooldown==false){
    						p2life = p2life - 1;
    						p2lifecooldown = true;
    						Timer timerp2life = new Timer(1100, new ActionListener(){
    		    				@Override
    		    				public void actionPerformed(ActionEvent ae){
    		    					p2lifecooldown = false;
    		    				}
    		    			});
    		    			timerp2life.setRepeats(false);
    		    			timerp2life.start();
    					}
    					if(tilestatus[player2xpos][player2ypos]==10){
    						tilestatus[player2xpos][player2ypos] = 3;
    						p2bombmod = p2bombmod + 1;
    					}
    					if(tilestatus[player2xpos][player2ypos]==11){
    						tilestatus[player2xpos][player2ypos] = 3;
    						p2powermod = p2powermod + 1;
    					}
    					if(p2life == 0){
    						JOptionPane.showMessageDialog(null, "Player 2 has died! Player 1 wins!");
    						System.exit(0);
    					}
    					Thread.sleep(1000/fps);
    				}
    				catch(Exception e){
    					break;
    				}
    			}
    	}
    	public void keyPressed(KeyEvent event){
    		if(event.getKeyCode() == KeyEvent.VK_UP && p2up == true){
    			p2left = false;
    			p2right = false;
    			p2down = false;
    			player2ypos = player2ypos - 1;
    			player2.y = player2ypos*32+52;
    			repaint();
    		}
    		if(event.getKeyCode() == KeyEvent.VK_LEFT && p2left == true){
    			p2up = false;
    			p2right = false;
    			p2down = false;
    			player2xpos = player2xpos - 1;
    			player2.x = player2xpos*32+32;
    			repaint();
    		}
    		if(event.getKeyCode() == KeyEvent.VK_DOWN && p2down == true){
    			p2left = false;
    			p2right = false;
    			p2up = false;
   				player2ypos = player2ypos + 1;
   				player2.y = player2ypos*32+52;
   				repaint();
    		}
    		if(event.getKeyCode() == KeyEvent.VK_RIGHT && p2right == true){
    			p2left = false;
    			p2up = false;
    			p2down = false;
   				player2xpos = player2xpos + 1;
   				player2.x = player2xpos*32+32;
   				repaint();
    		}
    		if(event.getKeyCode() == KeyEvent.VK_UP){
    			p2lastface = 2;
   				repaint();
    		}
    		if(event.getKeyCode() == KeyEvent.VK_LEFT){
    			p2lastface = 1;
   				repaint();
    		}
    		if(event.getKeyCode() == KeyEvent.VK_DOWN){
    			p2lastface = 0;
   				repaint();
    		}
    		if(event.getKeyCode() == KeyEvent.VK_RIGHT){
    			p2lastface = 3;
   				repaint();
    		}
    		if(event.getKeyCode() == KeyEvent.VK_SLASH && tilestatus[player2xpos][player2ypos]==3){
    			bomb(player2xpos, player2ypos, 1, p2powermod);
    			repaint();
    		}
    	}
    	public void keyReleased(KeyEvent event){
    		
    	}
    	public void keyTyped(KeyEvent event){
    		
    	}
    }
    
    void bomb(final int xpos, final int ypos, int player, final int powermod){
    	if(player == 0){
    		bombs = p1bombmod;
    		if(p1bombmod >= 1){
    			p1bombmod = p1bombmod - 1;
    			Timer timer1 = new Timer(3000, new ActionListener(){
    				@Override
    				public void actionPerformed(ActionEvent ae){
    					p1bombmod = p1bombmod + 1;
    				}
    			});
    			timer1.setRepeats(false);
    			timer1.start();
    		}
    	}
    	if(player == 1){
    		bombs = p2bombmod;
    		if(p2bombmod >= 1){
    			p2bombmod = p2bombmod - 1;
    			Timer timer2 = new Timer(3000, new ActionListener(){
    				@Override
    				public void actionPerformed(ActionEvent ae){
    					p2bombmod = p2bombmod + 1;
    				}
    			});
    			timer2.setRepeats(false);
    			timer2.start();
    		}
    	}
    	if(bombs > 0){//this is the confusing part, the actual bombs and exploding.
    		tilestatus[xpos][ypos] = 4; //I basically added a whole lot of timers to keep track of bombs.
    		Timer timer = new Timer(500, new ActionListener(){
    			@Override
    			public void actionPerformed(ActionEvent ae){
    				tilestatus[xpos][ypos] = 5;
    				repaint();
    			}
    		});
    		timer.setRepeats(false);
    		timer.start();
    		Timer timer3 = new Timer(1000, new ActionListener(){
    			@Override
    			public void actionPerformed(ActionEvent ae){
    				if(tilestatus[xpos][ypos]==5){
    					tilestatus[xpos][ypos] = 4;
    				}
    				repaint();
    			}
    		});
    		timer3.setRepeats(false);
    		timer3.start();
    		Timer timer4 = new Timer(1500, new ActionListener(){
    			@Override
    			public void actionPerformed(ActionEvent ae){
    				if(tilestatus[xpos][ypos]==4){
    					tilestatus[xpos][ypos] = 6;
    				}
    				repaint();
    			}
    		});
    		timer4.setRepeats(false);
    		timer4.start();
    		Timer timer5 = new Timer(2000, new ActionListener(){
    			@Override
    			public void actionPerformed(ActionEvent ae){
    				if(tilestatus[xpos][ypos]==6){
    					tilestatus[xpos][ypos] = 4;
    				}
    				repaint();
    			}
    		});
    		timer5.setRepeats(false);
    		timer5.start();
    		Timer timer6 = new Timer(2500, new ActionListener(){
    			@Override
    			public void actionPerformed(ActionEvent ae){
    				if(tilestatus[xpos][ypos]==4){
    					tilestatus[xpos][ypos] = 5;
    				}
    				repaint();
    			}
    		});
    		timer6.setRepeats(false);
    		timer6.start();
    		Timer timer7 = new Timer(3000, new ActionListener(){
    			@Override
    			public void actionPerformed(ActionEvent ae){
    				boolean blocked1 = false, blocked2 = false, blocked3 = false, blocked4 = false;
    				if(tilestatus[xpos][ypos]==5){
    				for(int x = 0; x < powermod+1; x++){
    					for(int y = 0; y < powermod+1; y++){
    						if(tilestatus[xpos+x][ypos]== 1){
    							blocked1 = true;
    						}
    						if((tilestatus[xpos+x][ypos]== 2 || tilestatus [xpos+x][ypos]== 3 || tilestatus [xpos+x][ypos]== 10 || tilestatus [xpos+x][ypos]== 11 || tilestatus [xpos+x][ypos]== 4 || tilestatus [xpos+x][ypos]== 5 || tilestatus [xpos+x][ypos]== 6) && blocked1 == false){
    							if(tilestatus[xpos+x][ypos]== 2){
    								Random rand = new Random();
    			    				int randomNum = rand.nextInt((10 - 1) + 1) + 1;
    			    				if(randomNum==1){
    			    					tilestatus[xpos+x][ypos] = 8;
    			    				}
    			    				else if(randomNum==2){
    			    					tilestatus[xpos+x][ypos] = 9;
    			    				}
    			    				else{
    			    					tilestatus[xpos+x][ypos] = 7;
    			    				}
    							}
    							else if(tilestatus [xpos+x][ypos]== 4 || tilestatus [xpos+x][ypos]== 5 || tilestatus [xpos+x][ypos]== 6){
    								tilestatus[xpos+x][ypos] = 12;
    							}
    							else{
    							tilestatus[xpos+x][ypos] = 7;
    							}
    						}
    						if(tilestatus[Math.abs(xpos-x)][ypos]== 1){
    							blocked2 = true;
    						}
    						if((tilestatus[Math.abs(xpos-x)][ypos]== 2 || tilestatus[Math.abs(xpos-x)][ypos]== 3 || tilestatus[Math.abs(xpos-x)][ypos]== 10 || tilestatus[Math.abs(xpos-x)][ypos]== 11 || tilestatus[Math.abs(xpos-x)][ypos]== 4 || tilestatus[Math.abs(xpos-x)][ypos]== 5 || tilestatus[Math.abs(xpos-x)][ypos]== 6) && blocked2 == false){
    							if(tilestatus[Math.abs(xpos-x)][ypos]== 2){
    								Random rand = new Random();
    			    				int randomNum = rand.nextInt((10 - 1) + 1) + 1;
    			    				if(randomNum==1){
    			    					tilestatus[Math.abs(xpos-x)][ypos] = 8;
    			    				}
    			    				else if(randomNum==2){
    			    					tilestatus[Math.abs(xpos-x)][ypos] = 9;
    			    				}
    			    				else{
    			    					tilestatus[Math.abs(xpos-x)][ypos] = 7;
    			    				}
    							}
    							else if(tilestatus [Math.abs(xpos-x)][ypos]== 4 || tilestatus [Math.abs(xpos-x)][ypos]== 5 || tilestatus [Math.abs(xpos-x)][ypos]== 6){
    								tilestatus[Math.abs(xpos-x)][ypos] = 12;
    							}
    							else{
    							tilestatus[Math.abs(xpos-x)][ypos] = 7;
    							}
    						}
    						if(tilestatus[xpos][Math.abs(ypos-y)]== 1){
    							blocked3 = true;
    						}
    						if((tilestatus[xpos][Math.abs(ypos-y)]== 2 || tilestatus[xpos][Math.abs(ypos-y)]== 3 || tilestatus[xpos][Math.abs(ypos-y)]== 10 || tilestatus[xpos][Math.abs(ypos-y)]== 11 || tilestatus[xpos][Math.abs(ypos-y)]== 4 || tilestatus[xpos][Math.abs(ypos-y)]== 5 || tilestatus[xpos][Math.abs(ypos-y)]== 6) && blocked3 == false){
    							if(tilestatus[xpos][Math.abs(ypos-y)]== 2){
    								Random rand = new Random();
    			    				int randomNum = rand.nextInt((10 - 1) + 1) + 1;
    			    				if(randomNum==1){
    			    					tilestatus[xpos][Math.abs(ypos-y)] = 8;
    			    				}
    			    				else if(randomNum==2){
    			    					tilestatus[xpos][Math.abs(ypos-y)] = 9;
    			    				}
    			    				else{
    			    					tilestatus[xpos][Math.abs(ypos-y)] = 7;
    			    				}
    							}
    							else if(tilestatus [xpos][Math.abs(ypos-y)]== 4 || tilestatus [xpos][Math.abs(ypos-y)]== 5 || tilestatus [xpos][Math.abs(ypos-y)]== 6){
    								tilestatus[xpos][Math.abs(ypos-y)] = 12;
    							}
    							else{
    							tilestatus[xpos][Math.abs(ypos-y)] = 7;
    							}
    						}
    						if(tilestatus[xpos][ypos+y]== 1){
    							blocked4 = true;
    						}
    						if((tilestatus[xpos][ypos+y]== 2 || tilestatus[xpos][ypos+y]== 3 || tilestatus[xpos][ypos+y]== 10 || tilestatus[xpos][ypos+y]== 11 || tilestatus[xpos][ypos+y]== 4 || tilestatus[xpos][ypos+y]== 5 || tilestatus[xpos][ypos+y]== 6) && blocked4 == false){
    							if(tilestatus[xpos][ypos+y]== 2){
    								Random rand = new Random();
    			    				int randomNum = rand.nextInt((10 - 1) + 1) + 1;
    			    				if(randomNum==1){
    			    					tilestatus[xpos][ypos+y] = 8;
    			    				}
    			    				else if(randomNum==2){
    			    					tilestatus[xpos][ypos+y] = 9;
    			    				}
    			    				else{
    			    					tilestatus[xpos][ypos+y] = 7;
    			    				}
    							}
    							else if(tilestatus [xpos][ypos+y]== 4 || tilestatus [xpos][ypos+y]== 5 || tilestatus [xpos][ypos+y]== 6){
    								tilestatus[xpos][ypos+y] = 12;
    							}
    							else{
    							tilestatus[xpos][ypos+y] = 7;
    							}
    						}
    					}
    				}
    				tilestatus[xpos][ypos] = 7;
    				repaint();
    			}
    			}
    		});
    		timer7.setRepeats(false);
    		timer7.start();
    		Timer timerf = new Timer(4000, new ActionListener(){
    			@Override
    			public void actionPerformed(ActionEvent ae){
    				if(tilestatus[xpos][ypos]==7){
    				for(int x = 0; x < powermod+1; x++){
    					for(int y = 0; y < powermod+1; y++){
    						if(tilestatus[xpos+x][ypos]== 7){
    							tilestatus[xpos+x][ypos] = 3;
    						}
    						else if(tilestatus[xpos+x][ypos]== 8){
    							tilestatus[xpos+x][ypos] = 10;
    						}
    						else if(tilestatus[xpos+x][ypos]== 9){
    							tilestatus[xpos+x][ypos] = 11;
    						}
    						if(tilestatus[Math.abs(xpos-x)][ypos]== 7){
    							tilestatus[Math.abs(xpos-x)][ypos] = 3;
    						}
    						else if(tilestatus[Math.abs(xpos-x)][ypos]== 8){
    							tilestatus[Math.abs(xpos-x)][ypos] = 10;
    						}
    						else if(tilestatus[Math.abs(xpos-x)][ypos]== 9){
    							tilestatus[Math.abs(xpos-x)][ypos] = 11;
    						}
    						if(tilestatus[xpos][Math.abs(ypos-y)]== 7){
    							tilestatus[xpos][Math.abs(ypos-y)] = 3;
    						}
    						else if(tilestatus[xpos][Math.abs(ypos-y)]== 8){
    							tilestatus[xpos][Math.abs(ypos-y)] = 10;
    						}
    						else if(tilestatus[xpos][Math.abs(ypos-y)]== 9){
    							tilestatus[xpos][Math.abs(ypos-y)] = 11;
    						}
    						if(tilestatus[xpos][ypos+y]== 7){
    							tilestatus[xpos][ypos+y] = 3;
    						}
    						else if(tilestatus[xpos][ypos+y]== 8){
    							tilestatus[xpos][ypos+y] = 10;
    						}
    						else if(tilestatus[xpos][ypos+y]== 9){
    							tilestatus[xpos][ypos+y] = 11;
    						}
    					}
    				}
    				tilestatus[xpos][ypos] = 3;
    				repaint();
    			}
    			}
    		});
    		timerf.setRepeats(false);
    		timerf.start();
    		Timer timere = new Timer(1000/fps, new ActionListener(){
    			@Override
    			public void actionPerformed(ActionEvent ae){
    				boolean blocked1 = false, blocked2 = false, blocked3 = false, blocked4 = false;
    				if(tilestatus[xpos][ypos]==12){
    					for(int x = 0; x < powermod+1; x++){
        					for(int y = 0; y < powermod+1; y++){
        						if(tilestatus[xpos+x][ypos]== 1){
        							blocked1 = true;
        						}
        						if((tilestatus[xpos+x][ypos]== 2 || tilestatus [xpos+x][ypos]== 3 || tilestatus [xpos+x][ypos]== 10 || tilestatus [xpos+x][ypos]== 11 || tilestatus [xpos+x][ypos]== 4 || tilestatus [xpos+x][ypos]== 5 || tilestatus [xpos+x][ypos]== 6) && blocked1 == false){
        							if(tilestatus[xpos+x][ypos]== 2){
        								Random rand = new Random();
        			    				int randomNum = rand.nextInt((10 - 1) + 1) + 1;
        			    				if(randomNum==1){
        			    					tilestatus[xpos+x][ypos] = 8;
        			    				}
        			    				else if(randomNum==2){
        			    					tilestatus[xpos+x][ypos] = 9;
        			    				}
        			    				else{
        			    					tilestatus[xpos+x][ypos] = 7;
        			    				}
        							}
        							else if(tilestatus [xpos+x][ypos]== 4 || tilestatus [xpos+x][ypos]== 5 || tilestatus [xpos+x][ypos]== 6){
        								tilestatus[xpos+x][ypos] = 12;
        							}
        							else{
        							tilestatus[xpos+x][ypos] = 7;
        							}
        						}
        						if(tilestatus[Math.abs(xpos-x)][ypos]== 1){
        							blocked2 = true;
        						}
        						if((tilestatus[Math.abs(xpos-x)][ypos]== 2 || tilestatus[Math.abs(xpos-x)][ypos]== 3 || tilestatus[Math.abs(xpos-x)][ypos]== 10 || tilestatus[Math.abs(xpos-x)][ypos]== 11 || tilestatus[Math.abs(xpos-x)][ypos]== 4 || tilestatus[Math.abs(xpos-x)][ypos]== 5 || tilestatus[Math.abs(xpos-x)][ypos]== 6) && blocked2 == false){
        							if(tilestatus[Math.abs(xpos-x)][ypos]== 2){
        								Random rand = new Random();
        			    				int randomNum = rand.nextInt((10 - 1) + 1) + 1;
        			    				if(randomNum==1){
        			    					tilestatus[Math.abs(xpos-x)][ypos] = 8;
        			    				}
        			    				else if(randomNum==2){
        			    					tilestatus[Math.abs(xpos-x)][ypos] = 9;
        			    				}
        			    				else{
        			    					tilestatus[Math.abs(xpos-x)][ypos] = 7;
        			    				}
        							}
        							else if(tilestatus [Math.abs(xpos-x)][ypos]== 4 || tilestatus [Math.abs(xpos-x)][ypos]== 5 || tilestatus [Math.abs(xpos-x)][ypos]== 6){
        								tilestatus[Math.abs(xpos-x)][ypos] = 12;
        							}
        							else{
        							tilestatus[Math.abs(xpos-x)][ypos] = 7;
        							}
        						}
        						if(tilestatus[xpos][Math.abs(ypos-y)]== 1){
        							blocked3 = true;
        						}
        						if((tilestatus[xpos][Math.abs(ypos-y)]== 2 || tilestatus[xpos][Math.abs(ypos-y)]== 3 || tilestatus[xpos][Math.abs(ypos-y)]== 10 || tilestatus[xpos][Math.abs(ypos-y)]== 11 || tilestatus[xpos][Math.abs(ypos-y)]== 4 || tilestatus[xpos][Math.abs(ypos-y)]== 5 || tilestatus[xpos][Math.abs(ypos-y)]== 6) && blocked3 == false){
        							if(tilestatus[xpos][Math.abs(ypos-y)]== 2){
        								Random rand = new Random();
        			    				int randomNum = rand.nextInt((10 - 1) + 1) + 1;
        			    				if(randomNum==1){
        			    					tilestatus[xpos][Math.abs(ypos-y)] = 8;
        			    				}
        			    				else if(randomNum==2){
        			    					tilestatus[xpos][Math.abs(ypos-y)] = 9;
        			    				}
        			    				else{
        			    					tilestatus[xpos][Math.abs(ypos-y)] = 7;
        			    				}
        							}
        							else if(tilestatus [xpos][Math.abs(ypos-y)]== 4 || tilestatus [xpos][Math.abs(ypos-y)]== 5 || tilestatus [xpos][Math.abs(ypos-y)]== 6){
        								tilestatus[xpos][Math.abs(ypos-y)] = 12;
        							}
        							else{
        							tilestatus[xpos][Math.abs(ypos-y)] = 7;
        							}
        						}
        						if(tilestatus[xpos][ypos+y]== 1){
        							blocked4 = true;
        						}
        						if((tilestatus[xpos][ypos+y]== 2 || tilestatus[xpos][ypos+y]== 3 || tilestatus[xpos][ypos+y]== 10 || tilestatus[xpos][ypos+y]== 11 || tilestatus[xpos][ypos+y]== 4 || tilestatus[xpos][ypos+y]== 5 || tilestatus[xpos][ypos+y]== 6) && blocked4 == false){
        							if(tilestatus[xpos][ypos+y]== 2){
        								Random rand = new Random();
        			    				int randomNum = rand.nextInt((10 - 1) + 1) + 1;
        			    				if(randomNum==1){
        			    					tilestatus[xpos][ypos+y] = 8;
        			    				}
        			    				else if(randomNum==2){
        			    					tilestatus[xpos][ypos+y] = 9;
        			    				}
        			    				else{
        			    					tilestatus[xpos][ypos+y] = 7;
        			    				}
        							}
        							else if(tilestatus [xpos][ypos+y]== 4 || tilestatus [xpos][ypos+y]== 5 || tilestatus [xpos][ypos+y]== 6){
        								tilestatus[xpos][ypos+y] = 12;
        							}
        							else{
        							tilestatus[xpos][ypos+y] = 7;
        							}
        						}
        					}
        				}
        				tilestatus[xpos][ypos] = 7;
        				repaint();
        			Timer timer0 = new Timer(1000, new ActionListener(){
        				@Override
            			public void actionPerformed(ActionEvent ae){
        					for(int x = 0; x < powermod+1; x++){
            					for(int y = 0; y < powermod+1; y++){
            						if(tilestatus[xpos+x][ypos]== 7){
            							tilestatus[xpos+x][ypos] = 3;
            						}
            						else if(tilestatus[xpos+x][ypos]== 8){
            							tilestatus[xpos+x][ypos] = 10;
            						}
            						else if(tilestatus[xpos+x][ypos]== 9){
            							tilestatus[xpos+x][ypos] = 11;
            						}
            						if(tilestatus[Math.abs(xpos-x)][ypos]== 7){
            							tilestatus[Math.abs(xpos-x)][ypos] = 3;
            						}
            						else if(tilestatus[Math.abs(xpos-x)][ypos]== 8){
            							tilestatus[Math.abs(xpos-x)][ypos] = 10;
            						}
            						else if(tilestatus[Math.abs(xpos-x)][ypos]== 9){
            							tilestatus[Math.abs(xpos-x)][ypos] = 11;
            						}
            						if(tilestatus[xpos][Math.abs(ypos-y)]== 7){
            							tilestatus[xpos][Math.abs(ypos-y)] = 3;
            						}
            						else if(tilestatus[xpos][Math.abs(ypos-y)]== 8){
            							tilestatus[xpos][Math.abs(ypos-y)] = 10;
            						}
            						else if(tilestatus[xpos][Math.abs(ypos-y)]== 9){
            							tilestatus[xpos][Math.abs(ypos-y)] = 11;
            						}
            						if(tilestatus[xpos][ypos+y]== 7){
            							tilestatus[xpos][ypos+y] = 3;
            						}
            						else if(tilestatus[xpos][ypos+y]== 8){
            							tilestatus[xpos][ypos+y] = 10;
            						}
            						else if(tilestatus[xpos][ypos+y]== 9){
            							tilestatus[xpos][ypos+y] = 11;
            						}
            					}
            				}
        					tilestatus[xpos][ypos] = 3;
            				repaint();
        				}
        			});
        			timer0.setRepeats(false);
        			timer0.start();
        			}
    			}
    		});
    		timere.setRepeats(true);
    		timere.start();
    	}
    }
    
	public static void main(String[] args) {
		new Main();
	}
}