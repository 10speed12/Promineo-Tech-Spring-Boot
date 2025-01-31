package pet.store.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {
	@Autowired
	private PetStoreService petStoreService;
	
	@PostMapping("/pet_store")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData postPetStore(
			@RequestBody PetStoreData petStoreData) {
		// Log current action data:
		log.info("POSTing petStoreData { }", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}
	
	@PutMapping("/pet_store/{petStoreId}")
	public PetStoreData updatePetStore(@PathVariable Long petStoreId,
			@RequestBody PetStoreData petStoreData) {
		// Set id of inputed PetStoreData to petStoreId:
		petStoreData.setPetStoreId(petStoreId);
		// Log current action data:
		log.info("Updating PetStore {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}
	
	@PostMapping("/{petStoreId}/employee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreEmployee addEmployeeToStore(@PathVariable Long petStoreId,
			@RequestBody PetStoreEmployee employeeData) {
		// Log current action data:
		log.info("POSTing petStoreEmployeeData { } for petStore with ID = { }",
				employeeData, petStoreId);
		return petStoreService.saveEmployee(petStoreId, employeeData);
	}
	
	@PostMapping("/{petStoreId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreCustomer addCustomerToStore(@PathVariable Long petStoreId,
			@RequestBody PetStoreCustomer customerData) {
		// Log current action data:
		log.info("POSTing petStoreCustomerData { } for petStore with ID = { }",
				customerData, petStoreId);
		return petStoreService.saveCustomer(petStoreId, customerData);
	}
	
	@GetMapping()
	public List<PetStoreData> listAllPetStores(){
		log.info("Retrieving list of all Pet Stores.");
		return petStoreService.retrieveAllPetStores();
	}
	
	@GetMapping("/{petStoreId}")
	public PetStoreData getPetStoreById(@PathVariable Long petStoreId) {
		log.info("Retrieving Pet Store with ID={ }",petStoreId);
		return petStoreService.getPetStoreById(petStoreId);
	}
	
	@DeleteMapping("/pet_store")
	public void deleteAllPetStores() {
		log.info("Attempting to delete all pet stores:");
		throw new UnsupportedOperationException("Deleting all stores is not allowed.");
	}
	
	@DeleteMapping("/pet_store/{petStoreId}")
	public Map<String,String> deletePetStoreById(@PathVariable Long petStoreId) {
		log.info("Attempting to delete PetStore with ID={ }",petStoreId);
		petStoreService.deletePetStoreById(petStoreId);
		return Map.of("message","Deletion of contributor with ID="+petStoreId+" was successful.");
	}
	
}
