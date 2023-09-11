package au.bzea.storepurchase.service

import au.bzea.storepurchase.service.Currencies
import spock.lang.Specification
import spock.lang.Unroll

class CurrenciesTest extends Specification {

   def currencies = []

   def setup() {
      given: "we load the currencies file"
      try {
         currencies = Currencies.getCurrencies()   
      } catch (Exception e) {
         println("exception caught : " e.getMessage())
      }
      assert currencies != null
   }

   def "count total currencies is 169"() {
      when: "we get the results"
      assert !currencies.isEmpty()

      then: "the count should be 169"
      currencies.size() == 169
    }

   def "USD is not included currencies"() {
      when: "we get the results"
      assert !currencies.isEmpty()

      then: "the list should include AUD"
      !currencies.contains("United States-Dollar")
   }

   @Unroll
   def "#currency is included currencies list"(String currency) {
      when: "we get the results"
      assert !currencies.isEmpty()

      then: "the list should include currency"
      currencies.contains(currency)

      where:
      currency                |_
      "Australia-Dollar"      |_
      "United Kingdom-Pound"  |_
      "Canada-Dollar"         |_
      "Euro Zone-Euro"        |_
      "New Zealand-Dollar"    |_
   }
}