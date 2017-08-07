package organizer;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SystemTrayDemo {

	void closeRun() {
        final Frame frame = new Frame("");
        frame.setUndecorated(true);
        frame.setType(Type.UTILITY);
        
		if (!SystemTray.isSupported()) {
			System.out.println("System tray is not supported !!! ");
			return;
		}
		SystemTray systemTray = SystemTray.getSystemTray();
		Image image = Toolkit.getDefaultToolkit().getImage("src/images/1.gif");

		PopupMenu trayPopupMenu = new PopupMenu();

		MenuItem action = new MenuItem("Show Window");
		trayPopupMenu.add(action);
		MenuItem close = new MenuItem("Close");
		trayPopupMenu.add(close);

		TrayIcon trayIcon = new TrayIcon(image, "Movie Collection", trayPopupMenu);
		trayIcon.setImageAutoSize(true);
		
		trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    frame.add(trayPopupMenu);
                	trayPopupMenu.show(frame, e.getXOnScreen(), e.getYOnScreen());
                }
            }
        });

		try {
			frame.setResizable(false);
            frame.setVisible(true);
			systemTray.add(trayIcon);
		} catch (AWTException awtException) {
			awtException.printStackTrace();
		}
		
		action.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Front().inIt();
			}
		});
		
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});	
	}
}
