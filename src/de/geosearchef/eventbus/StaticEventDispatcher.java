package de.geosearchef.eventbus;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

class StaticEventDispatcher extends EventDispatcher {
	
	StaticEventDispatcher(Method method) {
		super(method);
	}
	
	@Override
	public void invoke(Object event, Object[] parameters) {
		List<Object> parameterList = new LinkedList<>(Arrays.asList(parameters));
		parameterList.add(0, event);
		
		CompletableFuture.runAsync(() -> {
			try {
				method.invoke(null, parameterList.toArray());
			} catch(Exception e) {e.printStackTrace();}
		});
	}
}
