package organizer;
import javax.swing.*;
import java.awt.*;

public class ScrollableJTable extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ScrollableJTable() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 200));
        String data[][]={{"Hulk","Aish","20/12/2017"},{"Iron Man","Sunny","12/12/2015"}};
        String column[]={"Name","Owner","Date"};
        
        JTable table = new JTable(data,column);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 
        JScrollPane pane = new JScrollPane(table);
        add(pane, BorderLayout.CENTER);
    }

    public static void showFrame() {
        JPanel panel = new ScrollableJTable();
        panel.setOpaque(true);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Scrollable JTable");
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ScrollableJTable.showFrame();
            }
        });
    }
}
