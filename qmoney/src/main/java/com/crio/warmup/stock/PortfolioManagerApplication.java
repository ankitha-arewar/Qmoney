
package com.crio.warmup.stock;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.dto.TotalReturnsDto;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.crio.warmup.stock.portfolio.PortfolioManager;
import com.crio.warmup.stock.portfolio.PortfolioManagerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;


public class PortfolioManagerApplication {

  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Task:
  //       - Read the json file provided in the argument[0], The file is available in the classpath.
  //       - Go through all of the trades in the given file,
  //       - Prepare the list of all symbols a portfolio has.
  //       - if "trades.json" has trades like
  //         [{ "symbol": "MSFT"}, { "symbol": "AAPL"}, { "symbol": "GOOGL"}]
  //         Then you should return ["MSFT", "AAPL", "GOOGL"]
  //  Hints:
  //    1. Go through two functions provided - #resolveFileFromResources() and #getObjectMapper
  //       Check if they are of any help to you.
  //    2. Return the list of all symbols in the same order as provided in json.

  //  Note:
  //  1. There can be few unused imports, you will need to fix them to make the build pass.
  //  2. You can use "./gradlew build" to check if your code builds successfully.

  private static String[] args;


  public static List<String> mainReadFile(String[] args) throws IOException, URISyntaxException {

    File file = resolveFileFromResources(args[0]);
    ObjectMapper objectMapper = getObjectMapper();

    PortfolioTrade[] trades = objectMapper.readValue(file,PortfolioTrade[].class);
    List<String> symbol = new ArrayList<>();
    for(PortfolioTrade  trade : trades)
    {
      symbol.add(trade.getSymbol());
    }
    return symbol;

  }


  // Note:
  // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.







  // TODO: CRIO_TASK_MODULE_REST_API
  //  Find out the closing price of each stock on the end_date and return the list
  //  of all symbols in ascending order by its close value on end date.

  // Note:
  // 1. You may have to register on Tiingo to get the api_token.
  // 2. Look at args parameter and the module instructions carefully.
  // 2. You can copy relevant code from #mainReadFile to parse the Json.
  // 3. Use RestTemplate#getForObject in order to call the API,
  //    and deserialize the results in List<Candle>



  private static void printJsonObject(Object object) throws IOException {
    Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
    ObjectMapper mapper = new ObjectMapper();
    logger.info(mapper.writeValueAsString(object));
  }

  private static File resolveFileFromResources(String filename) throws URISyntaxException {
    return Paths.get(
        Thread.currentThread().getContextClassLoader().getResource(filename).toURI()).toFile();
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }


  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Follow the instructions provided in the task documentation and fill up the correct values for
  //  the variables provided. First value is provided for your reference.
  //  A. Put a breakpoint on the first line inside mainReadFile() which says
  //    return Collections.emptyList();
  //  B. Then Debug the test #mainReadFile provided in PortfoliomanagerApplicationTest.java
  //  following the instructions to run the test.
  //  Once you are able to run the test, perform following tasks and record the output as a
  //  String in the function below.
  //  Use this link to see how to evaluate expressions -
  //  https://code.visualstudio.com/docs/editor/debugging#_data-inspection
  //  1. evaluate the value of "args[0]" and set the value
  //     to the variable named valueOfArgument0 (This is implemented for your reference.)
  //  2. In the same window, evaluate the value of expression below and set it
  //  to resultOfResolveFilePathArgs0
  //     expression ==> resolveFileFromResources(args[0])
  //  3. In the same window, evaluate the value of expression below and set it
  //  to toStringOfObjectMapper.
  //  You might see some garbage numbers in the output. Dont worry, its expected.
  //    expression ==> getObjectMapper().toString()
  //  4. Now Go to the debug window and open stack trace. Put the name of the function you see at
  //  second place from top to variable functionNameFromTestFileInStackTrace
  //  5. In the same window, you will see the line number of the function in the stack trace window.
  //  assign the same to lineNumberFromTestFileInStackTrace
  //  Once you are done with above, just run the corresponding test and
  //  make sure its working as expected. use below command to do the same.
  //  ./gradlew test --tests PortfolioManagerApplicationTest.testDebugValues

  public static List<String> debugOutputs() {

     String valueOfArgument0 = "trades.json";
     String resultOfResolveFilePathArgs0 = "/home/crio-user/workspace/ankithaarewar789-ME_QMONEY_V2/qmoney/bin/test/assessments/trades.json";
     String toStringOfObjectMapper = "com.fasterxml.jackson.databind.ObjectMapper@1573f9fc";
     String functionNameFromTestFileInStackTrace = "ModuleOneTest.mainReadFile()";
     String lineNumberFromTestFileInStackTrace = "19.1";


    return Arrays.asList(new String[]{valueOfArgument0, resultOfResolveFilePathArgs0,
        toStringOfObjectMapper, functionNameFromTestFileInStackTrace,
        lineNumberFromTestFileInStackTrace});
  }

