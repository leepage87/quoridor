package gui_test;

import static org.junit.Assert.*;
import static ui.FirstWindow.MAIN_WINDOW_TITLE;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ui.FirstWindow;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;
import com.objogate.wl.Prober;

 
public class WalkingSkeletonTest {
	JFrameDriver driver;
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		FirstWindow.main();
		driver = new JFrameDriver(new GesturePerformer(), new AWTEventQueueProber(), JFrameDriver.named(MAIN_WINDOW_TITLE), JFrameDriver.showingOnScreen());
	}

	@After
	public void tearDown() throws Exception {
		driver.dispose();
	}

	@Test
	public void windowUpWithTitleAndLabel() {
		driver.hasTitle(MAIN_WINDOW_TITLE);
	}

}
