package organizer;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Front {

	static Movie m = new Movie();
	static DAO dao = new DAO();

	void inIt() {
		JFrame frame = new JFrame();
		GridLayout gl = new GridLayout(2, 3);
		frame.setLayout(gl);

		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		JPanel p4 = new JPanel();
		JPanel p5 = new JPanel();
		JPanel p6 = new JPanel();

		JLabel l1 = new JLabel("Add ");
		JLabel l2 = new JLabel("Search ");

		JTextField tf1 = new JTextField();
		JTextField tf2 = new JTextField();

		JButton b1 = new JButton("SUBMIT");
		b1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		JButton b2 = new JButton("SEARCH");
		b2.setCursor(new Cursor(Cursor.HAND_CURSOR));

		tf1.setPreferredSize(new Dimension(100, 20));
		tf2.setPreferredSize(new Dimension(100, 20));

		p1.add(l1);
		p2.add(tf1);
		p3.add(b1);
		p4.add(l2);
		p5.add(tf2);
		p6.add(b2);

		frame.add(p1);
		frame.add(p2);
		frame.add(p3);
		frame.add(p4);
		frame.add(p5);
		frame.add(p6);

		tf1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(
						new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Downloads"));
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int returnVal = chooser.showSaveDialog(null);

				if (returnVal != JFileChooser.CANCEL_OPTION) {
					String path = chooser.getSelectedFile().getAbsolutePath();
					String filename = chooser.getSelectedFile().getName();
					tf1.setText(path + "\\" + filename);
				}
			}
		});

		b1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String text = tf1.getText();
				m.setName(text);
				if (m.getName() == null || text.isEmpty())
					JOptionPane.showMessageDialog(frame, "No files Selected!!", "Error", JOptionPane.ERROR_MESSAGE);
				else {
					java.net.InetAddress localMachine = null;
					try {
						localMachine = java.net.InetAddress.getLocalHost();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					String user = System.getProperty("user.name");
					m.setOwner(localMachine.getHostName() + "-" + user);
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date date = new Date();
					String ds = (String) df.format(date);
					m.setDate(ds);
					System.out.println(ds);
					try {
						dao.insertMovie(m);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

		});

		frame.pack();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
		int taskBarSize = scnMax.bottom;
		frame.setLocation(screenSize.width - frame.getWidth(), screenSize.height - taskBarSize - frame.getHeight());
		frame.setResizable(false);
		frame.setVisible(true);
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		new Thread() {
			public void run() {
				new DirectoryDaemon();
			};
		}.start();
		new Front().inIt();
	}
}