  public static List<TotalReturnsDto> mainReadQuotesHelper (String[] args, List<PortfolioTrade> trades) throws IOException, URISyntaxException {

    RestTemplate restTemplate = new RestTemplate();    
    List<TotalReturnsDto> tests = new ArrayList<TotalReturnsDto>();    
    for (PortfolioTrade t : trades) {  
      String uri = "https://api.tiingo.com/tiingo/daily/" + t.getSymbol() + "/prices?startDate=" + t.getPurchaseDate().toString() + "&endDate=" + args[1] + "&token=f8787e0b658e07fc79c5ec2ff681f7526e1e4cac";    
      TiingoCandle[] results = restTemplate.getForObject(uri, TiingoCandle[].class);    
      if(results != null) {    
        tests.add(new TotalReturnsDto(t.getSymbol(),results[results.length - 1].getClose()));    
      }
    }
    
    return tests;
  }
  // Note:
  // Remember to confirm that you are getting same results for annualized returns as in Module 3.
  public static List<String> mainReadQuotes(String[] args) throws IOException, URISyntaxException {
    ObjectMapper objectMapper = getObjectMapper();
    List<PortfolioTrade> trades = Arrays.asList(objectMapper.readValue(resolveFileFromResources(args[0]), PortfolioTrade[].class));
    List<TotalReturnsDto> sortedByValue = mainReadQuotesHelper(args,trades); Collections.sort(sortedByValue, TotalReturnsDto.closingComparator); List<String> stocks = new ArrayList<String>();
    for (TotalReturnsDto trd: sortedByValue) {
      stocks.add(trd.getSymbol());
    }

    return stocks;
  }

  // TODO:
  //  After refactor, make sure that the tests pass by using these two commands
  //  ./gradlew test --tests PortfolioManagerApplicationTest.readTradesFromJson
  //  ./gradlew test --tests PortfolioManagerApplicationTest.mainReadFile
  public static List<PortfolioTrade> readTradesFromJson(String filename) throws IOException, URISyntaxException {
    //to get the filepath
     File file = resolveFileFromResources(filename);
     //creathing instance of objmapper
     ObjectMapper om = getObjectMapper();
     //reading the json file and deserializing it into the portforlio obj list

     PortfolioTrade[] trades = om.readValue(file,PortfolioTrade[].class);


     return Arrays.asList(trades);

  }


  // TODO:
  //  Build the Url using given parameters and use this function in your code to cann the API.
  public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {

    String baseUrl = "https://api.tiingo.com/tiingo/daily/{symbol}/prices";
    String formattedUrl = baseUrl.replace("{symbol}", trade.getSymbol());
    String endDateString = endDate.toString();
    String startDateString = trade.getPurchaseDate().toString();


    // Correct the URL to use endDate instead of startDate
    return String.format("%s?startDate=%s&endDate=%s&token=%s", formattedUrl,startDateString ,endDateString, token);
}
  // TODO:
  //  Ensure all tests are passing using below command
  //  ./gradlew test --tests ModuleThreeRefactorTest
  static Double getOpeningPriceOnStartDate(List<Candle> candles) {
    if(candles.isEmpty())
    {
     return 0.0;
    }
    return candles.get(0).getOpen();
  }


  public static Double getClosingPriceOnEndDate(List<Candle> candles) {
    int size = candles.size();
    if(candles.isEmpty())
    {
      return 0.0;
    }
    
    return candles.get(size-1).getClose();
     
  }


  public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {
     RestTemplate rt = new RestTemplate();
     String url = prepareUrl(trade, endDate, token);
     TiingoCandle[] candleArray = rt.getForObject(url, TiingoCandle[].class);
     if(candleArray != null)
     {
      return Arrays.asList(candleArray);
     }
     LocalDate prevDate = endDate.minusDays(1);
        while (prevDate.isAfter(trade.getPurchaseDate())) {
            url = prepareUrl(trade, prevDate, token);
            candleArray = rt.getForObject(url, TiingoCandle[].class);
            if (candleArray != null && candleArray.length > 0) {
                return Arrays.asList(candleArray);
            }
            prevDate = prevDate.minusDays(1);
        }
        return Collections.emptyList();
     }


