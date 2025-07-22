package com.shadangi54.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shadangi54.inventory.DTO.InventoryDTO;
import com.shadangi54.inventory.entity.Inventory;

@Repository
public interface InventoryDAO extends JpaRepository<Inventory, Long>{
	
	/**
	 * Finds inventory items by their SKU codes.
	 *
	 * @param skuCodes List of SKU codes to search for.
	 * @return List of Inventory objects matching the given SKU codes.
	 */
	List<Inventory> findBySkuCodeIn(List<String> skuCode);

	/**
	 * Finds an inventory item by its SKU code.
	 *
	 * @param skuCode The SKU code to search for.
	 * @return Inventory object matching the given SKU code, or null if not found.
	 */
	Inventory findBySkuCode(String skuCode);
	

}
