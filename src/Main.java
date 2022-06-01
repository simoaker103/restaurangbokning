
class Main {

	public static void main(String[] args) {
		
		Model model = new Model();
		Controller c = new Controller(model);
		View view = new View(c);
		
		c.setView(view);
	
	}
	
}
