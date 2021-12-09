package ssd.uz.wikiquickyapp.collection;

import java.util.Date;
import java.util.List;

public interface UsersCol {
    String getId();

    String getFirstName();

    String getLastName();

    String getPhoneNumber();

    String getEmail();

    Date getDateOfBirth();

    String getLivingAddress();

    Double getBalance();

    Double getProfitOfDay();

    Long getClientOrder();

    Long getWorkerOrder();

    Double getPoints();

    Boolean getIsVerified();

    Boolean getIsCheckedWorker();

    Long getAvatarPhoto();

    List<Long> getPassportPhotos();

    Long getVehicle();
}
