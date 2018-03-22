package idv.caemasar.plane;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

/**
 * 
 * 创建时间: 2018年3月22日 下午3:30:37
 * 
 * 描述: Plane.java
 *
 *
 * 
 * @author Caemasar
 * @version 1.0
 */
public class Plane implements Runnable {
	// 坐标
	double x, y;
	double speed;
	Image img;
	// 移动方向，存活状态
	boolean up, down, left, right, alive;
	// 发射子弹数
	int bulletSum;
	String name;

	public void setUp(boolean up) {
		this.up = up;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public Plane() {
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public Plane(String path, double x, double y, double speed, String name) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.alive = true;
		this.name = name;
		img = Util.getImage(path);

	}

	public void draw(Graphics g) {
		g.drawImage(img, (int) x, (int) y, null);
	}

	public void move(boolean up, boolean down, boolean left, boolean right) {
		if (up && y - speed > 0)
			y -= speed;
		if (down && y + speed < Constant.GrameHeight - new ImageIcon(img).getIconHeight())
			y += speed;
		if (left && x - speed > 0)
			x -= speed;
		if (right && x + speed < Constant.GrameWidht - new ImageIcon(img).getIconWidth())
			x += speed;
	}

	public Rectangle getRect() {
		return new Rectangle((int) x, (int) y, new ImageIcon(img).getIconWidth(), new ImageIcon(img).getIconHeight());
	}

	public void boom(Graphics g) {
		for (int i = 3; i > 0; i--) {
			g.drawImage(Util.getImage("boom0" + i + ".png"), (int) x, (int) y, null);
		}
	}

	public int getBulletSum() {
		return bulletSum;
	}

	public void setBulletSum(int bulletSum) {
		this.bulletSum = bulletSum;
	}

	public Bullet shot() {
		Bullet bullet = null;
		if (alive && bulletSum < Constant.BulletSum) {
			bullet = new Bullet("bullet02.png", (int) x - 7 + new ImageIcon(img).getIconWidth() / 2,
					(int) y - 15, Constant.BulletSpeed, 0.5, name);
			new Thread(bullet).start();
			bulletSum++;
		}
		return bullet;
	}

	@Override
	public void run() {
		while (true) {
			move(up, down, left, right);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
