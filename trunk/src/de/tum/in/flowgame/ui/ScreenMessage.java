package de.tum.in.flowgame.ui;

import java.awt.Color;

import javax.vecmath.Point2d;

public class ScreenMessage {
	

	private final String message;
	private Color color;
	private int x;
	private int y;
	private int size;

	public ScreenMessage(String message, Color color, Point2d position) {
		this(message, color, position, 12);
	}
	
	public ScreenMessage(String message, Color color, Point2d position, int size) {
		this.message = message;
		this.color = color;
		this.x = (int) position.x;
		this.y = (int) position.y;
		this.size = size;
	}

	public void move(int deltaX, int deltaY) {
		this.x += deltaX;
		this.y += deltaY;
	}

	public String getMessage() {
		return message;
	}

	public Color getColor() {
		return color;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
}
