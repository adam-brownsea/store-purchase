package au.bzea.storepurchase.service;

import au.bzea.storepurchase.model.Fiscal;
import au.bzea.storepurchase.model.CurrencyRate;

import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FiscalData {
    private static Logger logger = Logger.getLogger(FiscalData.class.getName());

    public static String getRate(String currency) {
        String url = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";
        String fields = "?fields=country_currency_desc,exchange_rate,record_date";
        String filter = "&filter=country_currency_desc:eq:" + currency + 
            ",record_date:gte:2021-07-01,record_date:lte:2022-07-01";

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url + fields + filter))
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
        HttpResponse<String> response = null;
		
        try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//System.out.println(response.body());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Fiscal fiscal = objectMapper.readValue(response.body(), Fiscal.class);        
            return fiscal.getData().get(0).getCountryCurrencyDesc();
        } catch (JsonProcessingException je) {
            logger.log(Level.SEVERE, je.getMessage());
            System.out.println(je.getMessage());
        }
        return "";
    }

}