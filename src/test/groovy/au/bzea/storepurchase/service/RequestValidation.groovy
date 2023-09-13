package au.bzea.storepurchase.service

import au.bzea.storepurchase.service.RequestValidation
import au.bzea.storepurchase.model.RequestTransaction
import spock.lang.Specification
import static groovy.json.JsonOutput.toJson

class RequestValidationTest extends Specification {

/*
    Map request = [
            transactionDate : "2022-12-31",
            description : "My test transaction post with 51 characters length.",
            usdAmount : "123.34"
        ]
*/
    def "all valid so no errors returned"() {
        given: "we have request with valid data"
        def request = new RequestTransaction("2022-12-31", "My test transaction.","123.34")

        when: "we validate the request"
        def errors = RequestValidation.validate(request)

        then: "we are returned no error values"
        errors.size() == 0
    }

    def "request supplied is empty return single error"() {
        given: "we have an empty request"
        def request = null
        when: "we validate the request"
        def errors = RequestValidation.validate(request)
        then: "we receive an error of empty request "
        errors.size() == 1
        errors[0] == "Empty request"
    }

    def "transaction date not supplied single error"() {
        given: "we have request with no tran date"
        def request = new RequestTransaction(null, "My test transaction.","123.34")

        when: "we validate the request"
        def errors = RequestValidation.validate(request)

        then: "we receive a single error"
        errors.size() == 1
        then: "that states missing tran date"
        errors[0] == "Transaction date is required"
    }

    def "transaction date is not valid date"() {
        given: "we have request with an invalid tran date"
        def request = new RequestTransaction("2022-12-32", "My test transaction.","123.34")

        when:  "we validate the request"
        def errors = RequestValidation.validate(request)

        then: "we receive a single error"
        errors.size() == 1
        then: "that states missing tran date"
        errors[0] == "Transaction date is not valid date"
    }

    def "transaction date is in the future"() {
        given: "we have request with future tran date"
        def request = new RequestTransaction("2023-12-31", "My test transaction.","123.34")

        when:  "we validate the request"
        def errors = RequestValidation.validate(request)

        then: "we receive a single error"
        errors.size() == 1
        then: "that states missing tran date"
        errors[0] == "Transaction date cannot be in the future"
    }

    def "description is longer than 50 characters"() {
        given: "we have request with description with length > 50"
        def request = new RequestTransaction("2022-12-31", "My test transaction post with 51 characters length.","123.34")

        when:  "we validate the request"
        def errors = RequestValidation.validate(request)

        then: "we receive a single error"
        errors.size() == 1
        then: "that states missing tran date"
        errors[0] == "Description is greater than 50 characters"
    }

    def "USD amount is not provided"() {
        given: "we have request amount not provided"
        def request = new RequestTransaction("2022-12-31", "My test transaction.", null)

        when:  "we validate the request"
        def errors = RequestValidation.validate(request)

        then: "we receive a single error"
        errors.size() == 1
        then: "that states missing USD amount"
        errors[0] == "USD amount is not provided"
    }

    def "USD amount is not a dollar amount"() {
        given: "we have request with amount that is not an amount"
        def request = new RequestTransaction("2022-12-31", "My test transaction.", "ABC")

        when:  "we validate the request"
        def errors = RequestValidation.validate(request)

        then: "we receive a single error"
        errors.size() == 1
        then: "that states amount is not an amount"
        errors[0] == "USD Amount is not dollar amount"
    }
    def "USD amount has more than 2 decimals"() {
        given: "we have request with amount that has too many decimals"
        def request = new RequestTransaction("2022-12-31", "My test transaction.", "123.456")

        when:  "we validate the request"
        def errors = RequestValidation.validate(request)

        then: "we receive a single error"
        errors.size() == 1
        then: "that states amount has too many decimals"
        errors[0] == "USD Amount has more than 2 decimals"
    }
    def "there are 2 validation errors"() {
        given: "we have request with description with length > 50"
        and: "an amount that has too many decimals"
        def request = new RequestTransaction("2022-12-31", "My test transaction post with 51 characters length.","123.347")

        when:  "we validate the request"
        def errors = RequestValidation.validate(request)

        then: "we receive a two errors"
        errors.size() == 2
    }

    def "there are 3 validation errors"() {
        given:  "we have request with future tran date"
        and: "a description with length > 50"
        and: "an amount that has too many decimals"
        def request = new RequestTransaction("2023-12-32", "My test transaction post with 51 characters length.","123.347")

        when:  "we validate the request"
        def errors = RequestValidation.validate(request)

        then: "we receive a two errors"
        errors.size() == 3
    }
    
    def "request is empty"() {
        given:  "we have request no data"
        and: "a description with length > 50"
        and: "an amount that has too many decimals"
        def request = new RequestTransaction()
    
        when:  "we validate the request"
        def errors = RequestValidation.validate(request)

        then: "we receive 2 errors for is required"
        errors.size() == 2
        and: "one is for trans date re"
        errors[0] == "Transaction date is required"
        and: "second is for USD amount"
        errors[1] == "USD amount is not provided"
    }
}