package net.square;

import net.square.address.AddressData;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

// This is an example class that could be like this in your project.
@SuppressWarnings("unused")
public class MainExample {
    // Main method that starts your process.
    public static void main(String[] args) {

        // For testing, the IP address of the provider https://prohosting24.de was used here.
        final String address = "45.142.115.247";

        // If changes need to be made to the license key, this method can be called.
        // However, if you do not have a plan, you do not have to
        // do this, otherwise it will take the Default parameter.
        ProxyAPI.setProxyKey("license_here");

        // How long should the objects stay in the cache?
        ProxyAPI.setDurationTime(60);

        // What should be the time format? See durationTime
        ProxyAPI.setDurationUnit(TimeUnit.MINUTES);

        // Thus, it is possible to access the information of an IP synchronously.
        try {
            AddressData addressData = ProxyAPI.fetchAddressDataForIPv4(address);

            String country = addressData.getCountry();
            String city = addressData.getCity();
            String provider = addressData.getProvider();
            // And so on...

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        // Thus, it is possible to access the information of an IP asynchronously.
        ProxyAPI.fetchAddressDataForIPv4Async(address).whenComplete((addressData, throwable) -> {

            // The exception is always null if everything worked properly.
            // If an error occurred during the process, this exception will
            // not be null and will be thrown. Thus, the process will then also be terminated.
            if(throwable != null) {
                throwable.printStackTrace();
                return;
            }

            String country = addressData.getCountry();
            String city = addressData.getCity();
            String provider = addressData.getProvider();

            // And so on...
        });
    }
}
