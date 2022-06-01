import java.util.ArrayList;
import java.awt.Color;
import java.awt.event.*;  

class Controller implements ActionListener, MouseListener {
	
	private View view;
	private Model model;
	
	public Controller(Model model) {
		
		this.model = model;
		
	}
	
	public void setView(View view) {
		
		this.view = view;
		
	}

	public ArrayList<Table> paintFloorplan(int width, int height) {
		
		model.createTablesShapes(width, height);
		return model.getTablesShapes();
		
	}

	public void OkButtonPressed(String nr, String name) {
		
		model.addQueueEntry(nr, name);
		view.updateQueuePanel(model.getQueueEntries());
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		view.showQueueInputDialog();
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		ArrayList<Table> tables = model.getTablesShapes();
		
		if(e.getSource().toString() == "Floorplan") {
			
			for (Table table : tables) {
				
				if(e.getX() >= table.getX() && e.getY() >= table.getY() && e.getX() <= (table.getX() + table.getWidth()) && e.getY() <= (table.getY() + table.getHeight())) {
					
					model.tableClicked(table);
					
				}
			}
			
			view.updateFloorplan();
			
		}
		
		if(e.getSource().toString() == "Tile") {
			
			QueueEntryTile qe = (QueueEntryTile) e.getSource();
			
			if(view.verifyDeleteEntry()) {
				
				model.deleteQueueEntry(qe.getId());
				view.updateQueuePanel(model.getQueueEntries());
				
			}	
			
		}
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		if(e.getSource().toString() == "Tile") {
			
			QueueEntryTile qe = (QueueEntryTile) e.getSource();
			qe.setColor(new Color(150,150,150));

		}
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		if(e.getSource().toString() == "Tile") {
			
			QueueEntryTile qe = (QueueEntryTile) e.getSource();
			qe.setColor(new Color(130,130,130));
			
		}
	
		
	}


}
