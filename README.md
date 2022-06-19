# Proxycheck.io Java-API

> A mature API to work sync/async with Proxycheck.io.
>
![Issues][issues]
![Forks][forks]
![Stars][stars]
![License][license]

## Installation

Get from maven

```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.SquareCodeFX</groupId>
    <artifactId>proxycheck.io-api</artifactId>
    <version>1.0.4</version>
    <scope>compile</scope>
</dependency>
```

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

Here are a few examples of how to work with the API. Examples are provided for both the synchronous and the asynchronous
part.

Init

```
ProxyAPI proxyAPI = ProxyAPI.builder()
    // If changes need to be made to the license key, this method can be called.
    // However, if you do not have a plan, you do not have to
    // do this, otherwise it will take the Default parameter.
    .proxyKey("license_here")
    // How long should the objects stay in the cache?
    .durationTime(60)
    // What should be the time format? See durationTime
    .durationUnit(TimeUnit.MINUTES)
    // Build class
    .build();
```


Fetch sync
```
try {

    AddressData addressData = ProxyAPI.fetchAddressDataForIPv4("214.196.212.251");
    
    } catch (ExecutionException e) {
        e.printStackTrace();
}
```


Fetch async
```
ProxyAPI.fetchAddressDataForIPv4Async("214.196.212.251").whenComplete((addressData, throwable) -> {
           
      if(throwable != null) {
          throwable.printStackTrace();
          return;
      }

      System.out.println(addressData.getProxy());
});
```

# Security Policy

## Supported Versions

The following versions are supported by us for support purposes. 
So if you use another version, do not expect any help from us.

| Version | Supported          |
| ------- | ------------------ |
| 0.0.1-9   | :x: |
| 1.0.0   | :white_check_mark:                |

## Reporting a Vulnerability

- For feature requests and reporting errors and bugs, use the GitHub issues task.
- For simple questions, use the GitHub issues task.
- For piracy and resource ownership related issues, email me: admin@squarecode.de


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
