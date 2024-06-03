package com.example.together.dboperations;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Used to parse MySQL dates using gson
 */
public class SQLDateAdapter extends TypeAdapter<Date> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.mysqlDateFormat);
    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(dateFormat.format(value));
        }
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        try {
            return new Date(dateFormat.parse(in.nextString()).getTime());
        } catch (ParseException e) {
            throw new IOException(e);
        }
    }
}
