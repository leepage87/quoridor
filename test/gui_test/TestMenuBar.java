package test.gui_test;

import static org.hamcrest.text.IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
//import static src.ui.MenuBarPrototype.*;

import javax.swing.JButton;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.ComponentDriver;
import com.objogate.wl.swing.driver.JButtonDriver;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.driver.JMenuBarDriver;
import com.objogate.wl.swing.driver.JMenuDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

import src.ui.*;
//import src.ui.FirstWindow.*;
public class TestMenuBar{

	// A JFrameDriver is the class that WindowLicker uses to interact with the Java event queue
	JFrameDriver driver;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
	//	MenuBarPrototype.main(); // I can call main if I want
		driver = new JFrameDriver(new GesturePerformer(), new AWTEventQueueProber(), JFrameDriver.named(MAIN_WINDOW_TITLE), JFrameDriver.showingOnScreen());
	}
	
	@After
	public void tearDown() throws Exception {
		driver.dispose();
	}
	/*
    @SuppressWarnings("unchecked")
    private JButtonDriver button(String name) {
        return new JButtonDriver(driver, JButton.class, ComponentDriver.named(name));
    }*/
    
    @SuppressWarnings("unchecked")
    private JMenuBarDriver label(String name) {
        return new JMenuBarDriver(driver, ComponentDriver.named("MenuBar"));
    }
    
	@Test
	public void fileMenuExists() {
		JMenuBarDriver label = label("");
		
	//	label.hasText(equalTo("File"));
	}
}