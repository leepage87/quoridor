package test.gui_test;

import static src.ui.GameBoardWithButtons.*;

import javax.swing.JButton;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.text.IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import src.main.Board;
import src.ui.FirstWindow;
import src.ui.GameBoardWithButtons;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.ComponentDriver;
import com.objogate.wl.swing.driver.JButtonDriver;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;


public class GameBoardTest {



	JFrameDriver driver;
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		Board b = new Board (2);
		GameBoardWithButtons game = new GameBoardWithButtons(b);
		driver = new JFrameDriver(new GesturePerformer(), new AWTEventQueueProber(), 
				JFrameDriver.named(MAIN_WINDOW_TITLE), JFrameDriver.showingOnScreen());
	}

	@After
	public void tearDown() throws Exception {
		driver.dispose();
	}

	@SuppressWarnings("unchecked")
	private JButtonDriver button(String name) {
		return new JButtonDriver(driver, JButton.class, ComponentDriver.named(name));
	}

	@SuppressWarnings("unchecked")
	private JLabelDriver label(String name) {
		return new JLabelDriver(driver, ComponentDriver.named(name));
	}


	@Test
	public void windowUpWithTitle() {
		driver.hasTitle(MAIN_WINDOW_TITLE);
	}
	@Test
	public void playerOneMovementTest() {

			String buttonName = "Button"+ 4 + "" +1;
			JButtonDriver button = button(buttonName);
			button.click();
			

	}
	/*
	@Test
	public void windowContainsButtons() {
		for (int i = 0; i < 9; ++i) {
			for (int j = 0; j < 9; j++)	{
			String buttonName = "Button"+ i + "" +j;
			JButtonDriver button = button(buttonName);
			button.click();
			}
		}
	}
	 */
	/*@Test
	public void windowContainsVerticalWalls() {
		for (int i = 1; i < 9; i++)	{
			for (int j = 1 + i%2; j < 9; j+=2)	{
				String wallName = "VertWall" + i + "" + j;
				JButtonDriver button = button(wallName);
				button.click();
			}
		}
	}*/
/*
	@Test
	public void windowContainsHorizontalWalls() {
		for (int i = 1; i < 9; i++) {
			for (int j = 1 + i%2; j < 9; j+=2) {
				String wallName = "HorzWall" + i + "" + j;
				JButtonDriver button = button(wallName);
				button.click();
			}
		}
	}
*/
}
/*
	@Test
	public void labelChangesWithButtonPushes() {
		JLabelDriver label = label(LABEL_NAME);

		label.hasText(equalTo(INITIAL_MESSAGE));
		for (int i = 0; i < BUTTON_TEXTS.length; ++i) {
			String buttonName = String.format("%s%d", BUTTON_NAME_PREFIX, i);
			JButtonDriver button = button(buttonName);
			button.click();
			label.hasText(equalTo(MESSAGES[i]));
		}

		// Make sure they can go back to previous values
		for (int i = 0; i < BUTTON_TEXTS.length; ++i) {
			String buttonName = String.format("%s%d", BUTTON_NAME_PREFIX, i);
			JButtonDriver button = button(buttonName);
			button.click();
			label.hasText(equalTo(MESSAGES[i]));
		}
	}	
 */

