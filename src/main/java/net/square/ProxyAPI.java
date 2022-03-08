package net.square;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.square.address.AddressData;
import net.square.exceptions.AddressDataFetchingException;
import net.square.exceptions.ProxyMalfunctionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;

@UtilityClass
@SuppressWarnings("unused")
public class ProxyAPI {

    // https://proxycheck.io is an online security service offering Proxy
    // and VPN detection and generalised IP location information.
    private static final String API_URL = "https://proxycheck.io/v2/%s?key=%s?vpn=1&asn=1";

    // If you have a plan on https://proxycheck.io, you should enter your key here. If you don't have one, you are
    // free to make 1000 requests per day for your own server/computer IP. I recommend buying a plan though.
    private static final String PROXY_KEY = "insert_key";

    // How long should an entry be stored?
    private static final int DURATION_TIME = 60;

    // In which time unit should the duration time last?
    private static final TimeUnit DURATION_UNIT = TimeUnit.MINUTES;

    // I couldn't think of a better way to turn the result from the website into a Java object without any problems.
    // Therefore, I have taken Gson.
    private final Gson gson = new Gson();

    // To avoid unnecessary requests, I have implemented the Guava LoadingCache. This keeps an IP in the cache for
    // 60 minutes, after which it is removed and must be fetched again from the website when it is accessed again.
    // I recommend installing an own cache beside this one.
    private final LoadingCache<String, AddressData> cacheCat = CacheBuilder.newBuilder()
        .expireAfterAccess(DURATION_TIME, DURATION_UNIT)
        .build(CacheLoader.from(ProxyAPI::fetchData));

    /**
     * Looks up an {@link AddressData} object for the passed IPv4 address inside the cache. If not cached, the cache
     * attempts to fetch this object via {@link ProxyAPI#fetchData(String)}.
     * <br>
     * The cached entry will expire after every access after the given time
     * (see {@link #DURATION_TIME} and {@link #DURATION_UNIT}).
     * <br>
     * Will throw if the cache fails during the fetch or when the fetched data returns null.
     *
     * @param ipAddress A IPv4 address in string representation.
     * @return An {@link AddressData} object for the given ipAddress
     * @throws ExecutionException Thrown when cache returned null or when an exception was thrown inside the cache.
     */
    public AddressData fetchAddressDataForIPv4(@NonNull String ipAddress) throws ExecutionException {
        // Checks if the passed argument is null. There are some jokers :P
        Preconditions.checkNotNull(ipAddress, "Field ipAddress cannot be null");
        return cacheCat.get(ipAddress);
    }

    /**
     * Wraps the execution of {@link #fetchAddressDataForIPv4(String)} inside a {@link CompletableFuture} to retrieve
     * its result asynchronously.
     * <br>
     * <p>
     * Thrown {@link Exception}s, like {@link AddressDataFetchingException}, have to be handled by using
     * {@link CompletableFuture#exceptionally(Function)} or {@link CompletableFuture#whenComplete(BiConsumer)}.
     *
     * @param ipAddress A IPv4 address in string representation.
     * @return The
     */
    public CompletableFuture<AddressData> fetchAddressDataForIPv4Async(@NonNull String ipAddress) {
        // Checks if the passed argument is null. There are some jokers :P
        Preconditions.checkNotNull(ipAddress, "Field ipAddress cannot be null");
        return CompletableFuture.supplyAsync(() -> {
            try {
                return fetchAddressDataForIPv4(ipAddress);
            } catch (ExecutionException e) {
                throw new CompletionException(e);
            }
        });
    }

    /**
     * Converts the response body received by the HTTP GET request to the https://proxycheck.io API
     * to an {@link AddressData} object.
     *
     * @param ipAddress A IPv4 address represented as a string.
     * @return An {@link AddressData} object containing the information of the response body.
     */
    @SneakyThrows
    private AddressData fetchData(@NonNull String ipAddress) {

        Preconditions.checkNotNull(ipAddress, "Field ipAddress cannot be null");

        JsonObject jsonObject;
        try {
            jsonObject = parseJsonObjectFromURL(formatURL(ipAddress));
        } catch (IOException e) {
            throw new AddressDataFetchingException("Failed to fetch data for address %s".formatted(ipAddress), e);
        }

        Preconditions.checkNotNull(jsonObject);
        Preconditions.checkNotNull(jsonObject.get("status"));
        Preconditions.checkArgument(jsonObject.get("status").getAsString().equalsIgnoreCase("ok"));

        // Processing of reports from https://proxycheck.io
        handleMessage(jsonObject);

        return gson.fromJson(jsonObject.getAsJsonObject(ipAddress), AddressData.class);
    }

    /**
     * This method processes the status as well as the messages from https://proxycheck.io
     * and passes them to the consumer in the form of an exception.
     * <br>
     * <p>
     * In this method, not all messages from https://proxycheck.io are processed. Only the problems that have an
     * impact on
     * the functionality of the API are caught here. If you want to have a full list of all possible errors you can
     * have a look at it here: https://proxycheck.io/api/#test_console
     *
     * @param jsonObject The object from the https://proxycheck.io website
     *                   <p>
     *                   It will throw an {@link ProxyMalfunctionException} is thrown if a message is present
     *                   in the return value. Since these are always negative in nature, the exception was named
     *                   MalfunctionException.
     */
    @SneakyThrows
    private void handleMessage(JsonObject jsonObject) {

        JsonElement message = jsonObject.get("message");

        if (message != null) {
            throw new ProxyMalfunctionException(message.getAsString());
        }
    }

    /**
     * Converts the response ({@link URL#openStream()} of a URL to a {@link JsonObject}.
     *
     * @param url The URL string from which to read and convert the response of.
     * @return The object if it was successfully converted.
     * @throws IOException Thrown when an error occurred while reading from the {@link java.io.InputStream} of the URL.
     */
    private JsonObject parseJsonObjectFromURL(@NonNull String url) throws IOException {
        return JsonParser.parseReader(
            new BufferedReader(new InputStreamReader(new URL(url).openStream()))).getAsJsonObject();
    }

    /**
     * Formats the {@link ProxyAPI#API_URL} together with the IPv4 address and the API key.
     *
     * @param address The IPv4 address as plain string.
     * @return A formatted string {@link ProxyAPI#API_URL}.
     */
    private String formatURL(@NonNull String address) {
        return String.format(API_URL, address, PROXY_KEY);
    }
}