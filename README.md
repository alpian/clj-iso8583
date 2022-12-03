# clj-iso8583

A Clojure library for parsing ISO8583 messages into Clojure maps.

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

Copyright © 2012 Ian Davies

Distributed under the Eclipse Public License, the same as Clojure.
