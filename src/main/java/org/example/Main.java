package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.bean.CsvToBean;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> list = parseCSV(columnMapping, fileName);
        String answer = listToJson(list);
        writeString(answer, "data.json");
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName){
        List<Employee> list = Arrays.asList();
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <T> String listToJson(List<T> list){
        Type listType = new TypeToken<List<T>>() {}.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String answer = gson.toJson(list, listType);
        return answer;
    }

    public static void writeString(String answer, String toFileName){
        File newfile = new File(toFileName);
        try (FileWriter writer = new FileWriter(newfile, false)){
            writer.write(answer);
        } catch (IOException ex){
            ex.getMessage();
        }
    }
}