  public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args)
      throws IOException, URISyntaxException {    
      
        List<AnnualizedReturn> annualizedReturns = new ArrayList<>();
        LocalDate endLocalDate = LocalDate.parse(args[1]);        
        File trades = resolveFileFromResources(args[0]); 
        ObjectMapper objectMapper = getObjectMapper();        
        PortfolioTrade[] tradeJsons = objectMapper.readValue(trades, PortfolioTrade[].class);
        
        for (int i = 0; i < tradeJsons.length; i++) { 
          annualizedReturns.add(getAnnualizedReturn(tradeJsons[i], endLocalDate));
        }
        
        // Sort in Descending order
        
        Comparator<AnnualizedReturn> SortByAnnReturn = Comparator.comparing (AnnualizedReturn::getAnnualizedReturn) .reversed(); Collections.sort(annualizedReturns, SortByAnnReturn);
        
        return annualizedReturns;  
      }

      public static AnnualizedReturn getAnnualizedReturn (PortfolioTrade trade, LocalDate endLocalDate) {                
        LocalDate startLocalDate = trade.getPurchaseDate();        
        if (startLocalDate.compareTo(endLocalDate) >= 0) {
        
        throw new RuntimeException();
        
        }        
        String url = prepareUrl(trade, endLocalDate, getToken());        
        RestTemplate restTemplate = new RestTemplate();        
        // api returns a list of results for each day's closing details        
        TiingoCandle[] stocksStartToEndDate = restTemplate.getForObject(url, TiingoCandle[].class);
        
        // Extract stocks for startDate & endDate
        
        if (stocksStartToEndDate != null) {        
                
        TiingoCandle stockStartDate = stocksStartToEndDate[0];        
        TiingoCandle stockLatest = stocksStartToEndDate[stocksStartToEndDate.length - 1];
        
        Double buyPrice = stockStartDate.getOpen();        
        Double sellPrice =stockLatest.getClose();
        
        AnnualizedReturn annualizedReturn = calculateAnnualizedReturns (endLocalDate, trade, buyPrice, sellPrice);        
        return annualizedReturn;
        
        } 
        else{
        
        return new AnnualizedReturn(trade.getSymbol(), Double.NaN, Double.NaN);
        
        }
      }

  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Return the populated list of AnnualizedReturn for all stocks.
  //  Annualized returns should be calculated in two steps:
  //   1. Calculate totalReturn = (sell_value - buy_value) / buy_value.
  //      1.1 Store the same as totalReturns
  //   2. Calculate extrapolated annualized returns by scaling the same in years span.
  //      The formula is:
  //      annualized_returns = (1 + total_returns) ^ (1 / total_num_years) - 1
  //      2.1 Store the same as annualized_returns
  //  Test the same using below specified command. The build should be successful.
  //     ./gradlew test --tests PortfolioManagerApplicationTest.testCalculateAnnualizedReturn

  public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
      PortfolioTrade trade, Double buyPrice, Double sellPrice) {
        // calculate total returns
        Double absReturn = (sellPrice - buyPrice) / buyPrice;
        String symbol = trade.getSymbol(); LocalDate purchaseDate = trade.getPurchaseDate();
        // calculate years
        Double numYears = (double) ChronoUnit.DAYS.between (purchaseDate, endDate) / 365;
        // calculate annualized returns using formula
        Double annualizedReturns = Math.pow((1 + absReturn), (1 / numYears)) - 1; 
        // return Annualized Return object
        return new AnnualizedReturn(symbol, annualizedReturns, absReturn);
        
  }

  // public static void main(String[] args) throws Exception {
    
  //   Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
  //   ThreadContext.put("runId", UUID.randomUUID().toString());

  //   printJsonObject(mainReadFile(args));


  //   printJsonObject(mainReadQuotes(args));



  //   printJsonObject(mainCalculateSingleReturn(args));

  // }


  public static String getToken() {
    return "f8787e0b658e07fc79c5ec2ff681f7526e1e4cac";
  }
  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Once you are done with the implementation inside PortfolioManagerImpl and
  //  PortfolioManagerFactory, create PortfolioManager using PortfolioManagerFactory.
  //  Refer to the code from previous modules to get the List<PortfolioTrades> and endDate, and
  //  call the newly implemented method in PortfolioManager to calculate the annualized returns.

  // Note:
  // Remember to confirm that you are getting same results for annualized returns as in Module 3.
  public static RestTemplate restTemplate = new RestTemplate();
  public static PortfolioManager portfolioManager = PortfolioManagerFactory.getPortfolioManager(restTemplate);
  public static List<AnnualizedReturn> mainCalculateReturnsAfterRefactor(String[] args)
      throws Exception {
       String file = args[0];
       LocalDate endDate = LocalDate.parse(args[1]);
       String contents = readFileAsString(file);
       ObjectMapper objectMapper = getObjectMapper();
       PortfolioTrade[]  portfolioTrades = objectMapper.readValue(contents, PortfolioTrade[].class);
       return portfolioManager.calculateAnnualizedReturn(Arrays.asList(portfolioTrades), endDate);
  }

 


  private static String readFileAsString(String file) {
    return file;
  }


  public static void main(String[] args) throws Exception {
    PortfolioManagerApplication.args = args;
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());
    printJsonObject(mainReadFile(args));
    printJsonObject(mainReadQuotes(args));
    printJsonObject(mainCalculateSingleReturn(args));
    printJsonObject(mainCalculateReturnsAfterRefactor(args));
  }
}

