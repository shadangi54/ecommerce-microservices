package com.shadangi54.inventory.manager;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.shadangi54.inventory.DTO.InventoryDTO;
import com.shadangi54.inventory.entity.Inventory;
import com.shadangi54.inventory.repository.InventoryDAO;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InventoryManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryManager.class);
	
	private InventoryDAO inventoryDAO;

	public List<InventoryDTO> checkStock(List<String> skuCodes) {
		LOGGER.info("Checking stock for SKU codes: {}", skuCodes);
			List<Inventory> inventoryList = inventoryDAO.findBySkuCodeIn(skuCodes);
			List<InventoryDTO> inventoryDTOList = inventoryList.stream()
					.map(inventory -> new InventoryDTO(inventory.getSkuCode(), inventory.getQuantity(),
							inventory.getQuantity() > 0))
					.toList();
			return inventoryDTOList;
	}
	
	@Transactional(TxType.REQUIRES_NEW)
	public void updateInventory(InventoryDTO inventoryDTO) {
		LOGGER.info("Checking inventory for SKU code: {}", inventoryDTO.getSkuCode());
	    Inventory existingInventory = inventoryDAO.findBySkuCode(inventoryDTO.getSkuCode());

	    if (existingInventory != null) {
	        LOGGER.info("Inventory found for SKU code: {}. Updating quantity.", inventoryDTO.getSkuCode());
	        existingInventory.setQuantity(inventoryDTO.getQuantity());
	        existingInventory.setModifiedBy("system");
	        existingInventory.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
	        inventoryDAO.save(existingInventory);
	        LOGGER.info("Inventory updated successfully for SKU code: {}", inventoryDTO.getSkuCode());
	    } else {
	        LOGGER.info("No inventory found for SKU code: {}. Adding new inventory.", inventoryDTO.getSkuCode());
	        Inventory newInventory = new Inventory();
	        newInventory.setSkuCode(inventoryDTO.getSkuCode());
	        newInventory.setQuantity(inventoryDTO.getQuantity());
	        newInventory.setCreatedBy("system");
	        newInventory.setModifiedBy("system");
	        newInventory.setCreatedDate(new Timestamp(System.currentTimeMillis()));
	        newInventory.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
	        
	        inventoryDAO.save(newInventory);
	        LOGGER.info("New inventory added successfully for SKU code: {}", inventoryDTO.getSkuCode());
	    }
	}
}
