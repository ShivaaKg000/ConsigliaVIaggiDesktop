package consigliaViaggiDesktop.model;

import java.util.ArrayList;
import java.util.List;


public class AccommodationDaoStub implements AccommodationDao{
	
	@Override
    public List<Accommodation> getAccommodationList(String city) {
        List<Accommodation> accommodationList=createAccommodationList(city);
        return accommodationList;
    }
	
	private List<Accommodation> createAccommodationList(String city) {
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
                    .setSubcategory(Subcategory.HOSTEL)
					.setAddress("Vialemanidalnaso")
					.setCity(city)
                    .create());

        }
        return accommodationList;
    }

	@Override
    public Accommodation getAccommodationById(int id) {
    	return createAccommodation(id);  
    }

	@Override
	public void createAccommodation(Accommodation accommodation) {

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
                .setSubcategory(Subcategory.HOSTEL)
                .setAddress("Vialemanidalnaso")
				.setCity("NAPOLI")
				.setLatitude(40.851799)
				.setLongitude(14.268120)
				.setImages("https://wips.plug.it/cips/buonissimo.org/cms/2020/03/pizza-senza-lievito.jpg")
                .create();
        
    }
}
