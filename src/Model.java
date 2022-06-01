import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

enum Shape {
	Circ, Rect;
}

class Model {
	
	private ArrayList<Table> tables;
	private LinkedList<QueueEntry> queueEntries;
	
	public Model() {
		tables = new ArrayList<Table>();
		queueEntries = new LinkedList<QueueEntry>();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createTablesShapes(int w, int h) {
		
		HashMap<Integer, Boolean> a = new HashMap<Integer, Boolean>();
		HashMap<Integer, LinkedList> l = new HashMap<Integer, LinkedList>();
		
		for(int i=0;i<16;i++) {
			a.put(i+1, false);
		}
		
		for(Table table : tables) {
			a.replace(table.getTableNr(), table.isActivated());
		}
		
		for(int i=0;i<16;i++) {
			l.put(i+1, new LinkedList<QueueEntry>());
		}
		
		tables.clear();
		
		tables.add(new Table(calcPosX(616, w), calcPosY(539, h), calcWidth(30, w), calcHeight(39, h), Shape.Rect, 11, a.get(11), l.get(11)));
		
		tables.add(new Table(calcPosX(656, w), calcPosY(539, h), calcWidth(30, w), calcHeight(39, h), Shape.Rect, 12, a.get(12), l.get(12)));
		tables.add(new Table(calcPosX(872, w), calcPosY(341, h), calcWidth(30, w), calcHeight(39, h), Shape.Rect, 16, a.get(16), l.get(16)));
		tables.add(new Table(calcPosX(872, w), calcPosY(423, h), calcWidth(30, w), calcHeight(39, h), Shape.Rect, 15, a.get(15), l.get(15)));
		tables.add(new Table(calcPosX(872, w), calcPosY(519, h), calcWidth(30, w), calcHeight(39, h), Shape.Rect, 14, a.get(14), l.get(14)));
		
		tables.add(new Table(calcPosX(538, w), calcPosY(629, h), calcWidth(39, w), calcHeight(62, h), Shape.Rect, 7, a.get(7), l.get(7)));
		tables.add(new Table(calcPosX(637, w), calcPosY(629, h), calcWidth(39, w), calcHeight(62, h), Shape.Rect, 8, a.get(8), l.get(8)));
		tables.add(new Table(calcPosX(735, w), calcPosY(629, h), calcWidth(39, w), calcHeight(62, h), Shape.Rect, 9, a.get(9), l.get(9)));
		tables.add(new Table(calcPosX(833, w), calcPosY(629, h), calcWidth(39, w), calcHeight(62, h), Shape.Rect, 10, a.get(10), l.get(10)));
		
		tables.add(new Table(calcPosX(99, w), calcPosY(191, h), calcWidth(55, w), calcHeight(55, h), Shape.Circ, 1, a.get(1), l.get(1)));
		tables.add(new Table(calcPosX(106, w), calcPosY(303, h), calcWidth(55, w), calcHeight(55, h), Shape.Circ, 2, a.get(2), l.get(2)));
		tables.add(new Table(calcPosX(165, w), calcPosY(386, h), calcWidth(55, w), calcHeight(55, h), Shape.Circ, 3, a.get(3), l.get(3)));
		tables.add(new Table(calcPosX(106, w), calcPosY(461, h), calcWidth(55, w), calcHeight(55, h), Shape.Circ, 4, a.get(4), l.get(4)));
		tables.add(new Table(calcPosX(166, w), calcPosY(539, h), calcWidth(55, w), calcHeight(55, h), Shape.Circ, 5, a.get(5), l.get(5)));
		tables.add(new Table(calcPosX(106, w), calcPosY(614, h), calcWidth(55, w), calcHeight(55, h), Shape.Circ, 6, a.get(6), l.get(6)));
		
		tables.add(new Table(calcPosX(743, w), calcPosY(505, h), calcWidth(77, w), calcHeight(77, h), Shape.Circ, 13, a.get(13), l.get(13)));
	
	}
	
	public ArrayList<Table> getTablesShapes(){
		
		return tables;
		
	}
	
	// Metoder för responsivitet, används inte som tänkt
	
	public int calcPosX(int x, int floorplanWidth) {
		return (int)(x/(double)1000 * floorplanWidth);
	}
	
	public int calcPosY(int y, int floorplanHeight) {
		return (int)(y/(double)750 * floorplanHeight);
	}
	
	public int calcWidth(int width, int floorplanWidth) {
		return (int)(width/(double)1000 * floorplanWidth);
	}
	
	public int calcHeight(int height, int floorplanHeight) {
		return (int)(height/(double)750 * floorplanHeight);
	}
	
	
	public void addQueueEntry(String nr, String name) {
		
		queueEntries.add(new QueueEntry(nr, name));
  		
	}


	public LinkedList<QueueEntry> getQueueEntries() {
		
		return queueEntries;
		
	}

	public void deleteQueueEntry(int id) {
		
		for(int i=0; i<queueEntries.size(); i++) {
			
			if(queueEntries.get(i).getId() == id) {
				
				queueEntries.remove(i);
				break;
				
			}
		}
		
	}

	public void tableClicked(Table table) {
		
		table.toggleActivated();
		
	}
	
}

class Table {
	
	private int width, height, x, y, tableNr;
	private boolean activated;
	private String shape;	
	
	public Table(int posX, int posY, int width, int height, Shape Shape, int tableNr, boolean activated, LinkedList<QueueEntry> queue) {
		
		x = posX;
		y = posY;
				
		this.height = height;
		this.width = width;
		
		this.shape = Shape.toString();
		
		this.tableNr = tableNr;
		
		this.activated = activated;
		
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public String getShape() {
		return shape;
	}
	
	public int getTableNr() {
		return tableNr;
	}
	
	public boolean isActivated() { return activated; }
	
	public void toggleActivated() { activated = !activated; }	
	
}

class QueueEntry {
	
	private int width, height;
	private String nr, name;
	private static int count;
	private int id;
	
	public QueueEntry(String nr, String name) {
		
		id = count;
		
		this.nr = nr;
		this.name = name;
		
		this.width = 145;
		this.height = 60;
		
		count++;

	}
	

	public String getNr() {
		return nr;
	}
	
	public String getName() {
		return name;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getId() {
		return id;
	}

}