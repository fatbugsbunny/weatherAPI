/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeatherAPI;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 *
 * @author HP NB
 */
public class WeatherAPI {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Input city name: ");
        String cityString = sc.nextLine();
        String city = Character.toUpperCase(cityString.charAt(0)) + cityString.substring(1);

        System.out.println("F (Fahrnheit) or C (Celsius) ?");
        String tempUnit = sc.nextLine().toLowerCase();
        String windUnit = "";

        System.out.println("Would you like to print the data to a file ? (y, n)");
        String location = sc.nextLine().toLowerCase();

        if (tempUnit.equals("C")) {
            windUnit = "kph";
        } else {
            windUnit = "mph";
        }

        try {
            URL url = new URL("http://api.weatherapi.com/v1/current.json?key=ec832338a85f42f9a5a134304231805&q=" + city);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                System.out.println("City name may not be correct or there was a problem with the app");
                throw new RuntimeException("Http response code:" + responseCode);
            } else {
                String inline = "";
                try (Scanner scanner = new Scanner(url.openStream())) {
                    while (scanner.hasNext()) {
                        inline += scanner.nextLine();
                    }
                }

                JSONParser parse = new JSONParser();
                JSONObject weatherData = (JSONObject) parse.parse(inline);
                JSONObject weather = (JSONObject) weatherData.get("current");

                String temperature = "The current temperature in " + city + " is " + weather.get("temp_" + tempUnit).toString() + tempUnit.toUpperCase() + " ";
                String humidity = "with " + weather.get("humidity").toString() + "% humidity ";
                String wind = "and a wind speed of " + weather.get("wind_" + windUnit).toString() + " " + windUnit;
                String weatherString = temperature + humidity + wind;

                String weatherFileString = "City: " + city + "\nTemperature: " + weather.get("temp_" + tempUnit).toString() + tempUnit.toUpperCase() + "\nHumidity: " + weather.get("humidity").toString() + "%" + "\nWind: " + weather.get("wind_" + windUnit).toString() + " " + windUnit;

                if (location.equals("y")) {
                    System.out.println("Choose the name of the file:");
                    String fileName = sc.nextLine();
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + ".txt"))) {
                        writer.write(weatherFileString);
                    }
                    System.out.println("Data written successfully");
                } else {

                    System.out.println(weatherString);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
