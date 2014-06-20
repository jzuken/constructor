package com.xcart.admin.managers.network;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import com.xcart.admin.managers.LogManager;
import com.xcart.admin.managers.XCartApplication;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;

/**
 * Created by Nikita on 13.10.13.
 */
public class HttpManager {

    private static final LogManager LOG = new LogManager(HttpManager.class.getSimpleName());


    //public static final String SHOP_NAME = "ec2-54-213-169-59.us-west-2.compute.amazonaws.com"; //TODO:
    //private static final String API = "/xcart/api/api2.php";
    //private static final String SERVER_URL = "http://ec2-54-213-169-59.us-west-2.compute.amazonaws.com";

    //public static final String SHOP_NAME = "54.213.38.9"; //TODO:
    //private static final String SERVER_URL = "https://54.213.38.9";
    //private static final String API = "/api/api2.php";

    // requests
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
    private static final String CHANGE_STATUS = "change_status";
    private static final String CHANGE_AVAILABLE = "change_available";
    private static final String LOGIN = "login";
    private static final String USER_ORDERS = "user_orders";
    private static final String REGISTER_GCM = "register_gcm";
    private static final String UNREGISTER_GCM = "unregister_gcm";
    private static final String GET_CONFIG = "get_config";

    // parameters
    private static final String REQUEST = "request";
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
    private static final String PRODUCT_ID = "product_id";
    private static final String AVAILABLE = "available";
    private static final String USER_ID = "user_id";
    private static final String KEY = "key";
    private static final String REG_ID = "regid";
    private static final String MANUFACTURER = "manufacturer";
    private static final String MODEL = "model";
    private static final String SERIAL = "serial";
    private static final String ANDROID_VERSION = "android_version";
    private static final String IMEI = "imei";
    private static final String IMSI = "imsi";
    private static final String CFGS_ARRAY = "cfgs_array";
    private static final String PPH_ORDER_DETAILS = "pph_order_details";

    // Dev server
    private static final String DEV_SERVER_URL = "http://vm-constructor.cloudapp.net";
    private static final String CHECK_SUBSCRIPTION = "/AppServerListener/api/shops/%s/checksubscribtion";
    private static final String SHOP_AUTHORIZATION = "/AppServerListener/api/shops/%s/ApiURL";

    //https://mobileadmin.x-cart.com/xcart/mobile_admin_api.php?request=get_config&key=FQMTED8L&cfgs_array=a%3A2%3A%7Bi%3A0%3Bs%3A23%3A%22General%3Acurrency_symbol%22%3Bi%3A1%3Bs%3A23%3A%22General%3Acurrency_format%22%3B%7D

    private HttpClient client;

    private Context context;
    private String serverUrl;
    private String key;

    public HttpManager() {
        this(null);
    }

    public HttpManager(Context context) {
        this.context = context;
        client = new SSLDefaultHttpClient();
        key = XCartApplication.getInstance().getPreferenceManager().getShopKey();
        serverUrl = XCartApplication.getInstance().getPreferenceManager().getShopUrl();
    }

    public String getDashboard() {
        Uri uri = Uri.parse(serverUrl).buildUpon().appendQueryParameter(REQUEST, DASHBOARD)
                .appendQueryParameter(KEY, key).build();
        return get(uri);
    }

    public String getOrderInfo(String orderId) {
        Uri uri = Uri.parse(serverUrl).buildUpon().appendQueryParameter(REQUEST, ORDER_INFO)
                .appendQueryParameter(ID, orderId)
                .appendQueryParameter(KEY, key).build();
        return get(uri);
    }

    public String getUsers(String from, String size, String search, String sort) {
        Uri uri = Uri.parse(serverUrl).buildUpon().appendQueryParameter(REQUEST, USERS)
                .appendQueryParameter(FROM, from).appendQueryParameter(SIZE, size).appendQueryParameter(SEARCH, search)
                .appendQueryParameter(SORT, sort).appendQueryParameter(KEY, key).build();
        return get(uri);
    }

    public String getProducts(String from, String size, String search, String lowStock) {
        Uri.Builder builder = Uri.parse(serverUrl).buildUpon()
                .appendQueryParameter(REQUEST, PRODUCTS)
                .appendQueryParameter(FROM, from)
                .appendQueryParameter(SIZE, size)
                .appendQueryParameter(SEARCH, search)
                .appendQueryParameter(KEY, key);
        if (lowStock != null) {
            builder.appendQueryParameter(LOW_STOCK, "1");
        }

        return get(builder.build());
    }

