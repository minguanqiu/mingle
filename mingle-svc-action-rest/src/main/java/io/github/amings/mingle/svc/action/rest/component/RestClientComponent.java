package io.github.amings.mingle.svc.action.rest.component;

import jakarta.annotation.PostConstruct;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;

/**
 * Client component
 *
 * @author Ming
 */

@Component
public class RestClientComponent {

    private SSLContext sslContext;

    private X509TrustManager x509TrustManager;

    public OkHttpClient.Builder buildClient(int connectTimeOut, int readTimeOut, boolean ignoreSSL) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(Duration.ofMillis(connectTimeOut))
                .readTimeout(Duration.ofMillis(readTimeOut));
        if (ignoreSSL) {
            ignoreSSL(builder);
        }
        return builder;
    }

    public void ignoreSSL(OkHttpClient.Builder builder) {
        builder.sslSocketFactory(sslContext.getSocketFactory(), x509TrustManager)
                .hostnameVerifier(((hostname, session) -> true));
    }

    private void buildSSLContext() {
        try {
            sslContext = SSLContext.getInstance("TLSv1.2");
            x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            };
            sslContext.init(null, new X509TrustManager[]{x509TrustManager}, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    private void init() {
        buildSSLContext();
    }

}


