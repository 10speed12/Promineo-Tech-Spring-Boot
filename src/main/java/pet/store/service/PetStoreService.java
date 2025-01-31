package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreEmployee;
import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Employee;
import pet.store.entity.Customer;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	@Autowired
	private PetStoreDao petStoreDao;
	@Autowired
	private EmployeeDao employeeDao;
	@Autowired
	private CustomerDao customerDao;
	
	@Transactional(readOnly=false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {
		Long petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);
		
		copyPetStoreFields(petStore,petStoreData);
		return new PetStoreData(petStoreDao.save(petStore));

	}

	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
	}

	private PetStore findOrCreatePetStore(Long petStoreId) {
		PetStore petStore;
		if(Objects.isNull(petStoreId)) {
			petStore = new PetStore();
		}else {
			petStore = findPetStoreById(petStoreId);
		}
		return petStore;
	}

	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId)
				.orElseThrow(() -> new NoSuchElementException(
						"Pet Store with ID=" + petStoreId +" was not found."));
	}
	
	@Transactional(readOnly=false)
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee employeeData) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long employeeId = employeeData.getEmployeeId();
		Employee employee = findOrCreateEmployee(petStoreId, employeeId);
		copyEmployeeFields(employee, employeeData);
		employee.setPetStore(petStore);
		petStore.getEmployees().add(employee);
		Employee dbEmployee = employeeDao.save(employee);
		return new PetStoreEmployee(dbEmployee);
	}
	
	private void copyEmployeeFields(Employee employee, PetStoreEmployee employeeData) {
		employee.setEmployeeFirstName(employeeData.getEmployeeFirstName());
		employee.setEmployeeLastName(employeeData.getEmployeeLastName());
		employee.setEmployeeJobTitle(employeeData.getEmployeeJobTitle());
		employee.setEmployeePhone(employeeData.getEmployeePhone());
		employee.setEmployeeId(employeeData.getEmployeeId());
	}
	
	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
		if(Objects.isNull(employeeId)) {
			return new Employee();
		}else {
			return findEmployeeById(petStoreId,employeeId);
		}
	}
	
	private Employee findEmployeeById(Long petStoreId, Long employeeId) {
		Employee employee = employeeDao.findById(employeeId)
				.orElseThrow(() -> new NoSuchElementException(
						"Employee with ID=" + employeeId +" was not found."));
		// Confirm that id of pet store for retrieved employee matches that of entered petStoreId:
 		if(employee.getPetStore().getPetStoreId() != petStoreId) {
			throw new IllegalArgumentException("Error, id of pet store for retrieved employee does "
				+ "not match entered value.");
		}
 		// Otherwise, return retrieved employee:
 		return employee;
	}
	
	@Transactional(readOnly=false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long customerId = petStoreCustomer.getCustomerId();
		Customer customer = findOrCreateCustomer(petStoreId, customerId);
		copyCustomerFields(customer, petStoreCustomer);
		customer.getPetStores().add(petStore);		
		petStore.getCustomers().add(customer);
		Customer dbCustomer = customerDao.save(customer);
		return new PetStoreCustomer(dbCustomer);
	}
	
	private void copyCustomerFields(Customer customer, PetStoreCustomer customerData) {
		customer.setCustomerFirstName(customerData.getCustomerFirstName());
		customer.setCustomerLastName(customerData.getCustomerLastName());
		customer.setCustomerEmail(customerData.getCustomerEmail());
		customer.setCustomerId(customerData.getCustomerId());
	}
	
	private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
		if(Objects.isNull(customerId)) {
			return new Customer();
		}else {
			return findCustomerById(petStoreId,customerId);
		}
	}
	
	private Customer findCustomerById(Long petStoreId, Long customerId) {
		Customer customer = customerDao.findById(customerId)
				.orElseThrow(() -> new NoSuchElementException(
						"Customer with ID=" + customerId +" was not found."));
		// Confirm that found customer has a petStore with Id of entered petStoreId in it's records:
		boolean found = false;
		for(PetStore petStore : customer.getPetStores()) {
			if(petStore.getPetStoreId() == petStoreId) {
		// If a pet store whose Id matches entered value is found in retrieved customers petStore list,
		// set found variable to true and exit the loop:
				found=true;
				break;
			}
		}
		if(found) {
			// If found is set to true, return retrieved customer:
			return customer;
		}else {
			// Otherwise, throw an IllegalArgumentException:
			throw new IllegalArgumentException("The customer with ID="+customerId
					+" is not a member of the pet store with ID="+petStoreId);
		}
	}

	@Transactional
	public List<PetStoreData> retrieveAllPetStores() {
		List<PetStore> petStores = petStoreDao.findAll();
		
		List<PetStoreData> result = new LinkedList<>();
		for(PetStore petStore : petStores) {
			// Create a new PetStoreData object named psd using the information of current petStore:
			PetStoreData psd = new PetStoreData(petStore);
			// Remove contents of the customer and employee lists for psd:
			psd.getCustomers().clear();
			psd.getEmployees().clear();
			// Add adjusted psd to result list:
			result.add(psd);
		}
		return result;
	}
	
	@Transactional
	public PetStoreData getPetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		// Convert retrieved PetStore into a new PetStoreData object and return it:
		return new PetStoreData(petStore);
	}

	@Transactional(readOnly = false)
	public void deletePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		petStoreDao.delete(petStore);
	}
}
