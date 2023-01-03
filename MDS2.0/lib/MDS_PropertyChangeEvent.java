


public class MDS_PropertyChangeEvent {
	
	
	
	public static final int PROPERTY_SET = 0;
	
	private int type;
	private String propertyName;
	private MDS_Property prop;
	
	
	
	public MDS_PropertyChangeEvent(String propertyName, MDS_Property prop, int type) {
		this.propertyName = propertyName;
		this.prop = prop;
		this.type = type;
	}
	
	
	public String getPropertyName() {
		return propertyName; 
	}
	
	
	public MDS_Property getProperty() {
		return prop;	
	}
	
	
	public int getChangeType() {
		return type;	
	}
}
