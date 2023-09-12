package au.bzea.storepurchase.service

import au.bzea.storepurchase.service.FiscalDataService
import au.bzea.storepurchase.model.CurrencyRate
import spock.lang.Specification
import java.text.SimpleDateFormat
import java.time.LocalDate

class FiscalDataServiceTest extends Specification {

    //======
    // getRate method
    //======
    def "determines exchange rate"() {
        given: "we set currency to Aust"
        def currency = "Australia-Dollar"
        and: "we set the date to a valid date"
        def tranDate = LocalDate.parse("2023-05-01", "yyyy-MM-dd")
        
        when: "we request a conversion rate"
        def exchangeRate = FiscalDataService.getRate(currency, tranDate);

        then: "we receive a successful value"
        println(exchangeRate)
        exchangeRate != 0
    }

    def "exchange rate can't be found from fiscal API"() {
        given: "we set the currency to Aust"
        def currency = "Australia-Dollar"
        and: "we set the date to 50 year old date"
        def tranDate = LocalDate.parse("1973-05-01", "yyyy-MM-dd")

        when: "we request a conversion rate"
        def exchangeRate = FiscalDataService.getRate(currency, tranDate);
        println(exchangeRate)

        then: "we recieve a exchange rate not found error"
        exchangeRate == 0
    }

    // Mock/Stub
    def "exchange rate can't be found as dates after tran date"() {
        given: "we set the currency to Aust"
        def currency = "Australia-Dollar"
        and: "we set the date to recent but old date"
        def tranDate = LocalDate.parse("1973-05-01", "yyyy-MM-dd")

        when: "we request a conversion rate"

        then: "we recieve a exchange rate not found error"
        false
    }


    //======
    // getDateRange method
    //======
    def "tran date is today, returns date from today and 6 months ago"() {
        given: "we set trnsaction date to today"
        def tranDate = LocalDate.now()

        when: "we request a date range"
        def dates = FiscalDataService.getDateRange(tranDate);
        println(tranDate)
        println(dates[1])
        
        then: "we receive expected date ranges"
        tranDate.minusMonths(6) == dates[0]
        tranDate == dates[1]

    }
    
    def "tran date 1 month ago, returns date from today and 6 months ago"() {
        given: "we set trnsaction date to 1 month ago"
        def pattern = "yyyy-MM-dd"
        def tranDate = LocalDate.now().minusMonths(1)
        def currDate = LocalDate.now()

        when: "we request a conversion rate"
        def dates = FiscalDataService.getDateRange(tranDate);
        
        then: "we receive expected date ranges"
        currDate.minusMonths(6) == dates[0]
        currDate == dates[1]
    }

    def "tran date 4 month ago, returns 3 months each from date"() {
        given: "we set trnsaction date to 4 months ago"
        def pattern = "yyyy-MM-dd"
        def tranDate = LocalDate.now().minusMonths(4)

        when: "we request a conversion rate"
        def dates = FiscalDataService.getDateRange(tranDate);

        then: "we receive expected date ranges"
        tranDate.minusMonths(3) == dates[0]
        tranDate.plusMonths(3) == dates[1]
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
        def tranDate = LocalDate.parse("2023-04-01", "yyyy-MM-dd")

        when: "we request a conversion rate"
        def result = FiscalDataService.findCurrencyRate(currencyRates, tranDate)
        def effectiveDate = LocalDate.parse(result.getEffectiveDate(), "yyyy-MM-dd")

        then: "we receive 1 currency rate"
        and: "a effective date less than tran date"
        !effectiveDate.isAfter(tranDate)
    }
    
    //TODO 
    def "returns null when passed null array to find currency rate "() {
        given: ""
        when: ""
        then:""
        false
    }

    //TODO 
    def "returns null when cant find date in array to find currency rate"() {
        given: ""
        when: ""
        then:""
        false
    }
}