package de.geosearchef.eventbus;

import java.io.IOException;
import java.lang.reflect.Method;

class StaticEventDispatcher extends EventDispatcher {
	
	StaticEventDispatcher(Method method) {
		super(method);
	}
	
	@Override
	public void invoke(Object event) {
		try {
			method.invoke(null, event);
		} catch(Exception e) {e.printStackTrace();}
	}
}
