package au.bzea.storepurchase.service

import au.bzea.storepurchase.service.Helper
import spock.lang.Specification
import spock.lang.Unroll
import java.math.BigDecimal;


class HelperTest extends Specification {

    @Unroll
    def "round amount #amount"(BigDecimal amount){
        given: "we have an amount"
        when: "we round the amount"
        def result = Helper.roundAmount(amount)

        then: "there are 2 decimal places"
        def stringResult = result.toPlainString();
        stringResult.substring(stringResult.indexOf(".") + 1).length() == 2
        and: "expected result is correct"
        result == expected

        where:
        amount      | expected
        1.1234      | 1.12
        1.555       | 1.56
        12345.233   | 12345.23
        9854.9567   | 9854.96
        1234        | 1234.00
        1           | 1.00
    }

    @Unroll
    def "encode a string"(value) {
        given: "we have a string"
        when: "we encode it"
        def result = Helper.encodeString(value)

        then: "we get expected result"
        result == expected

        where: 
        value                   | expected
        "Australia-Dollar"      |"Australia-Dollar"
        "United Kingdom-Pound"  |"United+Kingdom-Pound"
        "Canada-Dollar"         |"Canada-Dollar"
        "Euro Zone-Euro"        |"Euro+Zone-Euro"
        "New Zealand-Dollar"    |"New+Zealand-Dollar"
    }

    @Unroll
    def "decode a string"(value) {
        given: "we have a string"
        when: "we encode it"
        def result = Helper.decodeString(value)

        then: "we get expected result"
        result == expected

        where: 
        value                   | expected
        "Australia-Dollar"      |"Australia-Dollar"
        "United+Kingdom-Pound"  |"United Kingdom-Pound"
        "Canada-Dollar"         |"Canada-Dollar"
        "Euro%20Zone-Euro"      |"Euro Zone-Euro"
        "New+Zealand-Dollar"    |"New Zealand-Dollar"
    }
}