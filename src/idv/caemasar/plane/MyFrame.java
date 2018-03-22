package idv.caemasar.plane;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 
 * 创建时间: 2018年3月22日 下午3:30:31
 * 
 * 描述: MyFrame.java
 *
 *
 * 
 * @author Caemasar
 * @version 1.0
 */
public class MyFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4423990790477197800L;
	private Image bg1, bg2;
	private Plane hero;
	private int y;

	public static void main(String[] args) {
		new MyFrame();
	}

	public MyFrame() {
		bg1 = Util.getImage("bg_01.jpg");
		bg2 = Util.getImage("bg_02.jpg");
		hero = new Plane("plane01.png", Constant.GrameWidht / 2 - 30, Constant.GrameHeight - 60,
				Constant.PlaneSpeed, "hero");
		new Thread(hero).start();
		Util.planes.put("hero", hero);
		// 创建敌人
		for (int i = 0; i < Constant.EnemySum; i++) {
			Util.planes.put("enemy" + i, new Enemy("plane02.png", Constant.GrameWidht / 2, 0,
					Constant.PlaneSpeed, "enemy" + i, "(x+1)^2"));
		}
		// 启动敌人
		Util.enemyRun();

		this.addKeyListener(new MyMoveListener());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setTitle("打飞机");
		this.setLocation(500, 300);
		this.setIconImage(Util.getImage("icon48x48.png"));
		this.setSize(Constant.GrameWidht, Constant.GrameHeight);
		new RepaintThread(this).start();

	}

	public void paint(Graphics g) throws ConcurrentModificationException {
		drawBg(g);
		Set<String> set = Util.planes.keySet();
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();

			// System.out.println(key+" "+Util.bullets.size());
			Util.planes.get(key).draw(g);
			for (int i = 0; i < Util.bullets.size(); i++) {
				// 碰撞检测
				if (Util.planes.containsKey(key)
						&& Util.bullets.get(i).getRect().intersects(Util.planes.get(key).getRect())) {
					if (!(!Util.bullets.get(i).getOwner().equals("hero") && !key.equals("hero"))) {
						Util.planes.get(key).setAlive(false);
						Util.planes.get(key).boom(g);
						Util.planes.remove(key);
						if (key.equals("hero"))
							JOptionPane.showMessageDialog(this, "You lose!");
					}
				}
				if (Util.bullets.get(i).isAlive()) {
					Util.bullets.get(i).draw(g);
				} else {
					// 恢复子弹数
					if (Util.planes.get(Util.bullets.get(i).getOwner()) != null)
						Util.planes.get(Util.bullets.get(i).getOwner())
								.setBulletSum(Util.planes.get(Util.bullets.get(i).owner).getBulletSum() - 1);
					// 移除死亡子弹
					Util.bullets.remove(i);
				}
			}
		}
	}

	public void drawBg(Graphics g) {
		g.drawImage(bg1, 0, y - 800, null);
		g.drawImage(bg2, 0, y, null);
		y += 4;
		if (y == 800) {
			Image bg = bg1;
			bg1 = bg2;
			bg2 = bg;
			y = 0;
		}
	}

	// 双缓冲解决屏幕闪烁,好像并没有什么卵用

	private Image offScreenImage = null;

	public void update(Graphics g) {
		if (offScreenImage == null)
			offScreenImage = this.createImage(Constant.GrameWidht, Constant.GrameHeight);
		Graphics offG = offScreenImage.getGraphics();
		paint(offG);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	private class RepaintThread extends Thread {
		JFrame jf;

		public RepaintThread(JFrame jf) {
			this.jf = jf;
		}

		public void run() {
			while (true) {
				jf.repaint();
				// System.out.println("repaint！");
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 8方向移动
	private class MyMoveListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_W) {
				hero.setUp(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				hero.setLeft(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
				hero.setDown(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_D) {
				hero.setRight(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_J) {
				Bullet b = hero.shot();
				if (b != null)
					Util.bullets.add(b);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_W) {
				hero.setUp(false);
			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				hero.setLeft(false);
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
				hero.setDown(false);
			}
			if (e.getKeyCode() == KeyEvent.VK_D) {
				hero.setRight(false);
			}
		}
	}
}
