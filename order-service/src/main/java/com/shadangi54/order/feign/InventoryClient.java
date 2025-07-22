package com.shadangi54.order.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.shadangi54.order.DTO.InventoryDTO;


@FeignClient(name = "${inventory.service.name}", url = "${inventory.service.url}")
public interface InventoryClient {
	
	@GetMapping("/inventory")
	public ResponseEntity<List<InventoryDTO>> checkStock(@RequestParam List<String> skuCodes);
	
	@PostMapping("/inventory")
	public ResponseEntity<String> updateInventory(@RequestBody List<InventoryDTO> inventoryDTOList);
}
