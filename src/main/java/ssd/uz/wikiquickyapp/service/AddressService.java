package ssd.uz.wikiquickyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.entity.Address;
import ssd.uz.wikiquickyapp.entity.Location;
import ssd.uz.wikiquickyapp.payload.ReqAddress;
import ssd.uz.wikiquickyapp.payload.ReqLocation;
import ssd.uz.wikiquickyapp.repository.AddressRepository;
import ssd.uz.wikiquickyapp.repository.LocationRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    LocationRepository locationRepository;

    public Address addAddress(ReqAddress reqAddress) {
        Address address = new Address();
        address.setDistance(reqAddress.getDistance());
        address.setLocations(addLocations(reqAddress.getReqLocations()));
        return addressRepository.save(address);
    }

    private List<Location> addLocations(List<ReqLocation> reqLocations) {
        return locationRepository.saveAll(
                reqLocations.stream().map(reqLocation -> addLocation(reqLocation)).collect(Collectors.toList())
        );
    }

    public Location addLocation(ReqLocation reqLocation) {
        return locationRepository.save(new Location(reqLocation.getLan(), reqLocation.getLat()));
    }

}
