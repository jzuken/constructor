package com.xcart.xcartnew.managers.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.net.Uri;

import com.xcart.xcartnew.SSLDefaultHttpClient;
import com.xcart.xcartnew.managers.LogManager;

/**
 * Created by Nikita on 13.10.13.
 */
public class HttpManager {

    private static final LogManager LOG = new LogManager(HttpManager.class.getSimpleName());

    private static final String SERVER_URL = "https://54.213.38.9";
    private static final String API = "/api/api2.php";

    //requests
    private static final String DASHBOARD = "dashboard";
    private static final String ORDER_INFO = "order_info";
    private static final String USERS = "users";
    private static final String PRODUCTS = "products";
    private static final String UPDATE_PRODUCT_PRICE = "update_product_price";
    private static final String DELETE_PRODUCT = "delete_product";
    private static final String USER_INFO = "user_info";
    private static final String PRODUCT_INFO = "product_info";
    private static final String LAST_ORDERS = "last_orders";
    private static final String REVIEWS = "reviews";
    private static final String DELETE_REVIEW = "delete_review";
    private static final String CHANGE_TRACKING = "change_tracking";

    // parameters
    private static final String REQUEST = "request";
    private static final String SID = "sid";
    private static final String ID = "id";
    private static final String FROM = "from";
    private static final String SIZE = "size";
    private static final String SORT = "sort";
    private static final String SEARCH = "search";
    private static final String PRICE = "price";
    private static final String LOW_STOCK = "low_stock";
    private static final String DATE = "date";
    private static final String STATUS = "status";
    private static final String ORDER_ID = "order_id";
    private static final String TRACKING_NUMBER = "tracking_number";

    private HttpClient client;

    private String sid;

    public HttpManager(String sid) {
        client = new SSLDefaultHttpClient();
        this.sid = sid;
    }

    public String getDashboard() {
        Uri uri = Uri.parse(SERVER_URL).buildUpon().path(API)
                .appendQueryParameter(REQUEST, DASHBOARD)
                .appendQueryParameter(SID, sid)
                .build();
        return get(uri);
    }

    public String getOrderInfo(String orderId) {
        Uri uri = Uri.parse(SERVER_URL).buildUpon().path(API)
                .appendQueryParameter(REQUEST, ORDER_INFO)
                .appendQueryParameter(ID, orderId)
                .appendQueryParameter(SID, sid)
                .build();
        return get(uri);
    }

    public String getUsers(String from,String size, String search, String sort) {
        Uri uri = Uri.parse(SERVER_URL).buildUpon().path(API)
                .appendQueryParameter(REQUEST, USERS)
                .appendQueryParameter(FROM, from)
                .appendQueryParameter(SIZE, size)
                .appendQueryParameter(SEARCH, search)
                .appendQueryParameter(SORT, sort)
                .appendQueryParameter(SID, sid)
                .build();
        return get(uri);
    }

    public String getProducts(String from,String size, String search, String lowStock) {
        Uri.Builder builder = Uri.parse(SERVER_URL).buildUpon().path(API)
                .appendQueryParameter(REQUEST, PRODUCTS)
                .appendQueryParameter(FROM, from)
                .appendQueryParameter(SIZE, size)
                .appendQueryParameter(SEARCH, search)
                .appendQueryParameter(SID, sid);
        if(lowStock != null){
            builder.appendQueryParameter(LOW_STOCK, "1");
        }

        return get(builder.build());
    }

    public String updateProductPrice(String id, String price) {
        Uri uri = Uri.parse(SERVER_URL).buildUpon().path(API)
                .appendQueryParameter(REQUEST, UPDATE_PRODUCT_PRICE)
                .appendQueryParameter(ID, id)
                .appendQueryParameter(PRICE, price)
                .appendQueryParameter(SID, sid)
                .build();
        return get(uri);
    }

    public String deleteProduct(String id) {
        Uri uri = Uri.parse(SERVER_URL).buildUpon().path(API)
                .appendQueryParameter(REQUEST, DELETE_PRODUCT)
                .appendQueryParameter(ID, id)
                .appendQueryParameter(SID, sid)
                .build();
        return get(uri);
    }

    public String getUserInfo(String id) {
        Uri uri = Uri.parse(SERVER_URL).buildUpon().path(API)
                .appendQueryParameter(REQUEST, USER_INFO)
                .appendQueryParameter(ID, id)
                .appendQueryParameter(SID, sid)
                .build();
        return get(uri);
    }

    public String getProductInfo(String id) {
        Uri uri = Uri.parse(SERVER_URL).buildUpon().path(API)
                .appendQueryParameter(REQUEST, PRODUCT_INFO)
                .appendQueryParameter(ID, id)
                .appendQueryParameter(SID, sid)
                .build();
        return get(uri);
    }

    public String getLastOrders(String from, String size, String date, String search, String status) {
        Uri.Builder builder = Uri.parse(SERVER_URL).buildUpon().path(API)
                .appendQueryParameter(REQUEST, LAST_ORDERS)
                .appendQueryParameter(FROM, from)
                .appendQueryParameter(SIZE, size)
                .appendQueryParameter(DATE, date)
                .appendQueryParameter(SEARCH, search)
                .appendQueryParameter(SID, sid);

        if(status != null){
            builder.appendQueryParameter(STATUS, status);
        }
        return get(builder.build());
    }

    public String getReviews(String from,String size) {
        Uri uri = Uri.parse(SERVER_URL).buildUpon().path(API)
                .appendQueryParameter(REQUEST, REVIEWS)
                .appendQueryParameter(FROM, from)
                .appendQueryParameter(SIZE, size)
                .appendQueryParameter(SID, sid)
                .build();
        return get(uri);
    }

    public String deleteReview(String id) {
        Uri uri = Uri.parse(SERVER_URL).buildUpon().path(API)
                .appendQueryParameter(REQUEST, DELETE_REVIEW)
                .appendQueryParameter(ID, id)
                .appendQueryParameter(SID, sid)
                .build();
        return get(uri);
    }

    public String changeTrackingNumber(String id, String tracking) {
        Uri uri = Uri.parse(SERVER_URL).buildUpon().path(API)
                .appendQueryParameter(REQUEST, CHANGE_TRACKING)
                .appendQueryParameter(ORDER_ID, id)
                .appendQueryParameter(TRACKING_NUMBER, tracking)
                .appendQueryParameter(SID, sid)
                .build();
        return get(uri);
    }

    private String get(Uri uri) {
        LOG.d("get uri " + uri.toString());

        HttpGet get = new HttpGet(uri.toString());

        try {
            HttpResponse responseGet = client.execute(get);
            HttpEntity resEntityGet = responseGet.getEntity();
            if (resEntityGet != null) {
                return EntityUtils.toString(resEntityGet);
            }
        } catch (UnsupportedEncodingException e) {
            LOG.e("UnsupportedEncodingException", e);
        } catch (ClientProtocolException e) {
            LOG.e("ClientProtocolException", e);
        } catch (IOException e) {
            LOG.e("IOException", e);
        }
        return null;
    }
}
