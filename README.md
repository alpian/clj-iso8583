# clj-iso8583

A Clojure library for parsing ISO8583 messages into Clojure maps.

## Usage

# Parsing
((parser field-definitions) message-bytes)

# Writing
(write field-definitions {:pan "1111222233334444" :processing-code "010000" :transaction-amount "000000110000"})

# Fields

Coming soon...

## License

Copyright © 2012 Ian Davies

Distributed under the Eclipse Public License, the same as Clojure.
