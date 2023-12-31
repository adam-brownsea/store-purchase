package au.bzea.storepurchase.service

import au.bzea.storepurchase.service.FiscalDataService
import au.bzea.storepurchase.model.CurrencyRate
import spock.lang.Specification
import java.text.SimpleDateFormat
import java.time.LocalDate

// These tests could be changed to use stubs and mocks as 
// needs internet connection to test getRate
class FiscalDataServiceTest extends Specification {

    def fiscalDataService = new FiscalDataService()
   
    //======
    // getRate method
    //======
    def "determines exchange rate"() {
        given: "we set currency to Aust"
        def currency = "Australia-Dollar"
        and: "we set the date to a valid date"
        def tranDate = LocalDate.parse("2023-05-01")
        
        when: "we request a conversion rate"
        def exchangeRate = fiscalDataService.getRate(currency, tranDate);

        then: "we receive a successful value"
        println(exchangeRate)
        exchangeRate != 0
    }

    def "exchange rate can't be found from fiscal API"() {
        given: "we set the currency to Aust"
        def currency = "Australia-Dollar"
        and: "we set the date to 50 year old date"
        def tranDate = LocalDate.parse("1973-05-01")

        when: "we request a conversion rate"
        def exchangeRate = fiscalDataService.getRate(currency, tranDate);
        println(exchangeRate)

        then: "we recieve a exchange rate not found error"
        exchangeRate == 0
    }

    def "exchange rate can't be found as dates after tran date"() {
        given: "we set the currency to Aust"
        def currency = "Australia-Dollar"
        and: "we set the date to recent but old date"
        def tranDate = LocalDate.parse("1973-05-01")

        when: "we request a conversion rate"
        def exchangeRate = fiscalDataService.getRate(currency, tranDate);

        then: "we recieve no exchange rate"
        exchangeRate == 0
    }

    //======
    // findCurrencyRate method
    //======
    def "returns actual date less than date param from find currency rate"() {
        given: "we set and array of currency rates"
        def currencyRates = new ArrayList<CurrencyRate>()
        def currencyRate1 = new CurrencyRate("Australia-Dollar", "1.494", "2023-03-31")
        currencyRates.add(currencyRate1)
        def currencyRate2 = new CurrencyRate("Australia-Dollar", "1.471", "2022-12-31")
        currencyRates.add(currencyRate2)
        
        and: "we set the transaction date to a valid date"
        def tranDate = LocalDate.parse("2023-04-01")

        when: "we request a conversion rate"
        def result = fiscalDataService.findCurrencyRate(currencyRates, tranDate)
        def effectiveDate = LocalDate.parse(result.getEffectiveDate())

        then: "we receive 1 currency rate"
        and: "a effective date less than tran date"
        !effectiveDate.isAfter(tranDate)
    }
    
    def "returns null when passed null array to find currency rate "() {
        given: "we have a null array"
        def currencyRates = new ArrayList<CurrencyRate>()
        and: "we set the transaction date to a valid date"
        def tranDate = LocalDate.parse("2023-04-01")

        when: "we request a conversion rate"
        def result = fiscalDataService.findCurrencyRate(currencyRates, tranDate)

        then:"we a null response"
        result == null
    }

    def "returns null when cant find date in array to find currency rate"() {
        given: "we set and array of currency rates"
        def currencyRates = new ArrayList<CurrencyRate>()
        def currencyRate1 = new CurrencyRate("Australia-Dollar", "1.494", "2023-03-31")
        currencyRates.add(currencyRate1)
        def currencyRate2 = new CurrencyRate("Australia-Dollar", "1.471", "2022-12-31")
        currencyRates.add(currencyRate2)
        
        and: "we set the transaction date to a valid date in the past"
        def tranDate = LocalDate.parse("2020-04-01")

        when: "we request a conversion rate"
        def result = fiscalDataService.findCurrencyRate(currencyRates, tranDate)

        then:"we a null response"
        result == null
    }
}
