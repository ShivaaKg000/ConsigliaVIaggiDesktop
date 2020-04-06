package consiglia.viaggi.desktop.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ReviewOld {

 	private final SimpleStringProperty testo;
 	private final SimpleIntegerProperty id;
 	private final SimpleIntegerProperty idAccomodation;


    public ReviewOld(String setTesto,Integer setid, Integer setidAccomodation)
    {
        this.testo = new SimpleStringProperty(setTesto);
        this.id = new SimpleIntegerProperty(setid);
        this.idAccomodation = new SimpleIntegerProperty(setidAccomodation);
    }
    public String getTesto() {
        return testo.get();
    }
    public void setTesto(String setTesto) {
        testo.set(setTesto);
    }
	public int getId() {
		return id.get();
	}
	 public void setId(Integer setid){
	        id.set(setid);
	}
	public int getIdAccomodation() {
		return idAccomodation.get();
	}
	public void setIdAccomodation(Integer setidAccomodation) {
        idAccomodation.set(setidAccomodation);
    }
}
