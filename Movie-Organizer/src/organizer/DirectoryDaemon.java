package organizer;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DirectoryDaemon implements Runnable {
	Movie m = new Movie();
	DAO dao = new DAO();
	Thread t;

	public DirectoryDaemon() {
		t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}
	
	@Override
	public void run() {
		new SystemTrayDemo().closeRun();
		String videoText[] = { ".wmv", ".webm", ".vob", ".gifv", ".svi", ".roq", ".rmvb", ".rm", ".yuv", ".mov", ".qt",
				".ogv", ".ogg", ".nsv", ".mng", ".mp4", ".m4p", ".m4v", ".mpg", ".mpeg", ".m2v", ".mpg", ".mp2",
				".mpeg", ".mpe", ".mpv", ".mkv", ".mxf", ".m4v", ".gif", ".flv", ".f4v", ".f4p", ".f4a", ".f4b", ".flv",
				".drc", ".avi", ".amv", ".asf", ".3g2", ".3gp", ".webm" };
		Set<String> formats = new HashSet<String>(Arrays.asList(videoText));

		String pathToMonitor = (System.getProperty("user.home") + System.getProperty("file.separator") + "Downloads");
		Path faxFolder = Paths.get(pathToMonitor);
		WatchService watchService = null;
		try {
			watchService = FileSystems.getDefault().newWatchService();
			faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean valid = true;

		do {
			WatchKey watchKey = null;
			try {
				watchKey = watchService.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (WatchEvent<?> event : watchKey.pollEvents()) {
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
					String fileName = event.context().toString();
					System.out.println("File Created:" + fileName);
					File f = new File(pathToMonitor + "\\" + fileName);
					if (f.isDirectory()) {
						showDialog(fileName);
					} else {
						String extension = null;
						int i = fileName.lastIndexOf('.');
						if (i > 0)
							extension = fileName.substring(i);
						System.out.println(extension);
						if (formats.contains(extension.toLowerCase()))
							showDialog(fileName);
					}
				}
			}
			valid = watchKey.reset();
		} while (valid);
	}

	void showDialog(String fileName) {
		JFrame jf = new JFrame();
		jf.setSize(500, 500);
		int reply = JOptionPane.showConfirmDialog(jf, "Do You Want '" + fileName + "' to add into Collection?", "Add",
				JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			m.setName(fileName);

			String user = System.getProperty("user.name");
			java.net.InetAddress localMachine = null;
			try {
				localMachine = java.net.InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			m.setOwner(localMachine.getHostName() + "-" + user);

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String ds = (String) df.format(date);
			m.setDate(ds);

			try {
				dao.insertMovie(m);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}