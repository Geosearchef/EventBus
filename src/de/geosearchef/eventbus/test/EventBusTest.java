package de.geosearchef.eventbus.test;

import de.geosearchef.eventbus.EventBus;
import de.geosearchef.eventbus.EventHandler;

public class EventBusTest {
	
	//Registers all static methods in this class
	static {
		EventBus.register(EventBusTest.class);
	}
	
	//Registers all created objects, so they listen to event
	{
		EventBus.register(this);
	}
	
	
	@EventHandler
	public static void onEvent(TestEvent event) {
		System.out.println("Event: " + event.getValue());
	}
	
	@EventHandler
	public static void onShutdown(TestShutdownEvent event) {
		System.out.println("exiting...");
		System.exit(0);
	}
	
	
	@EventHandler
	public void onEventNonStatic(TestEvent event) {
		System.out.println(this.toString() + ": " + event.getValue());
	}
	
	
	
	public static void main(String args[]) {
		//Create object to start listening as well
		new EventBusTest();
		
		EventBus.post(new TestEvent(10));	
		EventBus.post(new TestShutdownEvent());
	}
}
