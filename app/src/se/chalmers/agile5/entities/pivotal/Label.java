package se.chalmers.agile5.entities.pivotal;


public final class Label extends PivotalData {
	
	public Label(int id, String title) {
		super(id, title);
	}
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof Label)
			return super.equals(object);
		return false;
	}
}
