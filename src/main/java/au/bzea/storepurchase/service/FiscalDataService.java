package au.bzea.storepurchase.service;

import au.bzea.storepurchase.model.FiscalDataset;
import au.bzea.storepurchase.model.CurrencyRate;

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

public class FiscalDataService {
    private static Logger logger = Logger.getLogger(FiscalDataService.class.getName());

    private static final int FULL_RANGE = 6;

    private static final int HALF_RANGE = 3;

    public static BigDecimal getRate(String currency, LocalDate transactionDate) {
        
        // Get the date range to search
        LocalDate[] dateRange = getDateRange(transactionDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String url = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";
        String fields = "?fields=country_currency_desc,exchange_rate,effective_date";
        String filter = "&filter=country_currency_desc:eq:" + currency 
            + ",effective_date:gte:" + dateRange[0].format(formatter)
            + ",effective_date:lte:" + dateRange[1].format(formatter);

        logger.info("Fields: "+ fields);
        logger.info("Filter: "+ filter);
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url + fields + filter))
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

		//System.out.println(response.body());
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

    // We require a 6 month range to search for dates that 
    // will likely have our date require exchange rate
    private static LocalDate[] getDateRange(LocalDate transactionDate) {
        
        // Get current date for comparisions
        LocalDate currentDate = LocalDate.now();

        LocalDate begRange = transactionDate;
        LocalDate endRange = transactionDate;
        if (transactionDate.equals(currentDate)) {
            // If transactionDate is equal to the current date, subtract 6 months from start range
            begRange = transactionDate.minusMonths(FULL_RANGE);
            endRange = currentDate;
        } else if (transactionDate.plusMonths(HALF_RANGE).isAfter(currentDate)) { 
            // If transactionDate + 3 months is after the current date, set the range accordingly
            begRange = currentDate.minusMonths(FULL_RANGE);
            endRange = currentDate;
        } else {
            // set the range to 3 months before and 3 months after transactionDate
            begRange = transactionDate.minusMonths(HALF_RANGE);
            endRange = transactionDate.plusMonths(HALF_RANGE);
        }

        // put the dates in the array
        LocalDate[] dateRange = { begRange, endRange };

        logger.info("TranDate: " + transactionDate);
        logger.info("BegDate: " + begRange.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        logger.info("EndDate: " + endRange.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        return dateRange;
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