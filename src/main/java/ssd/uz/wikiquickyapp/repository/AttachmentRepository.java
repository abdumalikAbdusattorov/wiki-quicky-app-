package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssd.uz.wikiquickyapp.entity.Attachment;


public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

}