    public String updateProductPrice(String id, String price) {
        Uri uri = Uri.parse(serverUrl).buildUpon()
                .appendQueryParameter(REQUEST, UPDATE_PRODUCT_PRICE)
                .appendQueryParameter(ID, id)
                .appendQueryParameter(PRICE, price)
                .appendQueryParameter(KEY, key).build();
        return get(uri);
    }

    public String deleteProduct(String id) {
        Uri uri = Uri.parse(serverUrl).buildUpon().
                appendQueryParameter(REQUEST, DELETE_PRODUCT)
                .appendQueryParameter(ID, id)
                .appendQueryParameter(KEY, key)
                .build();
        return get(uri);
    }

    public String getUserInfo(String id) {
        Uri uri = Uri.parse(serverUrl).buildUpon()
                .appendQueryParameter(REQUEST, USER_INFO)
                .appendQueryParameter(ID, id)
                .appendQueryParameter(KEY, key)
                .build();
        return get(uri);
    }

    public String getUserOrders(String from, String size, String id) {
        Uri uri = Uri.parse(serverUrl).buildUpon()
                .appendQueryParameter(REQUEST, USER_ORDERS)
                .appendQueryParameter(USER_ID, id)
                .appendQueryParameter(FROM, from)
                .appendQueryParameter(SIZE, size)
                .appendQueryParameter(KEY, key).build();
        return get(uri);
    }

    public String getProductInfo(String id) {
        Uri uri = Uri.parse(serverUrl).buildUpon()
                .appendQueryParameter(REQUEST, PRODUCT_INFO)
                .appendQueryParameter(ID, id)
                .appendQueryParameter(KEY, key)
                .build();
        return get(uri);
    }

    public String getLastOrders(String from, String size, String date, String search, String status) {
        Uri.Builder builder = Uri.parse(serverUrl)
                .buildUpon().appendQueryParameter(REQUEST, LAST_ORDERS)
                .appendQueryParameter(FROM, from)
                .appendQueryParameter(SIZE, size)
                .appendQueryParameter(DATE, date)
                .appendQueryParameter(SEARCH, search)
                .appendQueryParameter(KEY, key);

        if (status != null) {
            builder.appendQueryParameter(STATUS, status);
        }
        return get(builder.build());
    }

    public String getReviews(String from, String size) {
        Uri uri = Uri.parse(serverUrl).buildUpon()
                .appendQueryParameter(REQUEST, REVIEWS)
                .appendQueryParameter(FROM, from)
                .appendQueryParameter(SIZE, size)
                .appendQueryParameter(KEY, key)
                .build();
        return get(uri);
    }

    public String deleteReview(String id) {
        Uri uri = Uri.parse(serverUrl).buildUpon()
                .appendQueryParameter(REQUEST, DELETE_REVIEW)
                .appendQueryParameter(ID, id)
                .appendQueryParameter(KEY, key)
                .build();
        return get(uri);
    }

    public String changeTrackingNumber(String id, String tracking) {
        Uri uri = Uri.parse(serverUrl).buildUpon()
                .appendQueryParameter(REQUEST, CHANGE_TRACKING)
                .appendQueryParameter(ORDER_ID, id)
                .appendQueryParameter(TRACKING_NUMBER, tracking)
                .appendQueryParameter(KEY, key)
                .build();
        return get(uri);
    }

    public String changeStatus(String id, String status) {
        Uri uri = Uri.parse(serverUrl).buildUpon()
                .appendQueryParameter(REQUEST, CHANGE_STATUS)
                .appendQueryParameter(ORDER_ID, id)
                .appendQueryParameter(STATUS, status)
                .appendQueryParameter(KEY, key)
                .build();
        return get(uri);
    }

    public String changeStatusPPH(String id, String status, String pphOrderDetails) {
        Uri uri = Uri.parse(serverUrl).buildUpon()
                .appendQueryParameter(REQUEST, CHANGE_STATUS)
                .appendQueryParameter(ORDER_ID, id)
                .appendQueryParameter(STATUS, status)
                .appendQueryParameter(PPH_ORDER_DETAILS, pphOrderDetails)
                .appendQueryParameter(KEY, key)
                .build();
        return get(uri);
    }

    public String changeAvailable(String id, String availability) {
        Uri uri = Uri.parse(serverUrl).buildUpon()
                .appendQueryParameter(REQUEST, CHANGE_AVAILABLE)
                .appendQueryParameter(PRODUCT_ID, id)
                .appendQueryParameter(AVAILABLE, availability)
                .appendQueryParameter(KEY, key).build();
        return get(uri);
    }

    public String login(List<NameValuePair> nameValuePairs) {
        Uri uri = Uri.parse(serverUrl).buildUpon()
                .appendQueryParameter(REQUEST, LOGIN)
                .appendQueryParameter(KEY, key)
                .build();
        return post(uri, nameValuePairs);
    }

