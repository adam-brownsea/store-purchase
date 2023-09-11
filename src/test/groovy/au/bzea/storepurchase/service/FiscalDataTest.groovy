package au.bzea.storepurchase.service

import au.bzea.storepurchase.service.FiscalData
import spock.lang.Specification

class FiscalDataTest extends Specification {

    def "FiscalData determines exchange rate"() {
        given: "we set currency to Aust"
        and: "we set the date to a valid date"
        when: "we request a conversion rate"
        then: "we receive a successful value"
        false
    }
    def "FiscalData returns date from and to 3 months from each way date param"() {
        given: "we set the currency to Aust"
        and: "we set the date to 4 months ago"
        when: "we request a conversion rate"
        then: "we receive a successful value"
        and: "searched From 3 months before our date param"
        and: "searched To date 3 motnhs after date param "
        false
    }
    def "FiscalData returns actual date less than date param"() {
        given: "we set the currency to Aust"
        and: "we set the date to a valid date"
        when: "we request a conversion rate"
        then: ""
        false
    }
    def "FiscalData returns error when currency not valid"() {
        given: "we set the currency to Error"
        and: "we set the date to a valid date"
        when: "we request a conversion rate"
        then: "we receive invalid currency error"
        false
    }
    def "FiscalData return error when exchange rate can't be found"() {
        given: "we set the currency to Aust"
        and: "we set the date to 50 year old date"
        when: "we request a conversion rate"
        then: "we recieve a exchange rate not found error"
        false
    }
}
