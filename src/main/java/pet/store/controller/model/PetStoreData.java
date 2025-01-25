package pet.store.controller.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Data
@NoArgsConstructor
public class PetStoreData {
	
	private Long petStoreId;
	private String petStoreName;

	private String petStoreAddress;
	private String petStoreCity;
	private String petStoreState;
	private String petStoreZip;

	private String petStorePhone;

	private Set<PetStoreEmployee> employees = new HashSet<>();
	
	private Set<PetStoreCustomer> customers = new HashSet<>();
	// Constructor for creating from a petStore object:
	public PetStoreData(PetStore petStore){
		petStoreId = petStore.getPetStoreId();
		petStoreName = petStore.getPetStoreName();
		petStoreAddress = petStore.getPetStoreAddress();
		petStoreCity = petStore.getPetStoreCity();
		petStoreState = petStore.getPetStoreState();
		petStoreZip = petStore.getPetStoreZip();
		petStorePhone = petStore.getPetStorePhone();
		// Iterate through given petStore's customers list:
		for (Customer customer : petStore.getCustomers()) {
			// Add new PetStoreCustomer created from the data in current customer to customers list:
			customers.add(new PetStoreCustomer(customer));
		}
		// Iterate through given petStore's employees list:
		for(Employee employee : petStore.getEmployees()) {
			// Add new PetStoreEmployee created from the data in current employee to employees list:
			employees.add(new PetStoreEmployee(employee));
		}
	}
}