    public String checkSubscription(String shopUrl) {
        Uri uri = Uri.parse(DEV_SERVER_URL).buildUpon().path(String.format(CHECK_SUBSCRIPTION, shopUrl)).build();
        return get(uri);
    }

    public String shopAuthorization(String key, String shopUrl) {
        Uri uri = Uri.parse(DEV_SERVER_URL).buildUpon().path(String.format(SHOP_AUTHORIZATION, shopUrl))
                .appendQueryParameter(KEY, key)
                .build();
        return get(uri);
    }

    public String sendRegIdToBackend(String regId) {
        android.telephony.TelephonyManager telephonyManager = (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imeiString = telephonyManager.getDeviceId();
        String imsiString = telephonyManager.getSubscriberId();
        Uri uri = Uri.parse(serverUrl).buildUpon()
                .appendQueryParameter(REQUEST, REGISTER_GCM)
                .appendQueryParameter(REG_ID, regId)
                .appendQueryParameter(KEY, key)
                .appendQueryParameter(MANUFACTURER, Build.MANUFACTURER)
                .appendQueryParameter(MODEL, Build.MODEL)
                .appendQueryParameter(SERIAL, Build.SERIAL)
                .appendQueryParameter(ANDROID_VERSION, Build.VERSION.RELEASE)
                .appendQueryParameter(IMEI, imeiString)
                .appendQueryParameter(IMSI, imsiString).build();

        return get(uri);
    }

    public String unregisterGCMInBackend(String regId) {
        Uri uri = Uri.parse(serverUrl).buildUpon()
                .appendQueryParameter(REQUEST, UNREGISTER_GCM)
                .appendQueryParameter(REG_ID, regId)
                .appendQueryParameter(KEY, key).build();
        return get(uri);
    }

    public String unregisterGCMInBackend(String apiUrl, String apiKey, String regId) {
        Uri uri = Uri.parse(apiUrl).buildUpon()
                .appendQueryParameter(REQUEST, UNREGISTER_GCM)
                .appendQueryParameter(REG_ID, regId)
                .appendQueryParameter(KEY, apiKey)
                .build();
        return get(uri);
    }

    public String getProductUrl(String productId) {
        URI uri = URI.create(serverUrl);
        //TODO: remove hack
        if(serverUrl.contains("/xcart/")){
            return String.format("%s://%s/xcart/product.php?productid=%s", uri.getScheme(), uri.getHost(), productId);
        }else {
            return String.format("%s://%s/product.php?productid=%s", uri.getScheme(), uri.getHost(), productId);
        }
    }

    public String getProductEditUrl(String productId) {
        URI uri = URI.create(serverUrl);
        return String.format("%s://%s/xcart/admin/product_modify.php?productid=%s", uri.getScheme(), uri.getHost(), productId);
    }

    public String getCurrencyType(String url) {
        Uri uri = Uri.parse(url).buildUpon()
                .appendQueryParameter(REQUEST, GET_CONFIG)
                .appendQueryParameter(CFGS_ARRAY, "a%3A2%3A%7Bi%3A0%3Bs%3A23%3A%22General%3Acurrency_symbol%22%3Bi%3A1%3Bs%3A23%3A%22General%3Acurrency_format%22%3B%7D")
                .appendQueryParameter(KEY, key)
                .build();
        return get(uri);
    }

    private String get(Uri uri) {
        String url = uri.toString();
        LOG.d("get url " + url);

        HttpGet get = new HttpGet(url);

        try {
            HttpResponse responseGet = client.execute(get);
            HttpEntity resEntityGet = responseGet.getEntity();
            if (resEntityGet != null) {
                return EntityUtils.toString(resEntityGet);
            }
        } catch (UnsupportedEncodingException e) {
            LOG.e(e.getMessage(), e);
        } catch (ClientProtocolException e) {
            LOG.e(e.getMessage(), e);
        } catch (IOException e) {
            LOG.e(e.getMessage(), e);
        } catch (IllegalStateException e) {
            LOG.e(e.getMessage(), e);
        }
        return null;
    }

    private String post(Uri uri, List<NameValuePair> nameValuePairs) {
        String url = uri.toString();
        LOG.d("post url " + url);
        HttpClient client = new SSLDefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(httppost);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                return EntityUtils.toString(responseEntity);
            }
        } catch (UnsupportedEncodingException e) {
            LOG.e(e.getMessage(), e);
        } catch (ClientProtocolException e) {
            LOG.e(e.getMessage(), e);
        } catch (IOException e) {
            LOG.e(e.getMessage(), e);
        }
        return null;
    }
}
