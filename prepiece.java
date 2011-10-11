import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;


public class prepiece extends JPanel {
	private static final long serialVersionUID = 1L;

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gr = (Graphics2D)g;
		gr.setColor(Color.white);
		gr.fillRect(0,0,150,165);
		gr.setColor(Color.red);
		for (int i = 0; i < wotris.blocks; ++i) {
			gr.fillRect(wotris.next[i][0]*30+5, wotris.next[i][1]*30+15, 30, 30);
		}
		gr.setColor(Color.black);
		for (int i = 0; i < wotris.blocks; ++i) {
			gr.drawRect(wotris.next[i][0]*30+5, wotris.next[i][1]*30+15, 30, 30);
		}
	}

	public void paintChildren(Graphics g) { }
}
