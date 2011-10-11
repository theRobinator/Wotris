
public class dropper extends Thread {

	static boolean newpiece = false;
	
	public void run() {
		if (wotris.music != null) wotris.music.start();
		while (!wotris.dead && !wotris.paused) {
			try {
				Thread.sleep(wotris.speed);
			}catch (InterruptedException e) { }
			newpiece = wotris.fall();
			if (newpiece) {
				wotris.finishpiece();
				wotris.addPiece();
				newpiece = false;
			}
		}
		if (wotris.music != null) wotris.music.stop();
	}

}
