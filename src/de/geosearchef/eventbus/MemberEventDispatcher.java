package de.geosearchef.eventbus;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

class MemberEventDispatcher extends EventDispatcher {

	private List<Object> listeningObjects = new LinkedList<Object>();

	MemberEventDispatcher(Method method) {
		super(method);
	}

	void addObject(Object object) {
		if (!this.listeningObjects.contains(object))
			this.listeningObjects.add(object);
	}

	void removeObject(Object object) {
		this.removeObject(object);
	}

	@Override
	public void invoke(Object event) {
		listeningObjects.forEach(object -> {
			CompletableFuture.runAsync(() -> {
				try {
					method.invoke(object, event);
				} catch(Exception e) {e.printStackTrace();}
			});
		});
	}
}
