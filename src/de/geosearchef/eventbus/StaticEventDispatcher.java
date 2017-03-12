package de.geosearchef.eventbus;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

class StaticEventDispatcher extends EventDispatcher {
	
	StaticEventDispatcher(Method method) {
		super(method);
	}
	
	@Override
	public void invoke(Object event) {
		CompletableFuture.runAsync(() -> {
			try {
				method.invoke(null, event);
			} catch(Exception e) {e.printStackTrace();}
		});
	}
}
