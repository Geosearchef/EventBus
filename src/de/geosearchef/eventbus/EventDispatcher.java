package de.geosearchef.eventbus;

import java.lang.reflect.Method;

abstract class EventDispatcher {
	
	protected Method method;
	
	public EventDispatcher(Method method) {
		this.method = method;
		
		if(! method.isAccessible())
			method.setAccessible(true);
	}
	
	Class<?> getDeclaringClass() {
		return method.getDeclaringClass();
	}
	
	abstract void invoke(Object event, Object[] parameters);
}
