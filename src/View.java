import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.awt.geom.RoundRectangle2D;

class View extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Controller controller;
	
	private Floorplan floorplan;
	private JButton addButton;
	private JPanel queuePanel;
	private JScrollPane scrollPane;
	
	private ArrayList<QueueEntryTile> entryTileList;
	
	static final Color TABLE_BORDER_BLUE = Color.blue;
	static final Color TABLE_BORDER_RED = Color.red;
	static final Color TABLE_COLOR_BLUE = new Color(110, 110, 255);
	static final Color TABLE_COLOR_RED = new Color(255, 110, 110);


	public View(Controller controller) {
		
		this.controller = controller;
		
		entryTileList = new ArrayList<QueueEntryTile>();
		
		addButton = new JButton("Add");
		addButton.setBackground(new Color(130,130,130));
		addButton.setOpaque(true);
		
		addButton.addActionListener(controller);
						
		JLayeredPane layeredPane = new JLayeredPane();
		
		floorplan = new Floorplan(controller.paintFloorplan(1000, 750));
		
		queuePanel = new JPanel();
		queuePanel.setLayout(new BoxLayout(queuePanel, BoxLayout.PAGE_AXIS));
		queuePanel.setOpaque(false);
		
		queuePanel.setBackground(new Color(255,255,255,0));
			
		scrollPane = new JScrollPane(queuePanel);		
		
		floorplan.addMouseListener(controller);
		
		// Eftersom LayoutManager är null krävs det att man placerar ut komponenterna
		queuePanel.setBounds(410, 98, 180, 461);
		scrollPane.setBounds(queuePanel.getX(), queuePanel.getY(), queuePanel.getWidth(), queuePanel.getHeight());
		
		addButton.setBounds(410, 58, 80, 40);
		
		layeredPane.add(floorplan, 1, 0);
		layeredPane.add(scrollPane, 2, 0);
		layeredPane.add(addButton, 2, 0);
				
		add(layeredPane);
		
		setSize(1000, 778);
		setTitle("Table Picker");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		
	}
	
	public void updateQueuePanel(LinkedList<QueueEntry> queueList) {
		
		entryTileList.clear();
		queuePanel.removeAll();
		
		for(QueueEntry entry : queueList) {
			
			entryTileList.add(new QueueEntryTile(entry.getWidth(), entry.getHeight(), entry.getNr(), entry.getName(), entry.getId()));
			
		}
		
		queuePanel.add(Box.createRigidArea(new Dimension(0, 10)));

		for(QueueEntryTile tile : entryTileList) {
			
			tile.addMouseListener(controller);
			
			queuePanel.add(tile);
			queuePanel.add(Box.createRigidArea(new Dimension(0, 10)));
			
		}
		
		queuePanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		queuePanel.revalidate();
		queuePanel.repaint();
				
	}
	
	
	public void showQueueInputDialog() {
		
		QueueInputPanel inputPanel = new QueueInputPanel();
		
    	int result = JOptionPane.showConfirmDialog(this, inputPanel, "Queue Entry", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    	
    	
    	if((inputPanel.textNr.getText().isBlank() || inputPanel.textName.getText().isBlank() || !isNumeric(inputPanel.textNr.getText())) && result == JOptionPane.OK_OPTION) {
    		JOptionPane.showMessageDialog(this, "Please enter name and number of people.", "Message", JOptionPane.WARNING_MESSAGE);
    		return;
    	}
		
    	if(result == JOptionPane.OK_OPTION) {
    		controller.OkButtonPressed(inputPanel.textNr.getText(), inputPanel.textName.getText());
    	}
		
	}
	
	// https://www.baeldung.com/java-check-string-number
	public boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        Integer.parseInt(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}

	public void updateFloorplan() {
		
		floorplan.repaint();
		
	}

	public boolean verifyDeleteEntry() {
		
		int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this entry?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		if(result == JOptionPane.OK_OPTION) {
			return true;
		}
		else {
			return false;
		}
		

	}

}

