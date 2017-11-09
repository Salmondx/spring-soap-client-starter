# Declarative SOAP client for Apache CXF (with Spring Boot starter)
## About
This library adds's Feign/Retrofit like support for CXF web services clients. With several annotations, you can create fully functional web service client
with hystrix support and custom request/response serialization.

## Dependencies
Add several dependencies to your `build.gradle` file:
```groovy
dependencies {
    
}
```

CXF dependencies should also be included in your build file:
```groovy
dependencies {
    compile 'org.apache.cxf:cxf-core:{cxfVersion}'
    compile 'org.apache.cxf:cxf-rt-frontend-jaxws:{cxfVersion}'
    compile 'org.apache.cxf:cxf-rt-transports-http:{cxfVersion}'
    compile 'org.apache.cxf:cxf-rt-ws-security:{cxfVersion}'
    compile 'org.apache.cxf:cxf-rt-bindings-soap:{cxfVersion}'
}
```
## Examples
Examples are available in the `examples` subproject.

## How to use
### Service client
To enable, just add `@EnableSoapClients` annotation to your spring boot app:

```java
@SpringBootApplication
@EnableSoapClients
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

And create Feign-like interface:

```java
@SoapClient(service = LegacySoapService.class)
public interface ModernClientWithHystrix {
    @SoapMethod("Get")
    List<ResponseTest> plainParameters(@Param("accList") String accList, @Param("mod") String mod, @Param("date") LocalDate creationDate);

    @SoapMethod("Get")
    List<ResponseTest> customObject(RequestTest requestTest);

    @SoapMethod("Get")
    Observable<List<ResponseTest>> observableResponse(RequestTest requestTest);

    @SoapMethod("Get")
    HystrixCommand<List<ResponseTest>> hystrixResponse(RequestTest requestTest);

    @SoapMethod("Get")
    Single<List<ResponseTest>> singleResponse(RequestTest requestTest);
}
```

In the `@SoapClient` annotation you should provide CXF class for Soap web service (e.g. ``LegacySoapService.class``).

`@SoapMethod` annotation points to Soap service method's name, that could be obtained from WSDL or from service from `*PortType` (both full and short names are supported: `CustomerAccountsInfoGet` and `/CustomerAccountsInfo#Get` -> `Get`).

###Type conversion

As you know, SOAP services use deprecated and ugly types (e.g. XMLGregorianCalendar) and it is not the best idea to build an API upon this types. 

This library uses Spring's `ConversionService` for input and output parameters if types of the custom object properties and actual cxf input parameters don't match.

To add a custom type converter, just implement `org.springframework.core.convert.converter.Converter` interface:

```java
public class DateConverter implements Converter<LocalDate, XMLGregorianCalendar> {
    @Override
    public XMLGregorianCalendar convert(LocalDate source) {
        return new XMLGregorianCalendarImpl(new GregorianCalendar(source.getYear(), source.getMonthValue(), source.getDayOfMonth()));
    }
}
```
After, create a `@Bean` in the configuration class:
```java
@Bean
public Converter localDateConverter() {
    return new DateConverter();
}
```
NOTE: There are lots of common types converters in Spring conversion service, so add converters only for custom types (like XmlGregorianCalendar).


### Input parameters

In client method is possible to use both custom object and plain argument list:
```java
// Plain method parameters with @Param annotation

@SoapMethod("Get")
List<ResponseTest> plainParameters(@Param("accNmbr") String accNumber, @Param("mod") String mod, @Param("date") LocalDate currentDate);

// Custom object
@SoapMethod("Get")
List<ResponseTest> customObject(RequestTest requestTest);
```

#### Plain arguments
Plain arguments in interface method should be with `@Param` annotation, which points to necessary property in actual Soap request (e.q. `@Param("accNmbr")` points to
`"accNmbr"` property of `CustomerAccountInfo` type).


#### Custom object
The library supports custom objects in interface methods, which after will be serialized to cxf service input parameters.
To use them, write object with several annotations:

```java
@Data
public class RequestTest {
    @Field("accNmbr")
    private String accountNumber;
    private String mod;
    private LocalDate date;
}
```
If there is no `@Field` annotation with a property, serializator will compare them by name.

`WARNING`: Custom object should be a POJO with empty constructor and getters/setters.

### Response objects
Available response types:
```java

// Plain object
@SoapMethod("Get")
ResponseTest ...

// Collection of items
@SoapMethod("Get")
List<ResponseTest> ...

// Rx Observable from hystrixCommand.toObservable()
@SoapMethod("Get")
Observable<List<ResponseTest>> ...

// Hystrix command
@SoapMethod("Get")
HystrixCommand<List<ResponseTest>> ...

// Rx Single from hystrixCommand.toObservable().toSingle()
@SoapMethod("Get")
Single<List<ResponseTest>> ...
```

##### Single item
```java
@Data
@Response("outParms")
public class ResponseTest {
    private String key;
    @Field("total")
    private Long totalAmount;
}
```
`@Response` annotation's value point to the property of actual cxf response (if there are many of output parameters and you need only one. In other cases, 
you can omit this annotation):

```java
public class CustomerAccountInfo {
    @XmlElement(
        required = true
    )
    protected NotUsedParameters notUsedParameters;
    @XmlElement(
        required = true
    )
    protected NecessaryParams outParms;
}
```
In this example, `@Response` point to outParms property and deserializator will search properties from `CustomerAccountInfo` object.

```java
public class NecessaryParams {
    protected String key;
    protected BigDecimal total;
}
```

NOTE: Property `total` will be converted from `BigDecimal` type to `Long` during deserialization because of the type mismatch and the Spring's ConversionService (see [paragraph](#Type conversion))

##### Nested list items
To deserialize nested list items, just point `@Response` to the last property from actual response object:

```java
@Data
@Response("resultSetRow")
public class ResponseTest {
    private String accountNumber;
    @Field("currency")
    private String currencyCode;
}
```

The library will search for `resultSetRow` property in actual response and after will deserialize it to a collection with response items:
```java
@SoapMethod("Get")
List<ResponseTest> ...
```
Actual response item should looks like:
```java
public class CustomerAccountInfo {
    protected List<AccountInfo> resultSetRow;
}
```

### Hystrix support
Each `@SoapMethod` is wrapped with HystrixCommand by default. `HystrixCommand` is created with `HystrixCommandGroupKey` that
matches with the method name.

# To-Do
- Add tests and increase code coverage
- Add custom Hystrix fallback methods (currently, the HystrixRuntimeException is thrown)
- Add custom keys for `HystrixCommandGroupKey` option
