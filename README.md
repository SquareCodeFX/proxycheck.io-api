# Proxycheck.io Java-API
> A mature API to work sync/async with Proxycheck.io.
> 
![Issues][issues]
![Forks][forks]
![Stars][stars]
![License][license]

## Installation

Compile
```
maven build
```
Dependency's
```
<dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.22</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>31.0.1-jre</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.9.0</version>
            <scope>provided</scope>
        </dependency>
</dependencies>
```

## Usage example

Here are a few examples of how to work with the API. Examples are provided for both the synchronous and the asynchronous part.

Sync
```
try {
    AddressData addressData = ProxyAPI.fetchAddressDataForIPv4("214.196.212.251");
    } catch (ExecutionException e) {
        e.printStackTrace();
    }
```

Async
```
ProxyAPI.fetchAddressDataForIPv4Async("214.196.212.251").whenComplete((addressData, throwable) -> {
           
      if(throwable != null) {
          throwable.printStackTrace();
          return;
      }

      System.out.println(addressData.getProxy());

});
```

## Contributing

1. Fork it (<https://github.com/yourname/yourproject/fork>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request

[issues]: https://img.shields.io/github/issues/SquareCodeFX/proxycheck.io-api
[forks]: https://img.shields.io/github/forks/SquareCodeFX/proxycheck.io-api
[stars]: https://img.shields.io/github/stars/SquareCodeFX/proxycheck.io-api
[license]: https://img.shields.io/github/license/SquareCodeFX/proxycheck.io-api
