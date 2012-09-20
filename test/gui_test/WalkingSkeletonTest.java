package gui_test;

import static ui.FirstWindow.*;

import javax.swing.JButton;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.text.IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import ui.FirstWindow;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.ComponentDriver;
import com.objogate.wl.swing.driver.JButtonDriver;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

 
public class WalkingSkeletonTest {
	JFrameDriver driver;
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		FirstWindow.main();
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
	public void windowUpWithTitleAndLabel() {
		driver.hasTitle(MAIN_WINDOW_TITLE);
		JLabelDriver label = label(LABEL_NAME);
		label.hasText(equalToIgnoringWhiteSpace(INITIAL_MESSAGE));
	}
	
	@Test
	public void windowContainsButtons() {
		for (int i = 0; i < BUTTON_TEXTS.length; ++i) {
			String buttonName = String.format("Button%d", i);
			JButtonDriver button = button(buttonName);
			button.hasText(equalTo(BUTTON_TEXTS[i]));
		}
	}
	
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

}