class Floorplan extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private Image floorplanImage;
	
	private ArrayList<Table> tables;

	public Floorplan(ArrayList<Table> tables) {
		
		this.tables = tables;
		
		try {
			Image raw = ImageIO.read(new File("floorplan.png"));
			floorplanImage = raw.getScaledInstance(raw.getWidth(null), raw.getHeight(null), Image.SCALE_SMOOTH);
		}
		catch (IOException e) {
			System.out.println("Floorplan image not loaded");
			System.exit(1);
		}
		
		setSize(1000,750);
				
	}
	
	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D)g;
		
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2D.setStroke(new BasicStroke(4));
		g2D.drawImage(floorplanImage, 0,0, null);
				
		for(Table table : tables) {
						
			if(table.getShape() == "Rect") {
				
				g2D.setStroke(new BasicStroke(1));
				
				if(table.isActivated()) {
					g2D.setColor(View.TABLE_COLOR_RED);
					g2D.fillRect(table.getX(), table.getY(), table.getWidth(), table.getHeight());
					
					g2D.setColor(View.TABLE_BORDER_RED);
					g2D.drawRect(table.getX(), table.getY(), table.getWidth(), table.getHeight());

				}
				else {
					g2D.setColor(View.TABLE_COLOR_BLUE);
					g2D.fillRect(table.getX(), table.getY(), table.getWidth(), table.getHeight());
					
					g2D.setColor(View.TABLE_BORDER_BLUE);
					g2D.drawRect(table.getX(), table.getY(), table.getWidth(), table.getHeight());

				}
				
				g2D.setColor(new Color(255,255,255));
				g2D.setFont(new Font("Microsoft YaHei", Font.PLAIN, 20));
				
				if(table.getTableNr() == 7 || table.getTableNr() == 8 || table.getTableNr() == 9)
					g2D.drawString(Integer.toString(table.getTableNr()), table.getX() + (table.getWidth() / 2) - 5, table.getY() + (table.getHeight() / 2) + 7);
				else
					g2D.drawString(Integer.toString(table.getTableNr()), table.getX() + (table.getWidth() / 2) - 12, table.getY() + (table.getHeight() / 2) + 7);
				
	
			}
			
			if(table.getShape() == "Circ") {
				
				g2D.setStroke(new BasicStroke(1));
				
				if(table.isActivated()) {
					g2D.setColor(View.TABLE_COLOR_RED);
					g2D.fillOval(table.getX(), table.getY(), table.getWidth(), table.getHeight());
					
					g2D.setColor(View.TABLE_BORDER_RED);
					g2D.drawOval(table.getX(), table.getY(), table.getWidth(), table.getHeight());

				}
				else {
					g2D.setColor(View.TABLE_COLOR_BLUE);
					g2D.fillOval(table.getX(), table.getY(), table.getWidth(), table.getHeight());
					
					g2D.setColor(View.TABLE_BORDER_BLUE);
					g2D.drawOval(table.getX(), table.getY(), table.getWidth(), table.getHeight());
				}
				
				g2D.setColor(new Color(255,255,255));
				g2D.setFont(new Font("Microsoft YaHei", Font.PLAIN, 20));
				
				if(table.getTableNr() != 13)
					g2D.drawString(Integer.toString(table.getTableNr()), table.getX() + (table.getWidth() / 2) - 5, table.getY() + (table.getHeight() / 2) + 7);
				else
					g2D.drawString(Integer.toString(table.getTableNr()), table.getX() + (table.getWidth() / 2) - 12, table.getY() + (table.getHeight() / 2) + 7);


			}

		}
		
		g2D.setColor(Color.BLACK);

	}
	
	@Override
	public String toString() {
		return "Floorplan";
	}
	
	@SuppressWarnings("unused")
	private void drawBaseGraphics(Graphics2D g2D) {
		
		
	}

}

class QueueEntryTile extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private String name, nr;
	private int id;
	private Color color;
	
	public QueueEntryTile(int width, int height, String nr, String name, int id) {
		
		color = new Color(130,130,130);
		
		this.nr = nr;
		this.name = name;
		this.id = id;
		
		// BoxLayout använder Minimum-, Maximum- och Preferred-Size för att placera ut komponenter
		setMinimumSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		setPreferredSize(new Dimension(width, height));
				
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D)g;
		
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10);
        g2D.draw(roundedRectangle);
        
        g2D.setColor(color);
        g2D.fill(roundedRectangle);
        
        g2D.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        FontMetrics fontMetrics = getFontMetrics(getFont());
        
        int nameFontWidth = fontMetrics.stringWidth(name);
        
        g2D.setColor(Color.WHITE);
        
        if(Integer.valueOf(nr) != 1) {
        	int nrFontWidth = fontMetrics.stringWidth(nr + " people");
            g2D.drawString(nr + " people", getWidth() / 2 - nrFontWidth / 2, 26);

        } else {
        	int nrFontWidth = fontMetrics.stringWidth(nr + " person");
            g2D.drawString(nr + " person", getWidth() / 2 - nrFontWidth / 2, 26);

        }
        
        g2D.drawString(name, getWidth() / 2 - nameFontWidth / 2, 42);
		
	}
	
	@Override
	public String toString() {
		return "Tile";
	}


	public int getId() {
		return id;
	}


	public void setColor(Color color) {
		
		this.color = color;
		repaint();
		
	}
	
	
}

class QueueInputPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	JTextField textName, textNr;
	JLabel labelName, labelNr;

	public QueueInputPanel() {
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		textName = new JTextField();
		textNr = new JTextField();
		
		// Textrutorna vänsterjusteras
		textName.setAlignmentX(Component.LEFT_ALIGNMENT);
		textNr.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		labelName = new JLabel("Name:");
		labelNr = new JLabel("Number of people:");
		
		add(labelNr);
		add(textNr);
		add(labelName);
		add(textName);
		
	}
	
}