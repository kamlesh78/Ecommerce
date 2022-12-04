package org.ttn.ecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.ttn.ecommerce.repository.TokenRepository.BlackListTokenRepository;
import org.ttn.ecommerce.repository.categoryRepository.CategoryRepository;
import org.ttn.ecommerce.services.impl.LogoutServiceImpl;

@SpringBootTest
public class BlackListTokenTest {

    @Autowired
    BlackListTokenRepository blackListTokenRepository;

    @Autowired
    LogoutServiceImpl blackListTokenService;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void testBlackListService(){

    }
}
