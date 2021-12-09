package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssd.uz.wikiquickyapp.entity.AttachmentType;
import ssd.uz.wikiquickyapp.entity.enums.AttachmentTypeEnum;

public interface AttachmentTypeRepository extends JpaRepository<AttachmentType, Long> {
    AttachmentType findByType(AttachmentTypeEnum type);
}