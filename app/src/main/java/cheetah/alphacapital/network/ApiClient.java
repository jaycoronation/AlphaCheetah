package cheetah.alphacapital.network;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient
{
    public static String TOKEN_ID = "@alpha-capital-123-4$";
    //public static final String MAIN_URL = "http://192.168.50.52/AlphaCapitalReportApp/api/";
   // public static final String MAIN_URL = "http://demo1.coronation.in/AlphacapitalReportAppTest/api/";


    public static final String MAIN_URL = "https://demo1.coronation.in/AlphacapitalReportApp/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient()
    {
        if (retrofit == null)
        {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(new Interceptor()
            {
                @Override
                public Response intercept(@NonNull Chain chain) throws IOException
                {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Basic QWxwaGFjYXBpdGFsOkFwcC00U1RTQTY0TzYyV1FXWlIwUVNJSw==")
                            .build();
                    return chain.proceed(request);

                }
            }).addInterceptor(interceptor)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(MAIN_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
