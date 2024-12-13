package com.example.demo.repository;

import com.example.demo.entity.Blob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlobRepository extends JpaRepository<Blob , String>
{}
