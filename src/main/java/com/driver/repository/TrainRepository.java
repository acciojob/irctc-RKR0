package com.driver.repository;

import com.driver.model.Train;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface TrainRepository extends JpaRepository<Train,Integer> {

	
	@Query(value = "select * from trains where route = %:str% ",nativeQuery = true)
    List<Train> getStation(String str);
}
