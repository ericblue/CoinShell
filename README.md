# CoinShell 

## Overview

What is CoinShell?  CoinShell is first and foremost a Command-Line Shell For Interfacing with 
Cryptocurrencies (http://coinshell.io).  In addition to a Java-based CLI program, there's an embedded web 
server making it possible to make requests and recieve responses in JSON format.  

Why do I need this? There are many services and APIs available for looking up currency information.  And, exchanges
that contain your crypto transaction history.  CoinShell was created to make it easy to interface
with a variety of popular services, and get things done quickly from a single, private space.

In the spirit of privacy and decentralization, CoinShell will make it possible to run your own
secure server/API, store your personal crypto transaction history, and interface with popular 
crypto services.

**The current version makes it possible to:**

* Lookup current prices of cryptocurrencies and convert to your local currency (e.g. BTC->USD, ETH->EUR, or even BTC->ETH ).
* Lookup historical prices of cryptocurrencies and convert to your local currency (e.g. BTC->USD on 2012-01-01 or ETH->USD on 2014-10-17T23:01:22))

**The plan is to make it possible to:**

* View current transaction status via Blockchain.info, Etherchain.org, etc.
* Integrate with multiple exchanges, and download your transaction history from popular exchanges like Coinbase.
* Execute Buy and Sell orders orders via the CLI and web interface
* Execute commands via an interactive shell or scripting
 
_Note: This version is currently in an early-stage alpha state!_ 


## Quick Start

CoinShell is distributed as a self-contained .jar file and avilable for download via the dist directory.  See: https://github.com/ericblue/CoinShell/tree/master/dist

There is a bash-based launcher script (for Mac or LInux; WIndows .bat is coming) in the same directory. To launch:

```

$ ./coinshell.sh 
Using CoinShell jar ./coinshell-latest.jar
Welcome! CoinShell Initializing...
Launching CLI...
usage: coinshell
 -h,--help                shows this message
 -p,--price               Find current price of a coin
 -ph,--price_historical   Find historical price of a coin
 -v,--version             Get version number
 -w,--web                 launch embedded web server


```
### Lookup prices

```
$ ./coinshell.sh -p BTC USD
Using CoinShell jar ./coinshell-latest.jar
Welcome! CoinShell Initializing...
Launching CLI...
Making request...
BTC price = 18866.0 USD

```

### Lookup historical prices
```
 ./coinshell.sh -ph 2017-11-01 ETH USD
Using CoinShell jar ./coinshell-latest.jar
Welcome! CoinShell Initializing...
Launching CLI...
Making request...
ETH price = 289.42 USD

```

### Launch the embedded web server
``` 
$ ./coinshell.sh -w
Using CoinShell jar ./coinshell-latest.jar
Welcome! CoinShell Initializing...
Launching embedded CoinShell web server...
Web server ready.  Listening on http://localhost:8080. Hit <CTRL-C> to stop.

```

### Making HTTP requests

#### Explore the interactive API (Swagger)
http://localhost:8080/swagger-ui.html

See: See https://imgur.com/a/8aPZS


#### HTTP price lookup

```
$ curl 'http://localhost:8080/price/?from=BTC'

{"price":{"currencyCode":"USD","price":18893.69}}

```

##Supported Integrations

* CryptoCompare (https://www.cryptocompare.com/api/#)
* Coming Soon: Coinbase, Binance, Bittrex, GDAX and Gemini, BlockChain.info, etc.

## Development

### Generating a clean build
```
./gradlew clean build
```

### Generating a jar (copies to dist folder)
```
./gradlew clean build jar dist
```

### Running web server in dev mode
```
./gradlew bootRun
```

## License


       Copyright (C) 2017 CoinShell.org (Eric Blue)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
