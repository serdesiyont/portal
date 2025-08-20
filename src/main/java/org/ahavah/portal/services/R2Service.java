package org.ahavah.portal.services;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class R2Service {

    private final AmazonS3 s3Client;

    public String uploadFile(String bucketName, String bucketUrl, MultipartFile file) throws IOException {

            String fileName = file.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // Read the file into a byte array to make the stream retryable
            byte[] bytes = file.getBytes();
            InputStream inputStream = new ByteArrayInputStream(bytes);

            // Create the PutObjectRequest to send the file to R2.
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName,
                    fileName,
                    inputStream,
                    metadata
            );

            s3Client.putObject(putObjectRequest);

            // Construct and return the public URL of the uploaded object.
            return bucketUrl+"/" + fileName;


    }
}