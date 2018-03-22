package idv.caemasar.plane;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.ImageIcon;

/**
 * 
 * 创建时间: 2018年3月22日 下午3:30:10
 * 
 * 描述: Bullet.java
 *
 *
 * 
 * @author Caemasar
 * @version 1.0
 */
public class Bullet implements Runnable {
	// 坐标
	double x = 200, y = 200;
	double speed;
	Image img;
	// 方向（0,1）
	double direct;
	double o = new Random().nextDouble() * Math.PI * 2;
	boolean alive;
	String owner;

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public Bullet() {
	}

	public Bullet(String path, double x, double y, double speed, double direct, String owner) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.direct = direct;
		this.alive = true;
		this.owner = owner;
		img = Util.getImage(path);
	}

	public void draw(Graphics g) {
		g.drawImage(img, (int) x, (int) y, null);
	}

	public String getOwner() {
		return owner;
	}

	// 随机方向，遇边镜面反射
	public void drawRandom(Graphics g) {
		g.drawImage(img, (int) x, (int) y, null);
		x -= speed * Math.sin(o);

		y -= speed * Math.cos(o);
		if (x < 0 || x > Constant.GrameWidht) {
			o = Math.PI * 2 - o;
		}
		if (y < 0 || y > Constant.GrameHeight) {
			o = Math.PI - o;
		}
	}

	public Rectangle getRect() {
		return new Rectangle((int) x, (int) y, new ImageIcon(img).getIconWidth(), new ImageIcon(img).getIconHeight());
	}

	@Override
	public void run() {
		while (true) {
			x += speed * Math.sin(direct * Math.PI * 2);
			y += speed * Math.cos(direct * Math.PI * 2);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (y < 0 || x < 0 || y > Constant.GrameHeight || x > Constant.GrameWidht) {
				alive = false;
				break;
			}
		}
	}
}
