package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import processing.core.PApplet;
import routeFinder.view.GuiInputInterpretter;

public class GuiInputInterpretterTests {

	GuiInputInterpretter input;
	PApplet applet;
	
	@Before
	public void setUp() throws Exception {
		applet = new PApplet();
	}

	@Test
	public void hueristicSelectionSendsSignal() {
		applet.keyPressed = true;
		fail("Not yet implemented");
	}

}
