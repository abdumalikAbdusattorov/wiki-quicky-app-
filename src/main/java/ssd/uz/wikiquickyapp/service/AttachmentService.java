package ssd.uz.wikiquickyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ssd.uz.wikiquickyapp.collection.AttachmentCol;
import ssd.uz.wikiquickyapp.entity.Attachment;
import ssd.uz.wikiquickyapp.entity.AttachmentContent;
import ssd.uz.wikiquickyapp.entity.AttachmentType;
import ssd.uz.wikiquickyapp.entity.enums.AttachmentTypeEnum;
import ssd.uz.wikiquickyapp.exception.ResourceNotFoundException;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.payload.ResUploadFile;
import ssd.uz.wikiquickyapp.repository.AttachmentContentRepository;
import ssd.uz.wikiquickyapp.repository.AttachmentRepository;
import ssd.uz.wikiquickyapp.repository.AttachmentTypeRepository;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class AttachmentService {

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentTypeRepository attachmentTypeRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;

    @Transactional
    public ApiResponseModel uploadFile(MultipartHttpServletRequest request) throws IOException {
        Iterator<String> iterator = request.getFileNames();
        MultipartFile file;
        List<ResUploadFile> resUploadFiles = new ArrayList<>();
        while (iterator.hasNext()) {
            file = request.getFile(iterator.next());
            if ((file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png") || file.getContentType().equals("image/jpg")) && file.getSize() > 10240000) {
                return new ApiResponseModel(false, "Rasmning hajmi 2 MB dan ortiq.", resUploadFiles);
            }

            if (request.getParameter("type") != null) {

                AttachmentType type = attachmentTypeRepository.findByType(AttachmentTypeEnum.valueOf(request.getParameter("type")));

                if (!type.getContentTypes().contains(file.getContentType())) {
                    return new ApiResponseModel(false, "bunday tayp bazada yuq !", resUploadFiles);
                }
                if (type.equals(attachmentTypeRepository.findByType(AttachmentTypeEnum.USER_AVATAR))) {
                    if (file.getSize() > attachmentTypeRepository.findByType(AttachmentTypeEnum.USER_AVATAR).getSize()) {
                        return new ApiResponseModel(false, "Rasmning hajmi 1 MB dan ortiq.", resUploadFiles);
                    }
                }

                BufferedImage image = ImageIO.read(file.getInputStream());

                if (image.getWidth() > type.getWidth() || image.getHeight() > type.getHeight()) {
                    return new ApiResponseModel(false, "Rasmning o'lchovi mos emas. Tavsiya qilingan o'lcham: " + type.getWidth() + " x " + type.getHeight(), resUploadFiles);
                }

                AttachmentContent attachmentContent = attachmentContentRepository.save(new AttachmentContent(file.getBytes()));
                Attachment attachment = attachmentRepository.save(new Attachment(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getSize(),
                        type,
                        attachmentContent));
                resUploadFiles.add(new ResUploadFile(attachment.getId(),
                        attachment.getName(),
                        ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attach/").path(attachment.getId().toString()).toUriString(),
                        attachment.getContentType(),
                        attachment.getSize()));
                return new ApiResponseModel(true, "Saved", resUploadFiles);
            }
        }
        return new ApiResponseModel(false, "Not saved");
    }

    public HttpEntity<?> getAttachmentContent(Long attachmentId) {

        Attachment attachment = attachmentRepository.findById(attachmentId).orElseThrow(() -> new ResourceNotFoundException("Attachment", "id", attachmentId));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getName() + "\"")
                .body(attachment.getAttachmentContent().getContent());
    }

    @Transactional
    public ApiResponse deleteAttachment(Long attachmentId) {
        if (attachmentRepository.existsById(attachmentId)) {
            attachmentRepository.deleteById(attachmentId);
            return new ApiResponse("Attachment deleted", true);
        }
        return new ApiResponse("Attachment not deleted", false);
    }
}
