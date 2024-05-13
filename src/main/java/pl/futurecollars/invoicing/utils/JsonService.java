package pl.futurecollars.invoicing.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class JsonService {

  private final ObjectMapper mapper;

  public JsonService() {
    mapper = new ObjectMapper();
    mapper.findAndRegisterModules();
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  public String toJson(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (IOException e) {
      throw new RuntimeException("Failed to convert object to JSON", e);
    }
  }

  public <T> T toObject(String json, Class<T> clazz) {
    try {
      return mapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to convert JSON to object", e);
    }
  }
}
