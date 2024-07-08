package org.abondar.industrial.heromanager.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private SetOperations<String, Object> setOperations;

    private static final String REDIS_KEY = "authToken:";

    private static final String TEST_TOKEN = "testToken";

    @Test
    public void saveTokenTest(){
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        tokenService.saveToken(TEST_TOKEN);

        verify(valueOperations).set(eq(REDIS_KEY+TEST_TOKEN),eq(TEST_TOKEN),eq(600L),eq(TimeUnit.SECONDS));
    }


    @Test
    public void testTokenExists() {
        when(redisTemplate.opsForSet()).thenReturn(setOperations);

        when(setOperations.getOperations()).thenReturn(redisTemplate);

        when(redisTemplate.hasKey(REDIS_KEY + TEST_TOKEN)).thenReturn(true);

        assertTrue(tokenService.tokenExists(TEST_TOKEN));

        verify(redisTemplate).hasKey(REDIS_KEY + TEST_TOKEN);
    }
}
