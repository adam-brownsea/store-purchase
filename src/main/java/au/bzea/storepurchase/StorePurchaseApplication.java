package au.bzea.storepurchase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class StorePurchaseApplication {

	public static void main(String[] args)  {
		SpringApplication.run(StorePurchaseApplication.class, args);
        /* 
        try {
            System.out.println(CountryCurrencies.getCurrencies().size());
        } catch (Exception e) {
            System.out.println(e);
       
        }
        */
        //FiscalData.getRate("Australia-Dollar");
	}

}
