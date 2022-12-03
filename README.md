# com.oakmac.iso8583  [![Clojars Project](https://img.shields.io/clojars/v/com.oakmac/iso8583.svg)](https://clojars.org/com.oakmac/iso8583)

A Clojure library for parsing ISO8583 messages into Clojure maps.

## Versioning

This is an updated fork of [alpian/clj-iso8583]. The core logic is largely unchanged,
but the project has been updated to use more modern Clojure practices.

[alpian/clj-iso8583]:https://github.com/alpian/clj-iso8583

## Usage

### Parsing

```clj
((parser field-definitions) message-bytes)
```

### Writing

```clj
(write field-definitions {:pan "1111222233334444" :processing-code "010000" :transaction-amount "000000110000"})
```

## Development

Install [leiningen], then from the project directory:

```sh
# run unit tests
lein test
```

[leiningen]:https://leiningen.org/

## License

Original project copyright © 2012 Ian Davies.
Additional changes copyright © 2022 Chris Oakman.

Distributed under the [Eclipse Public License](LICENSE.txt).
