package com.slide_todo.slide_todoApp.util.jwt;

import com.slide_todo.slide_todoApp.util.redis.TokenRedisConnector;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class RedisBlacklist implements Blacklist {

  private final TokenRedisConnector tokenRedisConnector;
  private SimpleDateFormat formatter =
      new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

  @Override
  public void putToken(String token, String date) {
    Date parsedDate;
    try {
      parsedDate = formatter.parse(date);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    long ttl = parsedDate.getTime() - new Date(System.currentTimeMillis()).getTime();
    tokenRedisConnector.setWithTtl(token, date, ttl);
  }

  @Override
  public boolean containsToken(String token) {
    return tokenRedisConnector.exists(token);
  }

}
