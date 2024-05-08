package pl.futurecollars.invoicing;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InvoiceApplication {

  public static void main(String[] args) throws FileNotFoundException {
    SpringApplication.run(InvoiceApplication.class, args);

    InputStream inputStream = new FileInputStream("xyz.json");
    Reader reader = new InputStreamReader(inputStream);

    InputStream inputStream2 = new FileInputStream("xyz2.json");
    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream2);

  }
}
