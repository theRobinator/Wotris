import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class wotris extends JPanel implements KeyListener {
	private static final long serialVersionUID = 1L;
	public static final int width = 13, height = 22,blocksize = 23;
	public static int blocks;
	public static int[][] board, active, current, next;
	public static Random rand;
	public static boolean paused, dead;
	
	public static int score, level, lines, speed;
	
	public static Container pane;
	public static final JLabel scorefield = new JLabel("0"), levelfield = new JLabel("1"), linefield = new JLabel("0");
	public static final JRadioButton fourb = new JRadioButton("4"), fiveb = new JRadioButton("5");
	public static Sequencer music = null;
	public static Clip sound = null;
	public static AudioInputStream ais = null;
	
	public static wotris screen;
	public static prepiece preview;
	
	public static final int[][][] possibles4 = {
		new int[][]{
			new int[]{0,0}, //#
			new int[]{0,1}, //##
			new int[]{1,1}, // #
			new int[]{1,2}
		},
		new int[][] {
			new int[]{0,0}, //##
			new int[]{0,1}, //##
			new int[]{1,0},
			new int[]{1,1}
		},
		new int[][] {
			new int[]{0,0}, //#
			new int[]{0,1}, //###
			new int[]{1,1},
			new int[]{2,1}
		},
		new int[][] {
			new int[]{1,0}, // #
			new int[]{0,1}, //##
			new int[]{1,1}, //#
			new int[]{0,2}
		},
		new int[][] {
			new int[]{0,2}, //  #
			new int[]{0,1}, //###
			new int[]{1,1},
			new int[]{2,1}
		},
		new int[][] {
			new int[]{0,0}, //####
			new int[]{1,0},
			new int[]{2,0},
			new int[]{3,0}
		},
		new int[][] {
			new int[]{0,1}, // #
			new int[]{1,0}, //###
			new int[]{1,1},
			new int[]{2,1}
		}
	};
	public static final int[][][] possibles5 = {
		new int[][] {
			new int[]{0,1},//   ##
			new int[]{1,1},// ###
			new int[]{2,0},
			new int[]{2,1},
			new int[]{3,0}
		},
		new int[][] {
			new int[]{0,2},//  ##
			new int[]{1,1},// ##
			new int[]{1,2},// #
			new int[]{2,1},
			new int[]{2,0}
		},
		new int[][] {
			new int[]{0,1},//  #
			new int[]{1,0},// ###
			new int[]{1,1},//   #
			new int[]{2,1},
			new int[]{2,2}
		},
		new int[][] {
			new int[]{0,1},//  ##
			new int[]{1,0},// ###
			new int[]{1,1},
			new int[]{2,1},
			new int[]{2,0}
		},
		new int[][] {
			new int[]{0,0},// #
			new int[]{0,1},// #
			new int[]{0,2},// ###
			new int[]{1,2},
			new int[]{2,2}
		},
		new int[][] {
			new int[]{0,0},// ##
			new int[]{1,0},//  ###
			new int[]{1,1},
			new int[]{2,1},
			new int[]{3,1}
		},
		new int[][] {
			new int[]{0,1},//  # 
			new int[]{1,1},// ####
			new int[]{1,0},
			new int[]{2,1},
			new int[]{3,1}
		},
		new int[][] {
			new int[]{0,2},//  #
			new int[]{1,0},//  #
			new int[]{1,1},// ###
			new int[]{1,2},
			new int[]{2,2}
		},
		new int[][] {
			new int[]{0,0},// #
			new int[]{0,1},// ###
			new int[]{1,1},//   #
			new int[]{2,1},
			new int[]{2,2}
		},
		new int[][] {
			new int[]{0,0},// #
			new int[]{0,1},// ####
			new int[]{1,1},
			new int[]{2,1},
			new int[]{3,1}
		},
		new int[][] {
			new int[]{0,0},// # #
			new int[]{0,1},// ###
			new int[]{1,1},
			new int[]{2,0},
			new int[]{2,1}
		},
		new int[][] {
			new int[]{0,1},//  #
			new int[]{0,2},// ###
			new int[]{1,0},// #
			new int[]{1,1},
			new int[]{2,1}
		},
		new int[][] {
			new int[]{0,1},//    #
			new int[]{1,1},// ####
			new int[]{2,1},
			new int[]{3,0},
			new int[]{3,1}
		},
		new int[][] {
			new int[]{0,1},//   #
			new int[]{0,2},// ###
			new int[]{1,1},// #
			new int[]{2,1},
			new int[]{2,0}
		},
		new int[][] {
			new int[]{0,1},//   #
			new int[]{1,1},// ####
			new int[]{2,0},
			new int[]{2,1},
			new int[]{3,1}
		},
		new int[][] {
			new int[]{0,0},// #####
			new int[]{1,0},
			new int[]{2,0},
			new int[]{3,0},
			new int[]{4,0}
		},
		new int[][] {
			new int[]{0,1},//  #
			new int[]{1,0},// ###
			new int[]{1,1},//  #
			new int[]{1,2},
			new int[]{2,1}
		},
	};
	public static int[][][] possibles = possibles4;
	
	public static void main(String[] args) {
		//Initializations
		blocks = 4;
		board = new int[width][height];
		active = new int[blocks][2];
		current = new int[blocks][2];
		next = new int[blocks][2];
		rand = new Random(System.nanoTime());
		paused = dead = true;
		
		score = 0;
		level = 1;
		lines = 0;
		speed = 800;
		
		pane = null;
		music = null;
		sound = null;
		ais = null;
		possibles = possibles4;
		
		//Actual Code
		JFrame frame = null;
		if (args != null) {
			frame = new JFrame("Wotris");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			pane = frame.getContentPane();
		}else {
			pane = new Container();
			pane.setLayout(new BorderLayout());
		}
		try {
			music = MidiSystem.getSequencer();
			music.setSequence(MidiSystem.getSequence(new dropper().getClass().getResource("/song.mid")));
			music.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			music.open();
		}catch (Exception e) {
			music = null;
			e.printStackTrace();
		}
		
		if (args == null) pane.setBackground(Color.LIGHT_GRAY);
		
		screen = new wotris();
		screen.setPreferredSize(new Dimension(330,540));
		screen.setSize(330,540);
		screen.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent arg0) {
				screen.requestFocusInWindow();
			}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
		});
		if (args == null) screen.setBackground(Color.LIGHT_GRAY);
		pane.add(screen,BorderLayout.WEST);
		
		next = possibles[rand.nextInt(possibles.length)];
		
		JPanel stats = new JPanel(new GridBagLayout());
		if (args == null) stats.setBackground(Color.LIGHT_GRAY);
		Font f = new Font("monospaced",Font.PLAIN,12);
		GridBagConstraints c = new GridBagConstraints();
		JLabel lab;
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		lab = new JLabel("Blocks per piece:");
		lab.setFont(f);
		stats.add(lab,c);
		
		ButtonGroup grp = new ButtonGroup();
		grp.add(fourb);
		grp.add(fiveb);
		fourb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg) {
				possibles = possibles4;
				blocks = 4;
				active = new int[4][2]; 
				current = new int[4][2];
				next = new int[4][2];
				next = possibles[rand.nextInt(possibles.length)];
				preview.repaint();
				screen.requestFocusInWindow();
			}
		});
		fiveb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg) {
				possibles = possibles5;
				blocks = 5;
				active = new int[5][2]; 
				current = new int[5][2];
				next = new int[5][2];
				next = possibles[rand.nextInt(possibles.length)];
				preview.repaint();
				screen.requestFocusInWindow();
			}
		});
		if (args == null) {
			fourb.setBackground(Color.LIGHT_GRAY);
			fiveb.setBackground(Color.LIGHT_GRAY);
		}
		c.gridy = 1;
		c.gridwidth = 1;
		fourb.setFont(f);
		fourb.setSelected(true);
		stats.add(fourb,c);
		c.gridx = 1;
		fiveb.setFont(f);
		stats.add(fiveb,c);
		
		c.gridy = 2;
		c.gridx = 0;
		stats.add(new JLabel(" "),c);
		c.gridy = 3;
		lab = new JLabel("Score:");
		lab.setFont(f);
		stats.add(lab,c);
		c.gridx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		scorefield.setFont(f);
		stats.add(scorefield,c);
		c.gridy = 4;
		stats.add(new JLabel(" "),c);
		c.gridy = 5;
		c.gridx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		lab = new JLabel("Level: ");
		lab.setFont(f);
		stats.add(lab,c);
		c.gridx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		levelfield.setFont(f);
		stats.add(levelfield,c);
		c.gridy = 6;
		stats.add(new JLabel(" "),c);
		c.gridy = 7;
		c.gridx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		lab = new JLabel("Lines: ");
		lab.setFont(f);
		stats.add(lab,c);
		c.gridx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		linefield.setFont(f);
		stats.add(linefield,c);
		c.gridy = 8;
		stats.add(new JLabel(" "),c);
		c.gridy = 9;
		c.gridwidth = 2;
		lab = new JLabel("Next:");
		lab.setFont(f);
		c.gridx = 0;
		stats.add(lab,c);
		c.ipady = 155;
		c.gridy = 10;
		c.ipadx = 140;
		c.anchor = GridBagConstraints.CENTER;
		preview = new prepiece();
		preview.setPreferredSize(new Dimension(150,150));
		preview.setSize(150,150);
		stats.add(preview,c);
		c.gridy = 11;
		stats.add(new JLabel(),c);
		pane.add(stats);
		
		screen.addKeyListener(screen);
		screen.requestFocusInWindow();
		
		if (args != null) {
			frame.setSize(490,585);
			frame.setVisible(true);
		}
	}
	
	public void paintChildren(Graphics g) { }
	
	public void paintComponent(Graphics g) {
		Graphics2D gr = (Graphics2D)g;
		if (paused) {
			if (dead) {
				super.paintComponent(g);
				gr.setColor(Color.WHITE);
				gr.fillRect(25,25,300,508);
				
				gr.setFont(new Font("Monospaced",Font.BOLD,48));
				gr.setColor(Color.blue);
				gr.drawString("WOTRIS",88,125);
				gr.setColor(Color.black);
				gr.setFont(new Font("Monospaced",Font.PLAIN,12));
				gr.drawString("because graphics are for pansies", 62,154);
				gr.drawString("arrows = move", 124,218);
				gr.drawString("s = rotate",137,238);
				gr.drawString("d = drop",141,258);
				gr.drawString("ret = play/pause", 115,278);
			}else {
				gr.setColor(Color.DARK_GRAY);
				gr.fillRect(25,25,300,508);
				gr.setFont(new Font("Monospaced",Font.BOLD,36));
				gr.setColor(Color.BLACK);
				gr.drawString("PAUSED",113,240);
			}
		}else {
			if (dead) super.paintComponent(g);
			//Draw the background
			gr.setColor(Color.WHITE);
			gr.fillRect(25,25,300,508);
			
			//Draw the other pieces
			gr.setColor(Color.DARK_GRAY);
			for (int i = 0; i < width; ++i)
				for (int j = 0; j < height; ++j)
					if (board[i][j] != 0) gr.fillRect(i*blocksize+25,j*blocksize+25,blocksize,blocksize);
			
			//Draw the current piece
			gr.setColor(Color.red);
			for (int i = 0; i < blocks; ++i) {
				gr.fillRect(active[i][0]*blocksize+25,active[i][1]*blocksize+25,blocksize,blocksize);
			}
			gr.setColor(Color.black);
			gr.drawRect(25,25,299,507);
			for (int i = 0; i < blocks; ++i) {
				gr.drawRect(active[i][0]*blocksize+25,active[i][1]*blocksize+25,blocksize,blocksize);
			}
			if (dead) {
				gr.setColor(Color.LIGHT_GRAY);
				gr.fillRect(66,240,228,50);
				gr.setColor(Color.black);

				gr.setFont(new Font("Monospaced",Font.BOLD,36));
				
				gr.drawString("GAME OVER",76,275);
			}
		}
	}


	public boolean isFocusable() {
		return true;
	}
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == 83 || code == 38) rotate(); //F or up arrow
		else if (code == 37) moveLeft(); //left arrow
		else if (code == 39) moveRight(); //right arrow
		else if (code == 40) fall(); //down
		else if (code == 68 || code == 32) drop(); //D or space
		else if (code == 10) pause(); //return
	}


	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
	public static void moveLeft() {
		if (dead) return;
		int[][] newpos = new int[blocks][2];
		for (int i = 0; i < blocks; ++i) {
			newpos[i][0] = active[i][0]-1;
			newpos[i][1] = active[i][1];
			if (newpos[i][0] < 0 || board[newpos[i][0]][newpos[i][1]] != 0) return;
		}
		active = newpos;
		screen.repaint(0L,25,25,300,508);
	}
	public static void moveRight() {
		if (dead) return;
		int[][] newpos = new int[blocks][2];
		for (int i = 0; i < blocks; ++i) {
			newpos[i][0] = active[i][0]+1;
			newpos[i][1] = active[i][1];
			if (newpos[i][0] == width || board[newpos[i][0]][newpos[i][1]] != 0) return;
		}
		active = newpos;
		screen.repaint(0L,25,25,300,508);
	}
	public static void drop() {
		int h = 0;
		boolean breaking = false;
		while (true) {
			for (int i = 0; i < blocks; ++i) {
				if (active[i][1]+h >= height || board[active[i][0]][active[i][1]+h] != 0) {
					breaking = true;
					break;
				}
			}
			if (breaking) break;
			++h;
		}
		--h;
		if (h == -1) die();
		for (int i = 0; i < blocks; ++i) {
			active[i][1] += h;
		}
		screen.repaint(0L,25,25,300,508);
	}
	public static void rotate() {
		int[][] newpos = new int[blocks][2], newcurr = new int[blocks][2];
		int xtra = 0, ytra = 0;
		for (int i = 0; i < blocks; ++i) {
			newcurr[i][0] = current[i][1] * -1;
			if (newcurr[i][0] < xtra) xtra = newcurr[i][0];
			newcurr[i][1] = current[i][0];
			if (newcurr[i][1] < ytra) ytra = newcurr[i][1];
			newpos[i][0] = active[i][0]-current[i][0]+newcurr[i][0];
			newpos[i][1] = active[i][1]-current[i][1]+newcurr[i][1];
		}
		for (int i = 0; i < blocks; ++i) {
      if (xtra != 0 || ytra != 0) {
        newcurr[i][0] -= xtra;
        newpos[i][0] -= xtra;
        newcurr[i][1] -= ytra;
        newpos[i][1] -= ytra;
      }
			if (newpos[i][0] >= width || newpos[i][1] >= height || board[newpos[i][0]][newpos[i][1]] != 0) return;
		}
		active = newpos;
		current = newcurr;
		screen.repaint(0L,25,25,300,508);
	}
	public static boolean fall() {
		int[][] newpos = new int[blocks][2];
		boolean cant = false;
		for (int i = 0; i < blocks; ++i) {
			if (active[i][1] == height-1 || board[active[i][0]][active[i][1]+1] != 0) {
				cant = true;
				break;
			}
			newpos[i][0] = active[i][0];
			newpos[i][1] = active[i][1]+1;
		}
		if (!cant)
			active = newpos;
		screen.repaint(0L,25,25,300,508);
		return cant;
	}
	public static void addPiece() {
		current = next;
		next = possibles[rand.nextInt(possibles.length)];
		for (int i = 0; i < blocks; ++i) {
			active[i][0] = current[i][0]+5;
			active[i][1] = current[i][1];
			if (board[active[i][0]][active[i][1]] != 0) {
				die();
			}
		}
		preview.repaint();
		if (dead)
			screen.repaint(0L,66,240,228,50);
		else
			screen.repaint(0L,25,25,300,508);
	}
	public static void finishpiece() {
		for (int i = 0; i < blocks; ++i) {
			board[active[i][0]][active[i][1]] = 1;
		}
		int w, h;
		ArrayList<Integer> gone = new ArrayList<Integer>();
		boolean breaking = false;
		for (int i = 0; i < blocks; ++i) {
			h = active[i][1];
			breaking = false;
			for (w = 0; w < width; ++w) {
				if (board[w][h] == 0) {
					breaking = true;
					break;
				}
			}
			if (breaking) continue;
			gone.add(h);
		}
		if (!gone.isEmpty()) {
			int from = height-1, to = height -1, total = 0;
			for (; to >= 0; --to) {
				while (from >= 0 && gone.contains(from)) {
					from--;
					total++;
				}
				for (w = 0; w < width; ++w) {
					if (from >= 0)
						board[w][to] = board[w][from];
					else
						board[w][to] = 0;
				}
				--from;
			}
			switch (total) {
			case 1 : score += 20; break;
			case 2 : score += 60; break;
			case 3 : score += 100; break;
			case 4 : score += 150; break;
			default : score += 200; break;
			}
			scorefield.setText(score+"");
			lines += total;
			linefield.setText(lines+"");
			if (lines != total && lines / 10 != (lines-total) / 10) {
				level++;
				levelfield.setText(level+"");
				speed -= 25;
				if (music != null) music.setTempoFactor((level-1) / 20f + 1);
			}
		}
	}
	public static void die() {
		dead = true;
		paused = false;
		screen.repaint(0L,25,25,300,508);
		fourb.setEnabled(true);
		fiveb.setEnabled(true);
		if (music != null) {
			music.stop();
			music.setMicrosecondPosition(0L);
		}
		try {
			ais = AudioSystem.getAudioInputStream(new File("gameover.wav"));
			sound = AudioSystem.getClip();
			sound.open(ais);
			sound.start();
		}catch (Exception e) {}
	}
	public static void pause() {
		if (paused) {
			paused = false;
			if (dead) {
				board = new int[width][height];
				addPiece();
				dead = false;
			}
			new dropper().start();
			fourb.setEnabled(false);
			fiveb.setEnabled(false);
			screen.repaint(0L,25,25,300,508);
		}else if (dead) {
			board = new int[width][height];
			addPiece();
			dead = false;
			fourb.setEnabled(false);
			fiveb.setEnabled(false);
			score = lines = 0;
			level = 1;
			scorefield.setText("0");
			linefield.setText("0");
			levelfield.setText("1");
			if (music != null) music.setTempoFactor(1.0f);
			if (sound != null) {
				sound.close();
				try {ais.close();}catch(Exception e) {}
			}
			speed = 800;
			new dropper().start();
			screen.repaint(0L,25,25,300,508);
		}else {
			paused = true;
			if (music != null) music.stop();
			screen.repaint(0L,25,25,300,508);
		}
	}
}
