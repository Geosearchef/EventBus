package de.geosearchef.eventbus;

public class UnregisteredEventException extends RuntimeException {
	
	private Object event;
	
	
	public UnregisteredEventException(Object event) {
		this.event = event;
	}
	
	@Override
	public String toString() {
		return "Error posting " + event.toString() + " on event bus, class \"" + event.getClass().getName() + "\" not registered!";
	}
}
