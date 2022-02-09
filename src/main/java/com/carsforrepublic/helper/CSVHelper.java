package com.carsforrepublic.helper;


import com.carsforrepublic.configuration.PriceConfig;
import com.carsforrepublic.model.Car;
import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.Parameters;
import com.fathzer.soft.javaluator.StaticVariableSet;
import com.opencsv.CSVReader;
import org.apache.commons.csv.*;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * It's class which provide functionality to convert CSV to Java object and vice-versa
 */
@Component
public class CSVHelper {
    private static PriceConfig priceConfig;

    @Autowired
    public CSVHelper(PriceConfig priceConfig) {
        this.priceConfig = priceConfig;
    }

    public static String TYPE = "text/csv";
    static String[] HEADERs = {"CAR", "BASE_PRICE", "COLOUR", "NO_WHEEL_DRIVE", "AMPHIBIOUS", "ISERRORED", "ERROR", "FINALPRICE"};

    /**
     *
     * @param file
     * @return
     */
    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    /**
     *
     * @param is
     * @return
     */
    public static List<Car> csvToCars(InputStream is) {
        List<Car> cars = new ArrayList<Car>();
        try (BufferedReader carFileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVReader reader = new CSVReader(carFileReader)) {
            reader.skip(1);
            List<String[]> rawData = reader.readAll();
            rawData.forEach(row -> {

                Car car = Car.builder()
                        .carNumber(row[0])
                        .basePrice(row[1])
                        .colour(row[2])
                        .noWheelDrive(row[3])
                        .amphibious((row[4])).build();
                ValidateAndcalculateFinalPrice(car);
                cars.add(car);
                System.out.println(Arrays.toString(row));
            });
        } catch (Exception e) {
            throw new RuntimeException("Fail to parse CSV file: " + e.getMessage());
        }
        return cars;
    }

    /**
     * @param car
     */
    private static void ValidateAndcalculateFinalPrice(Car car) {
        if (!NumberUtils.isCreatable(car.getBasePrice())) {
            car.setErrorRecord(true);
            car.setError("Car Base Price is not provided in correct Format!! ");
        }
        if (!NumberUtils.isCreatable(car.getNoWheelDrive())) {
            car.setErrorRecord(true);
            car.setError("No Wheel Drive should be numeric , Please check !! ");
        }
        if (null == BooleanUtils.toBooleanObject(car.getAmphibious())) {
            car.setErrorRecord(true);
            car.setError("Amphibious should be boolean , Please check !! ");
        }
        if (!car.isErrorRecord()) {
            final DoubleEvaluator eval = new DoubleEvaluator();
            Parameters params = new Parameters();
            final StaticVariableSet<Double> variables = new StaticVariableSet<Double>();
            variables.set("price", NumberUtils.createDouble(car.getBasePrice()) > 150 ? NumberUtils.createDouble(car.getBasePrice()) * priceConfig.getBasepricemultiplier() : NumberUtils.createDouble(car.getBasePrice()));
            variables.set("colourcost", !priceConfig.getColourlist().contains(car.getColour()) ? priceConfig.getColourincludesvalue() : priceConfig.getColourexcludesvalue());
            variables.set("offroad", (NumberUtils.createInteger(car.getNoWheelDrive()) == 2 && BooleanUtils.toBooleanObject(car.getAmphibious())) ? 0 : 2000.00);
            car.setFinalPrice(eval.evaluate(priceConfig.getExpression(), variables));
        }

    }

    /**
     *
     * @param cars
     * @return
     */
    public static ByteArrayInputStream carsToCSV(List<Car> cars) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            csvPrinter.printRecord(HEADERs);
            for (Car car : cars) {
                List<String> data = Arrays.asList(
                        car.getCarNumber(),
                        car.getBasePrice(),
                        car.getColour(),
                        car.getNoWheelDrive(),
                        car.getAmphibious(),
                        String.valueOf(car.isErrorRecord()),
                        car.getError(),
                        String.valueOf(car.getFinalPrice())
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}


