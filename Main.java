import javax.swing.JFrame;
import gui.AppFrame;

/**
 * 
 */

/**
 * @author 201575091_Mtileni_MF
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame appframe = new AppFrame("Application name");
		appframe.setSize(1000, 500);
		appframe.setLocationRelativeTo(null);
		appframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		appframe.setVisible(true);

	}

}
