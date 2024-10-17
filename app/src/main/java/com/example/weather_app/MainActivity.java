package com.example.weather_app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle; //this is the main package
import android.os.*;
//for handling View,TextView and Buttons from xml we will use this import:
import android.view.View;
//this import is for creating the buttons
import android.widget.Button;
//the text view we created for getting them in java we have to import this:
import android.widget.TextView;
//to display the small alert toast is used:
import android.widget.Toast;
//we also need the json bcz when we get the api the data will be in the json format we have to convert it into normal text format for that purpose we need this:
import org.json.JSONObject;
//so we have to fetch data from api for that we need the reader:
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
//so we have to establish the connect between api and our app for that we need this:
import java.net.HttpURLConnection;
//we need url package for reading the urls:
import java.net.URL;
//for taking the url input and establishing a connnection we need this package
import java.util.concurrent.ExecutionException;





public class MainActivity extends AppCompatActivity {

    //now we have to create the variable for textview button edittext to access them
    TextView CityName;
    Button search;
    TextView show;
    String url;

//we have to create one more class to get the weather details so:
    class getWeather extends AsyncTask<String,Void,String>{
        @Override
    protected String doInBackground(String... urls){
            //here i am going to create a string builder whatever the detail we get from the get weather we will store it in this builder namin it as result
            StringBuilder result= new StringBuilder();
            try{
                //first we are going to store creating the url
                URL url=    new URL(urls[0]);                                      //now here ia am creating the object
                //now we have to establish a connection through internet:
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();  //here i am creating the url object and the connection
                urlConnection.connect();    //here i am connecting to the internet using the api

                //next i am going to read the input
                InputStream inputStream = urlConnection.getInputStream(); // i am goint to get thev values of api here now i am going to readthis value for this:
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                //next going to create a string
                String line = "";
                //now i am going to store the values that we read in this string line and also check wheather they are null or not
                while ((line = reader.readLine()) != null) {


                    //if it is not null then i am going to append the result
                    result.append(line).append("\n");



                }
                //after this i am going to return the result:
                return result.toString();




            }catch (Exception e){
                e.printStackTrace();
                return null;


            }

        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try{
                //we will type here what we are going to do
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("main");//HERE I AM GOING TO GET THE MAIN METHOD
                weatherInfo=weatherInfo.replace("temp","Temperature");//here i am replacing the temp in the app with Temperature the same way i will do with the temp max and humidity also
                weatherInfo=weatherInfo.replace("feels_like","Feels_Like");
                weatherInfo=weatherInfo.replace("temp_max","Temperature Max");
                weatherInfo=weatherInfo.replace("temp_min","Temperature Min");
                weatherInfo=weatherInfo.replace("pressure","Pressure");
                weatherInfo=weatherInfo.replace("humidity","Humidity");
                weatherInfo=weatherInfo.replace("{","");
                weatherInfo=weatherInfo.replace(",","\n");
                weatherInfo=weatherInfo.replace(":"," : ");
                show.setText(weatherInfo);



            }catch (Exception e){
                e.printStackTrace();

            }

        }


}



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    // now i am going to define the variables:
        CityName=findViewById(R.id.CityName);//here i am accessing the city name from our xml file
        search = findViewById(R.id.Search);
        show=findViewById(R.id.weather);

        //here creating the variable:
        //it will be a list of strings
        //like whats the temp max temp oressure humidity i am going to store all this in this list
        final String[] temp={""};


        // next iam going to create an onclick listener for our search button
        search.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                //here we define what will happen when we click on the search button
                //first i will create a small toast here this will create a small alert here
                Toast.makeText(MainActivity.this, "Button Clicked! ", Toast.LENGTH_SHORT).show();
                String city = CityName.getText().toString();
                //now i will paste the url of api with the api key:
                //and i am adding the city name in the urls
                //and also i am using the try and catch to handle the exceptions
                try {
                    if (!city.isEmpty()) {
                        url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + ",uk&appid=f1e6ae909bb28e9f81260728970611e9";
                    } else {
                        Toast.makeText(MainActivity.this, "Enter City", Toast.LENGTH_SHORT).show();
                        return; // Exit the method if no city is provided
                    }

                //now i am going to create a functions for weather detail:
                    getWeather task=new getWeather();
                    //by using the class i will get the weather details:
                    //i will create the variable in above.
                    //here i am going to store all the weather detail that come from getWeather function in a list temp
                    temp[0]=task.execute(url).get();    //using get to geting the data and storing it into temp list


                }catch (ExecutionException e) {      //these exceptions arise when u dont have the internet
                    e.printStackTrace();                                   //when we arise  the exception we simply print those
                }catch (InterruptedException e){                            //here we have one more exceptions interuption when you didnt get any input when you went to those urls means if u enter any invalid city so this exception will arise to handle this:

                    e.printStackTrace();
                }
                //if it didnt get the detail of weather then we have to print cannot able to find weather
                if (temp[0]==null){

                    show.setText("canot able to find weather");

                }
                }
        });

        };
    }
