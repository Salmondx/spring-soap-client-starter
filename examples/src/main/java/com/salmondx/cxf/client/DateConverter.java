package com.salmondx.cxf.client;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.springframework.core.convert.converter.Converter;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.util.GregorianCalendar;

/**
 * Created by Salmondx on 08/09/16.
 */
public class DateConverter implements Converter<LocalDate, XMLGregorianCalendar> {
    @Override
    public XMLGregorianCalendar convert(LocalDate source) {
        return new XMLGregorianCalendarImpl(new GregorianCalendar(source.getYear(), source.getMonthValue(), source.getDayOfMonth()));
    }
}
