package com.diplom.drinksmachine.web.uploadImg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;


@Service
public class UploadImage {

    @Value("${imgbb.token}")
    private String imgBBToken;

    public String writeToStore(byte[] fileBytes) throws Exception {
        String url = "https://api.imgbb.com/1/upload?key=" + imgBBToken;
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("image", Base64.getEncoder().encode(fileBytes));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ImgBBRes> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ImgBBRes.class);

        return response.getBody().getData().getDisplay_url();
    }
}
