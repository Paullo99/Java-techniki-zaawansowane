module Lab03 {
	exports com.gui;
	exports com.data;
	exports com.app;
	
	opens com.app to spring.core;

	requires com.fasterxml.jackson.annotation;
	requires java.desktop;
	requires spring.beans;
	requires spring.boot;
	requires spring.boot.autoconfigure;
	requires spring.context;
	requires spring.core;
	requires spring.web;
}