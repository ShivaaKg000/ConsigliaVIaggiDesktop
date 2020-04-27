package consigliaViaggiDesktop.model;

import java.util.ArrayList;
import java.util.List;


public class AccommodationDaoStub implements AccommodationDao{
	
	@Override
    public List<Accommodation> getAccommodationList(int id) {
        List<Accommodation> accommodationList=createAccommodationList(id);
        return accommodationList;
    }
	
	private List<Accommodation> createAccommodationList(int id) {
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			//e.printStackTrace();
		}
        List<Accommodation> accommodationList=new ArrayList<Accommodation>();
        for (int i=0; i<30;i++){
            accommodationList.add(new Accommodation.Builder()
            		.setId(i)
            		.setName("SDM")
            		.setDescription("volkswagen")
                    .setLogoUrl("vattelapiglianderculo.net")
                	.setRating((float) (1 + Math.random() * (5 - 1)))
                    .setCategory(Category.HOTEL)
                    .setSubcategory(Subcategory.MUSEUM)
                    .setAccommodationLocation(new Location.Builder().setAddress("Vialemanidalnaso")
                    												.setCity("NAPOLI")
                    												.build()
                    							)
                    .create());
        }
        return accommodationList;
    }

	@Override
    public Accommodation getAccommodationById(int id) {
    	return createAccommodation(id);  
    }
	
	private Accommodation createAccommodation(int id) {
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			//e.printStackTrace();
		}
        
        return new Accommodation.Builder()
        		.setId(id)
        		.setName("SDM")
        		.setDescription("volkswagen")
                .setLogoUrl("vattelapiglianderculo.net")
            	.setRating((float) (1 + Math.random() * (5 - 1)))
                .setCategory(Category.HOTEL)
                .setSubcategory(Subcategory.MUSEUM)
                .setAccommodationLocation(new Location.Builder().setAddress("Vialemanidalnaso")
                												.setCity("NAPOLI")
                												.build()
                							)
                .create();
        
    }
}
