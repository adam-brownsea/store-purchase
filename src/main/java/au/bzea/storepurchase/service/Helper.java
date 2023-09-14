package au.bzea.storepurchase.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class Helper {
    private static Logger logger = Logger.getLogger(Helper.class.getName());

    public static BigDecimal roundAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static String encodeString(String value) {
        String result = value;
        try{
            result = URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException uee) {
            logger.warning("UnsupportedEncodingException: " + uee.getMessage());
        }
        return result;
    }

    public static String decodeString(String value) {
        String result = value;
        try{
            result = URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException uee) {
            logger.warning("UnsupportedEncodingException: " + uee.getMessage());
        }
        return result;
    }

}
