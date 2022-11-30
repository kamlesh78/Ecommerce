package org.ttn.ecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.ttn.ecommerce.repository.TokenRepository.BlackListTokenRepository;
import org.ttn.ecommerce.services.tokenService.BlackListTokenService;

@SpringBootTest
public class BlackListTokenTest {

    @Autowired
    BlackListTokenRepository blackListTokenRepository;

    @Autowired
    BlackListTokenService blackListTokenService;

    @Test
    public void testBlackListService(){

        String token ="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrYW1sZXNoZGFuZ2k3OEBnbWFpbC5jb20iLCJpYXQiOjE2Njg3MDg0NTEsIlJPTEUiOiJST0xFX0NVU1RPTUVSIiwiZXhwIjoxNjY4NzA5MzUxfQ.79Two4cLJrKWFNACMLAT5Ctym0rbzLCVtUv3a-_3buH6ynSPJ6z08rZ0DHydvMm2VzW1CZizMg3hvZCuQwddFQ";
        System.out.println(blackListTokenService.blackListToken(token));



    }
}
