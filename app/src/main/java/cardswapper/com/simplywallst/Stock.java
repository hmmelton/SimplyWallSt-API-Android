package cardswapper.com.simplywallst;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by harrisonmelton on 5/5/16.
 * Each instance of this class represents a single company's stock, whose information can be
 * gathered.
 */
public class Stock {

    private static final String ENDPOINT = "https://simplywall.st/api/snowflake/%s:%s";
    private final OkHttpClient client = new OkHttpClient();

    private JSONObject mJson; // snowflake information about an individual stock
    private int[] mScores; // scores of different snowflake attributes

    public Stock(String exchange, String ticker) {
        getData(exchange, ticker);
    }

    /**
     * This method retrieves information about a specified stock.
     * @param exchange exchange on which specified stock is traded
     * @param ticker ticker symbol for specified stock on that exchange
     */
    private void getData(String exchange, String ticker) {
        Request request = new Request.Builder()
                .url(String.format(ENDPOINT, exchange, ticker))
                .build();

        try {
            Response response = client.newCall(request).execute();

            // sets JSON instance variable to info of stock
            mJson = new JSONObject(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method retrieves the name of the company whose stock was queried.
     * @return name of company
     * @throws JSONException thrown if name is not found
     */
    public String getCompanyName() throws JSONException {
        return mJson.getString("companyName");
    }

    /**
     * This method returns the combined exchange and stock symbols.
     * @return unique symbol
     * @throws JSONException thrown if unique symbol is not found
     */
    public String getUniqueSymbol() throws JSONException {
        return mJson.getString("uniqueSymbol");
    }

    /**
     * This method returns the symbol of the exchange on which the individual stock trades.
     * @return exchange symbol
     * @throws JSONException thrown if unique symbol is not found
     */
    public String getExchangeSymbol() throws JSONException {
        return mJson.getString("primaryExchangeSymbol");
    }

    /**
     * This method returns the specific stock's ticker symbol.
     * @return ticker symbol
     * @throws JSONException thrown if unique symbol is not found
     */
    public String getTickerSymbol() throws JSONException {
        return mJson.getString("primaryTickerSymbol");
    }

    /**
     * This method returns the "snowflake scores" for the specified stock.
     * @return snowflake scores
     * @throws JSONException thrown if unique symbol is not found
     */
    public int[] getSnowflakeScores() throws JSONException {
        JSONArray array = mJson.getJSONArray("snowflakeScores");
        // Value, Future, Past, Health, Income
        int[] values = new int[array.length()];
        for (int i = 0; i < values.length; i++) {
            values[i] = array.getInt(i);
        }

        if (mScores == null) {
            mScores = Arrays.copyOf(values, values.length);
        }
        return values;
    }

    /**
     * This method returns the "value" attribute of the individual stock.  This is not the stock's
     * current bid price.
     * @return value attribute of stock
     * @throws JSONException thrown if value cannot be found
     */
    public int getValue() throws JSONException {
        getSnowflakeScores();
        return mScores[0];
    }

    /**
     * This method returns the "future" attribute of the individual stock.
     * @return future attribute
     * @throws JSONException thrown if future cannot be found
     */
    public int getFuture() throws JSONException {
        getSnowflakeScores();
        return mScores[1];
    }

    /**
     * This method returns the "past" attribute of the individual stock.
     * @return past attribute
     * @throws JSONException thrown if past cannot be found
     */
    public int getPast() throws JSONException {
        getSnowflakeScores();
        return mScores[2];
    }

    /**
     * This method returns the "health" attribute of the individual stock.
     * @return past attribute
     * @throws JSONException thrown if health cannot be found
     */
    public int getHealth() throws JSONException {
        getSnowflakeScores();
        return mScores[3];
    }

    /**
     * This method returns the "income" attribute of the individual stock.
     * @return income attribute
     * @throws JSONException thrown if income cannot be found
     */
    public int getIncome() throws JSONException {
        getSnowflakeScores();
        return mScores[4];
    }

    /**
     * This method returns the color of the snowflake.
     * @return color of snowflake
     * @throws JSONException thrown if snowflake's color cannot be found
     */
    public double getSnowflakeColor() throws JSONException {
        return mJson.getDouble("snowflakeColour");
    }

}
