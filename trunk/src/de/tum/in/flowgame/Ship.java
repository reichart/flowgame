package de.tum.in.flowgame;

import java.io.IOException;
import java.util.Enumeration;

import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import de.tum.in.flowgame.behavior.KeyShipBehavior;
import de.tum.in.flowgame.behavior.ShipCollisionBehavior;

public class Ship extends TransformGroup {
	
	public static final float INITIAL_SHIP_PLACEMENT_X = 0;
	public static final float INITIAL_SHIP_PLACEMENT_Y = -1;
	public static final float INITIAL_SHIP_PLACEMENT_Z = -6f;
	private final KeyShipBehavior keyShipBehavior;
	
	private final Transform3D staticTransforms;
	
	public Ship(final GameLogic logic, TransformGroup viewTG) throws IOException {
		super();
		
		final Transform3D t3d = new Transform3D();
		t3d.setTranslation(new Vector3d(INITIAL_SHIP_PLACEMENT_X, INITIAL_SHIP_PLACEMENT_Y, INITIAL_SHIP_PLACEMENT_Z));

		this.setTransform(t3d);

		TransformGroup moveGroup = new TransformGroup();
		moveGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		this.addChild(moveGroup);

		TransformGroup rotationGroup = new TransformGroup();
		rotationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		moveGroup.addChild(rotationGroup);

		TransformGroup ship = loadShip();

		ship.setCollisionBounds(new BoundingBox(new Point3d(-0.35f, -0.15f, -0.5f), new Point3d(0.35f, 0.06f, 0.5f)));
		// Box box = new Box(0.7f, 0.19f, 1.0f, new Appearance());
		// box.setCollidable(false);
		rotationGroup.addChild(ship);
		System.out.println("Ship " + ship.getBounds());
		
//		KeyShipEllipseBehavior keyShipBehavior = new KeyShipEllipseBehavior(moveGroup, rotationGroup);
//		ship.addChild(keyShipBehavior);
//		keyShipBehavior.setSchedulingBounds(WORLD_BOUNDS);
		
		// Alternative KeyBehavior
		keyShipBehavior = new KeyShipBehavior(moveGroup, viewTG);
		ship.addChild(keyShipBehavior);
		keyShipBehavior.setSchedulingBounds(Game3D.WORLD_BOUNDS);

		ShipCollisionBehavior collisionBehavior = new ShipCollisionBehavior(ship, logic);
		ship.addChild(collisionBehavior);
		collisionBehavior.setSchedulingBounds(Game3D.WORLD_BOUNDS);
		
		staticTransforms = new Transform3D();
		
		Node node = this;
		while(node.getParent() != null){
			if(node.getParent() instanceof TransformGroup){
				TransformGroup tg = (TransformGroup)node.getParent();
				Transform3D trans = new Transform3D();
				tg.getTransform(trans);
				staticTransforms.mul(trans);
			}
			node=node.getParent();
		}
		
		//the following call multiplies the staticTransforms with all the transforms of the children of this node
		getTransformsOfChildren(this);
		
		
	}
	
	private void getTransformsOfChildren(TransformGroup tg){
		Enumeration<Node> children = tg.getAllChildren();
		while(children.hasMoreElements()){
			Node n = children.nextElement();
			if(n instanceof TransformGroup){
				Transform3D trans = new Transform3D();
				((TransformGroup)n).getTransform(trans);
				this.staticTransforms.mul(trans);
				getTransformsOfChildren((TransformGroup)n);
			}
		}
	}
	
//	protected static TransformGroup createShip(final GameLogic logic, TransformGroup viewTG) throws IOException {
//		
//	}
	
	private static TransformGroup loadShip() throws IOException {

		final Transform3D rotX = new Transform3D();
		rotX.rotX(Math.toRadians(90));

		final Transform3D rotY = new Transform3D();
		rotY.rotY(Math.toRadians(180));

		rotX.mul(rotY);

		TransformGroup rotateShip = new TransformGroup();
		rotateShip.setTransform(rotX);

		final BranchGroup ship = Utils.loadScene("/res/SFighter.obj");

		rotateShip.addChild(ship);

		return rotateShip;
	}
	
	public Vector3d getVector3dtShipPos(){
		return keyShipBehavior.getCoords();
	}

	public double getXPos() {
		return keyShipBehavior.getCoords().getX();
	}

	public double getYPos() {
		return keyShipBehavior.getCoords().getY();
	}

}
