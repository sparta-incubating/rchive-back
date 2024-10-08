package kr.sparta.rchive.global.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import kr.sparta.rchive.domain.post.exception.PostCustomException;
import kr.sparta.rchive.global.execption.GlobalCustomException;
import kr.sparta.rchive.global.execption.GlobalExceptionCode;
import kr.sparta.rchive.global.s3.exception.S3CustomException;
import kr.sparta.rchive.global.s3.exception.S3ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j(topic = "s3 image upload")
@Service
@RequiredArgsConstructor
public class S3ImageService {
    private static final String thumbnailDirectoryName = "thumbnails/dev/";
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public String getUrlAfterUpload(MultipartFile image) {
        String originalName = image.getOriginalFilename();
        validateImageFile(originalName);

        String randomImageName = getRandomImageName(originalName);
        log.info("s3 upload name : {}", randomImageName);

        try {
            PutObjectRequest request = new PutObjectRequest(
                    bucketName,
                    randomImageName,
                    image.getInputStream(),
                    createObjectMetadata(originalName)
            )
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(request);
        } catch (IOException e) {
            throw new S3CustomException(S3ExceptionCode.INTERNAL_SERVER_ERROR_IMAGE_UPLOAD_FAIL);
        }

        String url = amazonS3.getUrl(bucketName, randomImageName).toString();
        log.info("s3 url : {}", url);
        return url;
    }

    private void validateImageFile(String originalName) {
        int lastDotIndex = originalName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new S3CustomException(S3ExceptionCode.BAD_REQUEST_IMAGE_EXTENSION_NOT_EXIST);
        }

        String extention = originalName.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "bmp");

        if (!allowedExtentionList.contains(extention)) {
            throw new S3CustomException(S3ExceptionCode.BAD_REQUEST_IMAGE_EXTENSION_MISMATCH);
        }

    }

    private String getRandomImageName(String originalName) {
        String random = UUID.randomUUID().toString();
        return thumbnailDirectoryName + random + '-' + originalName;
    }

    private ObjectMetadata createObjectMetadata(String originalName) {
        ObjectMetadata metadata = new ObjectMetadata();
        String extention = originalName.substring(originalName.lastIndexOf("."));
        metadata.setContentType("image/" + extention);
        return metadata;
    }

    public void deleteS3Image(String imageUrl) {
        String key = getKeyFromImageAddress(imageUrl);
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception e) {
            throw new S3CustomException(S3ExceptionCode.INTERNAL_SERVER_ERROR_IMAGE_DELETE_FAIL);
        }
    }

    private String getKeyFromImageAddress(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            return URLDecoder.decode(url.getPath(), "UTF-8").substring(1);
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            throw new S3CustomException(S3ExceptionCode.INTERNAL_SERVER_ERROR_IMAGE_DELETE_FAIL);
        }
    }
}
