package com.example.demo.controller;

import com.example.demo.dto.BlobRequest;
import com.example.demo.dto.BlobResponse;
import com.example.demo.service.BlobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/blobs")
public class BlobController
{
	private static final Logger logger = LoggerFactory.getLogger(BlobController.class);
	private final BlobService blobService;

	public BlobController(BlobService blobService) {
		this.blobService = blobService;
	}

	@PostMapping
	public ResponseEntity<BlobResponse> storeBlob(@RequestBody BlobRequest request) {
		BlobResponse blobResponse = blobService.saveBlob(request);
		return ResponseEntity.status(blobResponse.statusCode()).body(blobResponse);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BlobResponse> retrieveBlob(@PathVariable String id) {
		BlobResponse blobResponse = blobService.getBlobById(id);
		return ResponseEntity.status(blobResponse.statusCode()).body(blobResponse);
	}
}
