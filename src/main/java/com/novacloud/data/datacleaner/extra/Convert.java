package com.novacloud.data.datacleaner.extra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 12/12/14.
 */
public class Convert {
    private static final String regex_forum = "((\\d{2,4}[-年/\\s]+\\d{1,2}[-月/\\s]*\\d{1,2}[\\s日/]*\\d{1,2}[:时]\\d{1,2}([:分]\\d{1,2})?))|(\\d{2,4}/\\d{1,2}/\\d{1,2}&nbsp;\\d{1,2}[:时]\\d{1,2}([:分]\\d{1,2})?)";
    private static final String regex_news = "(\\d{2,4}[-年/\\s]+\\d{1,2}[-月/\\s]*\\d{1,2}[\\s日/]*[\\s星期(日|天|六五|四|三|二|一)\\s/]*\\d{1,2}[:时]\\d{1,2}([:分]\\d{1,2})?)|(\\d{2,4}[-年./\\s]+\\d{1,2}[-月./\\s]*\\d{1,2}[\\s日/]*)";

    private static final Logger logger = LoggerFactory
            .getLogger(Convert.class);

    public static Number toNumber(String value) {
        try {
            return NumberFormat.getInstance().parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Number toNumber(String value, Number defaultValue) {
        try {
            return NumberFormat.getInstance().parse(value);
        } catch (ParseException e) {
            return defaultValue;
        }
    }


    public static Date toDate(String value) {
        return toDate(value, null);
    }

    /**
     * infer values whether is Number.class ,Date.class. Otherwise,return String.clsss
     *
     * @param values
     * @return
     */
    public static Class inferType(List<String> values) {
        int numberTypeValueCount = 0, dateTypeValueCount = 0;
        for (String value : values) {
            //Date type has higher piror than Number type ,because some date value both be number value
            if (toDate(value) != null)
                dateTypeValueCount++;
            else if (toNumber(value) != null)
                numberTypeValueCount++;
        }
        if (numberTypeValueCount == values.size())
            return Number.class;
        else if (dateTypeValueCount == values.size())
            return Date.class;
        else
            return String.class;
    }

    public static Date toDate(final String value, Date defaultValue) {
        Date findedDate = defaultValue;
        try {
            Pattern pattern = Pattern.compile(regex_forum + "|" + regex_news,
                    Pattern.DOTALL | Pattern.CASE_INSENSITIVE
                            | Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(value);
            if (matcher.find()) {
                findedDate = DateUtils.formatDateTime(matcher.group(0).trim());
            }
            return findedDate;
        } catch (Exception e) {
            logger.warn("convert todate error", e);
            return findedDate;
        }
    }

}
