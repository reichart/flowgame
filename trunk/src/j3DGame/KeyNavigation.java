package j3DGame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

public class KeyNavigation implements KeyListener {

	private TransformGroup tg;
	private float speed;
	
	public KeyNavigation (TransformGroup tg){
		this.tg = tg;
		speed = 0.05f;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		Transform3D trans = new Transform3D();
		
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			trans.set(new Vector3d(0.0f, 0.0f, -speed));
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			trans.set(new Vector3d(0.0f, 0.0f, speed));
		}
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			trans.set(new Vector3d(-speed, 0.0f, 0.0f));
		}
		
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			trans.set(new Vector3d(speed, 0.0f, 0.0f));
		}
		
		Transform3D update = new Transform3D();
		tg.getTransform(update);
		update.mul(trans);
		
		tg.setTransform(update);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
