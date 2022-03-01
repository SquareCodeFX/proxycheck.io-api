package net.square;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;
import net.square.impl.AddressObject;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@UtilityClass
public class ProxyAPI {

    // If you have a plan on https://proxycheck.io, you should enter your key here.
    // If you don't have one, you are free to make 1000 requests per day for your own server/computer IP.
    // I recommend buying a plan though.
    private static final String PROXY_KEY = "insert_key";

    // I couldn't think of a better way to turn the result from the website into a Java object without any problems.
    // Therefore, I have taken Gson.
    private final Gson gson = new Gson();

    // To avoid unnecessary requests, I have implemented the Guava LoadingCache.
    // This keeps an IP in the cache for 60 minutes, after which it is removed and must be
    // fetched again from the website when it is accessed again.
    // I recommend installing an own cache beside this one.
    private final LoadingCache<String, AddressObject> cacheCat = CacheBuilder.newBuilder()
        .expireAfterAccess(60, TimeUnit.MINUTES)
        .build(CacheLoader.from(ProxyAPI::fetchData));

    /**
     * Looks in the LoadingCache for the AddressObject of the passing IPv4.
     * If this is not available, the cache fetches this object via the fetchData method. This whole process is asynchronous.
     *
     * @param address The IPv4 as plain string
     * @param addressObjectConsumer Contains the AddressObject of the passed IPv4 address if available.
     * @param exceptionConsumer Contains the exception, if any, that may have occurred while processing the request.
     */
    public void getObjectFromIPv4(String address, Consumer<AddressObject> addressObjectConsumer,
                                  Consumer<ExecutionException> exceptionConsumer) {

        // Checks if the passed argument is null. There are some jokers :P
        Preconditions.checkNotNull(address, "Field address cannot be null");

        CompletableFuture.runAsync(() -> {
            try {
                addressObjectConsumer.accept(cacheCat.get(address));
            } catch (ExecutionException e) {
                exceptionConsumer.accept(e);
            }
        });
    }

    /**
     * Formats the return from the https://proxycheck.io page as AddressObject and inserts it into the cache.
     *
     * @param address The IPv4 as plain string
     * @return https://proxycheck.io result as AddressObject
     * @throws RuntimeException If an error occurs while establishing the connection or processing the result.
     */
    private AddressObject fetchData(String address) {

        JsonObject jsonObject;
        try {
            jsonObject = JsonParser.parseString(
                IOUtils.toString(new URL(formatIPv4(address)), StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (jsonObject.get("status").getAsString().equalsIgnoreCase("ok")) {
            AddressObject addressObject = gson.fromJson(
                jsonObject.get(address).getAsJsonObject().toString(), AddressObject.class);
            cacheCat.put(address, addressObject);
            return addressObject;
        }
        return null;
    }

    /**
     * Formats the https://proxycheck.io domain together with the IPv4 and the API key.
     *
     * @param address The IPv4 as plain string
     * @return Returns the https://proxycheck.io API URL formatted with the IPv4 and the API key.
     */
    private String formatIPv4(String address) {
        return String.format("https://proxycheck.io/v2/%s?key=%s?vpn=1&asn=1", address, PROXY_KEY);
    }
}
