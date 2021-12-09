package ssd.uz.wikiquickyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.repository.CompanyRepository;

@Service
public class CompaniyService {

    @Autowired
    CompanyRepository companyRepository;

    public ApiResponseModel getAllCompanies(){
        return new ApiResponseModel(true,"junatildi !",companyRepository.findAll());
    }

}
