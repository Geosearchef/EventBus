package de.geosearchef.eventbus;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


public class EventBus {
	
	private static Map<Class<?>, List<EventDispatcher>> eventHandlersByEvents = new HashMap<Class<?>, List<EventDispatcher>>();
	private static List<Class<?>> registeredClasses = new LinkedList<>();
	
	/**
	 * Register a class to listen on events
	 */
	public static void registerListener(Class<?> clazz) {	
		Stream.of(clazz.getMethods())
		.filter(method -> method.isAnnotationPresent(EventHandler.class) 
				&& method.getParameterCount() > 0)
		.forEach(method -> {
			if(Modifier.isStatic(method.getModifiers()))
				put(method.getParameterTypes()[0], new StaticEventDispatcher(method));
			else
				put(method.getParameterTypes()[0], new MemberEventDispatcher(method));
		});
		
		registeredClasses.add(clazz);
	}
	
	/**
	 * Register an object to listen on events
	 */
	public static void registerObject(Object object) {
		if(! registeredClasses.contains(object.getClass()))
			registerListener(object.getClass());
		
		eventHandlersByEvents.values().stream()
		.flatMap(eventHandlers -> eventHandlers.stream())
		.filter(eventHandler -> object.getClass().equals(eventHandler.getDeclaringClass())
				&& eventHandler instanceof MemberEventDispatcher)
		.forEach(eventHandler -> ((MemberEventDispatcher) eventHandler).addObject(object));
	}
	
	/**
	 * Stop an object from listening on events
	 */
	public static void unregister(Object object) {
		eventHandlersByEvents.values().stream()
		.flatMap(eventHandlers -> eventHandlers.stream())
		.filter(eventHandler -> eventHandler instanceof MemberEventDispatcher)
		.forEach(eventHandler -> ((MemberEventDispatcher) eventHandler).removeObject(object));
	}
	
	private static void put(Class<?> key, EventDispatcher value) {
		if(eventHandlersByEvents.containsKey(key))
			eventHandlersByEvents.get(key).add(value);
		else
			eventHandlersByEvents.put(key, new LinkedList<>(Arrays.asList(value)));
	}
	
	/**
	 * Will post an object on the event bus and inform all event handlers that receive this kind of object
	 * @param object
	 */
	public static void post(Object event) {
		if(eventHandlersByEvents.containsKey(event.getClass()))
			eventHandlersByEvents.get(event.getClass()).forEach(eventHandler -> eventHandler.invoke(event));
		else
			throw new UnregisteredEventException(event);
	}
}
