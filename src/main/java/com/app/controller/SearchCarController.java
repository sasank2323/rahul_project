package com.app.controller;


import com.app.entity.cars.Brand;
import com.app.entity.cars.Car;
import com.app.exceptions.InvalidParamException;
import com.app.repository.BrandRepository;
import com.app.repository.CarRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search-car")
public class SearchCarController {

    private BrandRepository brandRepository;
    private final CarRepository carRepository;

    public SearchCarController(CarRepository carRepository,BrandRepository brandRepository) {
        this.carRepository = carRepository;
        this.brandRepository = brandRepository;
    }

    //http://localhost:8080/api/v1/search-car/car?param=honda
    @GetMapping("/car")
    public  List<Car> searchCar (
            @RequestParam String param
    ){
        List<Car> cars = carRepository.searchCar(param);
        if (cars == null || cars.isEmpty()) {
            throw new InvalidParamException("This value does not exist in DB");
        }
        return cars;
    }

    //http://localhost:8080/api/v1/search-car/cars?pageNo=0&pageSize=2&sortBy=id&sortDir=asc
    @GetMapping("/cars")
    public List<Car> listAll(
            @RequestParam (defaultValue = "0", required = false)int pageNo,
            @RequestParam (defaultValue = "1", required = false)int pageSize,
            @RequestParam (defaultValue = "id", required = false)String sortBy,
            @RequestParam (defaultValue = "asc", required = false)String sortDir
    ){
        Sort sort = sortDir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable page = PageRequest.of(pageNo,pageSize,sort);
        Page<Car> records = carRepository.findAll(page);
        List<Car> listCar= records.getContent();
        return listCar;
    }
}
