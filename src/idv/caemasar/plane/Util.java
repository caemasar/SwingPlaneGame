package idv.caemasar.plane;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * 创建时间: 2018年3月22日 下午3:30:42
 * 
 * 描述: Util.java
 *
 *
 * 
 * @author Caemasar
 * @version 1.0
 */
public class Util {
	public static ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	// ConcurrentHashMap会自己检查修改操作
	public static ConcurrentHashMap<String, Plane> planes = new ConcurrentHashMap<String, Plane>();

	public static Image getImage(String path) {
		Image img = Toolkit.getDefaultToolkit().getImage(Util.class.getClassLoader().getResource(path));
		return img;
	}

	public static void enemyRun() {
		Iterator<String> iterator = planes.keySet().iterator();
		while (iterator.hasNext()) {
			new Thread(planes.get(iterator.next())).start();
		}
	}

}
