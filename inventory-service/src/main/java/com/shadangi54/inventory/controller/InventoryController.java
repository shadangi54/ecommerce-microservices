package com.shadangi54.inventory.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shadangi54.inventory.DTO.InventoryDTO;
import com.shadangi54.inventory.manager.InventoryManager;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/inventory")
@AllArgsConstructor
public class InventoryController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);
	
	private InventoryManager inventoryManager;
	
	@GetMapping
	public ResponseEntity<List<InventoryDTO>> checkStock(@RequestParam List<String> skuCodes){
		LOGGER.info("Checking stock for SKU codes: {}", skuCodes);
		try {
			List<InventoryDTO> inventoryList = inventoryManager.checkStock(skuCodes);
			return ResponseEntity.ok(inventoryList);
		} catch (Exception e) {
			LOGGER.error("Error checking stock for SKU codes: {}", skuCodes, e);
			return ResponseEntity.status(500).body(null);
		}
		
	}
	
	
	@PostMapping
	public ResponseEntity<String> updateInventory(@RequestBody List<InventoryDTO> inventoryDTOList) {
		LOGGER.info("Updating inventory for SKU codes: {}", inventoryDTOList);
		try {
			for (InventoryDTO inventoryDTO : inventoryDTOList) {
				inventoryManager.updateInventory(inventoryDTO);
			}
			return ResponseEntity.ok("Inventory updated successfully");
		} catch (Exception e) {
			LOGGER.error("Error updating inventory for SKU codes: {}", inventoryDTOList, e);
			return ResponseEntity.status(500).body("Failed to update inventory");
		}
	}
	
	
}
