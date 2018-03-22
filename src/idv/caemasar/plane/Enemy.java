package idv.caemasar.plane;

import java.util.Random;

import javax.swing.ImageIcon;

/**
 * 
 * 创建时间: 2018年3月22日 下午3:30:25
 * 
 * 描述: Enemy.java
 *
 *
 * 
 * @author Caemasar
 * @version 1.0
 */
public class Enemy extends Plane {
	// 轨迹
	String track;
	double o = new Random().nextDouble() * Math.PI * 2;

	public Enemy() {
	}

	public Enemy(String path, double x, double y, double speed, String name, String track) {
		super(path, x, y, speed, name);
		this.track = track;
	}

	// 随机方向，遇边镜面反射
	public void myTrack() {
		x -= speed * Math.sin(o);
		y -= speed * Math.cos(o);
		if (x < 0 || x > Constant.GrameWidht - new ImageIcon(img).getIconWidth()) {
			o = Math.PI * 2 - o;
		}
		if (y < 0 || y > Constant.GrameHeight - new ImageIcon(img).getIconHeight() - 100) {
			o = Math.PI - o;
		}
	}

	public void run() {
		while (true) {
			myTrack();
			if (alive && bulletSum < Constant.BulletSum) {
				Bullet bullet = new Bullet("bullet01.png", (int) x - 7 + new ImageIcon(img).getIconWidth() / 2,
						(int) y + new ImageIcon(img).getIconHeight(), Constant.BulletSpeed, 0, name);
				new Thread(bullet).start();
				Util.bullets.add(bullet);
				bulletSum++;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
