package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssd.uz.wikiquickyapp.entity.Company;

public interface CompanyRepository extends JpaRepository<Company,Long> {

}
