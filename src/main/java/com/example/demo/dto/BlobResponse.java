package com.example.demo.dto;

import com.example.demo.entity.Blob;

public record BlobResponse(String message, BlobData data ,int statusCode)
{}
