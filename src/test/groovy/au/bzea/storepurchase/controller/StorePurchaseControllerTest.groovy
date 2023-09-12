package au.bzea.storepurchase.controller

import au.bzea.storepurchase.controller.StorePurchaseController
import spock.lang.Specification

class StorePurchaseControllerTest extends Specification {

    def "create purchase successfully stored"(){
        given: "we have have a purchase transaction"
        when: "and we request to store the transaction"
        then: "we retrieve a success result"
        false
    }

    def "create purchase where description is greater than 50"(){
        given: "we have a transaction"
        and: "and the description length is greater than 50"
        when: "we store the transction"
        then: "we get a error that description ti greater than 50"
        and: "a http status of 400"
        false
    }

    def "create purchase where date is invalid"(){
        given: ""
        when: ""
        then: ""
        false
    }

    def "create purchase where transaction date is future date"(){
        given: ""
        when: ""
        then: ""
        false
    }

    def "create purchase where amount is negative"(){
        given: ""
        when: ""
        then: ""
        false
    }

    def "create purchase where amount is not rounded to a cent"(){
        given: ""
        when: ""
        then: ""
        false
    }

    def "retrieve successful and conversion correct"(){
        given: ""
        when: ""
        then: ""
        false
    }

    def "retrieving purchase with id not found"(){
        given: ""
        when: ""
        then: ""
        false
    }

    def "retrieving purchase returns error when currency not valid"(){
        given: ""
        when: ""
        then: ""
        false
    }

    def "retrieving purchase return error when exchange rate can't be found"(){
        given: ""
        when: ""
        then: ""
        false
    }
 
    def "retrieving purchase successful and cent fractions rounded down"(){
        given: ""
        when: ""
        then: ""
        false
    }

    def "retrieving purchase successful and cent fractions rounded up"(){
        given: ""
        when: ""
        then: ""
        false
    }
}