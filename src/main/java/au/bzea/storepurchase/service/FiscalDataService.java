package au.bzea.storepurchase.service;

import au.bzea.storepurchase.model.FiscalDataset;
import au.bzea.storepurchase.model.CurrencyRate;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.net.URI;
import java.net.http.HttpClient;
import java.io.IOException;
import java.math.BigDecimal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FiscalDataService {
    private static Logger logger = Logger.getLogger(FiscalDataService.class.getName());

    private static final int MINUS_MONTHS = 6;

    public BigDecimal getRate(String currency, LocalDate transactionDate) {
        
        // Get the date range to search
        LocalDate fromDate = transactionDate.minusMonths(MINUS_MONTHS);;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        currency = Helper.encodeString(currency);
        String url = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";
        String fields = "?fields=country_currency_desc,exchange_rate,effective_date";
        String filter = "&filter=country_currency_desc:eq:" + currency 
            + ",effective_date:gte:" + fromDate.format(formatter)
            + ",effective_date:lte:" + transactionDate.format(formatter);

        logger.info("Fields: "+ fields);
        logger.info("Filter: "+ filter);
        String uri = url + fields + filter;
        logger.info("Uri: "+ uri);
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uri))
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
        HttpResponse<String> response = null;
		
        try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			e.printStackTrace();
            return new BigDecimal(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
            return new BigDecimal(0);
		}

		logger.info(response.body());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            FiscalDataset fiscal = objectMapper.readValue(response.body(), FiscalDataset.class);        
            // Find rate for date in the Fiscal dataset
            CurrencyRate currencyRate = findCurrencyRate(fiscal.getData(), transactionDate);

            return currencyRate != null && currencyRate.getExchangeRate() != null 
                ? new BigDecimal(currencyRate.getExchangeRate()) : new BigDecimal(0);

        } catch (JsonProcessingException je) {
            logger.log(Level.SEVERE, je.getMessage());
            System.out.println(je.getMessage());
        }


        return new BigDecimal(0);
    }

    // Find the appropriate current rate record and return the current rate.
    private static CurrencyRate findCurrencyRate(List<CurrencyRate> currencyRates, LocalDate transactionDate) {

       // exception if data section empty
        if (currencyRates.isEmpty()) {
            return null;
        }

        CurrencyRate returnRate = null;

        for (CurrencyRate currencyRate: currencyRates) {
            LocalDate effectiveDate = LocalDate.parse(currencyRate.getEffectiveDate());
            // check if effectdate not after transaction date, and update result rate 
            if (!effectiveDate.isAfter(transactionDate)) {
                if (returnRate == null || effectiveDate.isAfter(LocalDate.parse(returnRate.getEffectiveDate()))) {
                    logger.info(currencyRate.toString());
                    returnRate = currencyRate;
                }
            } 
        }
        return returnRate;
    }

}