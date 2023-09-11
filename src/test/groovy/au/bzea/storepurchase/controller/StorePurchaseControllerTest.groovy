package au.bzea.storepurchase.controller

import au.bzea.storepurchase.controller.StorePurchaseController
import spock.lang.Specification

class StorePurchaseControllerTest extends Specification {

    def "new puchase successfully stored"(){
        given: "we have have a purchase transaction"
        when: "and we request to store the transaction"
        then: "we retrieve a success result"
        assert false
    }

    def "new puchase and not unique id"(){
        given: ""
        when: ""
        then: ""
        assert false
    }

    def "new purchase where description is greater than 50"(){
        given: ""
        when: ""
        then: ""
        assert false
    }

    def "new purchase where date is invalid"(){
        given: ""
        when: ""
        then: ""
        assert false
    }

    def "new purchase where transaction date is future date"(){
        given: ""
        when: ""
        then: ""
        assert false
    }

    def "new purchase where amount is negative"(){
        given: ""
        when: ""
        then: ""
        assert false
    }

    def "new purchase where amount is not rounded to a cent"(){
        given: ""
        when: ""
        then: ""
        assert false
    }

    def "retrieve successful and conversion correct"(){
        given: ""
        when: ""
        then: ""
        assert false
    }

    def "retrieving purchase with id not found"(){
        given: ""
        when: ""
        then: ""
        assert false
    }

    def "retrieving purchase returns error when currency not valid"(){
        given: ""
        when: ""
        then: ""
        assert false
    }

    def "retrieving purchase return error when exchange rate can't be found"(){
        given: ""
        when: ""
        then: ""
        assert false
    }
 
    def "retrieving purchase successful and cent fractions rounded down"(){
        given: ""
        when: ""
        then: ""
        assert false
    }

    def "retrieving purchase successful and cent fractions rounded up"(){
        given: ""
        when: ""
        then: ""
        assert false
    }
}